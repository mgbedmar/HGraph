package Presentation;


import Domain.DomainController;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class PresentationController
{
    private static DomainController dc;
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

    public static void main(String[] args)
    {
        dc = new DomainController();
        Scanner entrada = new Scanner(System.in);
        Integer x;
        do{
            showMainMenu();
            x = entrada.nextInt();
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
        Scanner entrada = new Scanner(System.in);
        Integer x;
        do{
            showQueryMenu();
            x = entrada.nextInt();
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
        System.out.println("Indica la parella refèrencia: ");
        NodeReference nRefSource = readNode();
        NodeReference nRefEnd = readNode();
        System.out.println("Indica el node font: ");
        NodeReference nSource = readNode();


        dc.queryByReference(nRefSource.name, nRefSource.type, nRefEnd.name, nRefEnd.type, nSource.name, nSource.type);
        goToResultMenu();
    }

    private static void goToResultMenu() {
        Scanner entrada = new Scanner(System.in);
        Integer x;
        do{
            showResultMenu();
            x = entrada.nextInt();
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
        Scanner entrada = new Scanner(System.in);
        System.out.println("Escriu el numero de columna:");
        Integer col = entrada.nextInt();
        System.out.println("Ascendentment(0) o descendentment(1):");
        Integer dir = entrada.nextInt();
        dc.sortResultByRow(col, dir);
    }

    private static void selectName()
    {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Escriu el nom a seleccionar:");
        String x = entrada.nextLine();
        dc.selectResultName(x);
    }

    private static void filterName()
    {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Escriu el nom a amagar:");
        String x = entrada.nextLine();
        dc.hideResultName(x);
    }

    /*
        private static void selectRows() {
            Scanner entrada = new Scanner(System.in);
            System.out.println("Escriu el primer numero del rang:");
            Integer x1 = entrada.nextInt();
            System.out.println("Escriu el segon numero del rang:");
            Integer x2 = entrada.nextInt();
            dc.selectResultRows(x1, x2);
        }
    */
    private static void hideRows() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Escriu el primer numero del rang:");
        Integer x1 = entrada.nextInt();
        System.out.println("Escriu el segon numero del rang:");
        Integer x2 = entrada.nextInt();
        dc.hideResultRows(x1, x2);
    }

    private static void hideRow() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Escriu el numero de fila a amagar:");
        Integer x = entrada.nextInt();
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
        System.out.println("======Resultat de la consulta======");
        Map<String, ArrayList<String>> filters =  dc.getFilters();
        Integer filteredNamesSize = filters.get("filteredNames").size();
        Integer filteredLinesSize = filters.get("filteredLines").size();
        Integer selectedNamesSize = filters.get("selectedNames").size();

        if(filteredLinesSize > 0 ||
                filteredNamesSize > 0 ||
                selectedNamesSize > 0)
        {
            System.out.println("Filtres:");
            if(filteredLinesSize> 0)
            {
                System.out.println("Linies amagades:");
                for(int i = 0; i < filteredLinesSize; i++)
                {
                    if(i%6 == 0)
                        System.out.println();
                    System.out.print(filters.get("filteredLines").get(i)+", ");
                }
            }

            if(selectedNamesSize > 0)
            {
                System.out.println("Noms seleccionats:");
                for(int i = 0; i < selectedNamesSize; i++)
                {
                    System.out.println("-"+filters.get("selectedNames").get(i));
                }
            }
            else if(filteredNamesSize > 0)
            {
                System.out.println("Noms amagats:");
                for(int i = 0; i < filteredNamesSize; i++)
                {
                    System.out.println("-"+filters.get("filteredNames").get(i));
                }
            }
        }
        System.out.println("Resultat:");
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


        printMenu(opts);
    }

    private static void queryNtoN() {
        System.out.println("Font:");
        String typeSource = readType();
        System.out.println("Destí:");
        String typeEnd = readType();


        dc.queryNtoN(typeSource, typeEnd);
        goToResultMenu();
    }

    private static void query1toN() {
        System.out.println("Indica la informació del node font:");
        NodeReference nsource = readNode();
        String typeEnd = readType();


        dc.query1toN(nsource.name, nsource.type, typeEnd);
        goToResultMenu();
    }

    private static void query1to1() {
        System.out.println("Indica la informació del node font:");
        NodeReference nsource = readNode();
        System.out.println("Indica la informació del node destí:");
        NodeReference nend = readNode();

        dc.query1to1(nsource.name, nsource.type, nend.name, nend.type);
        goToResultMenu();
    }

    private static void queryNeighbours() {
        NodeReference n = readNode();

        dc.queryNeighbours(n.name, n.type);
        goToResultMenu();
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
                "author",
                "paper",
                "therm",
                "conference"
        };

        Scanner entrada = new Scanner(System.in);
        System.out.println("Escull un tipus:");
        printMenu(localizedTypes);
        Integer x = entrada.nextInt();
        return types[x];
    }

    private static void goToEditGraph() {
        Scanner entrada = new Scanner(System.in);
        Integer x;
        do{
            showGraphMenu();
            x = entrada.nextInt();
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

        dc.removeEdge(e.nA.name, e.nA.type, e.nB.name, e.nB.type);
        System.out.println("Aresta " +e.toString()+" esborrada.");
    }

    private static void addEdge() {
        EdgeReference e = readEdge();

        dc.addEdge(e.nA.name, e.nA.type, e.nB.name, e.nB.type);
        System.out.println("Aresta " +e.toString()+" afegida.");
    }

    private static EdgeReference readEdge() {
        System.out.println("Indica els nodes que formen l'aresta:");
        NodeReference A = readNode();
        NodeReference B = readNode();

        EdgeReference e = new EdgeReference(A, B);

        return e;
    }

    private static void modifyNode() {
        Scanner entrada = new Scanner(System.in);
        NodeReference n = readNode();
        String newName = entrada.nextLine();

        dc.modifyNode(n.name, n.type, newName);
        System.out.println("Node "+n.toString() + " modificat.");
    }

    private static void removeNode() {
        NodeReference n = readNode();

        dc.removeNode(n.name, n.type);
        System.out.println("Node "+n.toString() + " esborrat.");
    }

    private static void addNode() {
        NodeReference n = readNode();


        dc.addNode(n.name, n.type);
        System.out.println("Node "+n.toString() + " afegit.");
    }

    private static NodeReference readNode() {

        Scanner entrada = new Scanner(System.in);
        System.out.println("Introdueix el nom del node: ");
        String name = entrada.nextLine();
        String type = readType();
        NodeReference nr = new NodeReference(name, type);
        return nr;
    }

    private static void printMenu(String[] opts) {
        for(int i = 0; i < opts.length; i++)
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
        System.out.println("Menu:");
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
        System.out.println("Escull una opció:");
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
        System.out.println("Selecciona el tipus de consulta:");
        printMenu(opts);
    }

}
