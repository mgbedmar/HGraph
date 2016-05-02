package ParserTest;


import GraphTest.Graph.*;

public class LineTest {
    public static void main(String[] args) {
        Controller c = new Controller();
        try {
            c.importGraph();

            Node a = c.g.getNode(654269, "paper");
            System.out.println(a.getName());
            System.out.print(c.g.getNeighbours(a).toString());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Fet!");
    }
}
