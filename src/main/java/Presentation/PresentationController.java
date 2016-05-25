package Presentation;


import Domain.DomainController;
import Domain.DomainException;
import Domain.IntermediateHeteSimMatrix;

import java.util.ArrayList;

public class PresentationController {
    private DomainController dc;
    private int MAX_FILES;

    public PresentationController(){
        dc = new DomainController();
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
            //TODO
        }
    }
    public void deleteProject(String projectName){
        try {
            dc.deleteProject(projectName);
        } catch (DomainException de) {
            //TODO
        }
    }

    public ArrayList<String[]> getNodesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getNodes(type);
        } catch (DomainException e) {
            //TODO
        }
        return res;
    }

    public ArrayList<String[]> getEdgesOfType(String type){
        ArrayList<String[]> res = new ArrayList<>();
        try{
            res = dc.getEdges(type);
        } catch (DomainException e) {
            //TODO
        }
        return res;
    }

    /* Retorna un String que representa el valor de HeteSim de la consulta */
    public String query1to1(String idSource, String typeSource,
                              String idEnd, String typeEnd) {
        try {
            /* Extreure les ids dels strings d'entrada */
            int id1, id2;
            String[] parts = idSource.split("-");
            id1 = Integer.parseInt(parts[0]);
            parts = idEnd.split("-");
            id2 = Integer.parseInt(parts[0]);
            dc.query1to1(id1, typeSource, id2, typeEnd);
            ArrayList<String> fila = dc.getResultRow();
            return fila.get(3);

        } catch(DomainException e) {
            //TODO
            e.printStackTrace();
        }

        return null;
    }


    public void log(String msg){
        System.out.println(msg);
    }
}
