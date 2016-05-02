package Persistence;

import java.io.*;
import java.util.*;

public class GraphFileManager {
    private String graphPath;
    private String fileAuthor;
    private String filePaper;
    private String fileTerm;
    private String fileConf;
    private String filePaperAuthor;
    private String filePaperTerm;
    private String filePaperConf;
    private BufferedReader bufReader;
    private BufferedWriter bufWriter;
    private String currentOutputFile;


    private void handleException(String message) throws PersistenceException {
        throw new PersistenceException(message);
    }

    private void initBufReader(String file) throws PersistenceException {
        try {
            FileReader fr = new FileReader(file);
            this.bufReader = new BufferedReader(fr);
        }
        catch (FileNotFoundException e) {
            handleException("Algun dels fitxers del graf no existeix o no es pot obrir.");
        }
    }

    private void initBufWriter(String file) throws IOException {
        if (bufWriter != null) {
            bufWriter.flush();
            bufWriter.close();
        }
        FileWriter fw = new FileWriter(file, false);
        bufWriter = new BufferedWriter(fw);
        currentOutputFile = file;
    }

    private String[] getLine() throws IOException {
        String line = bufReader.readLine();
        if (line != null) {
            String[] res = line.split("\t");
            return res;
        }
        else {
            this.bufReader.close();
            this.bufReader = null;
            return null;
        }
    }

    private String[] getElementFromFile(String file) throws PersistenceException, IOException {
        if (bufReader == null) {
            initBufReader(file);
        }
        return getLine();
    }

    private void setElementInFile(String file, String[] line) throws PersistenceException, IOException {
        if (bufWriter == null || currentOutputFile != file) {
            initBufWriter(file);
        }
        bufWriter.write(line[0]+"\t"+line[1]);
    }

    /** Constructora. Crea un Manager nou que gestiona un graf al
     * path donat. Ha de ser un path correcte i acabat en / o \.
     * @param path path del directori on es guarda el graf
     */
    public GraphFileManager(String path) {
        bufReader = null;
        bufWriter = null;

        graphPath = path;
        fileAuthor = path + "author.txt";
        filePaper = path + "paper.txt";
        fileTerm = path + "term.txt";
        fileConf = path + "conf.txt";
        filePaperAuthor = path + "paper_author.txt";
        filePaperTerm = path + "paper_term.txt";
        filePaperConf = path + "paper_conf.txt";
    }

    public void startTransaction() {

    }

    /* Retornen un array de Strings de dos posicions, una amb la id i l'altra amb el nom */

    public String[] getAuthor() throws PersistenceException, IOException {
        return getElementFromFile(fileAuthor);
    }

    public String[] getConf() throws PersistenceException, IOException {
        return getElementFromFile(fileConf);
    }

    public String[] getTerm() throws PersistenceException, IOException {
        return getElementFromFile(fileTerm);
    }

    public String[] getPaper() throws PersistenceException, IOException {
        return getElementFromFile(filePaper);
    }


    /* Retornen un array de Strings de dos posicions, una amb la id del paper i l'altra de l'altre */

    public String[] getPaperAuthor() throws PersistenceException, IOException {
        return getElementFromFile(filePaperAuthor);
    }

    public String[] getPaperTerm() throws PersistenceException, IOException {
        return getElementFromFile(filePaperTerm);
    }

    public String[] getPaperConf() throws PersistenceException, IOException {
        return getElementFromFile(filePaperConf);
    }



    public void addAuthor(String[] data) throws PersistenceException, IOException {
        setElementInFile(fileAuthor, data);
    }

    public void addPaper(String[] data) throws PersistenceException, IOException {
        setElementInFile(filePaper, data);
    }

    public void addTerm(String[] data) throws PersistenceException, IOException {
        setElementInFile(fileTerm, data);
    }

    public void addConf(String[] data) throws PersistenceException, IOException {
        setElementInFile(fileConf, data);
    }

    public void addPaperAuthor(String[] data) throws PersistenceException, IOException {
        setElementInFile(filePaperAuthor, data);
    }

    public void addPaperTerm(String[] data) throws PersistenceException, IOException {
        setElementInFile(filePaperTerm, data);
    }

    public void addPaperConf(String[] data) throws PersistenceException, IOException {
        setElementInFile(filePaperConf, data);
    }
}

