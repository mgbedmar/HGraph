package HeteSimTest.Graph;

import java.util.HashSet;
import java.util.Set;

public class Conf extends Node {
    public final static String TYPE = "conf";
    private HashSet<Node> paperadj;

    /**
     * Crea un node de tipus conferènecia
     * @param name
     */
    public Conf(String name) {
        super(name);
        paperadj = new HashSet<>();
    }

    /**
     * Crea un node de tipus conferènecia. La <em>id</em> l'identifica inequivocament d'un altre conferènecia
     * @param id ID unica de conferènecia
     * @param name
     */
    public Conf(int id, String name) {
        super(id, name);
        paperadj = new HashSet<>();
    }

    /**
     * Retorna una string que representa el tipus conferència
     * @return type
     */
    public String getType() {
        return Conf.TYPE;
    }


    /**
     * Afegeix una aresta que va desde el p.i. a <em>node</em>
     * @param node
     * @throws DomainException Si el node no es de tipus paper
     */
    void addEdge(Node node) throws DomainException {
        if(node.getType().equals(Paper.TYPE))
            paperadj.add(node);
        else
            throw new DomainException("No es pot afegir una aresta amb node font tipus '"+
                    this.TYPE+"' i node destí '"+node.getType()+"'");
    }

    /**
     * Esborra la areta formada per el p.i. i <em>node</em>
     * @param node
     * @throws DomainException Si l'aresta es de tipus incompatibles o si no existeix l'aresta
     */
    void removeEdge(Node node) throws DomainException
    {
        if(node.getType().equals(Paper.TYPE)) {
            if (!paperadj.remove(node)) {
                throw new DomainException("No existeix una aresta entre els dos nodes");
            }
        }
        else {
            throw new DomainException("No es pot esborrar una aresta amb node font tipus '"+
                    this.TYPE+"' i node destí '"+node.getType()+"'");

        }
    }

    /**
     * Obté els veïns del p.i.
     * @return
     */
    Set<Node> getNeighbours()
    {
        return paperadj;
    }

    /**
     * Obté els veïns d'un cert tipus
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
