package Persistence;


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

    public void startImport() {
    }
}
