package QueryTest;


import QueryTest.Graph.Author;
import QueryTest.Graph.Conf;
import QueryTest.Graph.Paper;
import QueryTest.Graph.Term;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class QueryTest
{
    private static DomainController dc;
    private static boolean verbose;
    private static boolean debug;
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
    private static Scanner in;
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
                    query1to1();
                    break;
                case 2:
                    query1toN();
                    break;
                case 3:
                    queryNtoN();
                    break;
                case 4:
                    queryByReference();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
            }
        }while(x != 0);
    }

    private static void queryByReference()
    {
        info("Indica la parella referència: ");
        NodeReference nRefSource = readNode();
        NodeReference nRefEnd = readNode();
        info("Indica el node font: ");
        NodeReference nSource = readNode();
        try
        {
            ArrayList<Integer> nRefSourceIds = dc.getNodes(nRefSource.name, nRefSource.type);
            ArrayList<Integer> nRefEndIds = dc.getNodes(nRefEnd.name, nRefEnd.type);
            ArrayList<Integer> nSourceIds = dc.getNodes(nSource.name, nSource.type);

            dc.queryByReference(nRefSourceIds.get(0), nRefSource.type,
                    nRefEndIds.get(0), nRefEnd.type,
                    nSourceIds.get(0), nSource.type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);

        }



    }

    private static void info(String s) {
        if(verbose)
            System.out.println(s);

    }

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

    private static void clearFilters()
    {
        dc.clearResultFilters();
    }

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

    private static void selectName()
    {
        info("Escriu el nom a seleccionar:");
        String x = in.nextLine();
        dc.selectResultName(x);
    }

    private static void filterName()
    {
        info("Escriu el nom a amagar:");
        String x = in.nextLine();
        dc.hideResultName(x);
    }

    private static void hideRows() {
        info("Escriu el primer numero del rang:");
        Integer x1 = in.nextInt();
        in.nextLine(); //Consume '\n'
        info("Escriu el segon numero del rang:");
        Integer x2 = in.nextInt();
        in.nextLine(); //Consume '\n'
        dc.hideResultRows(x1, x2);
    }

    private static void hideRow() {
        info("Escriu el numero de fila a amagar:");
        Integer x = readInt();
        in.nextLine(); //Consume '\n'
        dc.hideResultRow(x);
    }

    private static void showResultMenu() {
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

    private static void query1toN() {
        info("Indica la informació del node font:");
        NodeReference nsource = readNode();
        String typeEnd = readType();

        try
        {
            ArrayList<Integer> nsourceIds = dc.getNodes(nsource.name, nsource.type);
            dc.query1toN(nsourceIds.get(0), nsource.type, typeEnd);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    private static void query1to1() {
        info("Indica la informació del node font:");
        NodeReference nsource = readNode();
        info("Indica la informació del node destí:");
        NodeReference nend = readNode();

        try
        {
            ArrayList<Integer> nsourceIds = dc.getNodes(nsource.name, nsource.type);
            ArrayList<Integer> nendIds = dc.getNodes(nend.name, nend.type);
            dc.query1to1(nsourceIds.get(0), nsource.type,
                    nendIds.get(0), nend.type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    private static void queryNeighbours() {
        NodeReference n = readNode();

        try
        {
            ArrayList<Integer> nIds = dc.getNodes(n.name, n.type);
            dc.queryNeighbours(nIds.get(0), n.type);
            goToResultMenu();
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

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

    private static void removeEdge()
    {
        EdgeReference e = readEdge();

        try
        {
            ArrayList<Integer> naIds = dc.getNodes(e.nA.name, e.nA.type);
            ArrayList<Integer> nbIds = dc.getNodes(e.nB.name, e.nB.type);
            dc.removeEdge(naIds.get(0), e.nA.type,
                    nbIds.get(0), e.nB.type);
            System.out.println("Aresta " +e.toString()+" esborrada.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    private static void addEdge()
    {
        EdgeReference e = readEdge();

        try
        {
            ArrayList<Integer> naIds = dc.getNodes(e.nA.name, e.nA.type);
            ArrayList<Integer> nbIds = dc.getNodes(e.nB.name, e.nB.type);
            dc.addEdge(naIds.get(0), e.nA.type,
                    nbIds.get(0), e.nB.type);
            System.out.println("Aresta " +e.toString()+" afegida.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }
    }

    private static EdgeReference readEdge() {
        info("Indica els nodes que formen l'aresta:");
        NodeReference A = readNode();
        NodeReference B = readNode();

        EdgeReference e = new EdgeReference(A, B);

        return e;
    }

    private static void modifyNode() {
        NodeReference n = readNode();
        String newName = in.nextLine();
        try
        {
            ArrayList<Integer> nIds =dc.getNodes(n.name, n.type);
            dc.modifyNode(nIds.get(0), n.type, newName);
            System.out.println("Node "+n.toString() + " modificat.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    private static void removeNode() {
        NodeReference n = readNode();
        try
        {
            ArrayList<Integer> nIds =dc.getNodes(n.name, n.type);
            dc.removeNode(nIds.get(0), n.type);
            System.out.println("Node "+n.toString() + " esborrat.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

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

    private static NodeReference readNode() {

        info("Introdueix el nom del node: ");
        String name = in.nextLine();
        String type = readType();
        NodeReference nr = new NodeReference(name, type);
        return nr;
    }

    private static void printMenu(String[] opts) {
        for(int i = 0; i < opts.length && verbose; i++)
        {
            System.out.printf("%d - %s\n", i, opts[i]);
        }
    }

    private static void showMainMenu() {
        String[] opts = {
                "Sortir",
                "Editar graf",
                "Consultar graf"
        };
        info("Menu:");
        printMenu(opts);
    }

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

    private static void showQueryMenu(){
        String[] opts = {
                "tornar",
                "1 a 1",
                "1 a N",
                "N a N",
                "Per referencia"
        };
        info("Selecciona el tipus de consulta:");
        printMenu(opts);
    }

}
