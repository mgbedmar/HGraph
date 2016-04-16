package Domain.Graph;



public class Term extends Node {
    private final static String type = "term";
    //Este atributo solo esta una vez por subclasse. Los nombres
    //serian "author", "term", "conf", "paper"

    public Term(int id, String name) { 
     super(id,name);
    }

    /**
     * Getter.
     * @return type: tipus del node
     */
    public String getType() {
	return Term.type; /*retorna el atributo static type*/
    }

    public boolean equals(Term term) {
	return this.getID() == term.getID(); /*iguales si y solo si tienen la misma id*/
    }

    /**
     * Afegeix una aresta del p.i. a node. No afegeix l'aresta
     * simetrica!
     */
    protected void addRelationship(Node node) {
	/*En el caso de Author, solo se comprobaria que 
	  el node que te pasan es de tipo Paper y se llamaria 
	  a super addRelationship. Esto solo cambia en el caso 
	  de la subclase Paper, donde se pueden anadir aristas 
	  a todos los otros tipos. Solo habria que mirar que 
	  el que te pasan no es otro Paper y llamar a la super
	*/
   super.addRelationship(node);
    }
   
}
