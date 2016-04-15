package Domain;
import Domain.Graph.*;
import java.util.Set;


public class Consulta {

    private Consulta() {
        /* Constructor privat per evitar que s'instancii */
    }

    private static Resultat omplirAmbSet(Set<Node> set) {
        Resultat res = new Resultat(1);
        for (Node i: set)
            res.afegirFila(i);
        return res;
    }

    /**
     * Consulta de tots els nodes d'un tipus
     * @param graf: Graph on es fa la consulta.
     * @param type: el tipus que es vol consultar.
     * @return Un Resultat d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Resultat consultaTipus(Graph graf, String type) {
        Set<Node> set = graf.getSetOfNodes(type);
        Resultat res = omplirAmbSet(set);
        res.ordenar(1, true);
        return res;
    }

    /**
     * Consulta tots els veins d'un node del graf.
     * @param graf: Graph sobre el que es consulta.
     * @param node: Node del que es consulten els veins.
     * @return Un Resultat d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Resultat consultaVeins(Graph graf, Node node) {
        Set<Node> set = graf.getNeighbours(node);
        Resultat res = omplirAmbSet(set);
        res.ordenar(1, true);
        return res;
    }

    /**
     * Consulta tots els veins d'un node del graf.
     * @param graf: Graph sobre el que es consulta.
     * @param node: Node del que es consulten els veins.
     * @param type: tipus que es vol consultar
     * @return Un Resultat d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Resultat consultaVeins(Graph graf, Node node, String type) {
        Set<Node> set = graf.getNeighbours(node, type);
        Resultat res = omplirAmbSet(set);
        res.ordenar(1, true);
        return res;
    }

    /**
     * Consulta el HeteSim de dos nodes (cami obvi)
     * @param graf: Graph sobre el que es consulta.
     * @param a: Un dels nodes.
     * @param b: L'altre.
     * @return Un Resultat de tres columnes amb el HeteSim dels dos nodes.
     */
    public static Resultat consulta1a1(Graph graf, Node a, Node b) {
        Resultat res = new Resultat(3);
        return res;
    }

    /**
     * Consulta el HeteSim d'un node amb tots els del tipus especificat.
     * @return Un Resultat de dues columnes amb les files afegides i ordenat per segona columna descendent.
     */
    public static Resultat consulta1aN(Graph graf, Node node, String type) {
        Resultat res = new Resultat(2);
        return res;
    }

    /**
     * Troba parelles del tipus de b per c tan HeteSim com b ho és per a.
     * El node a ha de ser del mateix tipus que el c.
     * @return Un Resultat de dues columnes amb les files afegides i ordenat per segona columna descendent.
     */
    public static Resultat consultaReferencia(Graph graf, Node a, Node b, Node c) {
        Resultat res = new Resultat(2);
        return res;
    }

    /**
     * Fa una taula de nodes per HeteSim (per decidir)
     * @return Un Resultat de tres columnes amb les files afegides i ordenat per tercera columna descendent.
     */
    public static Resultat consultaNaN(Graph graf, String type1, String type2) {
        Resultat res = new Resultat(3);
        return res;
    }

}
