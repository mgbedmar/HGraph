package Presentation;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;


import Domain.DomainController;
import Domain.DomainException;
import Domain.IntermediateHeteSimMatrix;
import javafx.scene.web.WebEngine;

import javax.smartcardio.ATR;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

public class PresentationController {
    private DomainController dc;
    private WebEngine we;
    private int MAX_ROWS = 100;
    private String query1To1Result;
    private ArrayList<ArrayList<String>> result;
    private int currentNumCols;


    /**
     * Retorna la id mes gran de la llista.
     * @param ids llista d'enters
     * @return el maxim de la llista <em>ids</em>
     * @throws DomainException si la llista es buida
     */
    private Integer getBigger(ArrayList<Integer> ids) throws DomainException {
        if(ids.size() == 0)
            throw new DomainException("Hi ha hagut un problema desconegut.");

        Integer bigger = ids.get(0);
        for(Integer id : ids){
            if(id > bigger)
                bigger = id;
        }

        return bigger;
    }


    private ArrayList<ArrayList<String>> formResult(String toAdd) {
        ArrayList<ArrayList<String>> r = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = MAX_ROWS;
        if (numRows == 0) numRows = dc.getResultSize();

        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            //addNumsRow(i, Integer.parseInt(fila.get(0)));
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0))+1)));
            if (currentNumCols == 3)
                fila.add(1, toAdd);
            r.add(fila);
            ++i;
        }
        return r;
    }

    private ArrayList<ArrayList<String>> resultDetails() {
        String toAdd;
        if (dc.getResultSize() == 0) return new ArrayList<>();
        System.out.println("no zero");
        if (currentNumCols == 2) return basicResult();
        toAdd = result.get(0).get(1);
        return formResult(toAdd);
    }


    /**
     * Setter del resultat de la consulta 1 a 1. El posa el thread corresponent.
     * @param r String que conte el hetesim
     */
    protected void setQuery1To1Result(String r) {
        query1To1Result = r;
    }

    protected void setQueryResult(ArrayList<ArrayList<String>> r) {
        this.result = r;
    }

    public String getQuery1To1Result() {
        return query1To1Result;
    }

    public ArrayList<ArrayList<String>> getQueryResult() {
        return result;
    }


    public void saveAs(String name){
        try {
            dc.save(name);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
    }

    public void save(){
        try {
            dc.save();
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
    }


    public PresentationController(WebEngine webEngine){
        dc = new DomainController();
        we = webEngine;
    }

    public String[] getProjects(){
        return dc.getProjectList();
    }

    public boolean isProjectSelected(){
        return dc.isProjectSelected();
    }

    public void loadProject(String projectName) {
        try {
            dc.load(projectName);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
    }

    public void unSelectProject(){
        dc.unSelectProject();
    }
    public void deleteProject(String projectName){
        try {
            dc.deleteProject(projectName);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
    }

    public Integer addNode(String label, String type){
        try{
            dc.addNode(label, type);
            ArrayList<Integer> ids = dc.getNodes(label, type);
            return getBigger(ids);
        }catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }

        return null;
    }

    public boolean removeNode(int id, String type){
        try{
            dc.removeNode(id, type);
            return true;
        }catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
        return false;
    }

    public boolean addEdge(int idA, String nameA, int idB, String nameB){
        try {
            dc.addEdge(idA, nameA, idB, nameB);
            return true;
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }

        return false;
    }

    public boolean removeEdge(int idA, String nameA, int idB, String nameB){
        try {
            dc.removeEdge(idA, nameA, idB, nameB);
            return true;
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }

        return false;
    }

    public ArrayList<String[]> getNodes() {
        return dc.getNodes();
    }


    public ArrayList<String[]> getNodesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getNodes(type);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
        return res;
    }

    public ArrayList<String[]> getRelevantNodesOfType(String type, int quantity){
        ArrayList<String[]> res = getNodesOfType(type);
        res.sort( (a, b) -> Integer.parseInt(a[2])-Integer.parseInt(b[2]) );
        res = new ArrayList<>(res.subList(0, Math.min(res.size(), quantity)));
        return res;
    }

    public ArrayList<String[]> getEdgesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getEdges(type);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
        return res;

    }

    public int queryNeighboursSize(String id, String type){
        int res = 0;
        try{
            dc.queryNeighbours(Integer.parseInt(id), type);
            res = dc.getResultSize();
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
        return res;
    }


    private ArrayList<ArrayList<String>> basicResult() {
        ArrayList<ArrayList<String>> r = new ArrayList<>();
        result = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = MAX_ROWS;
        if (numRows == 0) numRows = dc.getResultSize();

        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            System.out.println(i);
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0)) + 1)));
            r.add(fila);
            ++i;
        }
        return r;
    }

    public ArrayList<ArrayList<String>> queryByType(String type) {
        try {
            currentNumCols = 2;
            dc.queryByType(type);
            return basicResult();
        } catch (DomainException e) {
            String scr = "app.events.showInfo(\"Error\", \"" + e.getFriendlyMessage() + "\", \"OK\");";
            we.executeScript("app.events.hidePopup(function(){"+scr+"});");
        }
        return null;
    }

    public ArrayList<ArrayList<String>> queryNeighbours(String id, String type) {
        try {
            currentNumCols = 2;
            dc.queryNeighbours(Integer.parseInt(id), type);
            return basicResult();
        } catch (DomainException e) {
            String scr = "app.events.showInfo(\"Error\", \"" + e.getFriendlyMessage() + "\", \"OK\");";
            we.executeScript("app.events.hidePopup(function(){"+scr+"});");
        }
        return null;
    }

    public ArrayList<ArrayList<String>> queryNeighbours(String id, String type, String typeEnd) {
        try {
            currentNumCols = 2;
            dc.queryNeighbours(Integer.parseInt(id), type, typeEnd);
            return basicResult();
        } catch (DomainException e) {
            String scr = "app.events.showInfo(\"Error\", \"" + e.getFriendlyMessage() + "\", \"OK\");";
            we.executeScript("app.events.hidePopup(function(){"+scr+"});");
        }
        return null;
    }

    /* Inicia una tasca per calcular una consulta 1 a 1 */
    public void query1to1(String idSource, String typeSource,
                          String idEnd, String typeEnd) {
        Task<String> task = new Query1To1Task(idSource, typeSource, idEnd, typeEnd, dc, we, this);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        /*task.exceptionProperty().addListener(new ChangeListener<Throwable>(){
                @Override public void changed(ObservableValue<? extends Throwable> ob, Throwable old, Throwable newv){
                    we.executeScript("app.events.showInfo('Eps!','"+newv.getMessage()+"', 'Cap problema');");
                }
            });
        //TODO? progress messages
        /*
        task.progressProperty().addListener((obs, oldProgress, newProgress) ->
                System.out.println(newProgress));


        task.valueProperty().addListener((observable, oldValue, newValue) ->
                we.executeScript("app.events.showInfo('Resultat','"+newValue+"', 'OK');"));*/

    }

    public void query1toN(String idSource, String typeSource, String typeEnd) {
        currentNumCols = 3;
        result = new ArrayList<>();
        QueryTask task = new Query1toNTask(idSource, typeSource, typeEnd, dc, we, this, MAX_ROWS);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void queryNtoN(String typeSource, String typeEnd) {
        currentNumCols = 4;
        result = new ArrayList<>();
        QueryTask task = new QueryNtoNTask(typeSource, typeEnd, dc, we, this, MAX_ROWS);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public void queryByReference(String nodeRefSourceID, String nodeRefSourceType,
                                 String nodeRefEndID, String nodeRefEndType,
                                 String nodeSourceID, String nodeSourceType) {
        currentNumCols = 3;
        result = new ArrayList<>();
        QueryTask task = new QueryByReferenceTask(nodeRefSourceID, nodeRefSourceType, nodeRefEndID,
                                                  nodeRefEndType, nodeSourceID, nodeSourceType,
                                                  dc, we, this, MAX_ROWS);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    public ArrayList<ArrayList<String>> sortResult(int col, int dir) {
        if (currentNumCols == 3) col = col - 1;
        System.out.println(col);
        if (col == 0) dc.resetResult();
        else dc.sortResultByRow(col, dir);
        System.out.println("despres de dc");
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> selectResultName(String name) {
        dc.selectResultName(name);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> unselectResultName(String name) {
        dc.unselectResultName(name);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> hideResultName(String name) {
        dc.hideResultName(name);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> unhideResultName(String name) {
        dc.unhideResultName(name);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> hideResultRow(String index) {
        dc.hideResultRow(Integer.parseInt(index)-1);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> unhideResultRow(String index) {
        dc.unhideResultRow(Integer.parseInt(index)-1);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> hideResultRows(String indexs) {
        String[] params = indexs.split("\\-");
        dc.hideResultRows(Integer.parseInt(params[0].trim())-1, Integer.parseInt(params[1].trim())-1);
        return resultDetails();
    }

    public ArrayList<ArrayList<String>> unhideResultRows(String indexs) {
        String[] params = indexs.split("\\-");
        dc.unhideResultRows(Integer.parseInt(params[0].trim())-1, Integer.parseInt(params[1].trim())-1);
        return resultDetails();
    }

    public void log(String msg){
        System.out.println(msg);
    }
}


class Query1To1Task extends Task<String> {

    private String idSource, idEnd, typeSource, typeEnd;
    private DomainController dc;
    private WebEngine we;
    private PresentationController pc;

    public Query1To1Task(String idSource, String typeSource,
                         String idEnd, String typeEnd,
                         DomainController dc, WebEngine we, PresentationController pc) {
        this.idSource = idSource;
        this.idEnd = idEnd;
        this.typeSource = typeSource;
        this.typeEnd = typeEnd;
        this.dc = dc;
        this.we = we;
        this.pc = pc;
    }

    @Override
    public String call() throws DomainException {
        int id1, id2;
        String[] parts = idSource.split("-");
        id1 = Integer.parseInt(parts[0]);
        parts = idEnd.split("-");
        id2 = Integer.parseInt(parts[0]);
        dc.query1to1(id1, typeSource, id2, typeEnd);
        ArrayList<String> fila = dc.getResultRow();
        return fila.get(3);
    }

    public void done() {
        try {
            String r = get();
            pc.setQuery1To1Result(r);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    we.executeScript("app.events.takeQuery1To1Result()");
                }
            });

        } catch (ExecutionException e) {
            Platform.runLater(new Runnable() {
                private String message;
                @Override public void run() {
                    String scr = "app.events.showInfo(\"Error\", \"" + message + "\", \"OK\");";
                    we.executeScript("app.events.hidePopup(function(){"+scr+"});");
                }

                public Runnable setParams(String message) {
                    this.message = message;
                    return this;
                }
            }.setParams(e.getMessage()));

            //TODO treure aixo:
            //e.printStackTrace();

        } catch (InterruptedException e) {
            //Aixo pot interessar-nos provocar-ho nosaltres mateixos per parar una tasca que duri massa
            we.executeScript("app.events.showInfo('Error.', '"+e.getMessage()+"', 'OK');");
        }
    }
}

abstract class QueryTask extends Task<ArrayList<ArrayList<String>>> {

    protected DomainController dc;
    protected WebEngine we;
    protected PresentationController pc;
    protected int maxRows;

    public QueryTask(DomainController dc, WebEngine we, PresentationController pc, int maxRows) {
        this.dc = dc;
        this.we = we;
        this.pc = pc;
        this.maxRows = maxRows;
    }

    public void done() {
        try {
            ArrayList<ArrayList<String>> r = get();
            pc.setQueryResult(r);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    we.executeScript("app.events.takeQueryResult()");
                }
            });

        } catch (ExecutionException e) {
            Platform.runLater(new Runnable() {
                private String message;
                @Override public void run() {
                    String scr = "app.events.showInfo(\"Error\", \"" + message + "\", \"OK\");";
                    we.executeScript("app.events.hidePopup(function(){"+scr+"});");
                }

                public Runnable setParams(String message) {
                    this.message = message;
                    return this;
                }
            }.setParams(e.getMessage()));

        } catch (InterruptedException e) {
            //TODO Aixo pot interessar-nos provocar-ho nosaltres mateixos per parar una tasca que duri massa
            we.executeScript("app.events.showInfo('Error.', '"+e.getMessage()+"', 'OK');");
        }
    }
}

class Query1toNTask extends QueryTask {
    private int idSource;
    private String typeSource, typeEnd;

    public Query1toNTask(String idSource, String typeSource, String typeEnd,
                         DomainController dc, WebEngine we, PresentationController pc, int maxRows) {
        super(dc, we, pc, maxRows);
        this.idSource = Integer.parseInt(idSource);
        this.typeSource = typeSource;
        this.typeEnd = typeEnd;
    }

    public ArrayList<ArrayList<String>> call() throws DomainException {
        dc.query1toN(idSource, typeSource, typeEnd);

        ArrayList<ArrayList<String>> r = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = maxRows;
        if (numRows == 0) numRows = dc.getResultSize();

        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0))+1)));
            fila.add(1, dc.getNodeName(idSource, typeSource)+"{"+String.valueOf(idSource)+"}");
            r.add(fila);
            ++i;
        }
        return r;
    }

}

class QueryNtoNTask extends QueryTask {
    private int idSource;
    private String typeSource, typeEnd;

    public QueryNtoNTask(String typeSource, String typeEnd,
                         DomainController dc, WebEngine we, PresentationController pc, int maxRows) {
        super(dc, we, pc, maxRows);
        this.typeSource = typeSource;
        this.typeEnd = typeEnd;
    }

    public ArrayList<ArrayList<String>> call() throws DomainException {
        dc.queryNtoN(typeSource, typeEnd);

        ArrayList<ArrayList<String>> r = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = maxRows;
        if (numRows == 0) numRows = dc.getResultSize();

        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0))+1)));
            r.add(fila);
            ++i;
        }
        return r;
    }

}

class QueryByReferenceTask extends QueryTask {
    private int idSource, idSourceRef, idEndRef;
    private String typeSourceRef, typeEndRef, typeSource;

    public QueryByReferenceTask(String nodeRefSourceID, String nodeRefSourceType,
                                String nodeRefEndID, String nodeRefEndType,
                                String nodeSourceID, String nodeSourceType,
                                DomainController dc, WebEngine we, PresentationController pc, int maxRows) {
        super(dc, we, pc, maxRows);
        this.idSourceRef = Integer.parseInt(nodeRefSourceID);
        this.typeSourceRef = nodeRefSourceType;
        this.idEndRef = Integer.parseInt(nodeRefEndID);
        this.typeEndRef = nodeRefEndType;
        this.idSource = Integer.parseInt(nodeSourceID);
        this.typeSource = nodeSourceType;
    }

    public ArrayList<ArrayList<String>> call() throws DomainException {
        dc.queryByReference(idSourceRef, typeSourceRef, idEndRef, typeEndRef, idSource, typeSource);

        ArrayList<ArrayList<String>> r = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = maxRows;
        if (numRows == 0) numRows = dc.getResultSize();

        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0))+1)));
            fila.add(1, dc.getNodeName(idSource, typeSource)+"{"+String.valueOf(idSource)+"}");
            r.add(fila);
            ++i;
        }
        return r;
    }

}
