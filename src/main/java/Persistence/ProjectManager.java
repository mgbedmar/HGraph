package Persistence;


import java.io.File;
import java.io.FilenameFilter;
import java.net.URISyntaxException;

public class ProjectManager {
    private final String JAR_PATH = getJAR_PATH();
    private final String PROJECTS_FOLDER_PATH = "projects";
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
