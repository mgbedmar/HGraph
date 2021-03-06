package Domain.Graph;
import Domain.DomainException;

import java.util.Set;

/**
 * @author Alejandro i Gerard
 */
public abstract class Node {
    private int id;
    private String name;
    
    /**
     * Crea un node
     * @param name: el nom del node
     */
    public Node(String name) {
    	this.name = name;
        this.id = -1;
    }

    /**
     * Crea un node amb id
     * @param name  el nom del node
     * @param id id del node
     */
    public Node(int id, String name) {
        this.name = name;
        this.id = id;
    }

    /**
     * Getter. Retorna la id del node
     * @return id: identificador
     */
    public int getID() {
    	return this.id;
    }

    /**
     * Setter. Nomes un Graph pot editar la id
     * @param id nova id
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
    void setName(String name) {
    	this.name = name;
    }

    /**
     * Dos nodes son iguals si son del mateix tipus i tenen la mateixa id
     * @param obj objecte per comparar
     * @return <em>true</em> si i nomes si son iguals
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Node)) return false;
        Node node = (Node) obj;
        return node.getType().equals(this.getType()) &&
                node.getID() == this.getID();
    }

    /**
     * Calcula un hashCode per un node.
     * @return hashcode
     */
    @Override
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
     * @param type tipus dels veins que es retornen
     * @return conjunt de veins de tipus <em>type</em>
     */
    abstract Set<Node> getNeighbours(String type);

    /**
     * Afegeix una aresta del p.i. a <em>node</em>. <b>No afegeix l'aresta
     * simetrica!</b>
     * @param node destí
     * @throws DomainException si no existeix el node
     */
    abstract void addEdge(Node node) throws DomainException;

    /**
     * Esborra l'aresta del p.i. a node. <b>No esborra l'aresta
     * simetrica!</b>
     * @param node destí
     * @throws DomainException si no existeix el node o l'aresta
     */
    abstract void removeEdge(Node node) throws DomainException;
}
