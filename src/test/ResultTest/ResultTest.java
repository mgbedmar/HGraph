package ResultTest;

import GraphTest.Graph.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ResultTest
{

    private static Scanner in;
    private static Result r;
    private static boolean verbose;
    private static boolean debug;
    private static int cols;


    public static void main(String args[])
    {
        initParams(args);
        in = new Scanner(System.in);
        r = new Result(0);
        try
        {

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
                        goToEditResult();
                        break;
                    case 2:
                        goToShowResult();
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


    private static void clearFilters()
    {
        r.unfilterAll();
    }

    private static void sortByRow()
    {
        info("Escriu el numero de columna:");
        Integer col = in.nextInt();
        in.nextLine(); //Consume '\n'
        info("Ascendentment(1) o descendentment(0):");
        Integer dir = in.nextInt();
        in.nextLine(); //Consume '\n'
        r.sort(col, (dir != 0));
    }

    private static void selectName()
    {
        info("Escriu el nom a seleccionar:");
        String x = in.nextLine();
        r.select(x);
    }

    private static void filterName()
    {
        info("Escriu el nom a amagar:");
        String x = in.nextLine();
        r.filter(x);
    }


    private static void hideRow() {
        info("Escriu el numero de fila a amagar:");
        Integer x = readInt();
        in.nextLine(); //Consume '\n'
        r.filter(x);
    }


    private static void goToShowResult(){
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
                    filterName();
                    break;
                case 3:
                    selectName();
                    break;
                case 4:
                    sortByRow();
                    break;
                case 5:
                    clearFilters();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
                    r.resetIndex();
                    break;
            }
        }while(x != 0);

    }

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
        info("Resultat:");

        ArrayList<String> fila = r.getRow();
        if (fila == null) System.out.println("<buit>");
        while (fila != null)
        {
            for(int j = 0; j < fila.size(); j++)
            {
                System.out.printf(fila.get(j) + " ");
            }
            System.out.println();
            fila = r.getRow();
        }

        printMenu(opts);
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

    private static void printMenu(String[] opts) {
        for(int i = 0; i < opts.length && verbose; i++)
        {
            System.out.printf("%d - %s\n", i, opts[i]);
        }
    }

    private static Integer readInt()
    {
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

    private static void info(String s) {
        if(verbose)
            System.out.println(s);

    }

    private static void showMainMenu() {
        String[] opts = {
                "Sortir",
                "Editar Resueltat",
                "Mostrar Resultat"
        };
        info("Menu:");
        printMenu(opts);
    }


    private static void goToEditResult()
    {
        Integer x;
        do{
            showEditResultMenu();
            x = readInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
                case 0:
                    break;
                case 1:
                    initResult();
                    break;
                case 2:
                    addRow();
                    break;
                default:
                    System.out.println("Si us plau, escriu una opció vàlida.");
                    break;
            }
        }while(x != 0);
    }

    private static void addRow() {
        Node a, b;
        Integer x;
        switch(cols){
            case 1:
                info("Afegint fila (Node):");
                a = readNode();
                r.addRow(a);
                break;
            case 2:
                info("Afegint fila (Node, Hetesim):");
                a = readNode();
                x = readInt();
                in.nextLine();
                r.addRow(a, x);
                break;
            case 3:
                info("Afegint fila (Node, Node, Hetesim):");
                a = readNode();
                b = readNode();
                x = readInt();
                in.nextLine();
                r.addRow(a, b, x);
                break;
        }
    }

    private static Node createNode(String name, String type)
    {

        switch(type)
        {
            case Author.TYPE:
                return new Author(name);
            case Term.TYPE:
                return new Term(name);
            case Paper.TYPE:
                return new Paper(name);
            default:
                return new Conf(name);
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

    private static Node readNode() {
        info("Introdueix el nom del node: ");
        String name = in.nextLine();
        String type = readType();
        Node nr = createNode(name, type);
        return nr;
    }

    private static void initResult() {
        info("Inicialitzar result:");

        Integer x;
        do
        {
            info("Numero de columnes (0..3):");
            x =readInt();
        }while(x > 3 || x < 0);
        cols = x;
        r = new Result(cols);
    }

    private static void showEditResultMenu() {
        String[] opts = {
                "tornar",
                "Inicialitzar Result",
                "Afegir fila"
        };
        info("Menu:");
        printMenu(opts);
    }
}