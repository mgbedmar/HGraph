package ParserTest;

import GraphTest.Graph.*;

import java.io.IOException;
import java.util.Set;


public class Controller {
    public GraphFileManager pers;
    public Graph g;

    public Controller() {
        g = new Graph();
        pers = new GraphFileManager();
    }

    public void importGraph() throws DomainException, IOException, PersistenceException {
        pers.startLoading(".");

        String[] elem;

        //Nodes
        while ((elem = pers.getAuthor()) != null) {
            Author a = new Author(Integer.parseInt(elem[0]), elem[1]);
            g.addNode(a);
        }
        while ((elem = pers.getPaper()) != null) {
            Paper a = new Paper(Integer.parseInt(elem[0]), elem[1]);
            g.addNode(a);
        }
        while ((elem = pers.getTerm()) != null) {
            Term a = new Term(Integer.parseInt(elem[0]), elem[1]);
            g.addNode(a);
        }
        while ((elem = pers.getConf()) != null) {
            Conf a = new Conf(Integer.parseInt(elem[0]), elem[1]);
            g.addNode(a);
        }

        //Arestes
        while ((elem = pers.getPaperAuthor()) != null) {
            Node a = g.getNode(Integer.parseInt(elem[0]), "paper");
            Node b = g.getNode(Integer.parseInt(elem[1]), "author");
            g.addEdge(a, b);
        }

        while ((elem = pers.getPaperTerm()) != null) {
            Node a = g.getNode(Integer.parseInt(elem[0]), "paper");
            Node b = g.getNode(Integer.parseInt(elem[1]), "term");
            g.addEdge(a, b);
        }

        while ((elem = pers.getPaperConf()) != null) {
            Node a = g.getNode(Integer.parseInt(elem[0]), "paper");
            Node b = g.getNode(Integer.parseInt(elem[1]), "conf");
            g.addEdge(a, b);
        }
    }

    public void saveGraph(String path) throws IOException, DomainException, PersistenceException {
        pers.startSaving(path);
        Set<Node> s = g.getSetOfNodes(Author.TYPE);
        for(Node n : s)
        {
            String[] line = {String.valueOf(n.getID()), n.getName()};
            System.out.println(n.getName());
            pers.addAuthor(line);
        }
        pers.addAuthor(null);
        System.out.println("Fet!");

        s = g.getSetOfNodes(Term.TYPE);
        for(Node n : s)
        {
            String[] line = {String.valueOf(n.getID()), n.getName()};
            pers.addTerm(line);
        }
        pers.addTerm(null);
        System.out.println("Fet!");

        s = g.getSetOfNodes(Conf.TYPE);
        for(Node n : s)
        {
            String[] line = {String.valueOf(n.getID()), n.getName()};
            pers.addConf(line);
        }
        pers.addConf(null);
        System.out.println("Fet!");

        s = g.getSetOfNodes(Paper.TYPE);
        for(Node n : s)
        {
            String[] line = {String.valueOf(n.getID()), n.getName()};
            pers.addPaper(line);
        }
        pers.addPaper(null);
        System.out.println("Fet!");

        for (Node n : s) {
            for (Node vei : g.getNeighbours(n, Author.TYPE)) {
                String[] line = {String.valueOf(n.getID()), String.valueOf(vei.getID())};
                pers.addPaperAuthor(line);
            }
        }
        pers.addPaperAuthor(null);
        System.out.println("Fet!");

        for (Node n : s) {
            for (Node vei : g.getNeighbours(n, Term.TYPE)) {
                String[] line = {String.valueOf(n.getID()), String.valueOf(vei.getID())};
                pers.addPaperTerm(line);
            }
        }
        pers.addPaperTerm(null);
        System.out.println("Fet!");

        for (Node n : s) {
            for (Node vei : g.getNeighbours(n, Conf.TYPE)) {
                String[] line = {String.valueOf(n.getID()), String.valueOf(vei.getID())};
                pers.addPaperConf(line);
            }
        }
        pers.addPaperConf(null);
        System.out.println("Fet!");

        pers.commit();

    }

}
