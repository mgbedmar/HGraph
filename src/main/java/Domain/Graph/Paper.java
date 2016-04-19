package Domain.Graph;

import java.util.HashSet;
import java.util.Set;
import Domain.Config;

public class Paper extends Node {
    public final static String TYPE = Config.paperType;
    private HashSet<Node> termadj;
    private HashSet<Node> confadj;
	private HashSet<Node> authoradj;

    /**
     *
     * @param name
     */
    public Paper(String name) {
    	super(name);
        termadj = new HashSet<>();
        confadj = new HashSet<>();
        authoradj = new HashSet<>();
    }

    public Paper(int id, String name) {
        super(id, name);
    }

    public String getType() {
        return Paper.TYPE;
    }

	void addEdge(Node node)
    {
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
    	}
    }

    /**
     *
     * @param node
     */
    void removeEdge(Node node)
    {
        switch(node.getType())
        {
            case Conf.TYPE:
                confadj.remove(node);
                break;
            case Author.TYPE:
                authoradj.remove(node);
                break;
            case Term.TYPE:
                termadj.remove(node);
                break;
        }
    }

    /**
     *
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
     *
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
    	    	return null;
    	}
    }


    
}
