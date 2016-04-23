package Presentation;


import Domain.DomainController;
import Domain.DomainException;
import Domain.Graph.Author;
import Domain.Graph.Conf;
import Domain.Graph.Paper;
import Domain.Graph.Term;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class PresentationController
{
    /**
     * El controlador de domini s'instancia al
     * controlador de presentacio.
     */
    private static DomainController dc;
    /**
     * Controla si esta activada l'opcio <em>verbose</em>, que permet no imprimir menus.
     */
    private static boolean verbose;
    /**
     * Controla si esta activada l'opcio <em>debug</em>, que permet obtenir el stackTrace de
     * les excepcions.
     */
    private static boolean debug;

    /**
     * Inner class per referir-se als nodes.
     */
    private static class NodeReference
    {
        public String name;
        public String type;

        public NodeReference(String n, String t)
        {
            name = n;
            type = t;
        }

        @Override
        public String toString(){
            return "(tipus: "+type+", nom: "+name+")";
        }
    }

    /**
     * Inner class per referir-se a les arestes.
     */
    private static class EdgeReference
    {
        public NodeReference nA;
        public NodeReference nB;

        public EdgeReference(NodeReference A, NodeReference B)
        {
            this.nA = A;
            this.nB = B;
        }
        @Override
        public String toString(){
            return "(tipusA: "+nA.type+", nomA: "+nA.name+", "+
                    "tipusB: "+nB.type+", nomB: "+nB.name+")";
        }
    }

    /**
     * Scanner per la lectura de dades de teclat.
     */
    private static Scanner in;

    /**
     * Programa principal.
     * @param args Pot contenir les opcions <em>debug</em> i <em>noverbose</em>
     */
    public static void main(String[] args)
    {
        initParams(args);
        try
        {
            dc = new DomainController();
            in = new Scanner(System.in);
            Integer x;
            do{
                showMainMenu();
                x = readInt();
                in.nextLine(); //Consume '\n'
                switch(x)
                {
                    case 0:
                        break;
                    case 1:
                        goToEditGraph();
                        break;
                    case 2:
                        goToQueryGraph();
                        break;
                    default:
                        System.out.println("Si us plau, escriu una opció vàlida.");
                        break;
                }
            }while(x != 0);

        }
        catch(Exception e)
        {
            System.out.println("PAM! ha petat.");
            if(debug)
            {
                e.printStackTrace(System.err);
            }
        }

    }

    /**
     * Llegeix un enter de teclat.
     * @return l'enter llegit
     */
    private static Integer readInt() {
        while(true)
        {
            try
            {
                Integer x = in.nextInt();
                return x;
            }
            catch(InputMismatchException e)
            {
                in.nextLine(); //Consume '\n'
                System.out.println("Si us plau, introdueix un numero.");
            }
        }

    }

    /**
     * Comprova les opcions amb que s'ha cridat i actualitza les variables corresponents
     * @param args els arguments del main
     */
    private static void initParams(String[] args)
    {
        //defaults
        verbose = true;
        debug = false;
        for(String arg : args)
        {
            switch(arg)
            {
                case "noverbose":
                    verbose = false;
                    break;
                case "debug":
                    debug = true;
                    break;
            }
        }
    }

    /**
     * Mostra el menu de consultes i llegeix l'opcio marcada
     */
    private static void goToQueryGraph() {
        Integer x;
        do{
            showQueryMenu();
            x = readInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
                case 0:
                    break;
                case 1:
                    queryByType();
                    break;
                case 2:
                    queryNeighbours();
                    break;
                case 3:
                    query1to1();
                    break;
                case 4:
                    query1toN();
                    break;
                case 5:
                    queryNtoN();
                    break;
                case 6:
                    queryByReference();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
            }
        }while(x != 0);
    }

    /**
     * Llegeix les dades necessaries per la consulta i va al menu de resultat
     */
    private static void queryByReference()
    {
        info("Indica la parella referència: ");
        NodeReference nRefSource = readNode();
        NodeReference nRefEnd = readNode();
        info("Indica el node font: ");
        NodeReference nSource = readNode();
        try
        {
            Integer refSourceId = selectId(dc.getNodes(nRefSource.name, nRefSource.type),
                nRefSource.name,
                nRefSource.type);
            Integer nRefEndId = selectId(dc.getNodes(nRefEnd.name, nRefEnd.type),
                    nRefEnd.name,
                    nRefEnd.type);
            Integer nSourceId = selectId(dc.getNodes(nSource.name, nSource.type),
                    nSource.name,
                    nSource.type);

            dc.queryByReference(refSourceId, nRefSource.type,
                    nRefEndId, nRefEnd.type,
                    nSourceId, nSource.type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);

        }

    }

    /**
     * Controla el cas en el que mes d'un node te el nom introduit
     * @param nodes Llista de les ids dels nodes que tenen el nom i tipus donats
     * @param name el nom
     * @param type el tipus
     * @return l'index del array que s'ha seleccionat
     * @throws DomainException si es genera en les capes inferiors
     */
    private static Integer selectId(ArrayList<Integer> nodes, String name, String type) throws DomainException {
        if(nodes.size() > 1)
        {
            System.out.println("Hi ha més d'un node amb el mateix nom ("+name+") i tipus ("+type+"):");
            for(int i = 0; i < nodes.size(); i++)
            {
                System.out.println("---("+i+") Id: "+nodes.get(i)+" \nVeïns:");
                dc.queryNeighbours(nodes.get(i), type);
                outputResult();
            }
            info("Selecciona un d'ells:");
            Integer x;
            do{
                x = readInt();
                in.nextLine(); //Consume '\n'
                if(x > nodes.size() || x < 0)
                    System.out.println("Escriu un numero del 0 al "+nodes.size());
            }while(x > nodes.size()|| x < 0);

            return nodes.get(x);
        }
        else
            return nodes.get(0);
    }

    /**
     * Escriu un string si l'opcio <em>verbose</em> esta activada
     * @param s l'string que s'escriu
     */
    private static void info(String s) {
        if(verbose)
            System.out.println(s);

    }

    /**
     * Mostra el menu de resultat i gestiona les opcions escollides
     */
    private static void goToResultMenu() {
        Integer x;
        do{
            showResultMenu();
            x = readInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
                case 0 :
                    break;
                case 1:
                    hideRow();
                    break;
                case 2:
                    hideRows();
                    break;
                case 3:
                    filterName();
                    break;
                case 4:
                    selectName();
                    break;
                case 5:
                    sortByRow();
                    break;
                case 6:
                    clearFilters();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
                    dc.resetResult();
                    break;
            }
        }while(x != 0);
    }

    /**
     * Demana al controlador de domini que tregui els filtres del resultat
     */
    private static void clearFilters()
    {
        dc.clearResultFilters();
    }

    /**
     * Demana al controlador de domini que ordeni de la manera escollida per l'usuari
     */
    private static void sortByRow()
    {
        info("Escriu el numero de columna:");
        Integer col = in.nextInt();
        in.nextLine(); //Consume '\n'
        info("Ascendentment(1) o descendentment(0):");
        Integer dir = in.nextInt();
        in.nextLine(); //Consume '\n'
        dc.sortResultByRow(col, dir);
    }

    /**
     * Selecciona un nom del resultat per a que nomes surtin files que el contenen
     */
    private static void selectName()
    {
        info("Escriu el nom a seleccionar:");
        String x = in.nextLine();
        dc.selectResultName(x);
    }

    /**
     * Filtra un nom del resultat per treure totes les files que el contenen
     */
    private static void filterName()
    {
        info("Escriu el nom a amagar:");
        String x = in.nextLine();
        dc.hideResultName(x);
    }

    /**
     * Amaga un rang de files a partir del seu numero de files
     */
    private static void hideRows() {
        info("Escriu el primer numero del rang:");
        Integer x1 = in.nextInt();
        in.nextLine(); //Consume '\n'
        info("Escriu el segon numero del rang:");
        Integer x2 = in.nextInt();
        in.nextLine(); //Consume '\n'
        dc.hideResultRows(x1, x2);
    }

    /**
     * Amaga una fila a partir del seu numero de fila
     */
    private static void hideRow() {
        info("Escriu el numero de fila a amagar:");
        Integer x = readInt();
        in.nextLine(); //Consume '\n'
        dc.hideResultRow(x);
    }

    /**
     * Mostra el menu d'opcions d'un resultat i gestiona les opcions seleccionades
     */
    private static void showResultMenu() {
        String[] opts = {
                "tornar",
                "Amagar una fila",
                "Amagar un rang",
                "Amagar per nom",
                "Mostrar per nom",
                "Ordenar per columna",
                "Treure tots els filtres"
        };
        info("======Resultat de la consulta======");
        Map<String, ArrayList<String>> filters =  dc.getFilters();
        Integer filteredNamesSize = filters.get("filteredNames").size();
        Integer filteredLinesSize = filters.get("filteredLines").size();
        Integer selectedNamesSize = filters.get("selectedNames").size();

        if(filteredLinesSize > 0 ||
                filteredNamesSize > 0 ||
                selectedNamesSize > 0)
        {
            info("Filtres:");
            if(filteredLinesSize> 0)
            {
                info("Linies amagades:");
                for(int i = 0; i < filteredLinesSize; i++)
                {
                    if(i%6 == 0)
                        System.out.println();
                    System.out.print(filters.get("filteredLines").get(i)+", ");
                }
            }

            if(selectedNamesSize > 0)
            {
                info("Noms seleccionats:");
                for(int i = 0; i < selectedNamesSize; i++)
                {
                    System.out.println("-"+filters.get("selectedNames").get(i));
                }
            }
            else if(filteredNamesSize > 0)
            {
                info("Noms amagats:");
                for(int i = 0; i < filteredNamesSize; i++)
                {
                    System.out.println("-"+filters.get("filteredNames").get(i));
                }
            }
        }
        info("Resultat:");

        outputResult();

        printMenu(opts);
    }

    /**
     * Imprimeix el resultat
     */
    private static void outputResult()
    {
        ArrayList<String> fila = dc.getResultRow();
        if (fila == null) System.out.println("<buit>");
        while (fila != null)
        {
            for(int j = 0; j < fila.size(); j++)
            {
                System.out.printf(fila.get(j) + " ");
            }
            System.out.println();
            fila = dc.getResultRow();
        }
    }

    /**
     * Llegeix les dades necessaries per la consulta i va al menu de resultat
     */
    private static void queryNtoN() {
        info("Font:");
        String typeSource = readType();
        info("Destí:");
        String typeEnd = readType();


        try {
            dc.queryNtoN(typeSource, typeEnd);
            goToResultMenu();
        } catch (DomainException de) {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix les dades necessaries per la consulta i va al menu de resultat
     */
    private static void query1toN() {
        info("Indica la informació del node font:");
        NodeReference nSource = readNode();
        String typeEnd = readType();

        try
        {
            Integer nSourceId = selectId(dc.getNodes(nSource.name, nSource.type),
                    nSource.name,
                    nSource.type);
            dc.query1toN(nSourceId, nSource.type, typeEnd);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix les dades necessaries per la consulta i va al menu de resultat
     */
    private static void query1to1() {
        info("Indica la informació del node font:");
        NodeReference nSource = readNode();
        info("Indica la informació del node destí:");
        NodeReference nEnd = readNode();

        try
        {
            Integer nSourceId = selectId(dc.getNodes(nSource.name, nSource.type),
                    nSource.name,
                    nSource.type);
            Integer nEndId = selectId(dc.getNodes(nEnd.name, nEnd.type),
                    nEnd.name,
                    nEnd.type);
            dc.query1to1(nSourceId, nSource.type,
                    nEndId, nEnd.type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix les dades necessaries per la consulta i va al menu de resultat
     */
    private static void queryNeighbours() {
        NodeReference n = readNode();

        try
        {
            Integer nId = selectId(dc.getNodes(n.name, n.type),
                    n.name,
                    n.type);
            dc.queryNeighbours(nId, n.type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix les dades necessaries per la consulta i va al menu de resultat
     */
    private static void queryByType() {
        String type = readType();
        try
        {
            dc.queryByType(type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix un tipus
     * @return el tipus llegit
     */
    private static String readType() {
        String[] localizedTypes = {
                "Autor",
                "Paper",
                "Terme",
                "Conferència"
        };
        String[] types = {
                Author.TYPE,
                Paper.TYPE,
                Term.TYPE,
                Conf.TYPE
        };

        info("Escull un tipus:");
        printMenu(localizedTypes);
        Integer x;
        do{
            x = readInt();
            in.nextLine(); //Consume '\n'
            if(x > types.length || x < 0)
                System.out.println("Escriu un numero del 0 al 3");
        }while(x > types.length || x < 0);

        return types[x];
    }

    /**
     * Mostra el menu per editar el graf i gestiona les opcions
     */
    private static void goToEditGraph() {
        Integer x;
        do{
            showGraphMenu();
            x = readInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
                case 0:
                    break;
                case 1:
                    addNode();
                    break;
                case 2:
                    removeNode();
                    break;
                case 3:
                    modifyNode();
                    break;
                case 4:
                    addEdge();
                    break;
                case 5:
                    removeEdge();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
                    break;
            }
        }while(x != 0);
    }

    /**
     * Demana les dades necessarias per esborrar una aresta i l'esborra
     */
    private static void removeEdge()
    {
        EdgeReference e = readEdge();

        try
        {
            Integer naId = selectId(dc.getNodes(e.nA.name, e.nA.type),
                    e.nA.name,
                    e.nA.type);
            Integer nbId = selectId(dc.getNodes(e.nB.name, e.nB.type),
                    e.nB.name,
                    e.nB.type);
            dc.removeEdge(naId, e.nA.type,
                    nbId, e.nB.type);
            System.out.println("Aresta " +e.toString()+" esborrada.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Demana les dades necessaries per afegir una aresta i l'afegeix
     */
    private static void addEdge()
    {
        EdgeReference e = readEdge();

        try
        {
            Integer naId = selectId(dc.getNodes(e.nA.name, e.nA.type),
                    e.nA.name,
                    e.nA.type);
            Integer nbId = selectId(dc.getNodes(e.nB.name, e.nB.type),
                    e.nB.name,
                    e.nB.type);
            dc.addEdge(naId, e.nA.type,
                    nbId, e.nB.type);
            System.out.println("Aresta " +e.toString()+" afegida.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }
    }

    /**
     * Llegeix una aresta.
     * @return la referencia a l'aresta llegida
     */
    private static EdgeReference readEdge() {
        info("Indica els nodes que formen l'aresta:");
        NodeReference A = readNode();
        NodeReference B = readNode();

        EdgeReference e = new EdgeReference(A, B);

        return e;
    }

    /**
     * Modifica un node
     */
    private static void modifyNode() {
        NodeReference n = readNode();
        String newName = in.nextLine();
        try
        {
            Integer nId = selectId(dc.getNodes(n.name, n.type),
                    n.name,
                    n.type);
            dc.modifyNode(nId, n.type, newName);
            System.out.println("Node "+n.toString() + " modificat.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix la info per esborrar un node i l'esborra
     */
    private static void removeNode() {
        NodeReference n = readNode();
        try
        {
            Integer nId = selectId(dc.getNodes(n.name, n.type),
                    n.name,
                    n.type);
            dc.removeNode(nId, n.type);
            System.out.println("Node "+n.toString() + " esborrat.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix la info per afegir un node i l'afegeix.
     */
    private static void addNode() {
        NodeReference n = readNode();
        try
        {
            dc.addNode(n.name, n.type);
            System.out.println("Node "+n.toString() + " afegit.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    /**
     * Llegeix les dades d'un node.
     * @return retorna la info del node llegit
     */
    private static NodeReference readNode() {

        info("Introdueix el nom del node: ");
        String name = in.nextLine();
        String type = readType();
        NodeReference nr = new NodeReference(name, type);
        return nr;
    }

    /**
     * Mostra un menu
     * @param opts nombre d'opcions del menu
     */
    private static void printMenu(String[] opts) {
        for(int i = 0; i < opts.length && verbose; i++)
        {
            System.out.printf("%d - %s\n", i, opts[i]);
        }
    }

    /**
     * Mostra el menu main
     */
    private static void showMainMenu() {
        String[] opts = {
                "Sortir",
                "Editar graf",
                "Consultar graf"
        };
        info("Menu:");
        printMenu(opts);
    }

    /**
     * Mostra el menu d'editar el graf
     */
    private static void showGraphMenu(){
        String[] opts = {
                "tornar",
                "Afegir node",
                "Esborrar node",
                "Modificar node",
                "Afegir aresta",
                "Esborrar aresta"
        };
        info("Escull una opció:");
        printMenu(opts);
    }

    /**
     * Mostra el menu de les consultes
     */
    private static void showQueryMenu(){
        String[] opts = {
                "tornar",
                "Nodes d'un cert tipus",
                "Veins d'un node",
                "1 a 1",
                "1 a N",
                "N a N",
                "Per referencia"
        };
        info("Selecciona el tipus de consulta:");
        printMenu(opts);
    }

}
