package Domain.Graph;

import java.util.HashSet;
import java.util.Set;

public class Paper extends Node {
    private final static String type = "paper";
    //Este atributo solo esta una vez por subclasse. Los nombres
    //serian "author", "term", "conf", "paper"
    //los autores estan en la lista de adyacencias de node
    private HashSet<Node> termadj;
    private HashSet<Node> confadj;

    public Paper(int id, String name) { 
    	super(id,name);
    	
    	
    }
    /**
     * Getter.
     * @return type: tipus del node
     */
    public String getType() {
	return Paper.type;//retorna el atributo static type
    }

    public boolean equals(Paper paper) {
	return this.getID() == paper.getID();//iguales si y solo si tienen la misma id
    }

    /**
     * Afegeix una Relationship del p.i. a node. No afegeix l'aresta
     * simetrica!
     */
    public Set<Node> getConfadj(){
    	return confadj;
    }
    public Set<Node> getTermadj(){
    	return termadj;
    }
    public Set<Node> getAutoradj(){
    	return super.getNeighbours();
    }
    
    protected void addRelationship(Node node) {
	/*En el caso de Author, solo se comprobaria que 
	  el node que te pasan es de tipo Paper y se llamaria 
	  a super addRelationship. Esto solo cambia en el caso 
	  de la subclase Paper, donde se pueden anadir aristas 
	  a todos los otros tipos. Solo habria que mirar que 
	  el que te pasan no es otro Paper y llamar a la super
	*/
    	if(!node.getType().equals("paper"))
    		switch(node.getType()){
    		case "Conf":
    			this.confadj.add(node);
    			break;
    		case "Term":
    			this.termadj.add(node);
    			break;
    		case "Author":
    			super.addRelationship(node);
    		    break;
    		    
    		}
    }
    protected Set<Node> getNeighbours(String type) {
    	
    	HashSet<Node> miset= new HashSet<Node>();
    	switch(type){
    		case "conf":
    			miset=(HashSet<Node>)this.getConfadj();
    		    break;
    		case "autor":
    			miset=(HashSet<Node>)this.getAutoradj();
    		    break;
    		case "term":
    	        miset=(HashSet<Node>)this.getTermadj();
    	    default:
    	    	miset= null;
    	   
    	}
    	
    	return miset;
    }
    
}
