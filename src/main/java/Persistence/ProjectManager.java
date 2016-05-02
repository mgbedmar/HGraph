package Persistence;


import java.io.File;
import java.io.FilenameFilter;

public class ProjectManager {
    private final String PROJECTS_FOLDER_PATH = "/projects";
    private String selectedProject;

    public ProjectManager(){

    }

    public void selectProject(){

    }

    public String getProjectPath() throws PersistenceException {
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
