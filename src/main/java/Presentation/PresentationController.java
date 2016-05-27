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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

public class PresentationController {
    private DomainController dc;
    private WebEngine we;
    private int MAX_FILES;
    private String query1To1Result;
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

    protected void setQuery1To1Result(String r) {
        query1To1Result = r;
    }

    public String getQuery1To1Result() {
        return query1To1Result;
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
            we.executeScript("app.events.showInfo('Eps!','"+de.getFriendlyMessage()+"', 'Cap problema');");
        }
    }
    public void deleteProject(String projectName){
        try {
            dc.deleteProject(projectName);
        } catch (DomainException de) {
            we.executeScript("app.events.showInfo('Eps!','"+de.getFriendlyMessage()+"', 'Cap problema');");
        }
    }

    public Integer addNode(String label, String type){
        try{
            dc.addNode(label, type);
            ArrayList<Integer> ids = dc.getNodes(label, type);
            return getBigger(ids);
        }catch (DomainException de) {
            we.executeScript("app.events.showInfo('Eps!','"+de.getFriendlyMessage()+"', 'Cap problema');");
        }

        return null;
    }

    public void removeNode(int id, String type){
        try{
            dc.removeNode(id, type);
        }catch (DomainException de) {
            we.executeScript("app.events.showInfo('Eps!','"+de.getFriendlyMessage()+"', 'Cap problema');");
        }
    }

    public boolean addEdge(int idA, String nameA, int idB, String nameB){
        try {
            dc.addEdge(idA, nameA, idB, nameB);
            return true;
        } catch (DomainException e) {
            e.printStackTrace();
        }

        return false;
    }



    public ArrayList<String[]> getNodesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getNodes(type);
        } catch (DomainException de) {
            we.executeScript("app.events.showInfo('Eps!','"+de.getFriendlyMessage()+"', 'Cap problema');");
        }
        return res;
    }

    public ArrayList<String[]> getEdgesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getEdges(type);
        } catch (DomainException de) {
            we.executeScript("app.events.showInfo('Eps!','"+de.getFriendlyMessage()+"', 'Cap problema');");
        }
        return res;
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
                */

        task.valueProperty().addListener((observable, oldValue, newValue) ->
                we.executeScript("app.events.showInfo('Resultat','"+newValue+"', 'OK');"));

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
                    we.executeScript("app.events.hidePopup();");
                    String scr = "app.events.showInfo(\"Error\", \"" + message + "\", \"OK\");";
                    we.executeScript(scr);
                }

                public Runnable setParams(String message) {
                    this.message = message;
                    return this;
                }
            }.setParams(e.getMessage()));

            //TODO treure aixo:
            e.printStackTrace();

        } catch (InterruptedException e) {
            we.executeScript("app.events.showInfo('Error.', '"+e.getMessage()+"', 'OK');");
        }
    }
}