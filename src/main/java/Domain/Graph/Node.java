package Domain.Graph;
import Domain.DomainException;

import java.util.Set;

public abstract class Node {
    private int id;
    private String name;
    
    /**
     * Constructor.
     * @param name: el nom del node
     */
    public Node(String name) {
    	this.name = name;
        this.id = -1;
    }

    /**
     * Constructor.
     * @param name: el nom del node
     */
    public Node(int id, String name) {
        this.name = name;
        this.id = id;
    }

    /**
     * Getter.
     * @return id: identificador
     */
    public int getID() {
    	return this.id;
    }

    /**
     * Setter. Només un Graph pot editar la id
     * @param id
     */
    public void setID(Integer id)
    {
        this.id = id;
    }

    /**
     * Getter.
     * @return type: tipus del node
     */
    public abstract String getType();

    /**
     * Getter.
     * @return name: nom del node
     */
    public String getName() {
    	return this.name;
    }

    /**
     * Setter.
     * @param name: nom nou del node
     */
    public void setName(String name) {
    	this.name = name;
    }

    //dos nodes són iguals si són del mateix tipus i tenen la mateixa id
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Node)) return false;
        Node node = (Node) obj;
        return node.getType().equals(this.getType()) &&
                node.getID() == this.getID();
    }

    public int hashCode() {
        Integer a = new Integer(this.id);
        return a.hashCode();
    }

    /**
     * Getter.
     * @return conjunt de veins
     */
    abstract Set<Node> getNeighbours();

    /**
     * Getter.
     * @return conjunt de veins de tipus type
     */
    abstract Set<Node> getNeighbours(String type);

    /**
     * Afegeix una aresta del p.i. a node. No afegeix l'aresta
     * simetrica!
     */
    abstract void addEdge(Node node) throws DomainException;

    /**
     * Esborra l'aresta del p.i. a node. No esborra l'aresta
     * simetrica!
     */
    abstract void removeEdge(Node node);
}
