package Domain.Graph;

import java.util.HashSet;
import java.util.Set;
import Domain.DomainException;

public class Paper extends Node {
    public final static String TYPE = "paper";
    private HashSet<Node> termadj;
    private HashSet<Node> confadj;
	private HashSet<Node> authoradj;

    /**
     * Crea un node de tipus paper
     * @param name
     */
    public Paper(String name) {
    	super(name);
        termadj = new HashSet<>();
        confadj = new HashSet<>();
        authoradj = new HashSet<>();
    }

    /**
     * Crea un node de tipus paper. La <em>id</em> l'identifica inequivocament d'un altre paper
     * @param id ID unica d'paper
     * @param name
     */
    public Paper(int id, String name) {
        super(id, name);
        termadj = new HashSet<>();
        confadj = new HashSet<>();
        authoradj = new HashSet<>();
    }

    /**
     * Retorna una string que representa el tipus paper
     * @return type
     */
    public String getType() {
        return Paper.TYPE;
    }

    /**
     * Afegeix una aresta que va desde el p.i. a <em>node</em>
     * @param node
     * @throws DomainException Si el node no es de tipus correcte
     */
	void addEdge(Node node) throws DomainException {
        switch(node.getType())
        {
    		case Conf.TYPE:
    			this.confadj.add(node);
    			break;
    		case Term.TYPE:
    			this.termadj.add(node);
    			break;
    		case Author.TYPE:
    			this.authoradj.add(node);
    		    break;
            default:
                throw new DomainException("No es pot afegir una aresta amb node font tipus '"+
                        this.TYPE+"' i node destí '"+node.getType()+"'");
    	}
    }

    /**
     * Esborra la areta formada per el p.i. i <em>node</em>
     * @param node
     * @throws DomainException Si l'aresta es de tipus incompatibles o si no existeix l'aresta
     */
    void removeEdge(Node node) throws DomainException {
        boolean existia = false;
        switch(node.getType())
        {
            case Conf.TYPE:
                if (confadj.remove(node)) existia = true;
                break;
            case Author.TYPE:
                if (authoradj.remove(node)) existia = true;
                break;
            case Term.TYPE:
                if (termadj.remove(node)) existia = true;
                break;
            default:
                throw new DomainException("No es pot esborrar una aresta amb node font tipus '"+
                        this.TYPE+"' i node destí '"+node.getType()+"'");
        }

        if (!existia)
            throw new DomainException("No existeix una aresta entre els dos nodes");
    }

    /**
     * Obté els veïns del p.i.
     * @return
     */
    Set<Node> getNeighbours()
    {
        HashSet<Node> r = new HashSet<>();
        r.addAll(this.confadj);
        r.addAll(this.authoradj);
        r.addAll(this.termadj);
        return r;
    }

    /**
     * Obté els veïns d'un cert tipus
     * @param type
     * @return
     */
	Set<Node> getNeighbours(String type)
    {

    	switch(type)
        {
    		case Conf.TYPE:
    			return confadj;
    		case Author.TYPE:
    			return authoradj;
    		case Term.TYPE:
    	        return termadj;
    	    default:
                return new HashSet<Node>();
    	}
    }


    
}
