package Domain;


import Domain.Graph.*;
import Persistence.PersistenceController;
import Persistence.PersistenceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author Gerard
 */
public class DomainController
{
    private Graph g;
    private Result r;
    private PersistenceController pc;

    /**
     * @param name Nom del node
     * @param type Tipus del node
     * @return Un node de tipus <em>type</em> amb nom <em>nom</em>
     * @throws DomainException si es produeix a les capes inferiors
     */
    private Node createNode(String name, String type) throws DomainException
    {

        switch(type)
        {
            case Author.TYPE:
                return new Author(name);
            case Term.TYPE:
                return new Term(name);
            case Paper.TYPE:
                return new Paper(name);
            case Conf.TYPE:
                return new Conf(name);
        }

        throw new DomainException("El tipus '"+type+"' no existeix.");
    }

    /**
     * Constructora
     */
    public DomainController()
    {
        pc = new PersistenceController();
        g = new Graph();
    }

    /**
     * Consulta les ids dels nodes amb un nom i tipus.
     * @param name Nom del node
     * @param type Tipus del node
     * @return Una llista de les ids dels nodes que tenen nom <em>name</em>
     * i tipus <em>type</em>. Si no n'hi ha cap, retorna una llista buida.
     * @throws DomainException si es produeix a les capes inferiors
     */
    public ArrayList<Integer> getNodes(String name, String type) throws DomainException
    {
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Node> nodes = g.getNodes(name, type);
        for (int i = 0; i < nodes.size(); i++)
        {
            ids.add(nodes.get(i).getID());
        }

        return ids;
    }

    /**
     * Afegeix un node
     * @param name Nom del node
     * @param type Tipus del node
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void addNode(String name, String type) throws DomainException
    {
        Node node = createNode(name, type);
        g.addNode(node);
    }

    /**
     * Esborra un node
     * @param id id del node
     * @param type Tipus del node
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void removeNode(int id, String type) throws DomainException
    {
        Node node = g.getNode(id, type);
        g.removeNode(node);
    }

    /**
     * Canvia el nom d'un node
     * @param id id del node
     * @param type Tipus del node
     * @param newName Nou nom per al node
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void modifyNode(int id, String type, String newName) throws DomainException
    {
        Node node = g.getNode(id, type);
        g.setNodeName(node, newName);
    }

    /**
     * Crea una aresta formada pels nodes <em>A</em> i <em>B</em>
     * @param idA id del node A
     * @param typeA tipus del node A
     * @param idB id del node B
     * @param typeB tipus del node B
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void addEdge(int idA, String typeA,
                        int idB, String typeB) throws DomainException
    {
        Node a = g.getNode(idA, typeA);
        Node b = g.getNode(idB, typeB);
        g.addEdge(a,b);
    }

    /**
     * Esborra l'aresta formada pels nodes <em>A</em> i <em>B</em>
     * @param idA id del node A
     * @param typeA tipus del node A
     * @param idB id del node B
     * @param typeB tipus de node B
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void removeEdge(int idA, String typeA,
                           int idB, String typeB) throws DomainException
    {
        Node a = g.getNode(idA, typeA);
        Node b = g.getNode(idB, typeB);
        g.removeEdge(a,b);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta de tipus
     * @param type nom del tipus a consultar
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void queryByType(String type) throws DomainException
    {
        r = Query.queryByType(g, type);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta 1 a 1
     * @param idSource Nom del node font
     * @param typeSource Tipus del node font
     * @param idEnd Nom del node desti
     * @param typeEnd Tipus del node desti
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void query1to1(int idSource, String typeSource,
                          int idEnd, String typeEnd) throws DomainException
    {


        Node source = g.getNode(idSource, typeSource);
        Node end = g.getNode(idEnd, typeEnd);

        r = Query.query1to1(g, source, end, Query.defaultPath.get(typeSource).get(typeEnd));

    }

    /**
     * Coloca a <em>r</em> el resultat de consultar els veins del node
     * @param id Nom del node font
     * @param type Tipus del node font
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void queryNeighbours(int id, String type) throws DomainException
    {
        Node source = g.getNode(id, type);
        r = Query.queryNeighbours(g, source);
    }

    /**
     * Coloca a <em>r</em> el resultat de consultar els veins del node
     * @param idSource Nom del node font
     * @param typeSource Tipus del node font
     * @param typeEnd Tipus dels nodes a consultar
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void queryNeighbours(int idSource, String typeSource, String typeEnd) throws DomainException
    {
        Node source = g.getNode(idSource, typeSource);
        r = Query.queryNeighbours(g, source, typeEnd);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta 1 a N
     * @param idSource Nom del node font
     * @param typeSource Tipus del node font
     * @param typeEnd Tipus desti
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void query1toN(int idSource, String typeSource, String typeEnd) throws DomainException
    {
        Node source = g.getNode(idSource, typeSource);
        r = Query.query1toN(g, source, Query.defaultPath.get(typeSource).get(typeEnd));
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta N a N
     * @param typeSource Tipus font
     * @param typeEnd Tipus desti
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void queryNtoN(String typeSource, String typeEnd) throws DomainException
    {
        r = Query.queryNtoN(g, Query.defaultPath.get(typeSource).get(typeEnd));
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta per referència
     * @param nodeRefSourceID id del node referencia font
     * @param nodeRefSourceType Tipus del node referencia font
     * @param nodeRefEndID id del node referencia desti
     * @param nodeRefEndType Tipus del node referencia desti
     * @param nodeSourceID id del node font
     * @param nodeSourceType Tipus del node font
     * @throws DomainException si es produeix a les capes inferiors
     */
    public void queryByReference(int nodeRefSourceID, String nodeRefSourceType,
                                 int nodeRefEndID, String nodeRefEndType,
                                 int nodeSourceID, String nodeSourceType) throws DomainException
    {
        if (!nodeRefSourceType.equals(nodeSourceType)) throw new
                DomainException("El primer i tercer node han de ser del mateix tipus, i no ho són. El primer "+
                "és de tipus '"+nodeRefSourceType+"' i el tercer és de tipus '"+nodeSourceType+"'.");

        Node refSource = g.getNode(nodeRefSourceID, nodeRefSourceType);
        Node refEnd = g.getNode(nodeRefEndID, nodeRefEndType);
        Node source = g.getNode(nodeSourceID, nodeSourceType);

        r = Query.queryByReference(g, refSource, refEnd, source,
                Query.defaultPath.get(nodeRefSourceType).get(nodeRefEndType), Query.TOL);
    }

    /**
     * Retorna una estructura amb tots els filtres.
     * @return Un map amb claus "filteredNames", "filteredLines" i
     * "selectedNames" associades cadascuna a un ArrayList
     */
    public Map<String,ArrayList<String>> getFilters()
    {
        return r.getFilters();
    }

    /**
     * Consultora de la mida del resultat.
     * @return el nombre total de files del resultat
     */
    public Integer getResultSize() {
        return r.size();
    }

    /**
     * Retorna la fila que toca des de la darrera
     * vegada que es va ordenar, filtrar o fer reset.
     * @return El primer element del ArrayList es un
     * enter unic per identificar la fila, els seguents
     * son n dades (el nom del node si es un node, el float
     * en String si es el HS). Retorna <em>null</em> si no queden files.
     */
    public ArrayList<String> getResultRow()
    {
        return r.getRow();
    }

    /**
     * Amaga una fila.
     * @param x: numero de la fila que es vol amagar
     */
    public void hideResultRow(Integer x)
    {
        r.filter(x);
    }

    /**
     * Desamaga una fila.
     * @param x: numero de la fila que es vol desamagar
     */
    public void unhideResultRow(Integer x)
    {
        r.unfilter(x);
    }

    /**
     * Amaga un rang de files.
     * @param x1: primer nombre del rang
     * @param x2: darrer nombre del rang (no inclos)
     */
    public void hideResultRows(Integer x1, Integer x2)
    {
        for(int i = x1; i < x2; i++)
            r.filter(i);
        r.resetIndex();
    }

    /**
     * Desmaga un rang de files.
     * @param x1: primer nombre del rang
     * @param x2: darrer nombre del rang (no inclos)
     */
    public void unhideResultRows(Integer x1, Integer x2)
    {
        for(int i = x1; i < x2; i++)
            r.unfilter(i);
        r.resetIndex();
    }

    /**
     * Amaga els resultats on apareix el nom <em>x</em>.
     * @param x nom no desitjat
     */
    public void hideResultName(String x)
    {
        r.filter(x);
    }

    public void unhideResultName(String x)
    {
        r.unfilter(x);
    }

    /**
     * Filtra per nom, nomes es mostren els resultats
     * on apareix el nom <em>x</em>.
     * @param x nom que se selecciona
     */
    public void selectResultName(String x)
    {
        r.select(x);
    }

    public void unselectResultName(String x)
    {
        r.unselect(x);
    }

    /**
     * Ordena el resultat segons el valor de la columna
     * <em>col</em>, ascendentment si <em>dir</em> es 1 i
     * descendentment si es 0.
     * @param col numero de columna pel que es vol ordenar
     * @param dir 1 si ascendent i 0 si no
     */
    public void sortResultByRow(Integer col, Integer dir)
    {
        r.sort(col, (dir != 0));
    }

    /**
     * Treu tots els filtres del resultat.
     */
    public void clearResultFilters()
    {
        r.unfilterAll();
        r.unselectAll();
    }

    /**
     * Despres d'executar aquest metode, getResultRow() comencara
     * a donar files per la primera segons l'ultim ordre escollit
     * i tenint en compte tots els filtres imposats.
     */
    public void resetResult() {
        r.resetIndex();
    }

    /**
     * Retorna la llista de projectes guardats
     * @return llista de projectes guardats
     */
    public String[] getProjectList(){
        return pc.getProjectList();
    }

    /**
     * Guarda el graf amb el nom especificat
     * @param name
     * @throws DomainException
     */
    public void save(String name) throws DomainException {
        try {
            pc.createProject(name);
            pc.selectProject(name);
            save();
        }catch (PersistenceException pe)
        {
            throw new DomainException("No s'ha pogut guardar el graf: "+pe.getFriendlyMessage());
        }

    }

    /**
     * Guarda el graf.
     * @throws DomainException si falla l'escriptura
     */
    public void save() throws DomainException {
        try
        {
            pc.startSaving();
            Set<Node> s = g.getSetOfNodes(Author.TYPE);
            for(Node n : s)
            {
                pc.addAuthor(n.getID(), n.getName());
            }
            s = g.getSetOfNodes(Conf.TYPE);
            for(Node n : s)
            {
                pc.addConf(n.getID(), n.getName());
            }
            s = g.getSetOfNodes(Term.TYPE);
            for(Node n : s)
            {
                pc.addTerm(n.getID(), n.getName());
            }

            s = g.getSetOfNodes(Paper.TYPE);
            for(Node n : s)
            {
                pc.addPaper(n.getID(), n.getName());
            }
            for(Node n : s)
            {
                for (Node a : g.getNeighbours(n, Author.TYPE))
                {
                    pc.addPaperAuthor(n.getID(), a.getID());
                }
            }
            for(Node n : s)
            {
                for (Node a : g.getNeighbours(n, Conf.TYPE))
                {
                    pc.addPaperConf(n.getID(), a.getID());
                }
            }
            for(Node n : s)
            {
                for (Node a : g.getNeighbours(n, Term.TYPE))
                {
                    pc.addPaperTerm(n.getID(), a.getID());
                }
            }
            pc.commit();

        }
        catch (PersistenceException e)
        {
            try
            {
                pc.rollback();
            }
            catch (PersistenceException pe)
            {
                throw new DomainException("Fatal error: no s'ha pogut guardar el graf ni tampoc recuperar " +
                        "la versio anterior del backup. Comprova els permisos i torna a intentar-ho.");
            }
            throw new DomainException("No s'han pogut guardar els canvis: "+e.getFriendlyMessage()+
                                      "\n Torna a intentar-ho o comprova els permisos.");
        }

    }

    /**
     * Carrega un graf a memoria.
     * @param projectName Nom del projecte del graf.
     * @throws DomainException si hi ha problemes a la lectura
     */
    public void load(String projectName) throws DomainException {
        String[] elem;

        try {
            g = new Graph();
            pc.selectProject(projectName);
            pc.startLoading();
            while ((elem = pc.getAuthor()) != null) {
                Author a = new Author(Integer.parseInt(elem[0]), elem[1]);
                g.addNode(a);
            }

            while ((elem = pc.getPaper()) != null) {
                Paper a = new Paper(Integer.parseInt(elem[0]), elem[1]);
                g.addNode(a);
            }
            while ((elem = pc.getTerm()) != null) {
                Term a = new Term(Integer.parseInt(elem[0]), elem[1]);
                g.addNode(a);
            }
            while ((elem = pc.getConf()) != null) {
                Conf a = new Conf(Integer.parseInt(elem[0]), elem[1]);
                g.addNode(a);
            }

            //Arestes
            while ((elem = pc.getPaperAuthor()) != null) {
                Node a = g.getNode(Integer.parseInt(elem[0]), "paper");
                Node b = g.getNode(Integer.parseInt(elem[1]), "author");
                g.addEdge(a, b);
            }

            while ((elem = pc.getPaperTerm()) != null) {
                Node a = g.getNode(Integer.parseInt(elem[0]), "paper");
                Node b = g.getNode(Integer.parseInt(elem[1]), "term");
                g.addEdge(a, b);
            }

            while ((elem = pc.getPaperConf()) != null) {
                Node a = g.getNode(Integer.parseInt(elem[0]), "paper");
                Node b = g.getNode(Integer.parseInt(elem[1]), "conf");
                g.addEdge(a, b);
            }

        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new DomainException("Hi ha hagut un problema al intentar carregar el graf: "+e.getFriendlyMessage());
        }

    }

    /**
     * Comprova si hi ha algun projecte seleccionat.
     * @return <em>true</em> si i nomes si hi ha un projecte seleccionat
     */
    public boolean isProjectSelected() {
        return pc.isProjectSelected();
    }

    /**
     * Crea un nou projecte.
     * @param name nom del projecte
     * @throws DomainException si no es pot crear el projecte
     */
    public void createProject(String name) throws DomainException {
        try {
            pc.createProject(name);
        } catch (PersistenceException e) {
            throw new DomainException("No s'ha pogut crear el projecte: "+e.getFriendlyMessage());
        }
    }

    /**
     * Selecciona un projecte.
     * @param name nom del projecte
     * @throws DomainException si no es pot seleccionar el projecte
     */
    public void selectProject(String name) throws DomainException {
        try {
            pc.selectProject(name);
        } catch (PersistenceException e) {
            throw new DomainException("No s'ha pogut seleccionar el projecte: "+e.getFriendlyMessage());
        }
    }

    public void unSelectProject(){
        pc.unSelectProject();
        g = new Graph();
    }

    /**
     * Esborra un projecte.
     * @param name projecte a esborrar
     * @throws DomainException si no existeix el projecte o no s'aconsegueix esborrar
     */
    public void deleteProject(String name) throws DomainException {
        try {
            pc.deleteProject(name);
        } catch (PersistenceException pe) {
            throw new DomainException("No s'ha pogut esborrar el projecte: " + pe.getFriendlyMessage());
        }
    }

    /**
     * Dona el nom d'un node a partir de la seva id i tipus
     * @param id id
     * @param type tipus
     * @return nom del node amb id <em>id</em> i tipus <em>type</em>
     * @throws DomainException si el tipus es incorrecte o el node no existeix
     */
    public String getNodeName(int id, String type) throws DomainException {
        return g.getNode(id, type).getName();
    }

    /**
     * Dona tots els nodes del graf, en format portable.
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, el nom i en la tercera el numero de veins.
     * En la cuarta, el tipus.
     */
    public ArrayList<String[]> getNodes() {
        ArrayList<String[]> l = new ArrayList<>();
        for (Node node: g.getSetOfNodes()) {
            String[] f = new String[4];
            f[0] = String.valueOf(node.getID());
            f[1] = node.getName();
            f[2] = String.valueOf(g.getNeighbours(node).size());
            f[3] = node.getType();
            l.add(f);
        }

        return l;
    }

    /**
     * Dona tots els nodes de tipus <em>type</em> del graf, en format portable.
     * @param type tipus dels nodes que es consulten
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, el nom i en la tercera el numero de veins
     * @throws DomainException si el tipus <em>type</em> es incorrecte
     */
    public ArrayList<String[]> getNodes(String type) throws DomainException {
        ArrayList<String[]> l = new ArrayList<>();
        for (Node node: g.getSetOfNodes(type)) {
            String[] f = new String[3];
            f[0] = String.valueOf(node.getID());
            f[1] = node.getName();
            f[2] = String.valueOf(g.getNeighbours(node).size());
            l.add(f);
        }

        return l;
    }

    /**
     * Dona totes les arestes de nodes de tipus Paper cap a nodes de tipus <em>type</em>.
     * @param type tipus desti de les arestes
     * @return Un ArrayList de parelles de String. En la primera posicio hi ha la id
     * del paper i en la segona, la id del vei de tipus <em>type</em>
     * @throws DomainException si el tipus <em>type</em> es incorrecte
     */
    public ArrayList<String[]> getEdges(String type) throws DomainException {
        ArrayList<String[]> l = new ArrayList<>();
        for (Node node: g.getSetOfNodes(type)) {
            for (Node paper: g.getNeighbours(node, Paper.TYPE)) {
                String[] f = new String[2];
                f[0] = String.valueOf(paper.getID());
                f[1] = String.valueOf(node.getID());
                l.add(f);
            }
        }
        return l;
    }
}
