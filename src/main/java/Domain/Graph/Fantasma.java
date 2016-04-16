package Domain.Graph;

public class Fantasma extends Node {
    private final static String type = "E";
    //Este atributo solo esta una vez por subclasse. Los nombres
    //serian "author", "term", "conf", "paper"

    public Fantasma(int id, String name) { 
       super(id,name);
    }
    public Fantasma(int id) {
       
    }
    /**
     * Getter.
     * @return type: tipus del node
     */
    public String getType() {
	return Fantasma.type;//retorna el atributo static type
    }

    public boolean equals(Fantasma fant) {
	return this.getID() == fant.getID();//iguales si y solo si tienen la misma id
    }
   
}
