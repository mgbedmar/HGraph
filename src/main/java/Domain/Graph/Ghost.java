package Domain.Graph;

import Domain.DomainException;

import java.util.Set;

public class Ghost extends Node {
    public final static String TYPE = "ghost";


    /**
     * Crea un node de tipus ghost. Aquest node es fa servir per calcular
     * l'HeteSim
     * @param id id del node
     */
    public Ghost(int id) {
    	super(id, null);
    }


    /**
     * Retorna un string que representa el tipus ghost
     * @return type: tipus del node
     */
    public String getType() {
	return Ghost.TYPE;
    }


    Set<Node> getNeighbours() {
        return null;
    }


    Set<Node> getNeighbours(String type) {
        return null;
    }


    void addEdge(Node node) throws DomainException {
        throw new DomainException("S'ha intentat afegir una aresta a un node ghost.");
    }


    void removeEdge(Node node) throws DomainException {
        throw new DomainException("S'ha intentat eliminar una aresta a un node ghost.");
    }


}
