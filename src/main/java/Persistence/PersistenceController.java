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

    public void startSaving() throws PersistenceException {
        try {
            gfm.startSaving(pm.getProjectPath());
        } catch (IOException e) {
            //TODO
        }
    }

    public void startLoad() throws PersistenceException {
        try {
            gfm.startLoading(pm.getProjectPath());
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos? Espai al disc?)");
        }
    }

    public String[] getAuthor() throws PersistenceException {
        try {
            return gfm.getAuthor();
        } catch (IOException e) {
            //TODO
        }
        return null;
    }

    public void addAuthor(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addAuthor(s);
        } catch (IOException e) {
            //TODO
        }
    }
}
