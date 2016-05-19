package Presentation;


import Domain.DomainController;

public class PresentationController {
    DomainController dc;

    public PresentationController(){
        dc = new DomainController();
    }

    public String[] getProjects(){
        return dc.getProjectList();
    }
}
