package Domain.Graph;
import java.util.HashSet;
import java.util.Set;

public abstract class Node {
    private int id;
    private String name;
    private String type;
    
    /**
     * Constructor.
     * @param name: el nom del node
     */
    public Node(String name, String type) {
    	this.name = name;
        this.id = -1;
        this.type = type;
    }

    /**
     * Constructor.
     * @param name: el nom del node
     */
    public Node(int id, String name, String type) {
        this.name = name;
        this.id = id;
        this.type = type;
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
    public String getType()
    {
        return this.type;
    }

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
    public boolean equals(Node node)
    {
        return node.getType().equals(this.getType()) &&
                node.getID() == this.getID();
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
    abstract void addEdge(Node node);

    /**
     * Esborra l'aresta del p.i. a node. No esborra l'aresta
     * simetrica!
     */
    abstract void deleteEdge(Node node);
}
