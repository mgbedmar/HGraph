package Persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import static java.nio.file.StandardCopyOption.*;

public class GraphFileManager {
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

    private void addElementInFile(String file, String[] line) throws PersistenceException, IOException {
        if (bufWriter == null || currentOutputFile != file) {
            initBufWriter(file);
        }
        if (line != null) {
            bufWriter.write(line[0] + "\t" + line[1]);
        }
    }

    private void backup(String path, String file) throws IOException {
        File source = new File(path+file+normExt);
        File target = new File(path+file+backupExt);
        target.createNewFile();
        Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
    }

    private void deleteBackup(String path, String file) throws IOException {
        File a = new File(path+file+backupExt);
        a.delete();
    }

    /** Constructora. Crea un Manager nou que gestiona un graf al
     * path donat. Ha de ser un path correcte i acabat en / o \.
     * @param path path del directori on es guarda el graf
     */
    public GraphFileManager(String path) {
        bufReader = null;
        bufWriter = null;
    }

    public void startSaving(String path) throws IOException {
        backup(path, fileAuthor);
        backup(path, filePaper);
        backup(path, fileTerm);
        backup(path, fileConf);
        backup(path, filePaperAuthor);
        backup(path, filePaperTerm);
        backup(path, filePaperConf);
    }

    public void commit(String path) throws IOException {
        deleteBackup(path, fileAuthor);
        deleteBackup(path, filePaper);
        deleteBackup(path, fileTerm);
        deleteBackup(path, fileConf);
        deleteBackup(path, filePaperAuthor);
        deleteBackup(path, filePaperTerm);
        deleteBackup(path, filePaperConf);
    }

    public void startLoading(String path) throws IOException {
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
        addElementInFile(fileAuthor, data);
    }

    public void addPaper(String[] data) throws PersistenceException, IOException {
        addElementInFile(filePaper, data);
    }

    public void addTerm(String[] data) throws PersistenceException, IOException {
        addElementInFile(fileTerm, data);
    }

    public void addConf(String[] data) throws PersistenceException, IOException {
        addElementInFile(fileConf, data);
    }

    public void addPaperAuthor(String[] data) throws PersistenceException, IOException {
        addElementInFile(filePaperAuthor, data);
    }

    public void addPaperTerm(String[] data) throws PersistenceException, IOException {
        addElementInFile(filePaperTerm, data);
    }

    public void addPaperConf(String[] data) throws PersistenceException, IOException {
        addElementInFile(filePaperConf, data);
    }
}

