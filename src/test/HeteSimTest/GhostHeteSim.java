package HeteSimTest;


import HeteSimTest.Graph.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GhostHeteSim {
    private static Node n;
    private static boolean verbose;
    private static boolean debug;
    private static Scanner in;

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
            in = new Scanner(System.in);
            Integer x;
            do{
                showMenu();
                x = readInt();
                in.nextLine(); //Consume '\n'
                switch(x)
                {
                    case 0:
                        break;
                    case 1:
                        goToEditNode();
                        break;
                    case 2:
                        queryNode();
                        break;
                    case 3:
                        compareNode();
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

    private static void compareNode() {
        info("Indica la informació del node a comparar:");
        Node n2 = readNode();
        info("Indica la id del node:");
        n2.setID(readInt());
        try
        {
            if(n.equals(n2))
                System.out.println("Son iguals.");
            else
                System.out.println("Son diferents.");
        }
        catch(NullPointerException e)
        {
            System.out.println("Si us plau, inicialitza el node abans de consultar-lo.");
        }

    }

    private static void queryNode() {
        try
        {
            System.out.println("Nom: "+n.getName()+" tipus: "+ n.getType() +" id: "+n.getID());
        }
        catch(NullPointerException e)
        {
            System.out.println("Si us plau, inicialitza el node abans de consultar-lo.");
        }

    }


    private static void goToEditNode() {
        Integer x;
        do{
            showEditMenu();
            x = readInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
                case 0:
                    break;
                case 1:
                    n = readNode();
                    break;
                case 2:
                    n = readNodeWithId();
                    break;
                case 3:
                    modifyId();
                    break;
                case 4:
                    modifyName();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
                    break;
            }
        }while(x != 0);
    }

    private static void modifyName() {
        try
        {
            info("Escriu el nou nom del node:");
            n.setName(in.nextLine());
        }
        catch(NullPointerException e)
        {
            System.out.println("Si us plau, inicialitza el node abans de consultar-lo.");
        }


    }

    private static void modifyId() {
        try
        {
            info("Escriu la nova id del node:");
            n.setID(readInt());
        }
        catch(NullPointerException e)
        {
            System.out.println("Si us plau, inicialitza el node abans de consultar-lo.");
        }
    }

    private static Node readNodeWithId() {
        info("Introdueix el nom del node: ");
        String name = in.nextLine();
        String type = readType();
        info("Introdueix la id del node: ");
        Integer id = readInt();
        Node nr = null;
        switch(type)
        {
            case Author.TYPE:
                nr = new Author(id, name);
                break;
            case Conf.TYPE:
                nr = new Conf(id, name);
                break;
            case Paper.TYPE:
                nr = new Paper(id, name);
                break;
            case Term.TYPE:
                nr = new Term(id, name);
                break;
        }
        return nr;
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
    private static Node readNode() {

        info("Introdueix el nom del node: ");
        String name = in.nextLine();
        String type = readType();
        Node nr = null;
        switch(type)
        {
            case Author.TYPE:
                nr = new Author(name);
                break;
            case Conf.TYPE:
                nr = new Conf(name);
                break;
            case Paper.TYPE:
                nr = new Paper(name);
                break;
            case Term.TYPE:
                nr = new Term(name);
                break;
        }
        return nr;
    }
    private static void showEditMenu() {
        String[] opts = {
                "Sortir",
                "Construir node(name)",
                "Construir node(id, name)",
                "Canviar id",
                "Canviar nom"
        };
        info("Menu:");
        printMenu(opts);
    }

    private static void info(String s) {
        if(verbose)
            System.out.println(s);

    }
    private static void showMenu() {
        String[] opts = {
                "Sortir",
                "Editar node",
                "Consultar node",
                "Comparar node"
        };
        info("Menu:");
        printMenu(opts);
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
    private static void printMenu(String[] opts) {
        for(int i = 0; i < opts.length && verbose; i++)
        {
            System.out.printf("%d - %s\n", i, opts[i]);
        }
    }
}
