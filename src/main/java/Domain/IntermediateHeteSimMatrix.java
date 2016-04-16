package Domain;


import Domain.Graph.Ghost;
import Domain.Graph.Graph;
import Domain.Graph.Node;

import java.util.HashMap;
import java.util.Set;

public class IntermediateHeteSimMatrix extends HeteSimMatrix {
    /**
     * Constructora. Crea la matriu U_tRow,E si u es cert.
     */
    public IntermediateHeteSimMatrix(Graph graf, String tRow, String tCol) {
        initIntermediateMatrix(graf, tRow, tCol);
    }

    private void initIntermediateMatrix(Graph graf, String tRow, String tCol) {
	/* Agafem el conjunt que indexa les files */
        Set<Node> rows = graf.getSetOfNodes(tRow);
        this.numRows = rows.size();
        Set<Node> cols;

	/* Inicialitzem hashmap */
        this.m = new HashMap<Node, HashMap<Node, Float>>(this.numRows);
        float one = 1;
        int k = 0;

        for (Node i : rows) {
            HashMap<Node, Float> dicCol =
                    new HashMap<Node, Float>(/* parametres */);
            cols = graf.getNeighbours(i, tCol);
            if (!cols.isEmpty()) {
		/* Normalitzar per files */
                Float v = new Float(one / ((float) cols.size()));
                for (Node j : cols) {
                    Ghost index = new Ghost(k);
                    dicCol.put(index, v);
                    k++;
                }
            }
            this.m.put(i, dicCol);
            this.numCols = k;
        }
    }


}
