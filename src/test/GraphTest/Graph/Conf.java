package GraphTest.Graph;

import java.util.HashSet;
import java.util.Set;
import GraphTest.Graph.*;

public class Conf extends Node {
    public final static String TYPE = Config.confType;
    private HashSet<Node> paperadj;

    /**
     *
     * @param name
     */
    public Conf(String name) {
        super(name);
        paperadj = new HashSet<>();
    }

    public Conf(int id, String name) {
        super(id, name);
        paperadj = new HashSet<>();
    }

    public String getType() {
        return Conf.TYPE;
    }

    void addEdge(Node node) throws DomainException {
        if(node.getType().equals(Paper.TYPE))
            paperadj.add(node);
        else
        throw new DomainException("No es pot afegir una aresta amb node font tipus '"+
                this.TYPE+"' i node destí '"+node.getType()+"'");
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
            return new HashSet<Node>();
    }



}
