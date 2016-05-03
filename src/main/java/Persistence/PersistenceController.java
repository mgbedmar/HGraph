package Persistence;


import java.io.IOException;

public class PersistenceController {
    private GraphFileManager gfm;
    private ProjectManager pm;

    public PersistenceController(){
        gfm = new GraphFileManager();
        pm = new ProjectManager();
    }

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
            e.printStackTrace(System.err);
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    public void startLoad() throws PersistenceException {
        try {
            gfm.startLoading(pm.getProjectPath());
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getAuthor() throws PersistenceException {
        try {
            return gfm.getAuthor();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getPaper() throws PersistenceException {
        try {
            return gfm.getPaper();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getTerm() throws PersistenceException {
        try {
            return gfm.getTerm();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getConf() throws PersistenceException {
        try {
            return gfm.getConf();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getPaperAuthor() throws PersistenceException {
        try {
            return gfm.getPaperAuthor();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getPaperTerm() throws PersistenceException {
        try {
            return gfm.getPaperTerm();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public String[] getPaperConf() throws PersistenceException {
        try {
            return gfm.getPaperConf();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    public void addAuthor(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addAuthor(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    public void addPaper(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addPaper(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    public void addConf(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addConf(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    public void addTerm(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addAuthor(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    public void commit() {
        try {
            gfm.commit();
        } catch (IOException e) {
            //No passa res
        }
    }

    public boolean isProjectSelected() {
        return pm.isProjectSelected();
    }

    public void createProject(String name) throws PersistenceException {
        pm.createProject(name);
    }
}
