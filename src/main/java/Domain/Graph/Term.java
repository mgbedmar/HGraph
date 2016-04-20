package Domain.Graph;

import java.util.HashSet;
import java.util.Set;
import Domain.Config;
import Domain.DomainException;

public class Term extends Node {
    public final static String TYPE = Config.termType;
    private HashSet<Node> paperadj;

    /**
     *
     * @param name
     */
    public Term(String name) {
        super(name);
        paperadj = new HashSet<>();
    }

    public Term(int id, String name) {
        super(id, name);
        paperadj = new HashSet<>();
    }

    public String getType() {
        return Term.TYPE;
    }

    void addEdge(Node node) throws DomainException
    {
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
    void removeEdge(Node node) throws DomainException {
        if(node.getType().equals(Paper.TYPE))
            paperadj.remove(node);
        else
            throw new DomainException("No es pot esborrar una aresta amb node font tipus '"+
                    this.TYPE+"' i node destí '"+node.getType()+"'");
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
