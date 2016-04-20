package main.java.Domain.Graph;

import java.util.ArrayList;
import java.util.Iterator;

public class TestNode {
	ArrayList<Node> misnodos;
	public TestNode(){
		misnodos=new ArrayList<Node>();
	}
    public TestNode(ArrayList<Node> milista){
    	misnodos=milista;
    }
    public void afegirNode(Node node){
        misnodos.add(node);	
    }
    public void borrarNode(Node node){
    	misnodos.remove(node);
    }
    public void ShowContent(){
    	for(int i=0;i<misnodos.size();i++){
    		System.out.println("Tenemos el siguiente nodo :" +misnodos.get(i).getID()+"del tipo "+misnodos.get(i).getType()+"y su contenido es "+misnodos.get(i).getName());
    		if(!misnodos.get(i).getType().equals("paper"))
    		this.ShowNeighbours(i);
    		else{
    			
    			
    			this.ShowNeighbours(i,"author");
    			this.ShowNeighbours(i,"conf");
    			this.ShowNeighbours(i,"term");
    		}
    		}
    }
    private void addallRelationships(){
    	for(int i=0;i<misnodos.size();i++){
    		for(int j=0;j<misnodos.size();j++){
    			System.out.println("Provant d'afegir"+misnodos.get(i).getID()+" "+misnodos.get(i).getName()+"de tipus"+misnodos.get(i).getType()+"amb el node"+misnodos.get(j).getID()+" "+misnodos.get(j).getName()+"de tipus"+ misnodos.get(j).getType());
    			misnodos.get(i).addRelationship(misnodos.get(j));
    }
    		}
    	}
    private void EraseAllRelationShips(){
    	
    	for(int i=0;i<misnodos.size();i++){
    		for(int j=0;j<misnodos.size();j++){
    			System.out.println("Provant d'afegir"+misnodos.get(i).getID()+" "+misnodos.get(i).getName()+"de tipus"+misnodos.get(i).getType()+"amb el node"+misnodos.get(j).getID()+" "+misnodos.get(j).getName()+"de tipus"+ misnodos.get(j).getType());
    			misnodos.get(i).addRelationship(misnodos.get(j));
    }
    		}
    	
    }
    private void ShowNeighbours(int i,String type){
    	Iterator<Node> itr = misnodos.get(i).getNeighbours(type).iterator();
    	System.out.println("Su lista de adyacencias por tipos es ");
    	switch(type){
    		 
    	case "author":
    		System.out.println("Autores");
    		while(itr.hasNext()){
    			System.out.println(itr.next().getID());
        		System.out.println(itr.next().getName());
        		System.out.println(itr.next().getType());
        		System.out.println("\n");
    		}
    	case "conf":
    		System.out.println("Conferencias");
    		
    		while(itr.hasNext()){
    			System.out.println(itr.next().getID());
        		System.out.println(itr.next().getName());
        		System.out.println(itr.next().getType());
        		System.out.println("\n");
    		}
    	case "term":
    		System.out.println("Termes");
    		while(itr.hasNext()){
    			System.out.println(itr.next().getID());
        		System.out.println(itr.next().getName());
        		System.out.println(itr.next().getType());
        		System.out.println("\n");
    		}
    	
    	}
    	System.out.println("No tiene más adyacentes");
    }
    private void ShowNeighbours(int i){
    	Iterator<Node> itr = misnodos.get(i).getNeighbours().iterator();
    	System.out.println("Su lista de adyacencias es ");
    	while(itr.hasNext()){
    		System.out.println(itr.next().getID());
    		System.out.println(itr.next().getName());
    		System.out.println("\n");
    	}
        System.out.println("No tiene mas adyacentes");    
    	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     TestNode mitest=new TestNode();
     mitest.afegirNode(new Paper(1,"Applications of Markov chains"));
     mitest.afegirNode(new Author(2,"Davide Careglio"));
     mitest.afegirNode(new Term(3,"Statistics"));
     mitest.afegirNode(new Conf(4,"IT MEETING"));
     mitest.ShowContent();
     System.out.println("Añadiremos aristas entre ellos todos entre todos ");
     mitest.addallRelationships();
     mitest.ShowContent();
     System.out.println("Ara esborrarem arestes una rera l'altra");
	}

}
