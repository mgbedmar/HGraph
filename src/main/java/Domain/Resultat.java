package Domain;
import java.util.*;
import Domain.Graph.*;


public class Resultat {

    protected class Fila {
        protected String node1;
        protected String node2;
        protected float hs;

        public Fila(Node a, Node b, float hs) {
            this.node1 = a.getName();
            this.hs = hs;
            this.node2 = b.getName();
        }
    }

    private ArrayList<Fila> res;
    private int nCols; //nombre de columnes
    private int nNodes; //nombre de nodes
    private int currentIndex; //l'index del que s'ha de retornar
    private ArrayList<String> desiredNames;
    private ArrayList<Integer> undesiredRows;
    private ArrayList<String> undesiredNames;

    /**
     * Constructora. 
     * @param n: nombre de columnes 
     */
    public Resultat(int n) {
        this.res = new ArrayList<Fila>();
        this.nCols = n;
        this.currentIndex = 0;
        if (n == 3) this.nNodes = 2;
        else this.nNodes = 1;
        this.desiredNames = new ArrayList<String>();
        this.undesiredRows = new ArrayList<Integer>();
        this.undesiredNames = new ArrayList<String>();
    }

    /**
     * Afegeix una fila amb nomes el node a
     * @param a: node de la fila
     */
    public void afegirFila(Node a) {
        Fila f = new Fila(a, null, -1);
        this.res.add(f);
    }

    /**
     * Afegeix una fila amb el node a i el seu HS
     * @param a: node de la fila
     * @param hs: mesura HeteSim del node a
     */
    public void afegirFila(Node a, float hs) {
        Fila f = new Fila(a, null, hs);
        this.res.add(f);
    }

    /**
     * Afegeix una fila amb dos nodes i el seu HS.
     * @param a: primer node de la fila
     * @param b: segon node de la fila
     * @param hs: mesura HeteSim del node a
     */
    public void afegirFila(Node a, Node b, float hs) {
        Fila f = new Fila(a, b, hs);
        this.res.add(f);
    }

    /**
     * Comprova si la fila compleix restriccions per mostrarla...
     */
    private boolean esValida(int index) {
        if (undesiredRows.contains(index)) return false;

        Fila factual = res.get(index);
        if (!desiredNames.isEmpty() && (!desiredNames.contains(factual.node1))) {
        /* no es buit i no esta el primer nom */
            if (nNodes == 1) return false; //si no te altre node ja esta
            else if (!desiredNames.contains(factual.node2)) {
                /* el segon node existeix pero no esta en els desitjats */
                return false;
            }
        }

        if (undesiredNames.contains(factual.node1)) {
            /* si el primer node es indesitjat */
            return false;
        }
        if (nNodes == 2 && undesiredNames.contains(factual.node2)) {
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
    private boolean avancarIndex() {
        while (currentIndex < res.size() && !esValida(currentIndex)) {
            ++currentIndex;
        }
        return currentIndex < res.size();
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
        if (avancarIndex()) {
            /* Muntem la fila... */
            ArrayList<String> filaReturn = new ArrayList<>(1+this.nCols);
            filaReturn.add(String.valueOf(currentIndex));
            filaReturn.add(res.get(currentIndex).node1);
            if (nNodes == 2)
                filaReturn.add(res.get(currentIndex).node2);
            if (nCols - nNodes == 1)
                filaReturn.add(String.valueOf(res.get(currentIndex).hs));

            /* Avancem l'index per la seguent obtenirFila */
            ++currentIndex;

            return filaReturn;
        }
        else return null;
    }

    /**
     * Ordena el resultat segons el valor de la columna 
     * numCol, ascendentment si ascend es cert i 
     * descendentment si es fals.
     */
    public void ordenar(int numCol, boolean ascend) {
        if (numCol == 1) {
            /* Ordenar pel primer node */
            if (ascend)
                res.sort(new FilaByFirstNameAscend());
            else
                res.sort(new FilaByFirstNameDescend());
        }

        else if (numCol == 2 && this.nNodes == 2) {
            /* Ordenar pel segon node */
            if (ascend)
                res.sort(new FilaBySecondNameAscend());
            else
                res.sort(new FilaBySecondNameDescend());
        }

        else {
            /* Ordenar pel HeteSim */
            if (ascend)
                res.sort(new FilaByHeteSimAscend());
            else
                res.sort(new FilaByHeteSimDescend());
        }

        currentIndex = 0;
    }

    /**
     * Filtra per nom, nomes es mostren els resultats 
     * on apareix el nom.
     */
    public void filtrar(String nom) {
        desiredNames.add(nom);
        currentIndex = 0;
    }

    /**
     * Treu el filtre per nom.
     */
    public void desfiltrar(String nom) {
        desiredNames.remove(nom);
        currentIndex = 0;
    }

    /**
     * Amaga els resultats on apareix el nom.
     */
    public void amagar(String nom) {
        undesiredNames.add(nom);
        currentIndex = 0;
    }

    /**
     * Desmaga els resultats on apareix el nom.
     */
    public void desamagar(String nom) {
        undesiredNames.remove(nom);
        currentIndex = 0;
    }

    /**
     * Amaga una fila.
     * @param index: numero de la fila que es vol amagar
     */
    public void amagar(int index) {
        undesiredRows.add(index);
        currentIndex = 0;
    }

    /**
     * Desmaga una fila.
     * @param index: numero de la fila que es vol desamagar
     */
    public void desamagar(int index) {
        undesiredRows.remove(index);
        currentIndex = 0;
    }
}


class FilaByFirstNameAscend implements Comparator<Resultat.Fila> {
    public int compare(Resultat.Fila one, Resultat.Fila another) {
        return one.node1.compareTo(another.node1);
    }
}

class FilaByFirstNameDescend implements Comparator<Resultat.Fila> {
    public int compare(Resultat.Fila one, Resultat.Fila another) {
        return -one.node1.compareTo(another.node1);
    }
}

class FilaBySecondNameAscend implements Comparator<Resultat.Fila> {
    public int compare(Resultat.Fila one, Resultat.Fila another) {
        return one.node2.compareTo(another.node2);
    }
}

class FilaBySecondNameDescend implements Comparator<Resultat.Fila> {
    public int compare(Resultat.Fila one, Resultat.Fila another) {
        return -one.node2.compareTo(another.node2);
    }
}

class FilaByHeteSimAscend implements Comparator<Resultat.Fila> {
    public int compare(Resultat.Fila one, Resultat.Fila another) {
        if (one.hs < another.hs) return -1;
        else if (one.hs == another.hs) return 0;
        else return 1;
    }
}

class FilaByHeteSimDescend implements Comparator<Resultat.Fila> {
    public int compare(Resultat.Fila one, Resultat.Fila another) {
        if (one.hs < another.hs) return 1;
        else if (one.hs == another.hs) return 0;
        else return -1;
    }
}