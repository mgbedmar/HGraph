package ResultTest;
import java.lang.reflect.Array;
import java.util.*;
import GraphTest.Graph.*;


public class Result {

    public static int numRowsResult = 10;

    protected class Row {
        protected String node1;

        protected Row(Node a) {
            this.node1 = a.getName();
        }

        protected String getFirstNode() {
            return node1;
        }

        protected String getSecondNode() {
            return null;
        }

        protected float getHeteSim() {
            return -1;
        }
    }

    protected class RowWithHS extends Row{
        protected float hs;

        protected RowWithHS(Node a, float hs) {
            super(a);
            this.hs = hs;
        }

        protected float getHeteSim() {
            return this.hs;
        }
    }

    protected class Row3Cols extends RowWithHS{
        protected String node2;

        protected Row3Cols(Node a, Node b, float hs) {
            super(a, hs);
            this.node2 = b.getName();
        }

        protected String getSecondNode() {
            return this.node2;
        }
    }

    private ArrayList<Row> res;
    private int nCols; //nombre de columnes
    private int nNodes; //nombre de nodes
    private int currentIndex; //l'index del que s'ha de retornar
    private ArrayList<String> selectedNames;
    private ArrayList<Integer> filteredRows;
    private ArrayList<String> filteredNames;

    /**
     * Constructora.
     * @param n: nombre de columnes
     */
    public Result(int n) {
        this.res = new ArrayList<Row>();
        this.nCols = n;
        this.currentIndex = 0;
        if (n == 3) this.nNodes = 2;
        else this.nNodes = 1;
        this.selectedNames = new ArrayList<String>();
        this.filteredRows = new ArrayList<Integer>();
        this.filteredNames = new ArrayList<String>();
    }

    /**
     * Per consultar la mida del resultats.
     * @return Retorna el nombre de files total.
     */
    public int size() {
        return res.size();
    }

    /**
     * Afegeix una fila amb nomes el node <em>a</em>
     * @param a: node de la fila
     */
    public void addRow(Node a) {
        Row f = new Row(a);
        this.res.add(f);
    }

    /**
     * Afegeix una fila amb el node <em>a</em> i el seu HS
     * @param a: node de la fila
     * @param hs: mesura HeteSim del node <em>a</em>
     */
    public void addRow(Node a, float hs) {
        RowWithHS f = new RowWithHS(a, hs);
        this.res.add(f);
    }

    /**
     * Afegeix una fila amb dos nodes i el seu HS.
     * @param a: primer node de la fila
     * @param b: segon node de la fila
     * @param hs: mesura HeteSim del node <em>a</em> amb el <em>b</em>
     */
    public void addRow(Node a, Node b, float hs) {
        Row3Cols f = new Row3Cols(a, b, hs);
        this.res.add(f);
    }

    /**
     * Comprova si la fila compleix restriccions per mostrarla...
     */
    private boolean isValid(int index) {
        if (filteredRows.contains(index)) return false;

        Row factual = res.get(index);
        if (!selectedNames.isEmpty() && (!selectedNames.contains(factual.getFirstNode()))) {
        /* no es buit i no esta el primer nom */
            if (nNodes == 1) return false; //si no te altre node ja esta
            else if (!selectedNames.contains(factual.getSecondNode())) {
                /* el segon node existeix pero no esta en els desitjats */
                return false;
            }
        }

        if (filteredNames.contains(factual.getFirstNode())) {
            /* si el primer node es indesitjat */
            return false;
        }
        if (nNodes == 2 && filteredNames.contains(factual.getSecondNode())) {
            /* si hi ha segon node i es indesitjat */
            return false;
        }

        /* Si res de lo anterior, es pot mostrar */
        return true;
    }

    /** Avanca l'index fins trobar fila valida per visualitzar o
     * be sortir de rang.
     * @return true si i nomes si esta dins de rang
     */
    private boolean moveIndex() {
        while (currentIndex < res.size() && !isValid(currentIndex)) {
            ++currentIndex;
        }
        return currentIndex < res.size();
    }

    /**
     * Retorna la fila que toca des de la darrera
     * vegada que es va ordenar, filtrar o fer reset.
     * @return El primer element del ArrayList es un
     * enter unic per identificar la fila, els seguents
     * son n dades (el nom del node si es un node, el float
     * en String si es el HS). Retorna <em>null</em> si no queden files.
     */
    public ArrayList<String> getRow() {
        if (moveIndex()) {
            /* Muntem la fila... */
            ArrayList<String> rowReturn = new ArrayList<>(1+this.nCols);
            rowReturn.add(String.valueOf(currentIndex));
            rowReturn.add(res.get(currentIndex).getFirstNode());
            if (nNodes == 2)
                rowReturn.add(res.get(currentIndex).getSecondNode());
            if (nCols - nNodes == 1)
                rowReturn.add(String.valueOf(res.get(currentIndex).getHeteSim()));

            /* Avancem l'index per la seguent obtenirFila */
            ++currentIndex;

            return rowReturn;
        }
        else return null;
    }

    /**
     * Ordena el resultat segons el valor de la columna
     * <em>numCol</em>, ascendentment si <em>ascend</em> es cert i
     * descendentment si es fals.
     */
    public void sort(int numCol, boolean ascend) {
        if (numCol == 1) {
            /* Ordenar pel primer node */
            if (ascend)
                res.sort(new RowByFirstNameAscend());
            else
                res.sort(new RowByFirstNameDescend());
        }

        else if (numCol == 2 && this.nNodes == 2) {
            /* Ordenar pel segon node */
            if (ascend)
                res.sort(new RowBySecondNameAscend());
            else
                res.sort(new RowBySecondNameDescend());
        }

        else {
            /* Ordenar pel HeteSim */
            if (ascend)
                res.sort(new RowByHeteSimAscend());
            else
                res.sort(new RowByHeteSimDescend());
        }

        currentIndex = 0;
        filteredRows.clear();
    }

    /**
     * Filtra per nom, nomes es mostren els resultats
     * on apareix el nom <em>nom</em>.
     */
    public void select(String nom) {
        selectedNames.add(nom);
        currentIndex = 0;
    }

    /**
     * Treu el filtre per nom.
     */
    public void unselect(String nom) {
        selectedNames.remove(nom);
        currentIndex = 0;
    }

    /**
     * Treu tots els filtres per nom.
     */
    public void unselectAll() {
        this.selectedNames.clear();
        currentIndex = 0;
    }

    /**
     * Amaga els resultats on apareix el nom <em>nom</em>.
     */
    public void filter(String nom) {
        filteredNames.add(nom);
        currentIndex = 0;
    }

    /**
     * Desamaga els resultats on apareix el nom.
     */
    public void unfilter(String nom) {
        filteredNames.remove(nom);
        currentIndex = 0;
    }

    /**
     * Amaga una fila.
     * @param index: numero de la fila que es vol amagar
     */
    public void filter(int index) {
        filteredRows.add(index);
        currentIndex = 0;
    }

    /**
     * Desamaga una fila.
     * @param index: numero de la fila que es vol desamagar
     */
    public void unfilter(int index) {
        filteredRows.remove(index);
        currentIndex = 0;
    }

    /**
     * Treu tots els filtres
     */
    public void unfilterAll() {
        filteredNames.clear();
        filteredRows.clear();
        selectedNames.clear();
        currentIndex = 0;
    }

    /**
     * Retorna una estructura amb tots els filtres.
     * @return: Un map amb claus "filteredNames", "filteredLines" i 
     * "selectedNames" associades cadascuna a un ArrayList<String>
     */
    public Map<String, ArrayList<String>> getFilters() {
        Map<String, ArrayList<String>> map = new HashMap<>();
        map.put("filteredNames", filteredNames);
        map.put("selectedNames", selectedNames);

        ArrayList<String> filteredLines = new ArrayList<>();
        for (Integer i: filteredRows) {
            filteredLines.add(String.valueOf(i));
        }

        map.put("filteredLines", filteredLines);

        return map;
    }

    /**
     * Despres d'executar aquest metode, getRow() comencara
     * a donar files per la primera segons l'ultim ordre escollit
     * i tenint en compte tots els filtres imposats.
     */
    public void resetIndex() {
        currentIndex = 0;
    }


}


class RowByFirstNameAscend implements Comparator<Result.Row> {
    public int compare(Result.Row one, Result.Row another) {
        return (one.getFirstNode()).compareTo(another.getFirstNode());
    }
}

class RowByFirstNameDescend implements Comparator<Result.Row> {
    public int compare(Result.Row one, Result.Row another) {
        return -(one.getFirstNode()).compareTo(another.getFirstNode());
    }
}

class RowBySecondNameAscend implements Comparator<Result.Row> {
    public int compare(Result.Row one, Result.Row another) {
        return (one.getSecondNode()).compareTo(another.getSecondNode());
    }
}

class RowBySecondNameDescend implements Comparator<Result.Row> {
    public int compare(Result.Row one, Result.Row another) {
        return -(one.getSecondNode()).compareTo(another.getSecondNode());
    }
}

class RowByHeteSimAscend implements Comparator<Result.Row> {
    public int compare(Result.Row one, Result.Row another) {
        if (one.getHeteSim() < another.getHeteSim()) return -1;
        else if (one.getHeteSim() == another.getHeteSim()) return 0;
        else return 1;
    }
}

class RowByHeteSimDescend implements Comparator<Result.Row> {
    public int compare(Result.Row one, Result.Row another) {
        if (one.getHeteSim() < another.getHeteSim()) return 1;
        else if (one.getHeteSim() == another.getHeteSim()) return 0;
        else return -1;
    }
}