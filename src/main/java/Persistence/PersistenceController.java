package Persistence;


import java.io.IOException;

public class PersistenceController {
    private GraphFileManager gfm;
    private ProjectManager pm;

    /**
     * Constructora.
     */
    public PersistenceController() {
        gfm = new GraphFileManager();
        pm = new ProjectManager();
    }

    /**
     * Dona la llista de projectes existents.
     * @return Llista de projectes.
     */
    public String[] getProjectList() {
        return pm.getProjectList();
    }

    /**
     * Selecciona un projecte.
     * @param pn nom del projecte
     * @throws PersistenceException si el projecte no existeix
     */
    public void selectProject(String pn) throws PersistenceException {
        pm.selectProject(pn);
    }

    /**
     * Inicialitza el sistema i crea els backups abans de comencar a guardar dades.
     * @throws PersistenceException si no s'aconsegueix crear backups
     */
    public void startSaving() throws PersistenceException {
        try {
            gfm.startSaving(pm.getProjectPath());
        } catch (IOException e) {//TODO stacktrace??
            e.printStackTrace(System.err);
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Inicialitza el sistema per comencar a carregar un graf.
     * @throws PersistenceException si no hi ha cap projecte seleccionat
     */
    public void startLoading() throws PersistenceException {
        try {
            gfm.startLoading(pm.getProjectPath());
        } catch (PersistenceException e) {
            throw new PersistenceException("No hi ha cap projecte seleccionat.");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getAuthor() throws PersistenceException {
        try {
            return gfm.getAuthor();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getPaper() throws PersistenceException {
        try {
            return gfm.getPaper();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getTerm() throws PersistenceException {
        try {
            return gfm.getTerm();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getConf() throws PersistenceException {
        try {
            return gfm.getConf();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getPaperAuthor() throws PersistenceException {
        try {
            return gfm.getPaperAuthor();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getPaperTerm() throws PersistenceException {
        try {
            return gfm.getPaperTerm();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Dona el node del tipus que toca. Una vegada que es comencen a llegir nodes d'aquest tipus,
     * cal continuar fins que s'acabin. Quan s'acaben retorna <em>null</em>.
     * @return les dues dades del node (id en format String, nom)
     * @throws PersistenceException si no existeix el fitxer o falla la lectura
     */
    public String[] getPaperConf() throws PersistenceException {
        try {
            return gfm.getPaperConf();
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la lectura. (Permisos?)");
        }
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param id id del node
     * @param name nom del node
     * @throws PersistenceException si no existeix el fitxer o falla l'escriptura
     */
    public void addAuthor(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addAuthor(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param id id del node
     * @param name nom del node
     * @throws PersistenceException si no existeix el fitxer o falla l'escriptura
     */
    public void addPaper(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addPaper(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param id id del node
     * @param name nom del node
     * @throws PersistenceException si no existeix el fitxer o falla l'escriptura
     */
    public void addConf(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addConf(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Guarda un nou node al graf. Una vegada es comencen a guardar nodes d'aquest tipus,
     * s'han de guardar tots.
     * @param id id del node
     * @param name nom del node
     * @throws PersistenceException si no existeix el fitxer o falla l'escriptura
     */
    public void addTerm(Integer id, String name) throws PersistenceException {
        String[] s = {id.toString(), name};
        try {
            gfm.addTerm(s);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat la escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Guarda una nova aresta al graf. Una vegada es comencen a guardar arestes d'aquest tipus,
     * s'han de guardar totes.
     * @param idPaper id del paper
     * @param idAnother id de l'altre
     * @throws PersistenceException si no es pot escriure
     */
    public void addPaperAuthor(Integer idPaper, Integer idAnother) throws PersistenceException {
        String[] data = {idPaper.toString(), idAnother.toString()};
        try {
            gfm.addPaperAuthor(data);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat l'escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Guarda una nova aresta al graf. Una vegada es comencen a guardar arestes d'aquest tipus,
     * s'han de guardar totes.
     * @param idPaper id del paper
     * @param idAnother id de l'altre
     * @throws PersistenceException si no es pot escriure
     */
    public void addPaperTerm(Integer idPaper, Integer idAnother) throws PersistenceException {
        String[] data = {idPaper.toString(), idAnother.toString()};
        try {
            gfm.addPaperTerm(data);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat l'escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Guarda una nova aresta al graf. Una vegada es comencen a guardar arestes d'aquest tipus,
     * s'han de guardar totes.
     * @param idPaper id del paper
     * @param idAnother id de l'altre
     * @throws PersistenceException si no es pot escriure
     */
    public void addPaperConf(Integer idPaper, Integer idAnother) throws PersistenceException {
        String[] data = {idPaper.toString(), idAnother.toString()};
        try {
            gfm.addPaperConf(data);
        } catch (IOException e) {
            throw new PersistenceException("Ha fallat l'escriptura. (Permisos? Espai al disc?)");
        }
    }

    /**
     * Confirma els canvis guardats fins ara i esborra els backups.
     * @throws PersistenceException
     */
    public void commit() throws PersistenceException {
        try {
            gfm.commit();
        } catch (PersistenceException pe) {
            //No passa res
        } catch (IOException ioe) {
            throw new PersistenceException("No s'ha pogut guardar el projecte.");
        }
    }

    /**
     * Comprova si hi ha algun projecte seleccionat.
     * @return <em>true</em> si i nomes si hi ha un projecte seleccionat
     */
    public boolean isProjectSelected() {
        return pm.isProjectSelected();
    }

    /**
     * Crea un nou projecte.
     * @param name nom del nou projecte
     * @throws PersistenceException si no aconsegueix crear el projecte o si ja existeix
     */
    public void createProject(String name) throws PersistenceException {
        pm.createProject(name);
    }

    /**
     * Esborra un projecte.
     * @param name projecte a esborrar
     * @throws PersistenceException si no existeix el projecte o no s'aconsegueix esborrar
     */
    public void deleteProject(String name) throws PersistenceException {
        pm.deleteProject(name);
    }

    /**
     * Deixa el sistema en l'estat anterior
     * @throws PersistenceException error IO
     */
    public void rollback() throws PersistenceException {
        try {
            gfm.rollback();
        } catch (IOException e) {
            throw new PersistenceException("No s'ha pogut fer rollback");
        }
    }
}
