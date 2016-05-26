package Presentation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;


import Domain.DomainController;
import Domain.DomainException;
import Domain.IntermediateHeteSimMatrix;
import javafx.scene.web.WebEngine;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

public class PresentationController {
    private DomainController dc;
    private WebEngine we;
    private int MAX_FILES;

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
        Task<String> task = new Task<String>() {
            @Override protected String call() throws Exception {
                /* Extreure les ids dels strings d'entrada */
                int id1, id2;
                String[] parts = idSource.split("-");
                id1 = Integer.parseInt(parts[0]);
                parts = idEnd.split("-");
                id2 = Integer.parseInt(parts[0]);
                dc.query1to1(id1, typeSource, id2, typeEnd);
                ArrayList<String> fila = dc.getResultRow();
                return fila.get(3);
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        task.exceptionProperty().addListener(new ChangeListener<Throwable>(){
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
                we.executeScript("app.events.showInfo('Resultat','"+newValue+"', 'ok');"));

    }


    public void log(String msg){
        System.out.println(msg);
    }
}
