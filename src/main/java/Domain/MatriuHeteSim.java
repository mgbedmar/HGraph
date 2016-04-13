package Domain;
import java.util.*;
import Domain.Graph.*;


public class MatriuHeteSim {
    private HashMap<Node, HashMap<Node, Float>> m; 
    private int numRows;
    private int numCols;


    /**
     * Constructora. Crea una matriu buida per poder fer un producte
     * a dins.
     */
    public MatriuHeteSim() {
	this.numRows = this.numCols = 0;
	this.m = new HashMap<Node, HashMap<Node, Float>>();
    }
    
    /**
     * Constructora (cas en que no es matriu intermitja). 
     * Crea la matriu U_tRow,TCol (transposada si i nomes 
     * si tr es cert)
     */
    public MatriuHeteSim(boolean tr, Graph graf,
			 String tRow, String tCol) {
	if (tr) initMatriu(graf, tCol, tRow);
	else initMatriu(graf, tRow, tCol);
    }

    private void initMatriu(Graph graf, String tRow, String tCol) {
	Set<Node> rows = graf.getSetOfNodes(tRow);
	this.numRows = rows.size();
	Set<Node> cols = graf.getSetOfNodes(tCol);
	this.numCols = cols.size();

	this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
	float one = 1;
	
	for (Node i: rows) {
	    HashMap<Node, Float> dicCol =
		    new HashMap<Node, Float>(/* parametres */);
	    cols = graf.getNeighbours(i, tCol);
	    if (!cols.isEmpty()) {
		Float v = new Float(one/((float)cols.size()));
		/* El nombre de columnes diferents de zero es el
		   que usem per normalitzar (per files) */
		for (Node j: cols) {
		    dicCol.put(j, v);
		}
	    }
	    this.m.put(i, dicCol);
	}
    }
    
    /**
     * Constructora. Crea la matriu U_tRow,E si u es cert, 
     * i crea 
     * V_E,TCol transposta si u es fals.
     */
    public MatriuHeteSim(Graph graf, String tRow, String tCol, boolean u) {
	if (u) initMatriuIntermitja(graf, tRow, tCol);
	else initMatriuIntermitja(graf, tCol, tRow); //transposta
    }

    private void initMatriuIntermitja(Graph graf, String tRow, String tCol) {
	/* Agafem el conjunt que indexa les files */
	Set<Node> rows = graf.getSetOfNodes(tRow);
	this.numRows = rows.size();
	Set<Node> cols;

	/* Inicialitzem hashmap */
	this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
	float one = 1;
	int k = 0;

	for (Node i: rows) {
	    HashMap<Node, Float> dicCol =
		    new HashMap<Node, Float>(/* parametres */);
	    cols = graf.getNeighbours(i, tCol);
	    if (!cols.isEmpty()) {
		/* Normalitzar per files */
		Float v = new Float(one/((float)cols.size()));
		for (Node j: cols) {
		    Fantasma index = new Fantasma(k);		    
		    dicCol.put(index, v);
		    k++;
		}
	    }
	    this.m.put(i, dicCol);
	    this.numCols = k;
	}
    }


    /**
     * Getter.
     * @return nombre de files de la matriu
     */
    public int numRows() {
        return this.numRows;
    }

    /**
     * Getter.
     * @return nombre de columnes de la matriu
     */
    public int numCols() {
        return this.numCols;
    }

    /**
     * Getter. El node i ha de ser part de les files.
     * @return valor de l'entrada i, j
     */
    public float value(Node i, Node j) {
    	Float v = this.m.get(i).get(j);
	    if (v == null) return 0;
	    else return v.floatValue();
    }

    /**
     * Getter.
     * @return Conjunt d'elements que indexen les files
     */
    public Set<Node> rowKeys() {
	return this.m.keySet();
    }

    /**
     * Getter. El node i ha d'estar a la matriu
     * @param i: node que indexa la fila
     * @return Conjunt de cols no zero de la fila i
     */
    public Set<Node> cols(Node i) {
	return this.m.get(i).keySet();
    }

    /**
     * Setter. El node i ha d'estar a la matriu
     * @param i: node que indexa la fila
     * @param j: node que indexa la columna
     * @param v: nou valor de la posicio (i,j)
     */
    public void setValue(Node i, Node j, float v) {
        if (v == 0)
            this.m.get(i).remove(j);
        else {
            Float f = new Float(v);
            this.m.get(i).put(j, f);
        }
    }

    /**
     * Guarda al p.i. el producte per files de A i B, es a dir, 
     * el producte de A per B transposta. 
     */
    public void productePerFiles(MatriuHeteSim A, MatriuHeteSim B) {
	this.numRows = A.numRows();
	this.numCols = B.numRows();
	this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
	float cij;
	
	for (Node i: A.rowKeys()) {
	    HashMap<Node, Float> dicCols = new HashMap<Node, Float>();
	    for (Node j: B.rowKeys()) {
		if (A.cols(i).size() < B.cols(j).size())
		    cij = cij(A, B, i, j);
		else
		    cij = cij(B, A, j, i);

		if (cij != 0) {
		    Float v = new Float(cij);
		    dicCols.put(j, v);
		}
	    }
	    this.m.put(i, dicCols);
	}
    }
    
    /* A es la que te menys cols en i */
    private float cij(MatriuHeteSim A, MatriuHeteSim B, Node i, Node j) {
	float cij = 0;
	for (Node k: A.cols(i)) {
	    cij += A.value(i,k) * B.value(j,k);
	}
	return cij;
    }

    /**
     * Guarda al p.i. el producte de A i B. B ha de ser una matriu 
     * intermitja de la forma U_t,E
     */
    public void producteHabitualIntermitja(MatriuHeteSim A, MatriuHeteSim B) {
	this.numRows = A.numRows();
	this.numCols = B.numCols();
	this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
	float cij;
	Fantasma j;
	
	for (Node i: A.rowKeys()) {
	    HashMap<Node, Float> dicCols = new HashMap<Node, Float>();
	    for (int jint = 0; jint < B.numCols(); jint++) {
		cij = 0;
		j = new Fantasma(jint);
		for (Node k: A.cols(i)) {
		    cij += A.value(i,k) * B.value(k,j);
		}

		if (cij != 0) {
		    Float v = new Float(cij);
		    dicCols.put(j, v);
		}
	    }
	    this.m.put(i, dicCols);
	}
    }

    /**
     * Guarda al p.i. el producte per files de A i B, es a dir, 
     * el producte de A per B transposta, normalitzat.
     */
    public void productePM(MatriuHeteSim A, MatriuHeteSim B) {
	this.numRows = A.numRows();
	this.numCols = B.numRows();
	this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
	float cij;
	
	for (Node i: A.rowKeys()) {
	    HashMap<Node, Float> dicCols = new HashMap<Node, Float>();
	    for (Node j: B.rowKeys()) {
		cij = cijnorm(A, B, i, j);
	
		if (cij != 0) {
		    Float v = new Float(cij);
		    dicCols.put(j, v);
		}
	    }
	    this.m.put(i, dicCols);
	}
    }


    private float cijnorm(MatriuHeteSim A, MatriuHeteSim B, Node i, Node j) {
	float cij = 0, normi = 0, normj = 0;
	for (Node k: A.cols(i)) {
	    cij += A.value(i,k) * B.value(j,k);
	    normi += A.value(i,k) * A.value(i,k);
	}
	for (Node k: B.cols(j)) {
	    normj += B.value(j,k) * B.value(j,k);
	}
	normi = (float)Math.sqrt((double)normi*normj);
	if (cij == 0) return cij;
	else return cij/normi;
    }


}
