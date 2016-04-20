package Domain;
import Domain.Graph.*;

import java.util.ArrayList;
import java.util.Set;


public class Query {

    private Query() {
        /* Constructor privat per evitar que s'instancii */
    }

    /**
     * Omple un <em>Result</em> amb el contingut d'un conjunt
     * @param set Conjunt amb els nodes per ficar a un resultat
     * @return Un <em>Result</em> d'una columna omplert amb els nodes de <em>set</em>
     */
    private static Result fillWithSet(Set<Node> set) {
        Result res = new Result(1);
        for (Node i: set)
            res.addRow(i);
        return res;
    }

    /**
     * Consulta de tots els nodes d'un tipus
     * @param graph: <em>Graph</em> on es fa la consulta.
     * @param type: el tipus que es vol consultar.
     * @return Un <em>Result</em> d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Result queryByType(Graph graph, String type) throws DomainException {
        Set<Node> set = graph.getSetOfNodes(type);
        Result res = fillWithSet(set);
        res.sort(1, true);
        return res;
    }

    /**
     * Consulta tots els veins d'un node del graf.
     * @param graph: <em>Graph</em> sobre el que es consulta.
     * @param node: <em>Node</em> del que es consulten els veins.
     * @return Un <em>Result</em> d'unajavjava columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Result queryNeighbours(Graph graph, Node node) {
        Set<Node> set = graph.getNeighbours(node);
        Result res = fillWithSet(set);
        res.sort(1, true);
        return res;
    }

    /**
     * Consulta tots els veins d'un node del graf.
     * @param graph: <em>Graph</em> sobre el que es consulta.
     * @param node: <em>Node</em> del que es consulten els veins.
     * @param type: tipus que es vol consultar
     * @return Un <em>Result</em> d'una columna amb les files afegides i ordenat per primera columna ascendent.
     */
    public static Result queryNeighbours(Graph graph, Node node, String type) throws DomainException {
        Set<Node> set = graph.getNeighbours(node, type);
        Result res = fillWithSet(set);
        res.sort(1, true);
        return res;
    }

    /**
     * Consulta el HeteSim de dos nodes (cami obvi)
     * @param graph: <em>Graph</em> sobre el que es consulta.
     * @param a: Un dels nodes.
     * @param b: L'altre.
     * @return Un <em>Result</em> de tres columnes amb el HeteSim dels dos nodes.
     */
    public static Result query1to1(Graph graph, Node a, Node b, ArrayList<String> path) throws DomainException {
        Result res = new Result(3);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        res.addRow(a, b, mat.value(a,b));
        return res;
    }

    /**
     * Consulta el HeteSim d'un node amb tots els del tipus especificat (que tinguin HS > 0).
     * @return Un <em>Result</em> de dues columnes amb les files afegides i ordenat per segona columna descendent.
     */
    public static Result query1toN(Graph graph, Node node, ArrayList<String> path) throws DomainException {
        Result res = new Result(2);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        for (Node j: mat.cols(node)) {
            res.addRow(j, mat.value(node,j));
        }
        return res;
    }

    /**
     * Troba parelles del tipus de <em>b</em> per <em>c</em> tan HeteSim com <em>b</em> ho és per <em>a</em>.
     * El node <em>a</em> ha de ser del mateix tipus que el <em>c</em>.
     * @return Un <em>Result</em> de dues columnes amb les files afegides i ordenat per segona columna descendent.
     */
    public static Result queryByReference(Graph graph, Node a, Node b, Node c,
                                          ArrayList<String> path, float tol) throws DomainException {
        Result res = new Result(2);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        for (Node d: mat.cols(c)) {
            if (Math.abs(mat.value(c,d) - mat.value(a,b)) < tol) {
                res.addRow(d, mat.value(c,d));
            }
        }
        return res;
    }

    /**
     * Fa una taula de nodes per HeteSim (per decidir)
     * @return Un <em>Result</em> de tres columnes amb les files afegides i ordenat per tercera columna descendent.
     */
    public static Result queryNtoN(Graph graph, ArrayList<String> path) throws DomainException {
        Result res = new Result(3);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        for (Node a: mat.rowKeys()) {
            for (Node b: mat.cols(a)) {
                res.addRow(a, b, mat.value(a,b));
            }
        }
        return res;
    }

}
