package Domain.Graph;
import java.util.HashSet;
import java.util.Set;

public abstract class Node {
    private int id;
    private String name;
    private HashSet<Node> adj;
    
    /**x
     * Constructor.
     * @param id: identificador
     * @param name: el nom del node
     */
    /*public Node(int id, String name) {
	
    }*/

    /**
     * Getter.
     * @return id: identificador
     */
    public int getID() {
	return this.id;
    }

    /**
     * Getter.
     * @return type: tipus del node
     */
    public abstract String getType();

    /**
     * Getter.
     * @return name: nom del node
     */
    public String getName() {
	return this.name;
    }

    /**
     * Setter.
     * @param name: nom nou del node
     */
    public void setName(String name) {

    }

    /* public boolean equals(Node node) {

    } //implementada a les subclasses
    //dos nodes són iguals si són del mateix tipus i tenen la mateixa id*/

    /**
     * Getter.
     * @return conjunt de veins
     */
    protected Set<Node> getNeighbours() {
	/*ojo! tiene que retornar una copia del Set privado,
	 *porque si no se podrían modificar los vecinos desde
	 *fuera. La copia tiene que ser shallow copy: es decir, 
	 *tiene que hacer copia del HashSet pero no de los nodos.
	 *Sirve con un clone() */
	return null;
    }

    /**
     * Getter.
     * @return conjunt de veins de tipus type
     */
    protected Set<Node> getNeighbours(String type) {
	/*aqui solo se comprueba si el type es paper, y si lo 
	  es, se llama a getNeighbours(). Este metodo hay 
	  que reimplementarlo en la subclase Paper, porque 
	  sus vecinos son de otros tipos. En ese caso hay que 
	  explorar el HashSet y devolver los que toquen.
	*/
	return null;
    }

    /**
     * Afegeix una aresta del p.i. a node. No afegeix l'aresta
     * simetrica!
     */
    protected void afegirAresta(Node node) {
	/* Cuidado, este metodo es delicado. Aqui se anade la 
	   arista sin mas, se anade node al HashSet y ya esta.
	   Despues, en las subclases hay que tener cuidado de
	   controlar que el tipo sea el correcto.
	 */
    }

    /**
     * Esborra l'aresta del p.i. a node. No esborra l'aresta
     * simetrica!
     * @return TRUE si i nomes si l'aresta existia
     */
    protected boolean esborrarAresta(Node node) {
	return true; //En principio esta se implementaria solo aqui
    }
}


