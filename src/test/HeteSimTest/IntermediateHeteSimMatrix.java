package HeteSimTest;


import GraphTest.Graph.*;

import java.util.HashMap;
import java.util.Set;


/**
 * @author Mireia
 */
public class IntermediateHeteSimMatrix extends HeteSimMatrix {

    /**
     * Atribut que, si es construeix la matriu intermitja U_t,e, sent e el tipus
     * intermig entre t i s, guarda la matriu intermitja U_s,e. Aixo es necessari
     * per ordenar de la mateixa manera el tipus intermig en els dos casos.
     */
    protected HashMap<Node, HashMap<Node, Float>> mv;

    /**
     * Constructora. Matriu buida.
     */
    public IntermediateHeteSimMatrix() {
        super();
    }

    /**
     * Constructora. Crea la matriu U_tRow,E i la seguent.
     */
    /**
     * Constructora. Crea la matriu U_tRow,E i la seguent.
     * @param graf graf sobre el que es calcula
     * @param tRow tipus que indexa files
     * @param tCol tipus que indexa columnes
     * @throws DomainException si es produeix a les capes inferiors
     */
    public IntermediateHeteSimMatrix(Graph graf, String tRow, String tCol) throws DomainException {
        initIntermediateMatrix(graf, tRow, tCol);
    }

    public void getAnother(IntermediateHeteSimMatrix matrix) {
	this.numRows = matrix.numRows();
	this.numCols = matrix.numCols();
	this.m = matrix.m;
	this.mv = matrix.mv;
    }

    /**
     * Inicialitza la matriu amb els valors U_tRow,E i U_TCol,E.
     * @param graf graf sobre el que es calcula
     * @param tRow tipus que indexa files
     * @param tCol tipus que indexa columnes
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void initIntermediateMatrix(Graph graf, String tRow, String tCol) throws DomainException {
	/* Agafem el conjunt que indexa les files */
        Set<Node> rows = graf.getSetOfNodes(tRow);
        this.numRows = rows.size();
        Set<Node> cols = graf.getSetOfNodes(tCol);
        mv = new HashMap<>();

        for (Node b: cols) {
            mv.put(b, new HashMap<>());
        }

	/* Inicialitzem hashmap */
        this.m = new HashMap<>(this.numRows);
        float one = 1;
        int k = 0;

        for (Node i : rows) {
            HashMap<Node, Float> dicCol = new HashMap<>();
            cols = graf.getNeighbours(i, tCol);
            if (!cols.isEmpty()) {
		/* Normalitzar per files */
                Float v = new Float(one / ((float) cols.size()));
                for (Node j : cols) {
                    Ghost index = new Ghost(k);
                    dicCol.put(index, v);
                    this.mv.get(j).put(index, one);
                    k++;
                }
            }
            this.m.put(i, dicCol);
        }
        this.numCols = k;

        for (Node i: graf.getSetOfNodes(tCol)) {
            for (Node j: this.mv.get(i).keySet()) {
                Float valor = this.mv.get(i).get(j);
                this.mv.get(i).put(j, valor/((float)this.mv.get(i).keySet().size()));
            }
        }
    }

    /**
     * Transforma la matriu U_t,e en la U_s,e
     */
    public void getAnother() {
        this.m = this.mv;
    }
}
