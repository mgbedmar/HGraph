package Domain.Graph;
import Domain.Config;

import java.util.*;

/**
 * @author Dani
 *
 */
public class Graph {
	private TreeMap<String,HashMap<Node,Node>> elements;
	private HashMap<String,ArrayList<Node>> dicNameNodes;
    private int maxID; //la id mes gran que s'ha assignat
	
	
	//Claus fixes per agrupar els nodes segons el tipus
	
	private final String AUTHORS = Config.authorType; //Clau pels autors
	private final String ARTICLES = Config.paperType; //Clau per els articles
	private final String CONFERENCES = Config.confType; //Clau per les conferencies
	private final String TERMS = Config.termType;     //Clau per els termes
	
	
	private final String  WRONG_TYPE = "error"; //Clau que farem servir per saber si hi ha hagut un error de tipus
	
	//Metodes privats per garantir la redundancia
	
	/**
	 * 
	 * @param t: tipus de node
	 * @return: retorna WRONG_TYPE si el tipus t no existeix o es incorrecte
	 */
	private String checkType(String t) {
		String tip;
		switch (t) {		
		case AUTHORS: case ARTICLES: case CONFERENCES: case TERMS:
			tip = t;
			break;			
		default:
			tip = WRONG_TYPE;
					
		}
		return tip;		
	}
	
	/**
	 * 
	 * @param id: ID del node
	 * @param type: tipus de Node
	 * @return: true si el node de tipus type i ID id existeix al graf
	 */
	private boolean containsNode(int id, String type) {
		if (!checkType(type).equals(WRONG_TYPE)) { //TODO
			Iterator<Node> it = elements.get(type).iterator();
			boolean found = false;
			while(it.hasNext() && !found) {
				found = (it.next().getID() == id);
			}
			return found;
		}
		//Excepcio Tipus erroni
		return false;
		
	}
	
	private boolean containsNode(String name, String type) {
		if (checkType(type) != WRONG_TYPE) { //TODO
			Iterator<Node> it = elements.get(type).iterator();
			boolean found = false;
			while(it.hasNext() && !found) {
				found = (it.next().getName() == name);
			}
			return found;
		}
		//Excepcio Tipus erroni
		return false;
		
	}

    private Node createNode(int id, String type)
    {

        switch(type)
        {
            case AUTHORS:
                return new Author(id, null);
            case TERMS:
                return new Term(id, null);
            case ARTICLES:
                return new Paper(id, null);
            case CONFERENCES:
                return new Conf(id, null);
        }

        return null;
    }
	
	
	//Metodes Publics
	
	
	public Graph () {
		elements = new TreeMap<>();
		elements.put(AUTHORS, new HashMap<>() );
		elements.put(ARTICLES, new HashMap<>());
		elements.put(CONFERENCES,new HashMap<>());
		elements.put(TERMS,new HashMap<>());

        dicNameNodes = new HashMap<>();

        maxID = 0;
	}

	/**
	 * 
	 * @return: retorna tots els nodes del graf
	 */
	public Set<Node> getSetOfNodes() {
		HashSet<Node> res = new HashSet<Node>();
		res.addAll(elements.get(AUTHORS).keySet());
		res.addAll(elements.get(ARTICLES).keySet());
		res.addAll(elements.get(CONFERENCES).keySet());
		res.addAll(elements.get(TERMS).keySet());
		
		return res;		
	}

	/**
	 * 
	 * @param type: tipus de Node
	 * @return: Tots els nodes del graf del tipus desitjat. Null si el tipus no existeix
	 */
	
	public Set<Node> getSetOfNodes(String type) {
		String clauConsulta = checkType(type);
		if (clauConsulta.equals(WRONG_TYPE)) {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {
			return elements.get(clauConsulta).keySet();
		}		
	}

	/**
	 * 
	 * @param id
	 * @param type
	 * @return: si existeix, retorna el node de tipus type i ID id. Null si no existeix
	 */
	
	public Node getNode(int id, String type) {
		String clauConsulta = checkType(type);
		if (clauConsulta.equals(WRONG_TYPE)) {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {
            Node aux = createNode(id, type);
			return elements.get(type).get(aux);
		}
	    
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public ArrayList<Node> getNodes(String name, String type) {
		String clauConsulta = checkType(type);
		if (clauConsulta.equals(WRONG_TYPE)) {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {
            ArrayList<Node> intern = dicNameNodes.get(name);
            if (intern == null) return null;
            else {
                ArrayList<Node> forRet = new ArrayList<>();
                for (int i = 0; i < intern.size(); i++) {
                    if (intern.get(i).getType().equals(type)) {
                        forRet.add(intern.get(i));
                    }
                }
                if (forRet.size() == 0) return null;
                else return forRet;
            }
		}
	}


	/**
	 * Consultora dels veins d'un node.
	 * @param node node del que es volen consultar els veins. Ha de ser un node
     *             obtingut del graf amb <em>getNode()</em>o <em>getNodes()</em>
	 * @return conjunt de veins de <em>node</em>
	 */
	public Set<Node> getNeighbours(Node node) {
        return node.getNeighbours();
	}
	
	/**
	 * Consultora dels veins d'un cert tipus d'un node.
	 * @param node node del que es volen consultar els veins. Ha de ser un node
     *             obtingut del graf amb <em>getNode()</em>o <em>getNodes()</em>
	 * @param type tipus dels nodes retornats
	 * @return un conjunt amb els veins de tipus <em>type</em> del node <em>node</em>
	 */
	public Set<Node> getNeighbours(Node node, String type) {
		if (!checkType(type).equals(WRONG_TYPE)) {
			return node.getNeighbours(type);
		}
		return null;
	} 
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	
	//AVIS --> S'ha d'implementar el constructor Node(nom) amb generacio automatica de ID's
	
	public void addNode(String name, String type) {
		switch (type) {
		
		case AUTHORS:
			this.elements.get(AUTHORS).add(new Author(name));			
			break;
		case ARTICLES:
			this.elements.get(ARTICLES).add(new Paper(name));
			break;
		case CONFERENCES:
			this.elements.get(CONFERENCES).add(new Conf(name));
			break;
		case TERMS:
			this.elements.get(TERMS).add(new Term(name));
			break;			
		default:
            //excepcio
					
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 */
	
	public void afegirNode(int id, String name, String type) {
				
		switch (type) {
		
		case AUTHORS:
			this.elements.get(AUTHORS).add(new Author(id,name));			
			break;
		case ARTICLES:
			this.elements.get(ARTICLES).add(new Paper(id,name));
			break;
		case CONFERENCES:
			this.elements.get(CONFERENCES).add(new Conf(id,name));
			break;
		case TERMS:
			this.elements.get(TERMS).add(new Term(id,name));
			break;			
		default:
			//aqui hi haura una Excepcio
					
		}
		
	}
	
	/**
	 * 
	 * @param id1
	 * @param type1
	 * @param id2
	 * @param type2
	 * @return
	 */
	
	public boolean afegirAresta(int id1, String type1, int id2, String type2) {
		if (containsNode(id1,type1) && containsNode(id2,type2)) {
			this.getNode(id1, type1).addEdge(this.getNode(id2, type2));
			this.getNode(id2, type2).addEdge(this.getNode(id1, type1));
			return true;
		}		
		return false;		
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean afegirAresta(Node a, Node b) {
		if (this.elements.get(a.getType()).contains(a) && this.elements.get(b.getType()).contains(b)) {
			a.addEdge(b); b.addEdge(a);
			return true;
		}		
		return false;
	}
	
	/**
	 * 
	 * @param name1, type1: nom i tipus del primer node
	 * @param name2, type2: nom i tipus del segon node
	 * @return : true si s'ha creat la relacio entre els dos nodes de forma simetrica.
	 * 			 false si:
	 * 					- Ja existia la relacio en ambdos nodes
	 * 					- Un o els dos nodes no existeixen al graf
	 * 					
	 */
	
	public boolean afegirAresta(String name1, String type1, String name2, String type2) {
		if (containsNode(name1,type1) && containsNode(name2,type2)) {
			
			Node a = this.getNode(name1, type1);
			Node b = this.getNode(name2, type2);
			boolean repe = a.getNeighbours(type2).contains(b) || b.getNeighbours(type1).contains(a);			
			if (!repe){
				a.addEdge(b);
				b.addEdge(a);
				return true;
			}
			return false;
		}		
		return false;
	}
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @return : true si el node ha estat borrat correctement. False si el tipus era invalid o no estaba al graf
	 */
	public boolean esborrarNode(int id, String type) {
		Iterator<Node> it = this.elements.get(type).iterator();
		boolean finish = false;
		while (it.hasNext() && !finish) {
			if (it.next().getID() == id) {
				finish = true;
				it.remove();
			}
		}
		return finish;
	}
	
	/**
	 * 
	 * @param name : nom del Node
	 * @param type : tipus del Node
	 * @return : true si el node ha estat borrat correctement. False si el tipus era invalid o no estaba al graf
	 */
	public boolean esborrarNode(String name, String type) {
		if (checkType(type)!= WRONG_TYPE) {
			Iterator<Node> it = this.elements.get(type).iterator();
			boolean finish = false;
			while (it.hasNext() && !finish) {
				if (it.next().getName() == name) {
					finish = true;
					it.remove();
				}
			}
			return finish;			
		}
		//Aquí hi hauria una Exception
		return false;
		
	}
	
	/**
	 * 
	 * @param node1: Node a
	 * @param node2: Node b
	 * @return true si ambdos nodes existien al Graf abans de la crida.
	 */
	
	public boolean esborrarAresta(Node node1, Node node2) {
		if (elements.get(node1.getType()).contains(node1) && elements.get(node2.getType()).contains(node2)) {
			node1.deleteEdge(node2); node2.deleteEdge(node1);
			return true;
		}
		return false;
	}
	
	
	

}