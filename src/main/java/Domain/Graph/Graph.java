package Domain.Graph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Dani
 *
 */
public class Graph {
	private TreeMap<String,HashSet<Node>> elements;
	private final String AUTHORS = "author";
	private final String ARTICLES = "paper";
	private final String CONFERENCES = "conf";
	private final String TERMS = "term";
	private final String  WRONG_TYPE = "error";
	
	
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
	
	private boolean containsNode(int id, String type) {
		if (checkType(type) != WRONG_TYPE) {
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
		if (checkType(type) != WRONG_TYPE) {
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
	
	
	
	
	//Constructora del Graf
	public Graph () {
		elements = new TreeMap<String,HashSet< Node>>();
		elements.put(AUTHORS, new HashSet<Node>() );
		elements.put(ARTICLES, new HashSet<Node>());
		elements.put(CONFERENCES,new HashSet<Node>());
		elements.put(TERMS,new HashSet<Node>());		
	}
	
	public HashSet<Node> getSetOfNodes() {
		HashSet<Node> res = new HashSet<Node>();
		res.addAll(elements.get(AUTHORS));
		res.addAll(elements.get(ARTICLES));
		res.addAll(elements.get(CONFERENCES));
		res.addAll(elements.get(TERMS));
		
		return res;		
	}
	/**
	 * 
	 * @param type: tipus de Node
	 * @return: Tots els nodes del graf del tipus dessitjat. Null si el tipus no existeix
	 */
	
	public HashSet<Node> getSetOfNodes(String type) {
		String clauConsulta = checkType(type);
		if (clauConsulta == WRONG_TYPE) {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {
			return elements.get(clauConsulta);			
		}		
	}
	/**
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	
	public Node getNode(int id, String type) {
		String clauConsulta = checkType(type);
		if (clauConsulta == WRONG_TYPE) {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {			
			for (Node nod : elements.get(clauConsulta)) {
				if (nod.getID() == id) return nod;				
			}
			return null;
		}
	    
	}
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public Node getNode(String name, String type) {
		String clauConsulta = checkType(type);
		if (clauConsulta == WRONG_TYPE) {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {			
			for (Node nod : elements.get(clauConsulta)) {
				if (nod.getName() == name) return nod;				
			}
			return null;
		}
	}
	
	/**
	 * 
	 * @param node
	 * @pre
	 * @return
	 */
	
	public Set<Node> getNeighbours(Node node) {
		if (elements.get(node.getType()).contains(node)) {
			return node.getNeighbours();
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param node
	 * @param type
	 * @return
	 */
	
	public Set<Node> getNeighbours(Node node, String type) {
		if (checkType(type)!= WRONG_TYPE && elements.get(node.getType()).contains(node)) {
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
	
	//AVï¿½S --> S'ha d'implementar el constructor Node(nom) amb generacio automatica de ID's
	
	public int afegirNode(String name, String type ) {
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
			//aquï¿½ hi haura una Excepciï¿½
					
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
			this.getNode(id1, type1).addRelationship(this.getNode(id2, type2));
			this.getNode(id2, type2).addRelationship(this.getNode(id1, type1));
			return true;
		}		
		return false;		
	}	
	public boolean afegirAresta(Node a, Node b) {
		if (this.elements.get(a.getType()).contains(a) && this.elements.get(b.getType()).contains(b)) {
			a.addRelationship(b); b.addRelationship(a);
			return true;
		}		
		return false;
	}
	
	/**
	 * 
	 * @param name1, type1: nom i tipus del primer node
	 * @param name2, type2: nom i tipus del segon node
	 * @return : true si s'ha creat la relacio entre els dos nodes.
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
				a.addRelationship(b);
				b.addRelationship(a);
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
			node1.deleteRelationship(node2); node2.deleteRelationship(node1);
			return true;
		}
		return false;
	}
	
	
	

}