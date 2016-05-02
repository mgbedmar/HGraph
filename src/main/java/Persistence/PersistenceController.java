package Persistence;


import java.io.IOException;

public class PersistenceController {
    private GraphFileManager gfm;
    private ProjectManager pm;

    public String[] getProjectList() {
        return pm.getProjectList();
    }

    public void selectProject(String pn) throws PersistenceException {
        pm.selectProject(pn);
    }

    public void save() {

    }

    public void startLoad() {
        gfm.startLoad();
    }

    public String[] getAuthor() throws PersistenceException {
        try {
            return gfm.getAuthor();
        } catch (IOException e) {
        }
    }
}
