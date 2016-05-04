package Domain.Graph;
import Domain.DomainException;

import java.util.*;

/**
 * @author Dani i Mireia
 */
public class Graph {
    /**
     * Les entrades del <em>TreeMap</em> son "author", "term", "conf" i "paper". Cadascuna
     * te com a valors un <em>HashMap</em> que te com a conjunt de claus tots els nodes del graf
     * i cada node te com a valor a ell mateix. D'aquesta manera es pot trobar un node rapidament
     * i retornar la versio amb adjacencies d'aquest node (el Set no ho permet).
     */
	private TreeMap<String,HashMap<Node,Node>> elements;

    /**
     * Diccionari que conte, per cada nom que estigui al graf, la llista de nodes que tenen
     * aquest nom. Gestiona noms repetits i permet trobar rapidament nodes al graf a partir del seu nom.
     */
    private HashMap<String,ArrayList<Node>> dicNameNodes;

    /**
     * Conte la id mes gran que s'ha ficat a un node del graf. S'utilitza per generar ids uniques.
     */
    private int maxID; //la id mes gran que s'ha ficat al graf
	

    //Metodes privats per garantir la no redundancia

    /**
     * Comprova si el tipus es correcte
     * @param t el <em>String</em> que cal comprovar si correspon a un tipus valid
     * @throws DomainException si el tipus <em>t</em> no es valid
     */
	private void checkType(String t) throws DomainException
    {
		switch (t)
        {
			case Author.TYPE:
            case Paper.TYPE:
            case Conf.TYPE:
            case Term.TYPE:
				break;
			default:
				throw new DomainException("El tipus '"+t+"' no existeix.");
		}
	}

    /**
     * Crea un node auxiliar per fer cerca
     * @param id la id del node
     * @param type tipus del node
     * @return Un node amb id <em>id</em>, tipus <em>type</em> i nom <em>null</em>
     * @throws DomainException si el tipus no es valid
     */
    private Node createNode(int id, String type) throws DomainException
    {
        checkType(type);
        switch(type)
        {
            case Author.TYPE:
                return new Author(id, null);
            case Term.TYPE:
                return new Term(id, null);
            case Paper.TYPE:
                return new Paper(id, null);
            default:
                return new Conf(id, null);
        }

    }

    /**
     * Esborra totes les arestes de nodes cap a <em>node</em>
     * @param node el node desti
     * @throws DomainException si l'aresta no existeix (impossible)
     */
    private void removeEdgesToNode(Node node) throws DomainException {
        for (Node a: node.getNeighbours())
        {
            a.removeEdge(node);
        }
    }
	
	
	//Metodes Publics

    /**
     * Constructora. Crea un graf buit.
     */
    public Graph() {
		elements = new TreeMap<>();
		elements.put(Author.TYPE, new HashMap<>());
		elements.put(Paper.TYPE, new HashMap<>());
		elements.put(Conf.TYPE,new HashMap<>());
		elements.put(Term.TYPE,new HashMap<>());

        dicNameNodes = new HashMap<>();

        maxID = 0;
	}

	/**
	 * Consultora dels nodes del graf.
	 * @return Un conjunt amb tots els nodes del graf
	 */
	public Set<Node> getSetOfNodes()
    {
		HashSet<Node> res = new HashSet<Node>();
		res.addAll(elements.get(Author.TYPE).keySet());
		res.addAll(elements.get(Paper.TYPE).keySet());
		res.addAll(elements.get(Conf.TYPE).keySet());
		res.addAll(elements.get(Term.TYPE).keySet());
		
		return res;		
	}

    /**
     * Consultora dels nodes d'un tipus donat.
     * @param type tipus de node
     * @return Tots els nodes del graf del tipus <em>type</em>
     * Modificacions en el conjunt de retorn canviaran l'estat del graf.
     * @throws DomainException si el tipus no existeix
     */
	public Set<Node> getSetOfNodes(String type) throws DomainException
    {
		checkType(type);
		return elements.get(type).keySet();
	}

	/**
	 * Obte un node a partir de la id i el tipus.
	 * @param id id del node que es busca
	 * @param type tipus del node que es busca
	 * @return El node de tipus <em>type</em> i id <em>id</em>
     * @throws DomainException si el tipus no existeix
	 */
	public Node getNode(int id, String type) throws DomainException
    {
		checkType(type);
		Node aux = createNode(id, type);
		Node g = elements.get(type).get(aux);
        if(g == null)
            throw new DomainException("El node amb id '"+id+"' i tipus '"+type+"' no existeix");

        return g;
	    
	}

	/**
	 * Obte tots els nodes d'un determinat tipus i nom.
	 * @param name nom dels nodes buscats
	 * @param type tipus dels nodes buscats
	 * @return Llista de nodes que tenen nom <em>name</em> i tipus <em>type</em>
     * @throws DomainException si el tipus no existeix o no hi ha cap node amb
     * nom <em>name</em> i tipus <em>type</em>
	 */
	public ArrayList<Node> getNodes(String name, String type) throws DomainException
    {
		checkType(type);
		ArrayList<Node> intern = dicNameNodes.get(name);
        if(intern == null)
            throw new DomainException("El nom '"+name+"' no correspon a cap node");

        ArrayList<Node> forRet = new ArrayList<>();
        for (int i = 0; i < intern.size(); i++)
        {
            if (intern.get(i).getType().equals(type))
            {
                forRet.add(intern.get(i)); //si es del tipus indicat
            }
        }

        if(forRet.isEmpty())
            throw new DomainException("El nom '"+name+"' no correspon a cap node de tipus '"+type+"'.");

        return forRet;

	}


	/**
	 * Consultora dels veins d'un node.
	 * @param node node del que es volen consultar els veins. Ha de ser un node
     *             obtingut del graf amb <em>getNode()</em> o <em>getNodes()</em>
	 * @return Conjunt de veins de <em>node</em>. Modificacions en el conjunt de retorn
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
	 * @return Un conjunt amb els veins de tipus <em>type</em> del node <em>node</em>.
     * Modificacions en el conjunt de retorn
     * canviaran l'estat del graf.
     * @throws DomainException si es produeix a les capes inferiors
	 */
	public Set<Node> getNeighbours(Node node, String type) throws DomainException
    {
		checkType(type);
		return node.getNeighbours(type);

	}

    /**
     * Afegeix un node al graf.
     * @param node node que es vol afegir al graf. Si te <em>id</em> negativa,
     *             es genera una id unica per ell. Si no, es presuposa que
     *             la id es unica i s'afegeix.
     */
    public void addNode(Node node)
    {
        if (node.getID() < 0)
        {
            ++maxID;
            node.setID(maxID);
        }
        else if (node.getID() > maxID)
            maxID = node.getID();

        elements.get(node.getType()).put(node, node);
        if (dicNameNodes.containsKey(node.getName()))
        {
            dicNameNodes.get(node.getName()).add(node);
        }
        else
        {
            ArrayList<Node> forDic = new ArrayList<>(1); //1 per estalviar memoria
            forDic.add(node);
            dicNameNodes.put(node.getName(), forDic);
        }
    }
	

	/**
	 * Afegeix una aresta de <em>a</em> a <em>b</em> (i la simetrica).
	 * @param a node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
	 * @param b node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     * @throws DomainException si es produeix a les capes inferiors
	 */
	public void addEdge(Node a, Node b) throws DomainException
    {
        a.addEdge(b);
        b.addEdge(a);
	}

    /**
     * Esborra un node del graf.
     * @param node node extret directament del graf (amb <em>getNode()</em>
     *             o be <em>getNodes()</em>
     * @throws DomainException si es produeix a les capes inferiors
     */
	public void removeNode(Node node) throws DomainException {
        if (dicNameNodes.containsKey(node.getName()))
        {
			dicNameNodes.get(node.getName()).remove(node);
		}
        else
            throw new DomainException("No existeix un node amb nom '"+node.getName()+"'");

        if(null == elements.get(node.getType()).remove(node))
            throw new DomainException("No existeix un node amb nom '"+node.getName()+"' i tipus '"+node.getType()+"'");

        removeEdgesToNode(node);

    }

    /**
     * Esborra, si existeix, l'aresta de <em>a</em> a <em>b</em>.
     * @param a node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     * @param b node extret directament del graf (amb <em>getNode()</em>
     *          o be <em>getNodes()</em>
     * @throws DomainException si es produeix a les capes inferiors
     */
	public void removeEdge(Node a, Node b) throws DomainException {
        a.removeEdge(b);
        b.removeEdge(a);
	}

    public void setNodeName(Node node, String newName){

        dicNameNodes.get(node.getName()).remove(node);

        node.setName(newName);
        ArrayList<Node> a = new ArrayList<>();
        a.add(node);
        dicNameNodes.put(newName, a);
    }

}