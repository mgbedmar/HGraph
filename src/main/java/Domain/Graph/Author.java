package Domain.Graph;

import java.util.HashSet;
import java.util.Set;
import Domain.Config;

public class Author extends Node {
    public final static String TYPE = Config.authorType;
    private HashSet<Node> paperadj;

    /**
     *
     * @param name
     */
    public Author(String name) {
        super(name);
    }

    public Author(int id, String name) {
        super(id, name);
    }

    public String getType() {
        return Author.TYPE;
    }

    void addEdge(Node node)
    {
        if(node.getType().equals(Paper.TYPE))
            paperadj.add(node);
    }

    /**
     *
     * @param node
     */
    void removeEdge(Node node)
    {
        if(node.getType().equals(Paper.TYPE))
            paperadj.remove(node);
    }

    /**
     *
     * @return
     */
    Set<Node> getNeighbours()
    {
        return paperadj;
    }

    /**
     *
     * @param type
     * @return
     */
    Set<Node> getNeighbours(String type)
    {
        if(type.equals(Paper.TYPE))
            return paperadj;
        else
            return null;
    }


}
