package GraphTest;

import GraphTest.Graph.*;

import java.util.*;

public class GraphTest {
    private static Graph g;
    private static boolean verbose;
    private static boolean debug;
    private static Scanner in;

    private static class EdgeReference
    {
        public Node nA;
        public Node nB;

        public EdgeReference(Node A, Node B)
        {
            this.nA = A;
            this.nB = B;
        }
        @Override
        public String toString(){
            return "(tipusA: "+nA.getType()+", nomA: "+nA.getName()+", "+
                    "tipusB: "+nB.getType()+", nomB: "+nB.getName()+")";
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
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");

            }
        }while(x != 0);
    }


    private static void info(String s) {
        if(verbose)
            System.out.println(s);

    }


    private static void queryNeighbours() {
        Node n = readNode();

        try
        {
            ArrayList<Node> nIds = g.getNodes(n.getName(), n.getType());
            Set<Node> set = g.getNeighbours(nIds.get(0));
            printSet(set);
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
            Set<Node> nodes = g.getSetOfNodes(type);
            printSet(nodes);
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    private static void printSet(Set<Node> set) {
        for (Node a: set) {
            System.out.println(a.getID()+" "+a.getName()+" "+a.getType()+" ");
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
                Config.authorType,
                Config.paperType,
                Config.termType,
                Config.confType
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
                    addEdge();
                    break;
                case 4:
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
            ArrayList<Node> naIds = g.getNodes(e.nA.getName(), e.nA.getType());
            ArrayList<Node> nbIds = g.getNodes(e.nB.getName(), e.nB.getType());
            g.removeEdge(naIds.get(0), nbIds.get(0));
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
            ArrayList<Node> naIds = g.getNodes(e.nA.getName(), e.nA.getType());
            ArrayList<Node> nbIds = g.getNodes(e.nB.getName(), e.nB.getType());
            g.addEdge(naIds.get(0), nbIds.get(0));
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
        Node A = readNode();
        Node B = readNode();

        EdgeReference e = new EdgeReference(A, B);

        return e;
    }

    private static void removeNode() {
        Node n = readNode();
        try
        {
            ArrayList<Node> nIds = g.getNodes(n.getName(), n.getType());
            g.removeNode(nIds.get(0));
            System.out.println("Node "+n.getName() + " esborrat.");
        }
        catch(DomainException de)
        {
            System.out.println(de.getFriendlyMessage());
            if(debug)
                de.printStackTrace(System.err);
        }

    }

    private static void addNode() {
        Node n = readNode();
        g.addNode(n);
        System.out.println("Node "+n.getName() + " afegit.");

    }

    private static Node createNode(String name, String type)
    {

        switch(type)
        {
            case Config.authorType:
                return new Author(name);
            case Config.termType:
                return new Term(name);
            case Config.paperType:
                return new Paper(name);
            default:
                return new Conf(name);
        }
    }

    private static Node readNode() {

        info("Introdueix el nom del node: ");
        String name = in.nextLine();
        String type = readType();
        Node nr = createNode(name, type);
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
                "Afegir aresta",
                "Esborrar aresta"
        };
        info("Escull una opció:");
        printMenu(opts);
    }

    private static void showQueryMenu(){
        String[] opts = {
                "tornar",
                "Nodes d'un cert tipus",
                "Veins d'un node"
        };
        info("Selecciona el tipus de consulta:");
        printMenu(opts);
    }

}
