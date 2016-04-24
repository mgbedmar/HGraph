package TestNode;
import TestNode.DomainException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import TestNode.Term;
import TestNode.Paper;
import TestNode.Node;
import TestNode.Author;
import TestNode.Conf;
import TestNode.Ghost;
import TestNode.DomainException;

public class TestNode {
    private static ArrayList<Node> misnodos;

    public TestNode() {
        misnodos = new ArrayList<Node>();
    }

    public TestNode(ArrayList<Node> milista) {
        misnodos = milista;
    }

    private Node getNode(int i) {
        return misnodos.get(i);
    }

     private void afegirNode(Node node) {
        misnodos.add(node);
    }

    private void removeNode(Node n) {
        int i = 0;
        while (!n.equals(misnodos.get(i)))
        {
            i++;
        }
        misnodos.remove(i);
    }

    private void reinitTest() {
        
            misnodos.clear();
        
    }


    private void readremoveEdge(Scanner miscan) {
        System.out.println("Tria el node source per afegir l'aresta indicant el seu id intern");
        int source, desti;
        this.listNode();
        source = miscan.nextInt();
        System.out.println("Tria el node desti per afegir l'aresta indicant el seu id intern");
        this.listNode();
        desti = miscan.nextInt();
        this.addEdge(source, desti);
        System.out.println("S'ha afegit l'aresta que va de " + this.getNode(source).getID() +
                " " + this.getNode(source).getName() + " " + this.getNode(source).getType() +
                " al node " + this.getNode(desti).getID() + " " + this.getNode(desti).getName() +
                " " + this.getNode(desti).getType());
    }

    private void eraseAllEdges() {
        Iterator<Node> itr;
        Node n = null;
        for (int i = 0; i < misnodos.size(); i++)
        {
            itr = misnodos.get(i).getNeighbours().iterator();
            n = misnodos.get(i);
            if (n.getType().equals(Config.paperType))
            {
                itr = misnodos.get(i).getNeighbours(Config.authorType).iterator();
                while (itr.hasNext())
                {
                    try
                    {
                        misnodos.get(i).removeEdge((Node) itr.next());
                    }
                    catch (DomainException ex)
                    {
                        ex.printStackTrace();

                    }
                }
                itr = misnodos.get(i).getNeighbours(Config.confType).iterator();
                while (itr.hasNext())
                {
                    try
                    {
                        misnodos.get(i).removeEdge((Node) itr.next());
                    }
                    catch (DomainException ex)
                    {
                        ex.printStackTrace();

                    }

                }
                itr = misnodos.get(i).getNeighbours(Config.termType).iterator();
                while (itr.hasNext())
                {
                    try
                    {
                        misnodos.get(i).removeEdge((Node) itr.next());
                    }
                    catch (DomainException ex)
                    {
                        ex.printStackTrace();

                    }

                }

            }
            else
            {
                while (itr.hasNext())
                {
                    try
                    {
                        misnodos.get(i).removeEdge((Node) itr.next());
                    }
                    catch (DomainException ex)
                    {

                    }
                }
            }
        }
    }

    public void ShowContent() {
        for (int i = 0; i < misnodos.size(); i++)
        {    
        	if(!misnodos.get(i).getType().equals(Config.ghostType))
        			{
        				System.out.println("Tenim el següent node :" + misnodos.get(i).getID() +
        				"del tipo " + misnodos.get(i).getType() + "y su contenido es " +
        				misnodos.get(i).getName());
        			
        	if (!misnodos.get(i).getType().equals(Config.paperType))
            
                this.ShowNeighbours(i);
            else if (misnodos.get(i).equals(Config.paperType))
            {
                System.out.println("Su lista de adyacencias por tipos es ");

                this.ShowNeighbours(i, Config.authorType);
                this.ShowNeighbours(i, Config.confType);
                this.ShowNeighbours(i, Config.termType);
            }
            
        			}
            else 
            {
            	System.out.println("Tenim un node Ghost amb id "+misnodos.get(i).getID()+"sense cap adjacent");;
            }
        }
    }

    private void listNode() {
        System.out.println("Id interno \t IdClase\tContingut\t Tipus");
        for (int i = 0; i < misnodos.size(); i++)
        {
            System.out.println(+i + "\t \t" + misnodos.get(i).getID() + "\t \t" +
                    misnodos.get(i).getName() + "\t " + misnodos.get(i).getType());
        }
    }

    private void addEdge(int i, int j) {
        try
        {
            misnodos.get(i).addEdge(misnodos.get(j));
        }
        catch (DomainException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void removeEdge(int i, int j) {
        try
        {
            misnodos.get(i).removeEdge(misnodos.get(j));
        }
        catch (Exception ex)
        {
        }
    }

    private void addallEdges() {
        for (int i = 0; i < misnodos.size(); i++)
        {
            for (int j = 0; j < misnodos.size(); j++)
            {
                System.out.println("Provant d'afegir" + misnodos.get(i).getID() + " " + misnodos.get(i).getName() + "de tipus" + misnodos.get(i).getType() + "amb el node" + misnodos.get(j).getID() + " " + misnodos.get(j).getName() + "de tipus" + misnodos.get(j).getType());
                try
                {
                    misnodos.get(i).addEdge(misnodos.get(j));

                }
                catch (DomainException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void ShowNeighbours(int i, String type) {
        Iterator<Node> itr = misnodos.get(i).getNeighbours(type).iterator();

        int j = 1;
        Node n;

        switch (type)
        {

            case Config.authorType:
                System.out.println("Autores");
                while (itr.hasNext())
                {
                    n = itr.next();
                    System.out.println("Id intern \t IdClase\tContingut");
                    System.out.print(j + "\t\t \t" + n.getID() + "\t ");
                    System.out.print(n.getName() + "\t ");
                    System.out.print(n.getType() + "\t ");
                    System.out.print("\n");
                    j++;
                }
                break;
            case Config.confType:
                System.out.println("Conferencias");
                while (itr.hasNext())
                {
                    n = itr.next();
                    System.out.println("Id intern \t IdClase\tContingut");
                    System.out.print(i + "\t \t\t" + n.getID() + " ");
                    System.out.print(n.getName() + "\t ");
                    System.out.print(n.getType() + "\t ");
                    System.out.print("\n");
                    i++;
                }
                break;
            case Config.termType:
                System.out.println("Termes");
                while (itr.hasNext())
                {
                    n = itr.next();
                    System.out.println("Id intern \t IdClase\tContingut\t ");
                    System.out.print(i + "\t \t\t" + n.getID());
                    System.out.print(n.getName() + "\t ");
                    System.out.print(n.getType() + "\t ");
                    System.out.print("\n");
                    i++;
                }
                break;
            default:
                break;

        }
        System.out.println("No té més adjacents");
    }

    private Node getNeighbour(int i, int id, String type) {
        Iterator<Node> itr = misnodos.get(i).getNeighbours(type).iterator();
        Node n = null;
        if (itr.hasNext())
        {
            for (int j = 1; j <= i; j++)
                n = (Node) itr.next();
        }

        return n;
    }

    private Node createNode(int id, String nombre, int tipus) {

        Node n = null;
        switch (tipus)
        {
            case 1:
                n = new Paper(id, nombre);
                this.afegirNode(n);
                break;
            case 2:
                n = new Author(id, nombre);
                this.afegirNode(n);
                break;
            case 3:
                n = new Term(id, nombre);
                this.afegirNode(n);
                break;
            case 4:
                n = new Conf(id, nombre);
                this.afegirNode(n);
                break;
            case 5:
                n = new Ghost(id);
                this.afegirNode(n);
                break;
            default:
                break;

        }

        return n;
    }

    private Node getNeighbour(int i, int id) {
        Node n = null;
        Iterator<Node> itr = misnodos.get(i).getNeighbours().iterator();

        if (itr.hasNext())
        {
            for (int j = 1; j <= i; j++)
                n = (Node) itr.next();
        }

        return n;
    }

    private void ShowNeighbours(int i) {
        Iterator<Node> itr = misnodos.get(i).getNeighbours().iterator();
        System.out.println("La seva llista d'adjacències és ");
        System.out.println("Id intern \t IdClase\tContingut\tTipus");
        Node n = null;
        int j = 0;
        while (itr.hasNext())
        {
            n = (Node) itr.next();
            if(!n.getType().equals(Config.ghostType))
            {
            	System.out.print(j + "\t\t " + n.getID() + "");
            	System.out.print("\t" + n.getName() + "\t ");
            	System.out.print("" + n.getType());
            	System.out.print("\n");
            	j++;
             }
           
            
         }
        System.out.println("No té més adjacents");
    }

    private Node eraseEdgePaper(Scanner miscan, int source, String type) {
        Node n = null;
        int desti;
        this.ShowNeighbours(source, type);
        desti = miscan.nextInt();

        n = this.getNeighbour(desti, source, type);
        try
        {
            this.getNode(source).removeEdge(n);
        }
        catch (Exception ex)
        {

        }
        return n;//return node de l'aresta eliminada
    }

    private void dropNode(Scanner miscan) {
        Node n = null;
        int esborrar;
        System.out.println("Tria quin node vols esborrar indicant el seu id intern\n");
        this.listNode();
        esborrar = miscan.nextInt();
        n = this.getNode(esborrar);
        System.out.println("Se ha borrado el nodo " + n.getID() + " " + n.getName() + "del tipus " + n.getType());
        this.removeNode(n);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        TestNode mitest = new TestNode();
        Node n = null;
        Scanner miscan = new Scanner(System.in);
        int option = 0;
        int id, tipus, source, desti, aresta;
        String nombre;

        while (option != 10)
        {
            System.out.println("Tria una opcio:\n1 Crear node\n2 Afegir aresta\n3 Esborrar aresta \n4 Mostrar info tots els nodes creats \n5 Esborrar totes les arestes,\n6 Afegir totes les arestes\n7 Consultar adjiacencies\n8 EsborrarNode\n9 Esborrar tots els nodes\n10 Sortir\n");
            option = miscan.nextInt();

            switch (option)
            {
                case 1:
                    System.out.println("Introdueix una id ");
                    id = miscan.nextInt();
                    miscan.nextLine();
                    System.out.println("Introdueix un nom ");
                    nombre = miscan.nextLine();
                    System.out.println("Tria un tipus\n 1 Paper\n 2 Author\n 3 Terme \n4Conferencia\n5 Fantasma");
                    tipus = miscan.nextInt();
                    n = mitest.createNode(id, nombre, tipus);

                    System.out.println("S'ha creat el node " + n.getType() + " " + n.getID() + " " + n.getName() + "\n");
                    break;
                case 2:
                    mitest.readremoveEdge(miscan);
                    break;
                case 3:
                    System.out.println("Tria el node source per esborrar l'aresta indicant el seu id intern");
                    mitest.listNode();
                    source = miscan.nextInt();
                    if (mitest.getNode(source).getType().equals(Config.paperType))
                    {
                        System.out.println("tria el tipus d'aresta que vols eliminar \n 1 Author \n 2 Conferència \n 3 Terme ");
                        aresta = miscan.nextInt();
                        switch (aresta)
                        {
                            case 1:
                                n = mitest.eraseEdgePaper(miscan, source, Config.authorType);
                                break;
                            case 2:
                                n = mitest.eraseEdgePaper(miscan, source, Config.confType);
                                break;
                            case 3:
                                n = mitest.eraseEdgePaper(miscan, source, Config.termType);
                                break;

                        }

                        System.out.println("S'ha esborrat l'aresta " + mitest.getNode(source).getID() + " " + mitest.getNode(source).getName() + " " + mitest.getNode(source).getType() + " a " + n.getID() + " " + n.getName() + " " + n.getType());
                    }
                    else
                    {
                        System.out.println("Tria el node desti per esborrar l'aresta indicant el seu id intern");
                        mitest.ShowNeighbours(source);
                        desti = miscan.nextInt();
                        n = mitest.getNeighbour(source, desti);
                        mitest.removeEdge(source, desti);
                    }
                    break;
                case 4:
                    mitest.ShowContent();
                    break;
                case 5:
                    mitest.eraseAllEdges();
                    System.out.println("Totes les llistes d'adjiacències estan buides");
                    break;
                case 6:
                    mitest.addallEdges();
                    break;
                case 7:
                    System.out.println("Tria el node source per consultar la llista d'adjacències indicant el seu id intern");
                    mitest.listNode();
                    source = miscan.nextInt();
                    if (mitest.getNode(source).getType().equals(Config.paperType))
                    {
                        System.out.println("tria el tipus de la llista d'adjacències \n 1 Author \n 2 Conferència \n 3 Terme ");
                        aresta = miscan.nextInt();
                        switch (aresta)
                        {
                            case 1:
                                mitest.ShowNeighbours(source, Config.authorType);

                                break;

                            case 2:
                                mitest.ShowNeighbours(source, Config.confType);

                                break;
                            case 3:
                                mitest.ShowNeighbours(source, Config.termType);

                                break;

                        }


                    }
                    else
                    {
                        mitest.ShowNeighbours(source);
                    }

                    break;


                case 8:
                    mitest.dropNode(miscan);
                    break;
                case 9:
                    System.out.println("Se han destruido todos los nodos");
                    mitest.reinitTest();
            }

        }
        System.out.println("Test terminado ");
        miscan.close();
    }
}
