package Domain.Graph;
import Domain.Config;
import Domain.DomainException;

import java.util.*;

/**
 * @author Dani
 *
 */
public class Graph {
	private TreeMap<String,HashMap<Node,Node>> elements;
	private HashMap<String,ArrayList<Node>> dicNameNodes;
    private int maxID; //la id mes gran que s'ha ficat al graf
	
	private final String  WRONG_TYPE = "error"; //Clau que farem servir per saber si hi ha hagut un error de tipus
	
	//Metodes privats per garantir la no redundancia
	
	/**
	 * 
	 * @param t: tipus de node
	 * @return: retorna WRONG_TYPE si el tipus t no existeix o es incorrecte
	 */
	private String checkType(String t) {
		String tip;
		switch (t) {		
		case Config.authorType: case Config.paperType: case Config.confType: case Config.termType:
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
            case Config.authorType:
                return new Author(id, null);
            case Config.termType:
                return new Term(id, null);
            case Config.paperType:
                return new Paper(id, null);
            case Config.confType:
                return new Conf(id, null);
        }

        return null;
    }

    private void removeEdgeFromTypeToNode(Node node, String type) {
        for (Node a: elements.get(type).keySet()) {
            a.removeEdge(node);
        }
    }
	
	
	//Metodes Publics

    /**
     * Constructora. Crea un graf buit.
     */
    public Graph () {
		elements = new TreeMap<>();
		elements.put(Config.authorType, new HashMap<>());
		elements.put(Config.paperType, new HashMap<>());
		elements.put(Config.confType,new HashMap<>());
		elements.put(Config.termType,new HashMap<>());

        dicNameNodes = new HashMap<>();

        maxID = 0;
	}

	/**
	 * Consultora dels nodes del graf.
	 * @return: un conjunt amb tots els nodes del graf
	 */
	public Set<Node> getSetOfNodes() {
		HashSet<Node> res = new HashSet<Node>();
		res.addAll(elements.get(Config.authorType).keySet());
		res.addAll(elements.get(Config.paperType).keySet());
		res.addAll(elements.get(Config.confType).keySet());
		res.addAll(elements.get(Config.termType).keySet());
		
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
			return elements.get(type).keySet();
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
                        forRet.add(intern.get(i)); //si es del tipus indicat
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
            ArrayList<Node> forDic = new ArrayList<>(1); //1 per estalviar memoria
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
	 */
	public void addEdge(Node a, Node b) throws DomainException {
        a.addEdge(b);
        b.addEdge(a);
	}

    /**
     * Esborra un node del graf.
     * @param node Node extret directament del graf (amb <em>getNode()</em>
     *             o be <em>getNodes()</em>
     */
	public void removeNode(Node node) {
        if (dicNameNodes.containsKey(node.getName())) {
			dicNameNodes.get(node.getName()).remove(node);
		}
        if (node.getType().equals(Config.paperType)) {
            removeEdgeFromTypeToNode(node, Config.termType);
            removeEdgeFromTypeToNode(node, Config.authorType);
            removeEdgeFromTypeToNode(node, Config.confType);
        }
        else {
            removeEdgeFromTypeToNode(node, Config.paperType);
        }
        elements.get(node.getType()).remove(node);
    }

    /**
     * Esborra, si existeix, l'aresta de <em>a</em> a <em>b</em>.
     * @param a Node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     * @param b Node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     */
	public void removeEdge(Node a, Node b) {
        a.removeEdge(b);
        b.removeEdge(a);
	}

}