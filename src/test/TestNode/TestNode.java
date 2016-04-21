package TestNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import TestNode.Term;
import TestNode.Paper;
import TestNode.Node;
import TestNode.Author;
import TestNode.Conf;
import TestNode.Ghost;
import TestNode.DomainException;
public class TestNode {
	private static ArrayList<Node> misnodos;
	public TestNode(){
		misnodos=new ArrayList<Node>();
	}
    public TestNode(ArrayList<Node> milista){
    	misnodos=milista;
    }
    private Node getNode(int i){
    	return misnodos.get(i);
    }
    public void afegirNode(Node node){
        misnodos.add(node);	
    }
    public void borrarNode(Node node){
    	misnodos.remove(node);
    }
    private void eraseEdge(int i, int j){
    	
    	try {
			misnodos.get(i).removeEdge(misnodos.get(j));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
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
    private void listNode(){
    	for(int i=0;i<misnodos.size();i++){
    		System.out.println(+i+" "+misnodos.get(i).getID()+" "+misnodos.get(i).getName()+" "+misnodos.get(i).getType());
    	}
    }
    private void addEdge(int i,int j){
    	try {
			misnodos.get(i).addEdge(misnodos.get(j));
		} catch (Domain.DomainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    private void removeEdge(int i,int j){
    	try{
    	misnodos.get(i).removeEdge(misnodos.get(j));
    }catch(Exception ex){
    }
    }
    private void addallRelationships(){
    	for(int i=0;i<misnodos.size();i++){
    		for(int j=0;j<misnodos.size();j++){
    			System.out.println("Provant d'afegir"+misnodos.get(i).getID()+" "+misnodos.get(i).getName()+"de tipus"+misnodos.get(i).getType()+"amb el node"+misnodos.get(j).getID()+" "+misnodos.get(j).getName()+"de tipus"+ misnodos.get(j).getType());
    			try {
    			misnodos.get(i).addEdge(misnodos.get(j));
                
    			}catch(Exception ex){
    				System.out.println(ex);
    			}
    		}
    	}
    }
    private void EraseAllRelationShips(){
    	Iterator miiter;
        Node n;
    	for(int i=0;i<misnodos.size();i++){
    		miiter=misnodos.get(i).getNeighbours().iterator();
    		while(miiter.hasNext()){
    			n=(Node)miiter.next();
    			System.out.println("Provant d'esborrar"+misnodos.get(i).getID()+" "+misnodos.get(i).getName()+"de tipus"+misnodos.get(i).getType()+"amb el node"+n.getID()+" "+n.getName()+"de tipus"+ n.getType());
    			try{
    			misnodos.get(i).removeEdge(n);
    			}catch(Exception ex){
    				
    			}
    			}
    		}
    	
    }
    private void ShowNeighbours(int i,String type){
    	Iterator<Node> itr = misnodos.get(i).getNeighbours(type).iterator();
    	System.out.println("Su lista de adyacencias por tipos es ");
    	int i=1;
    	Node n;
    	switch(type){
    		 
    	case "author":
    		System.out.println("Autores");
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println(i+" "+n.getID());
        		System.out.println(n.getName());
        		System.out.println(n.getType());
        		System.out.println("\n");
        		i++;
    		}
    	case "conf":
    		System.out.println("Conferencias");
    		
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println(i+" "+n.getID());
        		System.out.println(n.getName());
        		System.out.println(n.getType());
        		System.out.println("\n");
        		i++;
    		}
    	case "term":
    		System.out.println("Termes");
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println(i+" "+n.getID());
        		System.out.println(n.getName());
        		System.out.println(n.getType());
        		System.out.println("\n");
        		i++;
    		}
    	
    	}
    	System.out.println("No tiene más adyacentes");
    }
    private Node getNeighbour(int i,int id,String type){
      Iterator itr=misnodos.get(i).getNeighbours(type).
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
     
     Scanner miscan=new Scanner(System.in);
     int option=0;
     int id,tipus,source,desti,aresta;
     String nombre;
     
     while(option!=7){
    	 System.out.println("Tria una opcio:1 Crear node\n2Afegir aresta\3Esborrar aresta 4 Mostrar info tots els nodes creats \n5 Esborrar totes les arestes,\n6 Afegir totes les arestes\n7 Sortir");
    	 option=miscan.nextInt();
    	 
    	 switch(option){
    	 case 1:
    		 System.out.println("Introduceix una id ");
    		 id=miscan.nextInt();
    		 System.out.println("Introdueix un nom ");
    	     nombre=miscan.nextLine();
    	     System.out.println("Tria un tipus 1 Paper\n 2 Author\n 3 Terme \n4Conferencia\5 Fantasma");
    	     tipus=miscan.nextInt();
    	     switch(tipus){
    	     case 1:
    	    	 mitest.afegirNode(new Paper(id,nombre));
    	    	 break;
    	     case 2:
    	    	 mitest.afegirNode(new Author(id,nombre));
    	         break;
    	     case 3:
    	    	 mitest.afegirNode(new Term(id,nombre));
    	    	 break;
    	     case 4:
    	    	 mitest.afegirNode(new Conf(id,nombre));
    	    	 break;
    	     case 5:
    	    	 mitest.afegirNode(new Ghost(id));
    	    	 break;
    	    default:
    	    	break;
    	     
    	     
    	     }
    	   
    	 case 2:
    		 System.out.println("Tria el node source per afegir l'aresta");
    		 mitest.listNode();
    		 source=miscan.nextInt();
    		 System.out.println("Tria el node desti per afegir l'aresta");
    		 desti=miscan.nextInt();
    		 mitest.addEdge(source, desti);
    		 System.out.println("S'ha afegit l'aresta que va de "+mitest.getNode(source).getID()+" "+mitest.getNode(source).getName()+" "+mitest.getNode(source).getType() +" al node "+mitest.getNode(desti).getID()+""+mitest.getNode(desti).getName()+" "+mitest.getNode(desti).getType());
    	 case 3:
    		 System.out.println("Tria el node source per esborrar l'aresta");
    		 mitest.listNode();
    		 source=miscan.nextInt();
    		 if(mitest.getNode(source).getType().equals(Config.paperType))
    			 System.out.println("tria el tipus d'aresta que vols eliminar \n 1 Author \n 2 Conferència \n 3 Terme ");
    		     aresta=miscan.nextInt();
    		     switch(aresta){
    		     case 1:
    		    	 mitest.ShowNeighbours(source,Config.authorType);
    		    	 desti=miscan.nextInt();
    		    	 break;
    		    	 
    		     case 2:
    		    	 mitest.ShowNeighbours(source,Config.confType);
    		    	 desti=miscan.nextInt();
    		    	 break;
    		     case 3:
    		    	 mitest.ShowNeighbours(source,Config.termType);
    		    	 desti=miscan.nextInt();
    		    	 break;

    		     }
    			 
    		 System.out.println("Tria el node desti per esborrar l'aresta");
    		 desti=miscan.nextInt();
    		 mitest.removeEdge(source, desti);
    	 case 4:
    		 mitest.ShowContent();
    	 case 5:
    		 
    	 case 6:
    	 }
     
    	 mitest.ShowContent();
    	 System.out.println("Añadiremos aristas entre ellos todos entre todos ");
    	 mitest.addallRelationships();
    	 mitest.ShowContent();
    	 
    	 System.out.println("Ara esborrarem arestes una rera l'altra");
	}

}
}
