package Domain.Graph;

public class Ghost extends Node {
    private final static String type = "E";
    //Este atributo solo esta una vez por subclasse. Los nombres
    //serian "author", "term", "conf", "paper"

    public Ghost(int id, String name) { }

    public Ghost(int id) {

    }
    /**
     * Getter.
     * @return type: tipus del node
     */
    public String getType() {
	return Ghost.type;//retorna el atributo static type
    }

    public boolean equals(Ghost fant) {
	return this.getID() == fant.getID();//iguales si y solo si tienen la misma id
    }
   
}
