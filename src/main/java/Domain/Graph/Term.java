package Domain.Graph;

import java.util.HashSet;
import java.util.Set;

public class Term extends Node {
    public final static String TYPE = "term";
    private HashSet<Node> paperadj;

    /**
     *
     * @param name
     */
    public Term(String name) {
        super(name, Term.TYPE);
    }

    public Term(int id, String name) {
        super(id, name, Term.TYPE);
    }
    void addRelationship(Node node)
    {
        if(node.getType().equals(Paper.TYPE))
            paperadj.add(node);
    }

    /**
     *
     * @param node
     */
    void deleteRelationship(Node node)
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
        if(type == Paper.TYPE)
            return paperadj;
        else
            return null;
    }



}
