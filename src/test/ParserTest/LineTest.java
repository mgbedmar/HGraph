package ParserTest;


import GraphTest.Graph.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LineTest {
    public static void main(String[] args) {
        Controller c = new Controller();
        try {
            c.importGraph();

            Node a = c.g.getNode(4, "paper");
            System.out.println(a.getName());
            System.out.print(c.g.getNeighbours(a).toString());

            c.g.removeNode(a);
            c.saveGraph(".");

            /*FileWriter f = new FileWriter("./author.txt", false);
            BufferedWriter buf = new BufferedWriter(f);
            String[] line = {"hola", "adeu"};
            buf.write(line[0] + "\t" + line[1]);
            buf.newLine();
            buf.close();*/

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
