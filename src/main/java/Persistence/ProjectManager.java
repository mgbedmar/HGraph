package Persistence;


import java.io.File;
import java.io.FilenameFilter;

public class ProjectManager {
    private final String PROJECTS_FOLDER_PATH = "/projects";
    private String selectedProject;

    public ProjectManager(){

    }

    public void createProject(String projectName) throws PersistenceException
    {
        if(!projectExists(projectName))
        {
            File dir = new File(PROJECTS_FOLDER_PATH+"/"+projectName);
            dir.mkdir();
        }
        else
            throw new PersistenceException("El projecte ja existeix");
    }

    public void deleteProject(String projectName) throws PersistenceException
    {
        if(projectExists(projectName))
        {
            File dir = new File(PROJECTS_FOLDER_PATH+"/"+projectName);
            dir.delete();
        }
        else
            throw new PersistenceException("El projecte no existeix");
    }

    public void selectProject(String projectName) throws PersistenceException
    {
        if(projectExists(projectName))
            selectedProject = PROJECTS_FOLDER_PATH+"/"+projectName;
        else
            throw new PersistenceException("El projecte no existeix");
    }

    public boolean projectExists(String projectName)
    {
        String[] pl = getProjectList();
        for(String p : pl)
        {
            if(p.equals(projectName))
                return true;
        }

        return false;
    }

    public String getProjectPath() throws PersistenceException
    {
        if(selectedProject != null)
            return selectedProject;
        else
            throw new PersistenceException("El projecte no ha estat seleccionat");
    }

    public String[] getProjectList()
    {
        File file = new File(PROJECTS_FOLDER_PATH);
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
}
