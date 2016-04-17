package Domain.Graph;

import java.util.HashSet;
import java.util.Set;

public class Conf extends Node {
    public final static String TYPE = "conf";
    private HashSet<Node> paperadj;

    /**
     *
     * @param name
     */
    public Conf(String name) {
        super(name, Conf.TYPE);
    }

    public Conf(int id, String name) {
        super(id, name, Conf.TYPE);
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
    void deleteEdge(Node node)
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
