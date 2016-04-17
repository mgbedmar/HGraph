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


    /**
     * Constructora. Crea un graf buit.
     */
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
	 * Consultora dels nodes del graf.
	 * @return: un conjunt amb tots els nodes del graf
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
	 * Consultora dels nodes d'un tipus donat.
	 * @param type: tipus de node
	 * @return: Tots els nodes del graf del tipus <em>type</em>, <em>null</em> si el tipus no existeix.
     * Modificacions en el conjunt de retorn canviaran l'estat del graf.
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
	 * Obte un node a partir de la id i el tipus.
	 * @param id id del node que es busca
	 * @param type tipus del node que es busca
	 * @return: el node de tipus <em>type</em> i id <em>id</em>, <em>null</em> si no existeix
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
	 * Obte tots els nodes d'un determinat tipus i nom.
	 * @param name nom dels nodes buscats
	 * @param type tipus dels nodes buscats
	 * @return llista de nodes que tenen nom <em>name</em> i tipus <em>type</em>
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
	 * @return conjunt de veins de <em>node</em>. Modificacions en el conjunt de retorn
     * canviaran l'estat del graf.
	 */
	public Set<Node> getNeighbours(Node node) {
        return node.getNeighbours();
	}
	
	/**
	 * Consultora dels veins d'un cert tipus d'un node.
	 * @param node node del que es volen consultar els veins. Ha de ser un node
     *             obtingut del graf amb <em>getNode()</em>o <em>getNodes()</em>
	 * @param type tipus dels nodes retornats
	 * @return un conjunt amb els veins de tipus <em>type</em> del node <em>node</em>.
     * Modificacions en el conjunt de retorn
     * canviaran l'estat del graf.
	 */
	public Set<Node> getNeighbours(Node node, String type) {
		if (!checkType(type).equals(WRONG_TYPE)) {
			return node.getNeighbours(type);
		}
		return null;
	}

    /**
     * Afegeix un node al graf.
     * @param node Node que es vol afegir al graf. Si te <em>id</em> negativa,
     *             es genera una id unica per ell. Si no, es presuposa que
     *             la id es unica i s'afegeix.
     */
    public void addNode(Node node) {
        if (node.getID() < 0) {
            ++maxID;
            node.setID(maxID);
        }
        else if (node.getID() > maxID)
            maxID = node.getID();

        elements.get(node.getType()).put(node, node);
        if (dicNameNodes.containsKey(node.getName())) {
            dicNameNodes.get(node.getName()).add(node);
        }
        else {
            ArrayList<Node> forDic = new ArrayList<>(1);
            forDic.add(node);
            dicNameNodes.put(node.getName(), forDic);
        }
    }
	

	/**
	 * Afegeix una aresta de <em>a</em> a <em>b</em> (i la simetrica).
	 * @param a Node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
	 * @param b Node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
	 * @return <em>true</em> si l'aresta s'ha afegit i <em>false</em>
     * en cas contrari.
	 */
	public boolean addEdge(Node a, Node b) {
        a.addEdge(b);
        b.addEdge(a);
        //TODO Node ha de retornar tambe true i false si volem conservar el boolean
		return true;
	}

    private void removeEdgeFromTypeToNode(Node node, String type) {
        for (Node a: elements.get(type).keySet()) {
            a.removeEdge(node);
        }
    }

    /**
     * Esborra un node del graf.
     * @param node Node extret directament del graf (amb <em>getNode()</em>
     *             o be <em>getNodes()</em>
     * @return TODO no caldria return
     */
	public boolean removeNode(Node node) {
        dicNameNodes.get(node.getName()).remove(node);
        if (node.getType().equals(ARTICLES)) {
            removeEdgeFromTypeToNode(node, TERMS);
            removeEdgeFromTypeToNode(node, AUTHORS);
            removeEdgeFromTypeToNode(node, CONFERENCES);
        }
        else {
            removeEdgeFromTypeToNode(node, ARTICLES);
        }
        elements.get(node.getType()).remove(node);
        return true;
    }

    /**
     * Esborra, si existeix, l'aresta de <em>a</em> a <em>b</em>.
     * @param a Node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     * @param b Node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     * @return TODO si volem aixo s'ha de fer als nodes
     */
	public boolean removeEdge(Node a, Node b) {
        a.removeEdge(b);
        b.removeEdge(a);
        return true;
	}

}