package Persistence;


import java.io.*;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Gerard
 */

public class ProjectManager {
    /**
     * Path del jar que s'esta executant.
     */
    private final String JAR_PATH = getJAR_PATH();

    /**
     * Nom de la carpeta de projectes.
     */
    private final String PROJECTS_FOLDER_PATH = "projects";

    /**
     * Projecte actual.
     */
    private String selectedProject;

    /**
     * Retorna el path on esta el jar de l'aplicacio
     * @return un string amb el path del jar
     */
    private String getJAR_PATH() {
        try {
            File f = new
                    File(ProjectManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            return f.getParent();
        }catch (URISyntaxException ue) {
            return "";
        }
    }

    /**
     * Extreu el contingut d'un zip a una carpeta especificada.
     * @param zipFile path absolut del zip
     * @param extractFolder path absolut de la carpeta desti
     * @throws IOException error IO
     */
    private void extractFolder(String zipFile, String extractFolder) throws IOException
    {
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = extractFolder;

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        while (zipFileEntries.hasMoreElements())
        {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            String [] spl = currentEntry.split("/");
            currentEntry = spl[spl.length-1];

            if (!entry.isDirectory())
            {

                File destFile = new File(newPath, currentEntry);
                File destinationParent = destFile.getParentFile();

                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                int currentByte;
                byte data[] = new byte[BUFFER];

                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                        BUFFER);

                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }


        }

    }

    /**
     * Comprova si un fitxer ja existeix.
     * @param file path absolut del fitxer
     * @return true si el fitxer ja existeix
     * @throws IOException error IO
     */
    private boolean checkFile(String file) throws IOException {
        File f = new File(file);
        return !f.createNewFile();
    }

    /**
     * Comprova si esta ben format el projecte.
     * @path path del projecte
     * @throws IOException error IO
     */
    private boolean checkFolder(String path) throws IOException {
        boolean creat = true;
        String normExt = ".txt";
        creat = creat && checkFile(path+GraphFileManager.fileAuthor+normExt);
        creat = creat && checkFile(path+GraphFileManager.filePaper+normExt);
        creat = creat && checkFile(path+GraphFileManager.fileTerm+normExt);
        creat = creat && checkFile(path+GraphFileManager.fileConf+normExt);
        creat = creat && checkFile(path+GraphFileManager.filePaperTerm+normExt);
        creat = creat && checkFile(path+GraphFileManager.filePaperConf+normExt);
        creat = creat && checkFile(path+GraphFileManager.filePaperAuthor+normExt);
        return creat;
    }

    /**
     * Constructora. Crea un nou manager.
     */
    public ProjectManager() {
        selectedProject = null;
        File dir = new File(JAR_PATH+"/"+PROJECTS_FOLDER_PATH);
        dir.mkdir();
    }

    /**
     * Crea un nou projecte.
     * @param projectName nom del nou projecte
     * @throws PersistenceException si no aconsegueix crear el projecte o si ja existeix
     */
    public void createProject(String projectName) throws PersistenceException
    {
        if(!projectExists(projectName))
        {
            File dir = new File(JAR_PATH+"/"+PROJECTS_FOLDER_PATH+"/"+projectName);
            if (!dir.mkdirs()) throw new PersistenceException("No s'ha pogut crear el projecte.");
        }
        else
            throw new PersistenceException("El projecte ja existeix");
    }

    /**
     * Esborra un projecte.
     * @param projectName projecte a esborrar
     * @throws PersistenceException si no existeix el projecte o no s'aconsegueix esborrar
     */
    public void deleteProject(String projectName) throws PersistenceException
    {
        if(projectExists(projectName))
        {
            File dir = new File(JAR_PATH+"/"+PROJECTS_FOLDER_PATH+"/"+projectName);
            String[] graphFiles = dir.list();
            for(String s: graphFiles){
                File currentFile = new File(dir.getPath(), s);
                currentFile.delete();
            }
            if (!dir.delete())
                throw new PersistenceException("No s'ha pogut esborrar el projecte.");
        }
        else
            throw new PersistenceException("El projecte no existeix");
    }

    public void importProject(String zipFile, String projectName) throws PersistenceException {
        if (projectExists(projectName)) {
            throw new PersistenceException("El projecte ja existeix.");
        }
        else {
            try {
                extractFolder(zipFile, JAR_PATH+"/"+PROJECTS_FOLDER_PATH+"/"+projectName);
                if (!checkFolder(JAR_PATH+"/"+PROJECTS_FOLDER_PATH+"/"+projectName)) {
                    deleteProject(projectName);
                    throw new PersistenceException("El zip no està ben format");
                }
            } catch (IOException e) {
                throw new PersistenceException("No s'ha pogut importar el projecte.");
            }
        }
    }

    /**
     * Selecciona un projecte per treballar
     * @param projectName nom del projecte
     * @throws PersistenceException si el projecte no existeix
     */
    public void selectProject(String projectName) throws PersistenceException
    {
        if(projectExists(projectName))
            selectedProject = JAR_PATH+"/"+PROJECTS_FOLDER_PATH+"/"+projectName;
        else
            throw new PersistenceException("El projecte no existeix");
    }

    /**
     * Deixa de tenir seleccionat un projecte
     */
    public void unSelectProject()
    {
        selectedProject = null;
    }


    /**
     * Comprova si existeix un projecte amb el nom donat
     * @param projectName nom del projecte a comprovar
     * @return <em>true</em> si i nomes si el projecte existeix
     */
    public boolean projectExists(String projectName)
    {
        String[] pl = getProjectList();
        if(pl != null)
        {
            for(String p : pl)
            {
                if(p.equals(projectName))
                    return true;
            }
        }

        return false;
    }

    /**
     * Dona el path del projecte seleccionat
     * @return el path del projecte seleccionat
     * @throws PersistenceException si no hi ha cap projecte seleccionat
     */
    public String getProjectPath() throws PersistenceException
    {
        if(selectedProject != null)
            return selectedProject;
        else
            throw new PersistenceException("El projecte no ha estat seleccionat");
    }

    /**
     * Dona la llista de projectes existents
     * @return un array de strings amb els noms dels projectes existents
     */
    public String[] getProjectList()
    {
        File file = new File(JAR_PATH+"/"+PROJECTS_FOLDER_PATH);
        String[] directories = file.list(new FilenameFilter()
        {
            @Override
            public boolean accept(File current, String name)
            {
                return new File(current, name).isDirectory();
            }
        });

        return directories;
    }

    /**
     * Comprova si hi ha algun projecte seleccionat.
     * @return <em>true</em> si i nomes si hi ha un projecte seleccionat
     */
    public boolean isProjectSelected() {
        return (selectedProject != null);
    }
}
