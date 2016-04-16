package Domain;


import java.util.*;

public class Config {

    public int numRowsResult = 10;

    public static final String authorType = "author";
    public static final String paperType = "paper";
    public static final String termType = "term";
    public static final String confType = "conf";
    protected static final String ghostType = "ghost";


    /* Aixo es una opcio, seria un diccionari doble que al fer l'init de la classe
     * es posaria amb els valors que posa mes a baix. El problema es que per accedir-hi
     * cal una instancia de Config, pero si ho fem al Controlador aixo esta be. */
    public TreeMap<String, TreeMap<String, ArrayList<String>>> defaultPath;

    public Config() {
        defaultPath = new TreeMap<>();
        TreeMap<String, ArrayList<String>> aux;

        /* Camins per defecte Author-? */
        aux = new TreeMap<>();
        /* Author-Author*/
        aux.put(authorType, new ArrayList<>(Arrays.asList(authorType, paperType, authorType)));
        /* Author-Paper */
        aux.put(paperType, new ArrayList<>(Arrays.asList(authorType, paperType)));
        /* Author-Term */
        aux.put(termType, new ArrayList<>(Arrays.asList(authorType, paperType, termType)));
        /* Author-Conf */
        aux.put(confType, new ArrayList<>(Arrays.asList(authorType, paperType, confType)));
        /* Posem al defaultPath */
        defaultPath.put(authorType, aux);


        /* Camins per defecte Paper-? */
        aux = new TreeMap<>();
        /* Paper-Author*/
        aux.put(authorType, new ArrayList<>(Arrays.asList(paperType, authorType)));
        /* Paper-Paper */
        aux.put(paperType, new ArrayList<>(Arrays.asList(paperType, termType, paperType))); //?
        /* Paper-Term */
        aux.put(termType, new ArrayList<>(Arrays.asList(paperType, termType)));
        /* Author-Conf */
        aux.put(confType, new ArrayList<>(Arrays.asList(paperType, confType)));
        /* Posem al defaultPath */
        defaultPath.put(paperType, aux);


        /* Camins per defecte Term-? */
        aux = new TreeMap<>();
        /* Term-Author*/
        aux.put(authorType, new ArrayList<>(Arrays.asList(termType, paperType, authorType)));
        /* Term-Paper */
        aux.put(paperType, new ArrayList<>(Arrays.asList(termType, paperType)));
        /* Term-Term */
        aux.put(termType, new ArrayList<>(Arrays.asList(termType, paperType, termType)));
        /* Term-Conf */
        aux.put(confType, new ArrayList<>(Arrays.asList(termType, paperType, confType)));
        /* Posem al defaultPath */
        defaultPath.put(termType, aux);


        /* Camins per defecte Conf-? */
        aux = new TreeMap<>();
        /* Conf-Author*/
        aux.put(authorType, new ArrayList<>(Arrays.asList(confType, paperType, authorType)));
        /* Conf-Paper */
        aux.put(paperType, new ArrayList<>(Arrays.asList(confType, paperType)));
        /* Conf-Term */
        aux.put(termType, new ArrayList<>(Arrays.asList(confType, paperType, termType)));
        /* Conf-Conf */
        aux.put(confType, new ArrayList<>(Arrays.asList(confType, paperType, confType))); //?
        /* Posem al defaultPath */
        defaultPath.put(confType, aux);
    }

}
