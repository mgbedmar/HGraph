package Domain;
import Domain.Graph.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;


/**
 * @author Mireia
 */
public class Query {

    /**
     *  Tolerancia per la queryByReference.
     */
    public static float TOL = 0.1f;

    /**
     * Conte els camins per defecte utilitzats a les consultes.
     */
    public static final TreeMap<String, TreeMap<String, ArrayList<String>>> defaultPath = createPaths();

    /**
     * Instancia la variable <em>defaultPath</em>
     * @return el valor per defecte dels camins
     */
    private static TreeMap<String, TreeMap<String, ArrayList<String>>> createPaths() {
        TreeMap map = new TreeMap<>();
        TreeMap<String, ArrayList<String>> aux;

        /* Camins per defecte Author-? */
        aux = new TreeMap<>();
        /* Author-Author*/
        aux.put(Author.TYPE, new ArrayList<>(Arrays.asList(Author.TYPE, Paper.TYPE, Author.TYPE)));
        /* Author-Paper */
        aux.put(Paper.TYPE, new ArrayList<>(Arrays.asList(Author.TYPE, Paper.TYPE)));
        /* Author-Term */
        aux.put(Term.TYPE, new ArrayList<>(Arrays.asList(Author.TYPE, Paper.TYPE, Term.TYPE)));
        /* Author-Conf */
        aux.put(Conf.TYPE, new ArrayList<>(Arrays.asList(Author.TYPE, Paper.TYPE, Conf.TYPE)));
        /* Posem al defaultPath */
        map.put(Author.TYPE, aux);


        /* Camins per defecte Paper-? */
        aux = new TreeMap<>();
        /* Paper-Author*/
        aux.put(Author.TYPE, new ArrayList<>(Arrays.asList(Paper.TYPE, Author.TYPE)));
        /* Paper-Paper */
        aux.put(Paper.TYPE, new ArrayList<>(Arrays.asList(Paper.TYPE, Term.TYPE, Paper.TYPE))); //?
        /* Paper-Term */
        aux.put(Term.TYPE, new ArrayList<>(Arrays.asList(Paper.TYPE, Term.TYPE)));
        /* Author-Conf */
        aux.put(Conf.TYPE, new ArrayList<>(Arrays.asList(Paper.TYPE, Conf.TYPE)));
        /* Posem al defaultPath */
        map.put(Paper.TYPE, aux);


        /* Camins per defecte Term-? */
        aux = new TreeMap<>();
        /* Term-Author*/
        aux.put(Author.TYPE, new ArrayList<>(Arrays.asList(Term.TYPE, Paper.TYPE, Author.TYPE)));
        /* Term-Paper */
        aux.put(Paper.TYPE, new ArrayList<>(Arrays.asList(Term.TYPE, Paper.TYPE)));
        /* Term-Term */
        aux.put(Term.TYPE, new ArrayList<>(Arrays.asList(Term.TYPE, Paper.TYPE, Term.TYPE)));
        /* Term-Conf */
        aux.put(Conf.TYPE, new ArrayList<>(Arrays.asList(Term.TYPE, Paper.TYPE, Conf.TYPE)));
        /* Posem al defaultPath */
        map.put(Term.TYPE, aux);


        /* Camins per defecte Conf-? */
        aux = new TreeMap<>();
        /* Conf-Author*/
        aux.put(Author.TYPE, new ArrayList<>(Arrays.asList(Conf.TYPE, Paper.TYPE, Author.TYPE)));
        /* Conf-Paper */
        aux.put(Paper.TYPE, new ArrayList<>(Arrays.asList(Conf.TYPE, Paper.TYPE)));
        /* Conf-Term */
        aux.put(Term.TYPE, new ArrayList<>(Arrays.asList(Conf.TYPE, Paper.TYPE, Term.TYPE)));
        /* Conf-Conf */
        aux.put(Conf.TYPE, new ArrayList<>(Arrays.asList(Conf.TYPE, Paper.TYPE, Conf.TYPE))); //?
        /* Posem al defaultPath */
        map.put(Conf.TYPE, aux);

        return map;
    }

    /**
     * Constructor privat per evitar instanciacio.
     */
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
     * @throws DomainException si es genera en capes inferiors
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
     * @throws DomainException si es genera en capes inferiors
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
     * @param a Un dels nodes.
     * @param b L'altre.
     * @param path Cami entre els nodes que se segueix a la consulta
     * @return Un <em>Result</em> de tres columnes amb el HeteSim dels dos nodes.
     * @throws DomainException si es genera en capes inferiors
     */
    public static Result query1to1(Graph graph, Node a, Node b, ArrayList<String> path) throws DomainException {
        Result res = new Result(3);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        res.addRow(a, b, mat.value(a,b));
        return res;
    }

    /**
     * Consulta el HeteSim d'un node amb tots els del tipus especificat (que tinguin HS mes gran que 0).
     * @param graph <em>Graph</em> sobre el que es consulta.
     * @param node node del que es busquen rellevancies
     * @param path cami entre els nodes que se segueix a la consulta
     * @return Un <em>Result</em> de dues columnes amb les files afegides i ordenat per segona columna descendent.
     * @throws DomainException si es genera en capes inferiors
     */
    public static Result query1toN(Graph graph, Node node, ArrayList<String> path) throws DomainException {
        Result res = new Result(2);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        for (Node j: mat.cols(node)) {
            res.addRow(j, mat.value(node,j));
        }
        res.sort(2, false);
        return res;
    }

    /**
     * Troba parelles del tipus de <em>b</em> per <em>c</em> tan HeteSim com <em>b</em> ho és per <em>a</em>.
     * El node <em>a</em> ha de ser del mateix tipus que el <em>c</em>.
     * @param graph graf sobre el que es calcula
     * @param a primer node de referencia
     * @param b segon node de referencia
     * @param c primer node de resultat
     * @param path cami que se segueix per calcular el HeteSim
     * @param tol tolerancia que s'utilitza per seleccionar "semblants"
     * @return Un <em>Result</em> de dues columnes amb les files afegides i ordenat per segona columna descendent.
     * @throws DomainException si es genera a les capes inferiors
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
        res.sort(2, false);
        return res;
    }

    /**
     * Fa una taula de nodes per HeteSim (per decidir)
     * @return Un <em>Result</em> de tres columnes amb les files afegides i ordenat per tercera columna descendent.
     */
    /**
     * Fa una taula de nodes per HeteSim
     * @param graph graf sobre el que es calcula
     * @param path cami pel HeteSim
     * @return Un <em>Result</em> de tres columnes amb les files afegides i ordenat per tercera columna descendent.
     * @throws DomainException si es genera a les capes inferiors
     */
    public static Result queryNtoN(Graph graph, ArrayList<String> path) throws DomainException {
        Result res = new Result(3);
        HeteSimMatrix mat = HeteSim.run(graph, path);
        for (Node a: mat.rowKeys()) {
            for (Node b: mat.cols(a)) {
                res.addRow(a, b, mat.value(a,b));
            }
        }
        res.sort(3, false);
        return res;
    }

}
