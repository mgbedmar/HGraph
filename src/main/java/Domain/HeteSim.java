package Domain;
import java.util.*;
import Domain.Graph.*;

public class HeteSim {

    /**
     * Retorna una matriu que te els valors de HeteSim
     * segons el cami cami, entre tots els nodes del 
     * tipus pel que comenca el cami i tots els nodes 
     * del tipus pel que acaba el cami
     */
    public static MatriuHeteSim calcul(Graph graf,
				       ArrayList<String> cami) {

	ArrayList<String> cami1 = new ArrayList<String>();
	ArrayList<String> cami2 = new ArrayList<String>();
	subdividirCami(cami, cami1, cami2);
	MatriuHeteSim pm1, pm2;
	pm1 = calcularPM(graf, cami1, cami2.get(1));
	pm2 = calcularPM(graf, cami2, cami1.get(cami1.size()-2));
	MatriuHeteSim resultat = new MatriuHeteSim();
	resultat.productePM(pm1, pm2);
	return resultat;
    }

    /* Retorna la matriu PM del cami cami */
    private static MatriuHeteSim calcularPM(Graph graf,
					   ArrayList<String> cami,
					   String tNextE) {
	MatriuHeteSim uini, ufi, uprod;
	boolean interm = cami.get(cami.size()-1).equals("E");

	/*Si el cami nomes te 2 tipus i un intermig anem ja a intermig */
	if (cami.size() == 2 && interm) {
	    uini = new MatriuHeteSim(graf, cami.get(0), tNextE, true);
	}
	else {
	    uini = new MatriuHeteSim(false, graf, cami.get(0), cami.get(1));
	}

	int numMatriusNormals = cami.size()-1;
	if (interm) numMatriusNormals--;
	for (int i = 1; i < numMatriusNormals; i++) {
	    /* Calculem la U_i,i+1 */
	    ufi = new MatriuHeteSim(true, graf, cami.get(i), cami.get(i+1));
	    /* Multipliquem pel que ja portem */
	    uprod = new MatriuHeteSim();
	    uprod.productePerFiles(uini, ufi);
	    /* El que portem ara es el resultat del producte*/
	    uini = uprod;
	}

	/* Si no hi ha E, ja hem acabat. Si hi ha E pero nomes long 2, tambe */
	if (!interm || cami.size() == 2) return uini;
	/* Cas contrari, calculem la ultima */
	else {
	    /* La matriu final es la intermitja de size-2,tNextE */
	    ufi = new MatriuHeteSim(graf, cami.get(cami.size()-2), tNextE, true);
	    uprod = new MatriuHeteSim();
	    uprod.producteHabitualIntermitja(uini, ufi);
	    return uprod;
	}

    }


    /* Separa el cami per la meitat afegint el node intermig
     * si cal 
     */
    private static void subdividirCami(ArrayList<String> cami,
				       ArrayList<String> cami1,
				       ArrayList<String> cami2) {
	cami1.clear();
	cami2.clear();
	int longitud = cami.size()/2 + 1;

	for (int i = 0; i < longitud-1; i++) {
	    cami1.add(i, cami.get(i));
	    cami2.add(i, cami.get(cami.size()-1-i));
	}
	if (cami.size()%2 == 0) {
	    cami1.add(longitud-1, "E");
	    cami2.add(longitud-1, "E");
	}
	else {
	    cami1.add(longitud-1, cami.get(longitud-1));
	    cami2.add(longitud-1, cami.get(longitud-1));
	}
    }
}
