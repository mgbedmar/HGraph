package ParserTest;

import GraphTest.Graph.*;


public class Controller {
    private PersistenceLineByLine pers;
    public Graph g;

    public Controller() {
        g = new Graph();
        pers = new PersistenceLineByLine(null);
    }

    public void importGraph() throws DomainException {
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

}
