package Domain;
import Domain.Graph.*;
import java.util.Set;


public class Query {

    private Query() {
        /* Constructor privat per evitar que s'instancii */
    }

    private static Result fillWithSet(Set<Node> set) {
        Result res = new Result(1);
        for (Node i: set)
            res.addRow(i);
        return res;
    }

    /**
     * Consulta de tots els nodes d'un tipus
     * @param graf: <em>Graph</em> on es fa la consulta.
     * @param type: el tipus que es vol consultar.
     * @return Un <em>Result</em> d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Result queryType(Graph graf, String type) {
        Set<Node> set = graf.getSetOfNodes(type);
        Result res = fillWithSet(set);
        res.sort(1, true);
        return res;
    }

    /**
     * Consulta tots els veins d'un node del graf.
     * @param graf: <em>Graph</em> sobre el que es consulta.
     * @param node: <em>Node</em> del que es consulten els veins.
     * @return Un <em>Result</em> d'unajavjava columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Result queryNeighbours(Graph graf, Node node) {
        Set<Node> set = graf.getNeighbours(node);
        Result res = fillWithSet(set);
        res.sort(1, true);
        return res;
    }

    /**
     * Consulta tots els veins d'un node del graf.
     * @param graf: <em>Graph</em> sobre el que es consulta.
     * @param node: <em>Node</em> del que es consulten els veins.
     * @param type: tipus que es vol consultar
     * @return Un <em>Result</em> d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Result queryNeighbours(Graph graf, Node node, String type) {
        Set<Node> set = graf.getNeighbours(node, type);
        Result res = fillWithSet(set);
        res.sort(1, true);
        return res;
    }

    /**
     * Consulta el HeteSim de dos nodes (cami obvi)
     * @param graf: <em>Graph</em> sobre el que es consulta.
     * @param a: Un dels nodes.
     * @param b: L'altre.
     * @return Un <em>Result</em> de tres columnes amb el HeteSim dels dos nodes.
     */
    public static Result query1a1(Graph graf, Node a, Node b) {
        Result res = new Result(3);
        return res;
    }

    /**
     * Consulta el HeteSim d'un node amb tots els del tipus especificat.
     * @return Un <em>Result</em> de dues columnes amb les files afegides i ordenat per segona columna descendent.
     */
    public static Result query1aN(Graph graf, Node node, String type) {
        Result res = new Result(2);
        return res;
    }

    /**
     * Troba parelles del tipus de <em>b</em> per <em>c</em> tan HeteSim com <em>b</em> ho és per <em>a</em>.
     * El node <em>a</em> ha de ser del mateix tipus que el <em>c</em>.
     * @return Un <em>Result</em> de dues columnes amb les files afegides i ordenat per segona columna descendent.
     */
    public static Result queryReference(Graph graf, Node a, Node b, Node c) {
        Result res = new Result(2);
        return res;
    }

    /**
     * Fa una taula de nodes per HeteSim (per decidir)
     * @return Un <em>Result</em> de tres columnes amb les files afegides i ordenat per tercera columna descendent.
     */
    public static Result queryNaN(Graph graf, String type1, String type2) {
        Result res = new Result(3);
        return res;
    }

}
