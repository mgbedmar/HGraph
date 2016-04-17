package Domain.Graph;

import java.util.HashSet;
import java.util.Set;
import Domain.Config;

public class Term extends Node {
    public final static String TYPE = Config.termType;
    private HashSet<Node> paperadj;

    /**
     *
     * @param name
     */
    public Term(String name) {
        super(name);
    }

    public Term(int id, String name) {
        super(id, name);
    }

    public String getType() {
        return Term.TYPE;
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
