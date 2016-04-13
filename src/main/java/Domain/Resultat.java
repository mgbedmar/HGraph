package Domain;
import java.util.*;
import Domain.Graph.*;

public class Resultat {
    private class Fila {
	public ArrayList<Node> nodes;
	public float hs;
	public byte marcat;

	public Fila(int n, Node a, Node b, float hs) {
	    if (n == 1 || n == 2) n = 1; //nombre de nodes
	    else n = 2;
	    this.nodes = new ArrayList<Node>(n);
	    this.nodes.add(a);
	    this.hs = hs;
	    if (n == 2) this.nodes.add(b);
	}
    }

    private ArrayList<Fila> res;
    private int n; //nombre de columnes
    private int currentIndex; //l'index del que s'ha de retornar

    /** 
     * Constructora. 
     * @param n: nombre de columnes 
     */
    public Resultat(int n) {
	this.res = new ArrayList<Fila>();
	this.n = n;
	this.currentIndex = 0;
    }

    /**
     * Afegeix una fila amb nomes el node a
     * @param a: node de la fila
     */
    public void afegirFila(Node a) {
	Fila f = new Fila(n, a, null, -1);
	this.res.add(f);
    }

    /**
     * Afegeix una fila amb el node a i el seu HS
     * @param a: node de la fila
     * @param hs: mesura HeteSim del node a
     */
    public void afegirFila(Node a, float hs) {
	Fila f = new Fila(n, a, null, hs);
	this.res.add(f);
    }

    /**
     * Afegeix una fila amb dos nodes i el seu HS.
     * @param a: primer node de la fila
     * @param b: segon node de la fila
     * @param hs: mesura HeteSim del node a
     */
    public void afegirFila(Node a, Node b, float hs) {
	Fila f = new Fila(n, a, b, hs);
	this.res.add(f);
    }

    /**
     * Retorna la fila que toca des de la darrera 
     * vegada que es va ordenar o filtrar. 
     * @return El primer element del ArrayList es un 
     * enter unic per identificar la fila, els seguents 
     * son n dades (el nom del node si es un node, el float 
     * en String si es el HS). Retorna null si no queden files.
     */
    public ArrayList<String> obtenirFila() {
	if (this.currentIndex < res.size()) {
	    ArrayList<String> fila = new ArrayList<String>(1+this.n);
	    Fila f = res.get(currentIndex);

	    /* Muntem la fila a retornar */
	    String str = String.valueOf(currentIndex);
	    fila.add(str);
	    for (int i = 0; i < f.nodes.size(); i++) {
		str = f.nodes.get(i).getName();
		fila.add(str);
	    }
	    if (f.hs >= 0) { //si en la consulta hi ha HS
		str = String.valueOf(f.hs);
		fila.add(str);
	    }

	    /* Incrementem index per la seguent consulta */
	    ++currentIndex;
	    
	    return fila;
	}
	
	else return null;
    }


    /**
     * Ordena el resultat segons el valor de la columna 
     * numCol, ascendentment si ascend es cert i 
     * descendentment si es fals.
     */
    public void ordenar(int numCol, boolean ascend) {
	this.currentIndex = 0;
	
    }

    /**
     * Filtra per nom, nomes es mostren els resultats 
     * on apareix el nom.
     */
    public int filtrar(String nom) {
	return 1;
    }
}
