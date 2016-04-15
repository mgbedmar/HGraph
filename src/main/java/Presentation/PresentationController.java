package Presentation;


import java.util.ArrayList;
import java.util.Scanner;

public class PresentationController
{

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

        //TODO controller call
        //consultaReferencia(nRefSource.name, nRefSource.type, nRefEnd.name, nRefEnd.type, nSource.name, nSource.type)

    }

    private static void queryNtoN() {
        System.out.println("Font:");
        String typeSource = readType();
        System.out.println("Destí:");
        String typeEnd = readType();

        //TODO controller call
        //consultaNaN(typeSource, typeEnd)
    }

    private static void query1toN() {
        System.out.println("Indica la informació del node font:");
        NodeReference nsource = readNode();
        String typeEnd = readType();

        //TODO controller call
        //consulta1aN(nsource.name, nsource.type, typeEnd)
    }

    private static void query1to1() {
        System.out.println("Indica la informació del node font:");
        NodeReference nsource = readNode();
        System.out.println("Indica la informació del node destí:");
        NodeReference nend = readNode();

        //TODO controller call
        //consultaVeins(n.name, n.type)
    }

    private static void queryNeighbours() {
        NodeReference n = readNode();
        //TODO controller call
        //consultaVeins(n.name, n.type)
    }

    private static void queryByType() {
        String type = readType();

        //TODO controller call
        //consultaTipus(type)
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
                    eraseNode();
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
        //TODO: controller call
        //esborrarAresta(e.nA.name, e.nA.type, e.nB.name, e.nB.type)

        System.out.println("Aresta " +e.toString()+" esborrada.");
    }

    private static void addEdge() {
        EdgeReference e = readEdge();

        //TODO: controller call
        //afegirAresta(e.nA.name, e.nA.type, e.nB.name, e.nB.type)

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
        //TODO: controller call
        //modificarNode(n.name, n.type, newName)
        System.out.println("Node "+n.toString() + " modificat.");
    }

    private static void eraseNode() {
        NodeReference n = readNode();

        //TODO: controller call
        //esborrarNode(n.name, n.type)
        System.out.println("Node "+n.toString() + " esborrat.");
    }

    private static void addNode() {
        NodeReference n = readNode();

        //TODO: controller call
        //afegirNode(n.name, n.type)
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
            System.out.printf("%d - %s", i, opts[i]);
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
