package GraphTest.Graph;

import java.util.Set;
import GraphTest.Graph.*;

public class Ghost extends Node {
    public final static String TYPE = Config.ghostType;

       

    public Ghost(int id) {
    	super(id, null);
    }


    /**
     * Getter.
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


    void addEdge(Node node) {

    }


    void removeEdge(Node node) {

    }


}
