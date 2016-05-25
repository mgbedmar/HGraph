package Presentation;


import Domain.DomainController;
import Domain.DomainException;

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

    public ArrayList<String[]> query1to1() {

        return null;
    }


    public void log(String msg){
        System.out.println(msg);
    }
}
