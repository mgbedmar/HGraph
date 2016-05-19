package Presentation;


import Domain.DomainController;
import Domain.DomainException;

public class PresentationController {
    DomainController dc;

    public PresentationController(){
        dc = new DomainController();
    }

    public String[] getProjects(){
        return dc.getProjectList();
    }

    public void loadProject(String projectName) {
        try {
            dc.load(projectName);
        } catch (DomainException de) {
            de.printStackTrace(); //TODO
        }
    }
}
