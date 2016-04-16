package Domain;
import java.util.*;
import Domain.Graph.*;


public class HeteSimMatrix {
    protected HashMap<Node, HashMap<Node, Float>> m;
    protected int numRows;
    protected int numCols;


    /**
     * Constructora. Crea una matriu buida per poder fer un producte
     * a dins.
     */
    public HeteSimMatrix() {
        this.numRows = this.numCols = 0;
        this.m = new HashMap<Node, HashMap<Node, Float>>();
    }

    /**
     * Constructora.
     * Crea la matriu U_tRow,TCol (transposada si i nomes 
     * si tr es cert)
     */
    public HeteSimMatrix(boolean tr, Graph graf,
                         String tRow, String tCol) {
        if (tr) initMatrix(graf, tCol, tRow);
        else initMatrix(graf, tRow, tCol);
    }

    private void initMatrix(Graph graf, String tRow, String tCol) {
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
    public void rowProduct(HeteSimMatrix A, HeteSimMatrix B) {
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
    private float cij(HeteSimMatrix A, HeteSimMatrix B, Node i, Node j) {
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
    public void intermediateProduct(HeteSimMatrix A, IntermediateHeteSimMatrix B) {
        this.numRows = A.numRows();
        this.numCols = B.numCols();
        this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
        float cij;
        Ghost j;

        for (Node i: A.rowKeys()) {
            HashMap<Node, Float> dicCols = new HashMap<Node, Float>();
            for (int jint = 0; jint < B.numCols(); jint++) {
                cij = 0;
                j = new Ghost(jint);
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
    public void productPM(HeteSimMatrix A, HeteSimMatrix B) {
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


    private float cijnorm(HeteSimMatrix A, HeteSimMatrix B, Node i, Node j) {
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
