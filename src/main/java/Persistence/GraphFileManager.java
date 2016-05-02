package Persistence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphFileManager {
    private String graphPath;
    private BufferedReader buf;

    private void initBuf(String file) throws FileNotFoundException {
        FileReader fr = new FileReader(file);
        this.buf = new BufferedReader(fr);
    }

    private String[] getLine() throws IOException {
        String line = buf.readLine();
        if (line != null) {
            String[] res = line.split("\t");
            return res;
        }
        else {
            this.buf = null;
            return null;
        }
    }

    private String[] getElementFromFile(String file) {
        try {
            if (buf == null) {
                initBuf(file);
            }
            return getLine();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Constructora. Crea un Manager nou que gestiona un graf al
     * path donat. Ha de ser un path correcte i acabat en / o \.
     * @param path path del directori on es guarda el graf
     */
    public GraphFileManager(String path) {
        this.graphPath = path;
        buf = null;
    }

    /* Retornen un array de Strings de dos posicions, una amb la id i l'altra amb el nom */

    public String[] getAuthor() {
        return getElementFromFile("author.txt");
    }

    public String[] getConf() {
        return getElementFromFile("conf.txt");
    }

    public String[] getTerm() {
        return getElementFromFile("term.txt");
    }

    public String[] getPaper() {
        return getElementFromFile("paper.txt");
    }


    /* Retornen un array de Strings de dos posicions, una amb la id del paper i l'altra de l'altre */

    public String[] getPaperAuthor() {
        return getElementFromFile("paper_author.txt");
    }

    public String[] getPaperTerm() {
        return getElementFromFile("paper_term.txt");
    }

    public String[] getPaperConf() {
        return getElementFromFile("paper_conf.txt");
    }
}

