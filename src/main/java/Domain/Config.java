package Domain;


import java.util.*;

public class Config {

    public int numRowsResult = 10;

    public static final String authorType = "author";
    public static final String paperType = "paper";
    public static final String termType = "term";
    public static final String confType = "conf";
    protected static final String ghostType = "ghost";


    /* Camins per defecte Author-? */
    public static final ArrayList<String> defaultPathAuthorToAuthor =
            new ArrayList<>(Arrays.asList(authorType, paperType, authorType));
    public static final ArrayList<String> defaultPathAuthorToPaper =
            new ArrayList<>(Arrays.asList(authorType, paperType));
    public static final ArrayList<String> defaultPathAuthorToTerm =
            new ArrayList<>(Arrays.asList(authorType, paperType, termType));
    public static final ArrayList<String> defaultPathAuthorToConf =
            new ArrayList<>(Arrays.asList(authorType, paperType, confType));


    /* Camins per defecte Paper-? */
    public static final ArrayList<String> defaultPathPaperToAuthor =
            new ArrayList<>(Arrays.asList(paperType, authorType));
    public static final ArrayList<String> defaultPathPaperToPaper =
            new ArrayList<>(Arrays.asList(paperType, termType, paperType)); //??
    public static final ArrayList<String> defaultPathPaperToTerm =
            new ArrayList<>(Arrays.asList(paperType, termType));
    public static final ArrayList<String> defaultPathPaperToConf =
            new ArrayList<>(Arrays.asList(paperType, confType));

    /* Camins per defecte Term-? */
    public static final ArrayList<String> defaultPathTermToAuthor =
            new ArrayList<>(Arrays.asList(termType, paperType, authorType));
    public static final ArrayList<String> defaultPathTermToPaper =
            new ArrayList<>(Arrays.asList(termType, paperType));
    public static final ArrayList<String> defaultPathTermToTerm =
            new ArrayList<>(Arrays.asList(termType, paperType, termType));
    public static final ArrayList<String> defaultPathTermToConf =
            new ArrayList<>(Arrays.asList(termType, paperType, confType));

    /* Camins per defecte Conf-? */
    public static final ArrayList<String> defaultPathConfToAuthor =
            new ArrayList<>(Arrays.asList(confType, paperType, authorType));
    public static final ArrayList<String> defaultPathConfToPaper =
            new ArrayList<>(Arrays.asList(confType, paperType));
    public static final ArrayList<String> defaultPathConfToTerm =
            new ArrayList<>(Arrays.asList(confType, paperType, termType));
    public static final ArrayList<String> defaultPathConfToConf =
            new ArrayList<>(Arrays.asList(confType, paperType, confType)); //??
}
