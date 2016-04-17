package Domain.Graph;

import java.util.HashSet;
import java.util.Set;

public class Paper extends Node {
    public final static String TYPE = "paper";
    private HashSet<Node> termadj;
    private HashSet<Node> confadj;
	private HashSet<Node> authoradj;

    /**
     *
     * @param name
     */
    public Paper(String name) {
    	super(name, Paper.TYPE);
    }

    public Paper(int id, String name) {
        super(id, name, Paper.TYPE);
    }

	void addRelationship(Node node)
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
    void deleteRelationship(Node node)
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
