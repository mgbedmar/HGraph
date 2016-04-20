package Presentation;


import Domain.Config;
import Domain.DomainController;
import Domain.DomainException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class PresentationController
{
    private static DomainController dc;
    private static boolean verbose;
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
        verbose = !(args.length > 0 && args[0].equals("noverbose"));

        dc = new DomainController();
        in = new Scanner(System.in);
        Integer x;
        do{
            showMainMenu();
            x = in.nextInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
                case 1:
                    goToEditGraph();
                    break;
                case 2:
                    goToQueryGraph();
                    break;
            }
        }while(x != 0);

    }

    private static void goToQueryGraph() {
        Integer x;
        do{
            showQueryMenu();
            x = in.nextInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
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
            }
        }while(x != 0);
    }

    private static void queryByReference() {
        info("Indica la parella referència: ");
        NodeReference nRefSource = readNode();
        NodeReference nRefEnd = readNode();
        info("Indica el node font: ");
        NodeReference nSource = readNode();

        ArrayList<Integer> nRefSourceIds = dc.getNodes(nRefSource.name, nRefSource.type);
        ArrayList<Integer> nRefEndIds = dc.getNodes(nRefEnd.name, nRefEnd.type);
        ArrayList<Integer> nSourceIds = dc.getNodes(nSource.name, nSource.type);
        if(nRefSourceIds.isEmpty() || nRefEndIds.isEmpty() || nSourceIds.isEmpty())
        {
            System.out.println("Algun dels noms introduits no es correcte");
        }
        else
        {
            dc.queryByReference(nRefSourceIds.get(0), nRefSource.type,
                    nRefEndIds.get(0), nRefEnd.type,
                    nSourceIds.get(0), nSource.type);
            goToResultMenu();
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
            x = in.nextInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
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

    /*
        private static void selectRows() {
            info("Escriu el primer numero del rang:");
            Integer x1 = in.nextInt();
            in.nextLine(); //Consume '\n'
            info("Escriu el segon numero del rang:");
            Integer x2 = in.nextInt();
            in.nextLine(); //Consume '\n'
            dc.selectResultRows(x1, x2);
        }
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

    private static void hideRow() {
        info("Escriu el numero de fila a amagar:");
        Integer x = in.nextInt();
        in.nextLine(); //Consume '\n'
        dc.hideResultRow(x);
    }

    private static void showResultMenu() {
        String[] opts = {
                "tornar",
                "Amagar una fila",
                "Amagar un rang",
                //"Mostrar un rang",
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
        /*
        Integer resultSize = dc.getResultSize();
        if(resultSize > 0)
        {
            for(int i = 0; i < resultSize; i++)
            {
                ArrayList<String> fila = dc.getResultRow();
                for(int j = 0; j < fila.size(); j++)
                {
                    System.out.printf(fila.get(j));
                }
                System.out.println();
            }
        }
        else
        {
            System.out.println("<buit>");
        }
        */

        printMenu(opts);
    }

    private static void queryNtoN() {
        info("Font:");
        String typeSource = readType();
        info("Destí:");
        String typeEnd = readType();


        dc.queryNtoN(typeSource, typeEnd);
        goToResultMenu();
    }

    private static void query1toN() {
        info("Indica la informació del node font:");
        NodeReference nsource = readNode();
        String typeEnd = readType();

        ArrayList<Integer> nsourceIds = dc.getNodes(nsource.name, nsource.type);
        if(nsourceIds.isEmpty())
        {
            System.out.println("El node amb nom "+nsource.name+" no existeix al graf.");
        }
        else
        {
            dc.query1toN(nsourceIds.get(0), nsource.type, typeEnd);
            goToResultMenu();
        }

    }

    private static void query1to1() {
        info("Indica la informació del node font:");
        NodeReference nsource = readNode();
        info("Indica la informació del node destí:");
        NodeReference nend = readNode();

        ArrayList<Integer> nsourceIds = dc.getNodes(nsource.name, nsource.type);
        ArrayList<Integer> nendIds = dc.getNodes(nend.name, nend.type);
        if(nsourceIds.isEmpty() || nendIds.isEmpty())
        {
            System.out.println("Algun dels noms escrits no es troba al graf");
        }
        else
        {
            dc.query1to1(nsourceIds.get(0), nsource.type,
                    nendIds.get(0), nend.type);
            goToResultMenu();
        }

    }

    private static void queryNeighbours() {
        NodeReference n = readNode();

        ArrayList<Integer> nIds = dc.getNodes(n.name, n.type);
        if(nIds.isEmpty())
        {
            System.out.println("El node amb nom "+n.name+" no existeix al graf.");
        }
        else
        {
            dc.queryNeighbours(nIds.get(0), n.type);
            goToResultMenu();
        }

    }

    private static void queryByType() {
        String type = readType();

        dc.queryByType(type);
        goToResultMenu();
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
        Integer x = in.nextInt();
        in.nextLine(); //Consume '\n'
        return types[x];
    }

    private static void goToEditGraph() {
        Integer x;
        do{
            showGraphMenu();
            x = in.nextInt();
            in.nextLine(); //Consume '\n'
            switch(x)
            {
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
                    break;
            }
        }while(x != 0);
    }

    private static void removeEdge() {
        EdgeReference e = readEdge();

        ArrayList<Integer> naIds = dc.getNodes(e.nA.name, e.nA.type);
        ArrayList<Integer> nbIds = dc.getNodes(e.nB.name, e.nB.type);
        if(naIds.isEmpty() || nbIds.isEmpty())
        {
            System.out.println("Algun dels noms es incorrecte.");
        }
        else
        {
            dc.removeEdge(naIds.get(0), e.nA.type,
                    nbIds.get(0), e.nB.type);
            System.out.println("Aresta " +e.toString()+" esborrada.");
        }


    }

    private static void addEdge() {
        EdgeReference e = readEdge();

        ArrayList<Integer> naIds = dc.getNodes(e.nA.name, e.nA.type);
        ArrayList<Integer> nbIds = dc.getNodes(e.nB.name, e.nB.type);
        if(naIds.isEmpty() || nbIds.isEmpty())
        {
            System.out.println("Algun dels noms es incorrecte.");
        }
        else
        {
            try {
                dc.addEdge(naIds.get(0), e.nA.type,
                        nbIds.get(0), e.nB.type);
                System.out.println("Aresta " +e.toString()+" afegida.");
            } catch (DomainException e1) {
                System.out.println(e1.getFriendlyMessage());
            }
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
        ArrayList<Integer> nIds =dc.getNodes(n.name, n.type);
        if(nIds.isEmpty())
        {
            System.out.println("El nom introduit no es troba al graf.");
        }
        else
        {
            dc.modifyNode(nIds.get(0), n.type, newName);
            System.out.println("Node "+n.toString() + " modificat.");
        }

    }

    private static void removeNode() {
        NodeReference n = readNode();
        ArrayList<Integer> nIds =dc.getNodes(n.name, n.type);
        if(nIds.isEmpty())
        {
            System.out.println("El nom introduit no es troba al graf.");
        }
        else
        {
            dc.removeNode(nIds.get(0), n.type);
            System.out.println("Node "+n.toString() + " esborrat.");
        }


    }

    private static void addNode() {
        NodeReference n = readNode();

        dc.addNode(n.name, n.type);
        System.out.println("Node "+n.toString() + " afegit.");
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
