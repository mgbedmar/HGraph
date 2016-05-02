package ParserTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import static java.nio.file.StandardCopyOption.*;


public class GraphFileManager {
    private String path;
    private static String fileAuthor = "/author";
    private static String filePaper = "/paper";
    private static String fileTerm = "/term";
    private static String fileConf = "/conf";
    private static String filePaperAuthor = "/paper_author";
    private static String filePaperTerm = "/paper_term";
    private static String filePaperConf = "/paper_conf";
    private static String normExt = ".txt";
    private static String backupExt = ".bak";
    private BufferedReader bufReader;
    private BufferedWriter bufWriter;


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
        FileWriter fw = new FileWriter(file, false);
        bufWriter = new BufferedWriter(fw);
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
            initBufReader(path+file+normExt);
        }
        return getLine();
    }

    private void addElementToFile(String file, String[] line) throws PersistenceException, IOException {
        if (bufWriter == null) {
            initBufWriter(path+file+normExt);
        }
        if (line != null) {
            bufWriter.write(line[0] + "\t" + line[1]);
            bufWriter.newLine();
        }
        else {
            bufWriter.flush();
            bufWriter.close();
            bufWriter = null;
        }
    }

    private void backup(String path, String file, String extA, String extB) throws IOException {
        File source = new File(path+file+extA);
        File target = new File(path+file+extB);
        Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
    }

    private void deleteBackup(String path, String file) throws IOException {
        File a = new File(path+file+backupExt);
        a.delete();
    }

    private void makeBackups(String path, String extA, String extB) throws IOException {
        backup(path, fileAuthor, extA, extB);
        backup(path, filePaper, extA, extB);
        backup(path, fileTerm, extA, extB);
        backup(path, fileConf, extA, extB);
        backup(path, filePaperAuthor, extA, extB);
        backup(path, filePaperTerm, extA, extB);
        backup(path, filePaperConf, extA, extB);
    }

    private void deleteBackups(String path) throws IOException {
        deleteBackup(path, fileAuthor);
        deleteBackup(path, filePaper);
        deleteBackup(path, fileTerm);
        deleteBackup(path, fileConf);
        deleteBackup(path, filePaperAuthor);
        deleteBackup(path, filePaperTerm);
        deleteBackup(path, filePaperConf);
    }

    /** Constructora. Crea un Manager nou.
     */
    public GraphFileManager() {
        bufReader = null;
        bufWriter = null;
    }

    public void startSaving(String path) throws IOException {
        this.path = path;
        bufReader = null;
        bufWriter = null;
        makeBackups(path, normExt, backupExt);
    }

    public void commit() throws IOException {
        deleteBackups(path);
    }

    public void rollback() throws IOException {
        makeBackups(path, backupExt, normExt);
        deleteBackups(path);
    }

    public void startLoading(String path) throws IOException {
        this.path = path;
        bufReader = null;
        bufWriter = null;
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
        addElementToFile(fileAuthor, data);
    }

    public void addPaper(String[] data) throws PersistenceException, IOException {
        addElementToFile(filePaper, data);
    }

    public void addTerm(String[] data) throws PersistenceException, IOException {
        addElementToFile(fileTerm, data);
    }

    public void addConf(String[] data) throws PersistenceException, IOException {
        addElementToFile(fileConf, data);
    }

    public void addPaperAuthor(String[] data) throws PersistenceException, IOException {
        addElementToFile(filePaperAuthor, data);
    }

    public void addPaperTerm(String[] data) throws PersistenceException, IOException {
        addElementToFile(filePaperTerm, data);
    }

    public void addPaperConf(String[] data) throws PersistenceException, IOException {
        addElementToFile(filePaperConf, data);
    }
}

