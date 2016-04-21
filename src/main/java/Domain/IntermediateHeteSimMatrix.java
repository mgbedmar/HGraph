package Domain;


import Domain.Graph.Ghost;
import Domain.Graph.Graph;
import Domain.Graph.Node;

import java.util.HashMap;
import java.util.Set;

public class IntermediateHeteSimMatrix extends HeteSimMatrix {

    /**
     * Constructora. Matriu buida.
     */
    public IntermediateHeteSimMatrix() {
        super();
    }

    protected HashMap<Node, HashMap<Node, Float>> mv;
    /**
     * Constructora. Crea la matriu U_tRow,E i la seguent.
     */
    public IntermediateHeteSimMatrix(Graph graf, String tRow, String tCol) throws DomainException {
        initIntermediateMatrix(graf, tRow, tCol);
    }

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
     * Transforma la matriu U_A,E en la U_E,B
     */
    public void getAnother() {
        this.m = this.mv;
    }
}
