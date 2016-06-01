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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.smartcardio.ATR;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;

/**
 * @author Alejandro i Dani
 */

public class PresentationController {
    /**
     * Controlador de domini.
     */
    private DomainController dc;

    /**
     * WebEngine (comunicacio amb Javascript).
     */
    private WebEngine we;

    /**
     * Stage de la UI.
     */
    private Stage stage;

    /**
     * Maxim de files que surt per pantalla en un resultat.
     */
    private int MAX_ROWS = 100;

    /**
     * Resultat de la consulta 1 a 1.
     */
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

    /**
     * Gestiona una excepcio traient un popup.
     * @param msg missatge de l'excepcio.
     */
    private void handleException(String msg) {
        we.executeScript("app.events.hidePopup(function() {app.events.showInfo(\"Error\", \"" + msg + "\", \"OK\"); });");
    }

    /**
     * Forma el resultat d'una consulta en un format adequat pel Javascript.
     * @param toAdd String per afegir repetit a la primera columna
     * @return array amb les files del resultat, que porten un numero de fila, els noms i el hetesim
     */
    private ArrayList<ArrayList<String>> formResult(String toAdd) {
        ArrayList<ArrayList<String>> r = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = MAX_ROWS;
        if (numRows == 0) numRows = dc.getResultSize();

        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0))+1)));
            if (currentNumCols == 3)
                fila.add(1, toAdd);
            r.add(fila);
            ++i;
        }
        return r;
    }

    /**
     * Forma el resultat amb alguns retocs depenent del tipus de consulta.
     * @return array amb les files del resultat, que porten un numero de fila, els noms i el hetesim
     */
    private ArrayList<ArrayList<String>> resultDetails() {
        String toAdd;
        if (dc.getResultSize() == 0) return new ArrayList<>();
        if (currentNumCols == 2) return basicResult();
        toAdd = result.get(0).get(1);
        return formResult(toAdd);
    }

    /**
     * Forma el resultat d'una consulta en un format adequat pel Javascript.
     * @return array amb les files del resultat, que porten un numero de fila, els noms i el hetesim
     */
    private ArrayList<ArrayList<String>> basicResult() {
        ArrayList<ArrayList<String>> r = new ArrayList<>();
        result = new ArrayList<>();
        ArrayList<String> fila;
        int i = 1;
        int numRows = MAX_ROWS;
        if (numRows == 0) numRows = dc.getResultSize();
        while (i <= numRows && (fila = dc.getResultRow()) != null) {
            fila.set(0, String.valueOf((Integer.parseInt(fila.get(0)) + 1)));
            r.add(fila);
            ++i;
        }
        return r;
    }


    /**
     * Setter del resultat de la consulta 1 a 1. El posa el thread corresponent.
     * @param r String que conte el hetesim
     */
    protected void setQuery1To1Result(String r) {
        query1To1Result = r;
    }


    /**
     * Posa el resultat de la consulta. Ho fa el thread que s'encarrega de la consulta.
     * @param r resultat
     */
    protected void setQueryResult(ArrayList<ArrayList<String>> r) {
        this.result = r;
    }


    /**
     * Constructora. Instancia el controlador de presentacio.
     * @param webEngine webEngine per comunicar-se amb la vista (JS)
     * @param stg Stage de l'aplicacio
     */
    public PresentationController(WebEngine webEngine, Stage stg){
        dc = new DomainController();
        we = webEngine;
        stage = stg;
    }

    /**
     * Obte el resultat de la consulta. La crida el Javascript.
     * @return hetesim en un String
     */
    public String getQuery1To1Result() {
        return query1To1Result;
    }

    /**
     * Dona el resultat de la ultima consulta (no 1 a 1) realitzada.
     * @return resultat
     */
    public ArrayList<ArrayList<String>> getQueryResult() {
        return result;
    }

    /**
     * Desa el projecte actiu amb un altre nom.
     * @param name nom nou del projecte
     */
    public void saveAs(String name){
        try {
            dc.save(name);
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
    }

    /**
     * Desa els canvis del projecte actiu.
     */
    public void save(){
        try {
            dc.save();
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
    }

    /**
     * Posa a <em>maxRows</em> l'atribut <em>MAX_ROWS</em>.
     * @param maxRows nou valor per <em>MAX_ROWS</em>
     */
    public void setMaxRows(int maxRows) {
        MAX_ROWS = maxRows;
    }

    /**
     * Dona una llista dels projectes existents.
     * @return llista amb els noms dels projectes
     */
    public String[] getProjects(){
        return dc.getProjectList();
    }

    /**
     * Consulta si hi ha un projecte actiu o no.
     * @return cert si i nomes si hi ha un projecte actiu
     */
    public boolean isProjectSelected(){
        return dc.isProjectSelected();
    }

    /**
     * Carrega un projecte.
     * @param projectName nom del projecte
     */
    public void loadProject(String projectName) {
        try {
            dc.load(projectName);
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
    }

    /**
     * Des-selecciona el projecte actiu.
     */
    public void unSelectProject(){
        dc.unSelectProject();
    }

    /**
     * Esborra un projecte.
     * @param projectName nom del projecte
     */
    public void deleteProject(String projectName){
        try {
            dc.deleteProject(projectName);
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
    }

    /**
     * Mostra un file chooser per a que l'usuari esculli un arxiu zip per importar.
     * @return el path absolut del arxiu escollit
     */
    public String showFileChooser() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sel·lecciona un zip");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("ZIP", "*.zip"));
        File selected = fc.showOpenDialog(stage);
        return selected.getAbsolutePath();
    }

    /**
     * Importa un projecte a partir d'un zip com el de la DBLP.
     * @param zipFile path absolut del zip
     * @param projectName nom del projecte
     */
    public void importProject(String zipFile, String projectName) {
        try {
            System.out.println(zipFile);
            dc.importProject(zipFile, projectName);
        } catch (DomainException e) {
            handleException(e.getFriendlyMessage());
        }
    }

    /**
     * Afegeix un node al graf.
     * @param label nom del node
     * @param type tipus del node
     * @return id assignada al node
     */
    public Integer addNode(String label, String type){
        try{
            dc.addNode(label, type);
            ArrayList<Integer> ids = dc.getNodes(label, type);
            return getBigger(ids);
        }catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }

        return null;
    }

    /**
     * Elimina un node del graf.
     * @param id id del node
     * @param type tipus del node
     * @return cert si i nomes si s'ha eliminat el node
     */
    public boolean removeNode(int id, String type){
        try{
            dc.removeNode(id, type);
            return true;
        }catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
        return false;
    }

    /**
     * Afegeix una aresta al graf entre el node A i el B
     * @param idA id del node A
     * @param typeA tipus del node A
     * @param idB id del node B
     * @param typeB nom
     * @return cert si l'aresta s'ha afegit
     */
    public boolean addEdge(int idA, String typeA, int idB, String typeB){
        try {
            dc.addEdge(idA, typeA, idB, typeB);
            return true;
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }

        return false;
    }

    /**
     * Esborra una aresta al graf entre el node A i el B
     * @param idA id del node A
     * @param typeA tipus del node A
     * @param idB id del node B
     * @param typeB nom
     * @return cert si l'aresta s'ha esborrat
     */
    public boolean removeEdge(int idA, String typeA, int idB, String typeB){
        try {
            dc.removeEdge(idA, typeA, idB, typeB);
            return true;
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }

        return false;
    }

    /**
     * Dona tots els nodes del graf, en format portable.
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, el nom i en la tercera el numero de veins.
     * En la quarta, el tipus.
     */
    public ArrayList<String[]> getNodes() {
        return dc.getNodes();
    }

    /**
     * Retorna els veins d'un determinat tipus d'un node donat.
     * @param id id del node
     * @param type tipus del node
     * @param typeEnd tipus de les arestes
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, el nom i en la tercera el numero de veins.
     */
    public ArrayList<String[]> getNeighbours(String id, String type, String typeEnd){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getNeighbours(Integer.parseInt(id), type, typeEnd);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
        return res;
    }

    /**
     * Retorna les arestes d'un node.
     * @param id id del node
     * @param type tipus del node
     * @param typeEnd tipus de les arestes
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id del node i en la segona, la id del vei
     */
    public ArrayList<String[]> getNeighbourEdges(String id, String type, String typeEnd){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getNeighbourEdges(Integer.parseInt(id), type, typeEnd);
        } catch (DomainException de) {
            System.err.println(de.getFriendlyMessage());
            we.executeScript("app.events.showInfo(\"Eps!\",\""+de.getFriendlyMessage()+"\", \"Cap problema\")");
        }
        return res;
    }

    /**
     * Retorna tots el nodes d'un tipus donat.
     * @param type el tipus
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, el nom i en la tercera el numero de veins.
     */
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

    /**
     * Obte els <em>quantity</em> nodes del tipus <em>type</em> que mes veins.
     * @param type tipus dels nodes
     * @param quantity quantitat maxima que es retorna
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, el nom i en la tercera el numero de veins.
     */
    public ArrayList<String[]> getRelevantNodesOfType(String type, int quantity){
        ArrayList<String[]> res = getNodesOfType(type);
        res.sort( (a, b) -> Integer.parseInt(b[2])-Integer.parseInt(a[2]) );
        res = new ArrayList<>(res.subList(0, Math.min(res.size(), quantity)));
        return res;
    }

    /**
     * Obte les arestes d'un determinat tipus
     * @param type tipus de les arestes
     * @return Retorna un ArrayList de parelles de String. En la primera posicio
     * hi ha la id, en la segona, la id del vei.
     */
    public ArrayList<String[]> getEdgesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getEdges(type);
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
        return res;

    }

    /**
     * Retorna el tamany del resultat a la consulta de veins.
     * @param id id del node
     * @param type tipus del node
     * @return numero de veins
     */
    public int queryNeighboursSize(String id, String type){
        int res = 0;
        try{
            dc.queryNeighbours(Integer.parseInt(id), type);
            res = dc.getResultSize();
        } catch (DomainException de) {
            handleException(de.getFriendlyMessage());
        }
        return res;
    }

    /**
     * Fa una consulta per tipus del tipus <em>type</em>.
     * @param type tipus de la consulta
     * @return resultat
     */
    public ArrayList<ArrayList<String>> queryByType(String type) {
        try {
            currentNumCols = 2;
            dc.queryByType(type);
            return basicResult();
        } catch (DomainException e) {
            handleException(e.getFriendlyMessage());
        }
        return null;
    }

    /**
     * Fa una consulta de veins del node donat.
     * @param id id del node
     * @param type tipus del node
     * @return resultat
     */
    public ArrayList<ArrayList<String>> queryNeighbours(String id, String type) {
        try {
            currentNumCols = 2;
            dc.queryNeighbours(Integer.parseInt(id), type);
            return basicResult();
        } catch (DomainException e) {
            handleException(e.getFriendlyMessage());
        }
        return null;
    }

    /**
     * Consulta dels veins d'un node de tipus <em>typeEnd</em>.
     * @param id id del node
     * @param type tipus del node
     * @param typeEnd tipus dels veins a consultar
     * @return resultat
     */
    public ArrayList<ArrayList<String>> queryNeighboursOfType(String id, String type, String typeEnd) {
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

    /**
     * Fa una consulta 1 a 1 entre els dos nodes donats, en un thread separat.
     * @param idSource id font
     * @param typeSource tipus font
     * @param idEnd id desti
     * @param typeEnd tipus desti
     */
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

    /**
     * Fa una consulta 1 a N entre el node i el tipus donats, en un thread separat.
     * @param idSource id font
     * @param typeSource tipus font
     * @param typeEnd tipus desti
     */
    public void query1toN(String idSource, String typeSource, String typeEnd) {
        currentNumCols = 3;
        result = new ArrayList<>();
        QueryTask task = new Query1toNTask(idSource, typeSource, typeEnd, dc, we, this, MAX_ROWS);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    /**
     * Fa una consulta N a N entre el node i el tipus donats, en un thread separat.
     * @param typeSource tipus font
     * @param typeEnd tipus desti
     */
    public void queryNtoN(String typeSource, String typeEnd) {
        currentNumCols = 4;
        result = new ArrayList<>();
        QueryTask task = new QueryNtoNTask(typeSource, typeEnd, dc, we, this, MAX_ROWS);
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Fa una consulta per referencia entre la parella de nodes i el tercer node.
     * @param nodeRefSourceID id node referencia font
     * @param nodeRefSourceType tipus node referencia font
     * @param nodeRefEndID id node referencia desti
     * @param nodeRefEndType tipus node referencia desti
     * @param nodeSourceID id node font
     * @param nodeSourceType tipus node font
     */
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

    /**
     * Ordena el resultat segons la columna col, ascendenment si dir es cert
     * @param col columna de la taula
     * @param dir ascendent?
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> sortResult(int col, int dir) {
        if (currentNumCols == 3) col = col - 1;
        if (col == 0) dc.resetResult();
        else dc.sortResultByRow(col, dir);
        return resultDetails();
    }

    /**
     * Selecciona les files del resultat que contenen el nom <em>name</em>
     * @param name nom seleccionat
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> selectResultName(String name) {
        dc.selectResultName(name);
        return resultDetails();
    }

    /**
     * Desfa el filtre de l'operacio anterior.
     * @param name nom seleccionat
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> unselectResultName(String name) {
        dc.unselectResultName(name);
        return resultDetails();
    }

    /**
     * Amaga les files del resultat que contenen el nom <em>name</em>
     * @param name nom amagat
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> hideResultName(String name) {
        dc.hideResultName(name);
        return resultDetails();
    }

    /**
     * Desfa el filtre de l'operacio anterior.
     * @param name nom amagat
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> unhideResultName(String name) {
        dc.unhideResultName(name);
        return resultDetails();
    }

    /**
     * Amaga la fila del resultat amb numero de fila <em>index</em>.
     * @param index fila amagada
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> hideResultRow(String index) {
        dc.hideResultRow(Integer.parseInt(index)-1);
        return resultDetails();
    }

    /**
     * Desmaga la fila del resultat amb numero de fila <em>index</em>.
     * @param index fila amagada
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> unhideResultRow(String index) {
        dc.unhideResultRow(Integer.parseInt(index)-1);
        return resultDetails();
    }

    /**
     * Amaga el rang de files donat per <em>indexs</em>.
     * @param indexs string de la forma n-m, on n es un natural i m un altre
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> hideResultRows(String indexs) {
        String[] params = indexs.split("\\-");
        dc.hideResultRows(Integer.parseInt(params[0].trim())-1, Integer.parseInt(params[1].trim())-1);
        return resultDetails();
    }

    /**
     * Desamaga el rang de files donat per <em>indexs</em>.
     * @param indexs string de la forma n-m, on n es un natural i m un altre
     * @return les noves files del resultat per representar
     */
    public ArrayList<ArrayList<String>> unhideResultRows(String indexs) {
        String[] params = indexs.split("\\-");
        dc.unhideResultRows(Integer.parseInt(params[0].trim())-1, Integer.parseInt(params[1].trim())-1);
        return resultDetails();
    }

    public void log(String msg){
        System.out.println(msg);
    }
}

/*
 * Classes que calculen les queries en un thread diferent. La funcio call()
 * crida en cada cas a la consulta corresponent amb els paràmetres que de la classe.
 * La funcio done() recull el resultat i el posa al controlador amb un runLater.
 */
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

        } catch (ExecutionException | InterruptedException e) {
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
