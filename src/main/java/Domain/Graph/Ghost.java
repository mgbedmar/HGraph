package Domain.Graph;

import java.util.Set;

public class Ghost extends Node {
    public final static String TYPE = "E";

       

    public Ghost(String name) {
    	super(name, Ghost.TYPE);
    }

    public Ghost(int id) {
        super("",Ghost.TYPE);
        this.setID(id);

    }
    /**
     * Getter.
     * @return type: tipus del node
     */
    public String getType() {
	return Ghost.TYPE;//retorna el atributo static type
    }


    Set<Node> getNeighbours() {
        return null;
    }


    Set<Node> getNeighbours(String type) {
        return null;
    }


    void addRelationship(Node node) {

    }


    void deleteRelationship(Node node) {

    }


}
