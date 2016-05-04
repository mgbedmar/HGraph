package Persistence;

import java.io.*;
import java.nio.file.Files;
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
    private String currentFile;


    /**
     * Fica l'excepcio a una <em>PersistenceException</em> i la llenca.
     * @param message misatge de l'excepcio
     * @throws PersistenceException sempre
     */
    private void handleException(String message) throws PersistenceException {
        throw new PersistenceException(message);
    }

    /**
     * Instancia el <em>BufferedReader</em> amb el fitxer <em>file</em>.
     * @param file fitxer per llegir
     * @throws PersistenceException si el fitxer <em>file</em> no existeix
     */
    private void initBufReader(String file) throws PersistenceException {
        try {
            FileReader fr = new FileReader(file);
            this.bufReader = new BufferedReader(fr);
        }
        catch (FileNotFoundException e) {
            handleException("Algun dels fitxers del graf no existeix o no es pot obrir.");
        }
    }

    /**
     * Instancia el <em>BufferedWriter</em> amb el fitxer <em>file</em>.
     * @param file fitxer per escriure
     * @throws IOException si hi ha error IO
     */
    private void initBufWriter(String file) throws IOException {
        if (bufWriter != null) {
            bufWriter.flush();
            bufWriter.close();
        }
        FileWriter fw = new FileWriter(file, false);
        bufWriter = new BufferedWriter(fw);
        currentFile = file;
    }

    /**
     * Dona una linia (dos strings) del fitxer que s'estigui llegint actualment
     * (i.e. el del reader).
     * @return la linia que toca del fitxer obert
     * @throws IOException si hi ha error IO
     */
    private String[] getLine() throws IOException, PersistenceException {
        String line = bufReader.readLine();
        if (line != null) {
            String[] split = line.split("\t");
            if (split.length != 2) {
                handleException("Fitxer mal format.");
            }
            return split;
        }
        else {
            this.bufReader.close();
            this.bufReader = null;
            return null;
        }
    }

    /**
     * Comprova si hi ha un buffer obert i dona la linia que toca.
     * @param file fitxer per obrir
     * @return linia que toca
     * @throws PersistenceException no existeix el fitxer
     * @throws IOException error IO
     */
    private String[] getElementFromFile(String file) throws PersistenceException, IOException {
        if (bufReader == null) {
            initBufReader(path+file+normExt);
        }
        return getLine();
    }

    /**
     * Afegeix una linia al fitxer obert (o per obrir).
     * @param file fitxer on s'escriu
     * @param line dos strings per escriure separats per un tabulador \t
     * @throws IOException error IO
     */
    private void addElementToFile(String file, String[] line) throws IOException {
        file = path+file+normExt;
        if (bufWriter == null || !currentFile.equals(file)) {
            initBufWriter(file);
        }
        bufWriter.write(line[0] + "\t" + line[1]);
        bufWriter.newLine();
    }

    /**
     * Copia el fitxer que te path <em>path</em>, nom <em>file</em> i extensio <em>extA</em> a un
     * altre fitxer amb mateixos path i nom i amb extensio <em>extB</em>
     * @param path path del sistema
     * @param file nom del fitxer
     * @param extA extensio del fitxer antic
     * @param extB extensio del nou fitxer
     * @throws IOException error IO
     */
    private void backup(String path, String file, String extA, String extB) throws IOException {
        File source = new File(path+file+extA);
        if (source.exists()) {
            File target = new File(path + file + extB);
            Files.copy(source.toPath(), target.toPath(), REPLACE_EXISTING);
        }
    }

    /**
     * Esborra el backup del fitxer amb path <em>path</em> i nom <em>file</em>.
     * @param path path del sistema
     * @param file nom del fitxer (sense extensio)
     * @throws IOException error IO
     */
    private void deleteBackup(String path, String file) throws PersistenceException {
        File a = new File(path+file+backupExt);
        if (!a.delete()) {
            throw new PersistenceException("No s'ha pogut esborrar el backup.");
        }
    }

    /**
     * Copia tots els fitxers del graf d'extensio <em>extA</em> a extensio <em>extB</em>.
     * @param path path del sistema
     * @param extA extensio del fitxer antic
     * @param extB extensio del fitxer nou
     * @throws IOException error IO
     */
    private void makeBackups(String path, String extA, String extB) throws IOException {
        backup(path, fileAuthor, extA, extB);
        backup(path, filePaper, extA, extB);
        backup(path, fileTerm, extA, extB);
        backup(path, fileConf, extA, extB);
        backup(path, filePaperAuthor, extA, extB);
        backup(path, filePaperTerm, extA, extB);
        backup(path, filePaperConf, extA, extB);
    }

    /**
     * Esborra tots els backups del graf.
     * @param path path del sistema
     * @throws IOException error IO
     */
    private void deleteBackups(String path) throws PersistenceException {
        deleteBackup(path, fileAuthor);
        deleteBackup(path, filePaper);
        deleteBackup(path, fileTerm);
        deleteBackup(path, fileConf);
        deleteBackup(path, filePaperAuthor);
        deleteBackup(path, filePaperTerm);
        deleteBackup(path, filePaperConf);
    }

    /**
     * Crea, si no existeix ja, un fitxer amb el path i nom indicat per <em>file</em>.
     * @param file Path i nom del fitxer per crear
     * @throws IOException error IO
     */
    private void createNonExistingFile(String file) throws IOException {
        File f = new File(file);
        f.createNewFile();
    }

    /**
     * Crea els fitxers del graf que faltin.
     * @throws IOException error IO
     */
    private void createNonExistingFiles() throws IOException {
        createNonExistingFile(path+fileAuthor+normExt);
        createNonExistingFile(path+filePaper+normExt);
        createNonExistingFile(path+fileTerm+normExt);
        createNonExistingFile(path+fileConf+normExt);
        createNonExistingFile(path+filePaperTerm+normExt);
        createNonExistingFile(path+filePaperConf+normExt);
        createNonExistingFile(path+filePaperAuthor+normExt);
    }

    /** Constructora. Crea un Manager nou.
     */
    public GraphFileManager() {
        bufReader = null;
        bufWriter = null;
    }

    /**
     * Inicialitza el sistema i crea els backups abans de comencar a guardar dades.
     * @param path path del graf que es guarda
     * @throws IOException error IO
     */
    public void startSaving(String path) throws IOException {
        this.path = path;
        bufReader = null;
        bufWriter = null;
        makeBackups(path, normExt, backupExt);
    }

    /**
     * Confirma els canvis guardats fins ara i esborra els backups.
     * @throws IOException error IO
     */
    public void commit() throws IOException, PersistenceException {
        if (bufWriter != null) {
            bufWriter.flush();
            bufWriter.close();
            bufWriter = null;
        }
        createNonExistingFiles();
        deleteBackups(path);
    }

    //TODO que no copii, sino que canvii el nom
    /**
     * Denega tots els canvis des del darrer <em>startSaving()</em> i deixa el graf en l'estat anterior.
     * @throws IOException error IO
     */
    public void rollback() throws IOException, PersistenceException {
        makeBackups(path, backupExt, normExt);
        deleteBackups(path);
    }

    /**
     * Inicialitza el sistema per comencar a carregar un graf.
     * @param path path del nou graf a carregar
     */
    public void startLoading(String path) {
        this.path = path;
        bufReader = null;
        bufWriter = null;
    }

    /* Retornen un array de Strings de dos posicions, una amb la id i l'altra amb el nom */

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getAuthor() throws PersistenceException, IOException {
        return getElementFromFile(fileAuthor);
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getConf() throws PersistenceException, IOException {
        return getElementFromFile(fileConf);
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getTerm() throws PersistenceException, IOException {
        return getElementFromFile(fileTerm);
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getPaper() throws PersistenceException, IOException {
        return getElementFromFile(filePaper);
    }


    /* Retornen un array de Strings de dos posicions, una amb la id del paper i l'altra de l'altre */

    /**
     * Dona l'aresta d'aquest tipus que toca. Una vegada que es comencen a llegir arestes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dades dels dos nodes (id del paper, id de l'altre)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getPaperAuthor() throws PersistenceException, IOException {
        return getElementFromFile(filePaperAuthor);
    }

    /**
     * Dona l'aresta d'aquest tipus que toca. Una vegada que es comencen a llegir arestes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dades dels dos nodes (id del paper, id de l'altre)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getPaperTerm() throws PersistenceException, IOException {
        return getElementFromFile(filePaperTerm);
    }

    /**
     * Dona l'aresta d'aquest tipus que toca. Una vegada que es comencen a llegir arestes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dades dels dos nodes (id del paper, id de l'altre)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public String[] getPaperConf() throws PersistenceException, IOException {
        return getElementFromFile(filePaperConf);
    }


    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param data (id, nom)
     * @throws IOException error IO
     */
    public void addAuthor(String[] data) throws IOException {
        addElementToFile(fileAuthor, data);
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param data (id, nom)
     * @throws IOException error IO
     */
    public void addPaper(String[] data) throws IOException {
        addElementToFile(filePaper, data);
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param data (id, nom)
     * @throws IOException error IO
     */
    public void addTerm(String[] data) throws IOException {
        addElementToFile(fileTerm, data);
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param data (id, nom)
     * @throws IOException error IO
     */
    public void addConf(String[] data) throws IOException {
        addElementToFile(fileConf, data);
    }

    /**
     * Guarda una nova aresta al graf. Una vegada es comencen a guardar arestes d'aquest tipus,
     * s'han de guardar totes.
     * @param data (id_paper, id_altre)
     * @throws IOException error IO
     */
    public void addPaperAuthor(String[] data) throws IOException {
        addElementToFile(filePaperAuthor, data);
    }

    /**
     * Guarda una nova aresta al graf. Una vegada es comencen a guardar arestes d'aquest tipus,
     * s'han de guardar totes.
     * @param data (id_paper, id_altre)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public void addPaperTerm(String[] data) throws IOException {
        addElementToFile(filePaperTerm, data);
    }

    /**
     * Guarda una nova aresta al graf. Una vegada es comencen a guardar arestes d'aquest tipus,
     * s'han de guardar totes.
     * @param data (id_paper, id_altre)
     * @throws PersistenceException si no existeix el fitxer
     * @throws IOException error IO
     */
    public void addPaperConf(String[] data) throws IOException {
        addElementToFile(filePaperConf, data);
    }
}

