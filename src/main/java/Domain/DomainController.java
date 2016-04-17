package Domain;


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
    private Node createNode(String name, String type)
    {

        switch(type)
        {
            case "author":
                return new Author(name);
            case "term":
                return new Term(name);
            case "paper":
                return new Paper(name);
            case "conf":
                return new Conf(name);
        }

        return null;
    }


    /**
     * Afegeix un node
     * @param name Nom del node
     * @param type Tipus del node
     */
    public void addNode(String name, String type)
    {
        g.afegirNode(name, type);
    }

    /**
     * Esborra un node
     * @param name Nom del node
     * @param type Tipus del node
     */
    public void removeNode(String name, String type)
    {
        g.esborrarNode(name, type);
    }

    /**
     * Canvia el nom d'un node
     * @param name Nom del node
     * @param type Tipus del node
     * @param newName Nou nom per el node
     */
    public void modifyNode(String name, String type, String newName)
    {
        //TODO graph -> modificarNode()
        //g.modificarNode(name, type, newName)/(Node, newName)
    }

    /**
     * Crea una aresta formada pels nodes <em>A</em> i <em>B</em>
     * @param nameA
     * @param typeA
     * @param nameB
     * @param typeB
     */
    public void addEdge(String nameA, String typeA,
                        String nameB, String typeB)
    {
        Node A = createNode(nameA, typeA);
        Node B = createNode(nameB, typeB);

        g.afegirAresta(A, B);
    }

    /**
     * Esborra l'aresta formada pels nodes <em>A</em> i <em>B</em>
     * @param nameA Nom del node A
     * @param typeA Tipus del node A
     * @param nameB Nom del node B
     * @param typeB Tipus de node B
     */
    public void removeEdge(String nameA, String typeA,
                           String nameB, String typeB)
    {
        Node A = createNode(nameA, typeA);
        Node B = createNode(nameB, typeB);
        g.esborrarAresta(A, B);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta de tipus
     * @param type Nom del tipus a consultar
     */
    public void queryByType(String type)
    {
        r = Query.queryByType(g, type);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta 1 a 1
     * @param nomSource Nom del node font
     * @param typeSource Tipus del node font
     * @param nomEnd Nom del node destí
     * @param typeEnd Tipus del node destí
     */
    public void query1to1(String nomSource, String typeSource,
                          String nomEnd, String typeEnd)
    {
        Node source = createNode(nomSource, typeSource);
        Node end = createNode(nomEnd, typeEnd);

        //TODO path
        //r = Query.query1to1(g, source, end);
    }

    /**
     * Coloca a <em>r</em> el resultat de consultar els veins del node
     * @param name Nom del node font
     * @param type Tipus del node font
     */
    public void queryNeighbours(String name, String type)
    {
        Node source = createNode(name, type);
        r = Query.queryNeighbours(g, source);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta 1 a N
     * @param sourceName Nom del node font
     * @param sourceType Tipus del node font
     * @param typeEnd Tipus destí
     */
    public void query1toN(String sourceName, String sourceType, String typeEnd)
    {
        Node source = createNode(sourceName, sourceType);
        //TODO path
        //r = Query.query1toN(g, source, typeEnd);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta N a N
     * @param typeA Tipus font
     * @param typeB Tipus destí
     */
    public void queryNtoN(String typeA, String typeB)
    {
        //TODO path
        //r = Query.queryNtoN(g, typeA, typeB);
    }

    /**
     * Coloca a <em>r</em> el resultat de fer una consulta per referència
     * @param nodeRefSourceName Nom del node referència font
     * @param nodeRefSourceType Tipus del node referència font
     * @param nodeRefEndName Nom del node referència destí
     * @param nodeRefEndType Tipus del node referència destí
     * @param nodeSourceName Nom del node font
     * @param nodeSourceType Tipus del node font
     */
    public void queryByReference(String nodeRefSourceName, String nodeRefSourceType,
                                 String nodeRefEndName, String nodeRefEndType,
                                 String nodeSourceName, String nodeSourceType)
    {

        Node refSource = createNode(nodeRefSourceName, nodeRefSourceType);
        Node refEnd = createNode(nodeRefEndName, nodeRefEndType);
        Node source = createNode(nodeSourceName, nodeSourceType);
        //TODO path
        //r = Query.queryByReference(g, refSource, refEnd, source);
    }

    public Map<String,ArrayList<String>> getFilters() {
        return r.getFilters();
    }

    public Integer getResultSize() {
        return r.size();
    }


    public ArrayList<String> getResultRow() {
        return r.getRow();
    }

    public void hideResultRow(Integer x) {
        r.filter(x);
    }

    public void hideResultRows(Integer x1, Integer x2) {
        for(int i = x1; i < x2; i++)
            r.filter(i);
    }

    public void hideResultName(String x) {
        r.filter(x);
    }

    public void selectResultName(String x) {
        r.select(x);
    }

    public void sortResultByRow(Integer col, Integer dir) {
        r.sort(col, (dir != 0));
    }

    public void clearResultFilters() {
        r.unfilterAll();
    }
/*
    public void selectResultRows(Integer x1, Integer x2) {
        //TODO
    }
    */
}
