package Domain;
import java.util.*;
import Domain.Graph.*;


public class HeteSimMatrix {
    /**
     * Matriu indexada per nodes que te com a valors el HeteSim entre ells, o be
     * una mesura intermitja (per exemple les U de l'article). Esta implementada
     * com a matriu dispersa (no es guarden els zeros). Es protected per a que
     * la seva subclasse hi pugui accedir.
     */
    protected HashMap<Node, HashMap<Node, Float>> m;

    /**
     * Numero de files de la matriu.
     */
    protected int numRows;

    /**
     * Numero de columnes de la matriu.
     */
    protected int numCols;


    /**
     * Constructora. Crea una matriu buida per poder fer un producte
     * a dins.
     */
    public HeteSimMatrix() {
        this.numRows = this.numCols = 0;
        this.m = new HashMap<>();
    }


    /**
     * Constructora. Crea la matriu <em>U_tRow,tCol</em>.
     * @param graf graf sobre el que es treballa
     * @param tRow tipus dels nodes que indexen files
     * @param tCol tipus dels nodes que indexen columnes
     * @throws DomainException
     */
    public HeteSimMatrix(Graph graf, String tRow, String tCol) throws DomainException {
        initMatrix(graf, tRow, tCol);
    }

    private void initMatrix(Graph graf, String tRow, String tCol) throws DomainException {
        Set<Node> rows = graf.getSetOfNodes(tRow);
        this.numRows = rows.size();
        Set<Node> cols = graf.getSetOfNodes(tCol);
        this.numCols = cols.size();

        this.m = new HashMap<>(this.numRows);
        float one = 1;

        for (Node i: rows) {
            HashMap<Node, Float> dicCol =
                    new HashMap<>(/* parametres */);
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
     * Getter. El node <em>i</em> ha de ser del tipus que indexa les files, el node <em>j</em> ha de ser del tipus
     * que indexa les columnes.
     * @return valor de l'entrada <em>i</em>, <em>j</em>
     */
    public float value(Node i, Node j) {
        Float v = this.m.get(i).get(j);
        if (v == null) return 0;
        else return v.floatValue();
    }

    /**
     * Getter.
     * @return conjunt d'elements que indexen les files
     */
    public Set<Node> rowKeys() {
        return this.m.keySet();
    }

    /**
     * Getter. El node <em>i</em> ha d'estar a la matriu
     * @param i: node que indexa la fila
     * @return conjunt de columnes amb valor no zero de la fila <em>i</em>
     */
    public Set<Node> cols(Node i) {
        return this.m.get(i).keySet();
    }

    /**
     * Setter. El node <em>i</em> ha d'estar a la matriu.
     * @param i: node que indexa la fila
     * @param j: node que indexa la columna
     * @param v: nou valor de la posicio (<em>i</em>,<em>j</em>)
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
     * Guarda al p.i. el producte per files de <em>A</em> i <em>B</em>, es a dir,
     * el producte de <em>A</em> per <em>B</em> transposta.
     */
    public void rowProduct(HeteSimMatrix A, HeteSimMatrix B) {
        this.numRows = A.numRows();
        this.numCols = B.numRows();
        this.m = new HashMap<>(this.numRows);
        float cij;

        for (Node i: A.rowKeys()) {
            HashMap<Node, Float> dicCols = new HashMap<>();
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

    /**
     * Calcula el valor de l'entrada <em>i</em>, <em>j</em> en la matriu producte per files
     * @param A
     * @param B
     * @param i
     * @param j
     * @return el valor de c_ij
     */
    private float cij(HeteSimMatrix A, HeteSimMatrix B, Node i, Node j) {
        float cij = 0;
        for (Node k: A.cols(i)) {
            cij += A.value(i,k) * B.value(j,k);
        }
        return cij;
    }

    /**
     * Guarda al p.i. el producte de <em>A</em> i <em>B</em>.
     * @param A matriu de tipus U_t,s
     * @param B matriu intermitja del tipus U_s,e
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
     * Guarda al p.i. el resultat de calcular el producte usual entre <em>A</em> i <em>B</em>.
     * @param A matriu de tipus U_t,s
     * @param B matriu de tipus U_s,r
     * @param Bcols conjunt que indexa les columnes de <em>B</em>
     */
    public void usualProduct(HeteSimMatrix A, HeteSimMatrix B, Set<Node> Bcols) {
        this.numRows = A.numRows();
        this.numCols = B.numRows();
        this.m = new HashMap<>(this.numRows);
        float cij;

        for (Node i: A.rowKeys()) {
            HashMap<Node, Float> dicCols = new HashMap<>();
            for (Node j: Bcols) {
                cij = 0;
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
    /**
     * Guarda al p.i. el producte per files normalitzat de <em>A</em> i <em>B</em>, es a dir,
     * el producte de <em>A</em> per <em>B</em> transposta, normalitzat.
     * @param A matriu de tipus PM_P1
     * @param B matriu de tipus PM_P2^-1
     */
    public void productPM(HeteSimMatrix A, HeteSimMatrix B) {
        this.numRows = A.numRows();
        this.numCols = B.numRows();
        this.m = new HashMap<>(this.numRows);
        float cij;

        for (Node i: A.rowKeys()) {
            HashMap<Node, Float> dicCols = new HashMap<>();
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


    /**
     * Calcula el valor de l'entrada <em>i</em>, <em>j</em> de la matriu producte per files de <em>A</em>
     * i <em>B</em>, normalitzat.
     * @param A
     * @param B
     * @param i
     * @param j
     * @return el valor
     */
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

