package QueryTest;


import Domain.*;
import Domain.DomainException;
import Domain.Graph.*;

import java.util.ArrayList;
import java.util.Map;

public class DomainController
{
    private Graph g;
    private Result r;

    /**
     * @param name Nom del node
     * @param type Tipus del node
     * @return Un node de tipus <em>type</em> amb nom <em>nom</em>
     */
    private Node createNode(String name, String type) throws Domain.DomainException
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

        throw new Domain.DomainException("El tipus '"+type+"' no existeix.");
    }

    /**
     * Constructora
     */
    public DomainController()
    {
        g = new Graph();
    }

    /**
     * Consulta les ids dels nodes amb un nom i tipus.
     * @param name Nom del node
     * @param type Tipus del node
     * @return Una llista de les ids dels nodes que tenen nom <em>name</em>
     * i tipus <em>type</em>. Si no n'hi ha cap, retorna una llista buida.
     */
    public ArrayList<Integer> getNodes(String name, String type) throws Domain.DomainException
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
     */
    public void addNode(String name, String type) throws Domain.DomainException
    {
        Node node = createNode(name, type);
        g.addNode(node);
    }

    /**
     * Esborra un node
     * @param id id del node
     * @param type Tipus del node
     */
    public void removeNode(int id, String type) throws Domain.DomainException
    {
        Node node = g.getNode(id, type);
        g.removeNode(node);
    }

    /**
     * Canvia el nom d'un node
     * @param id id del node
     * @param type Tipus del node
     * @param newName Nou nom per al node
     */
    public void modifyNode(int id, String type, String newName) throws Domain.DomainException
    {
        Node node = g.getNode(id, type);
        node.setName(newName);
    }

    /**
     * Crea una aresta formada pels nodes <em>A</em> i <em>B</em>
     * @param idA
     * @param typeA
     * @param idB
     * @param typeB
     */
    public void addEdge(int idA, String typeA,
                        int idB, String typeB) throws Domain.DomainException
    {
        Node a = g.getNode(idA, typeA);
        Node b = g.getNode(idB, typeB);
        g.addEdge(a,b);
    }

    /**
     * Esborra l'aresta formada pels nodes <em>A</em> i <em>B</em>
     * @param idA Nom del node A
     * @param typeA Tipus del node A
     * @param idB Nom del node B
     * @param typeB Tipus de node B
     */
    public void removeEdge(int idA, String typeA,
                           int idB, String typeB) throws Domain.DomainException
    {
        Node a = g.getNode(idA, typeA);
        Node b = g.getNode(idB, typeB);
        g.removeEdge(a,b);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta de tipus
     * @param type Nom del tipus a consultar
     */
    public void queryByType(String type) throws Domain.DomainException
    {
        r = Query.queryByType(g, type);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta 1 a 1
     * @param idSource Nom del node font
     * @param typeSource Tipus del node font
     * @param idEnd Nom del node destí
     * @param typeEnd Tipus del node destí
     */
    public void query1to1(int idSource, String typeSource,
                          int idEnd, String typeEnd) throws Domain.DomainException
    {
        Node source = g.getNode(idSource, typeSource);
        Node end = g.getNode(idEnd, typeEnd);

        r = Query.query1to1(g, source, end, Query.defaultPath.get(typeSource).get(typeEnd));


    }

    /**
     * Coloca a <em>r</em> el resultat de consultar els veins del node
     * @param id Nom del node font
     * @param type Tipus del node font
     */
    public void queryNeighbours(int id, String type) throws Domain.DomainException
    {
        Node source = g.getNode(id, type);
        r = Query.queryNeighbours(g, source);
    }

    /**
     * Coloca a <em>r</em> el resultat de consultar els veins del node
     * @param idSource Nom del node font
     * @param typeSource Tipus del node font
     * @param typeEnd Tipus dels nodes a consultar
     */
    public void queryNeighbours(int idSource, String typeSource, String typeEnd) throws Domain.DomainException
    {
        Node source = g.getNode(idSource, typeSource);
        r = Query.queryNeighbours(g, source, typeEnd);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta 1 a N
     * @param idSource Nom del node font
     * @param typeSource Tipus del node font
     * @param typeEnd Tipus destí
     */
    public void query1toN(int idSource, String typeSource, String typeEnd) throws Domain.DomainException
    {
        Node source = g.getNode(idSource, typeSource);
        r = Query.query1toN(g, source, Query.defaultPath.get(typeSource).get(typeEnd));
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta N a N
     * @param typeSource Tipus font
     * @param typeEnd Tipus destí
     */
    public void queryNtoN(String typeSource, String typeEnd) throws Domain.DomainException
    {
        r = Query.queryNtoN(g, Query.defaultPath.get(typeSource).get(typeEnd));
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta per referència
     * @param nodeRefSourceID id del node referència font
     * @param nodeRefSourceType Tipus del node referència font
     * @param nodeRefEndID id del node referència destí
     * @param nodeRefEndType Tipus del node referència destí
     * @param nodeSourceID id del node font
     * @param nodeSourceType Tipus del node font
     */
    public void queryByReference(int nodeRefSourceID, String nodeRefSourceType,
                                 int nodeRefEndID, String nodeRefEndType,
                                 int nodeSourceID, String nodeSourceType) throws DomainException
    {
        Node refSource = g.getNode(nodeRefSourceID, nodeRefSourceType);
        Node refEnd = g.getNode(nodeRefEndID, nodeRefEndType);
        Node source = g.getNode(nodeSourceID, nodeSourceType);

        r = Query.queryByReference(g, refSource, refEnd, source,
                Query.defaultPath.get(nodeRefSourceType).get(nodeRefEndType), Query.TOL);
    }

    public Map<String,ArrayList<String>> getFilters()
    {
        return r.getFilters();
    }

    public Integer getResultSize() {
        return r.size();
    }

    public ArrayList<String> getResultRow()
    {
        return r.getRow();
    }

    public void hideResultRow(Integer x)
    {
        r.filter(x);
    }

    public void hideResultRows(Integer x1, Integer x2)
    {
        for(int i = x1; i < x2; i++)
            r.filter(i);
    }

    public void hideResultName(String x)
    {
        r.filter(x);
    }

    public void selectResultName(String x)
    {
        r.select(x);
    }

    public void sortResultByRow(Integer col, Integer dir)
    {
        r.sort(col, (dir != 0));
    }

    public void clearResultFilters()
    {
        r.unfilterAll();
        r.unselectAll();
    }

    public void resetResult() {
        r.resetIndex();
    }
/*
    public void selectResultRows(Integer x1, Integer x2) {
        //TODO
    }
    */
}
