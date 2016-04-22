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
    private void eraseAllEdges(){
    	Iterator itr;
    	Node n=null;
    	for(int i=0;i<misnodos.size();i++){
    		itr=misnodos.get(i).getNeighbours().iterator();
    		n=misnodos.get(i);
    		if (n.getType().equals(Config.paperType)){
    			itr=misnodos.get(i).getNeighbours(Config.authorType).iterator();
    			while(itr.hasNext()){
    				try{
    				misnodos.get(i).removeEdge((Node)itr.next());
    				}catch(Exception ex){
    					ex.printStackTrace();
    					
    				}
    			}
    			itr=misnodos.get(i).getNeighbours(Config.confType).iterator();
        		while(itr.hasNext()){
        			try{
        			misnodos.get(i).removeEdge((Node)itr.next());
        			}catch(Exception ex){
        				ex.printStackTrace();
        					
        			}
        			
        		}
        		itr=misnodos.get(i).getNeighbours(Config.termType).iterator();
        		while(itr.hasNext()){
        			try{
        			misnodos.get(i).removeEdge((Node)itr.next());
        			}catch(Exception ex){
        				ex.printStackTrace();
        					
        			}
        			
        		}
    				
    			}
    		
    		else{
    		while(itr.hasNext())
    		    
    			try{
    			misnodos.get(i).removeEdge((Node)itr.next());
    			}catch(Exception ex){
    				
    			}
    		}
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
    private void addallEdges(){
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
    	int j=1;
    	Node n;
    	switch(type){
    		 
    	case "author":
    		System.out.println("Autores");
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println(j+" "+n.getID());
        		System.out.println(n.getName());
        		System.out.println(n.getType());
        		System.out.println("\n");
        		j++;
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
      Iterator itr=misnodos.get(i).getNeighbours(type).iterator();
      Node n=null;
      if (itr.hasNext())
    
      for(int j=1;j<=i;j++){
       n=(Node)itr.next();
      }
      return n;
    }
    private Node getNeighbour(int i,int id){
        Iterator itr=misnodos.get(i).getNeighbours().iterator();
        Node n=null;
        if (itr.hasNext())
      
        for(int j=1;j<=i;j++){
         n=(Node)itr.next();
        }
        return n;
      }
    private void ShowNeighbours(int i){
    	Iterator<Node> itr = misnodos.get(i).getNeighbours().iterator();
    	System.out.println("Su lista de adyacencias es ");
    	Node n=null;
    	int j=1;
    	while(itr.hasNext()){
    		n=(Node)itr.next();
    		System.out.println(j+" "+n.getID());
    		System.out.println(n.getName());
    		System.out.println("\n");
    		i++;
    	}
        System.out.println("No tiene mas adyacentes");    
    	}
  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     TestNode mitest=new TestNode();
     Node n=null;
     Scanner miscan=new Scanner(System.in);
     int option=0;
     int id,tipus,source,desti,aresta;
     String nombre;
     
     while(option!=8){
    	 System.out.println("Tria una opcio:\n1 Crear node\n2Afegir aresta\n3Esborrar aresta \n4 Mostrar info tots els nodes creats \n5 Esborrar totes les arestes,\n6 Afegir totes les arestes\n7 Consultar adjiacencies\n8Sortir\n");
    	 option=miscan.nextInt();
    	 
    	 switch(option){
    	 case 1:
    		 System.out.println("Introduceix una id ");
    		 id=miscan.nextInt();
    		 System.out.println("Introdueix un nom ");
    	     nombre=miscan.nextLine();
    	     System.out.println("Tria un tipus\n 1 Paper\n 2 Author\n 3 Terme \n4Conferencia\n5 Fantasma");
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
    	     System.out.println("");
    	   break;
    	 case 2:
    		 System.out.println("Tria el node source per afegir l'aresta");
    		 mitest.listNode();
    		 source=miscan.nextInt();
    		 System.out.println("Tria el node desti per afegir l'aresta");
    		 desti=miscan.nextInt();
    		 mitest.addEdge(source, desti);
    		 System.out.println("S'ha afegit l'aresta que va de "+mitest.getNode(source).getID()+" "+mitest.getNode(source).getName()+" "+mitest.getNode(source).getType() +" al node "+mitest.getNode(desti).getID()+""+mitest.getNode(desti).getName()+" "+mitest.getNode(desti).getType());
    		break;
    	 case 3:
    		 System.out.println("Tria el node source per esborrar l'aresta");
    		 mitest.listNode();
    		 source=miscan.nextInt();
    		 if(mitest.getNode(source).getType().equals(Config.paperType)){
    			 System.out.println("tria el tipus d'aresta que vols eliminar \n 1 Author \n 2 Conferència \n 3 Terme ");
    		     aresta=miscan.nextInt();
    		     switch(aresta){
    		     case 1:
    		    	 mitest.ShowNeighbours(source,Config.authorType);
    		    	 desti=miscan.nextInt();
    		    	 mitest.getNeighbour(desti, source, Config.authorType);
    		    	 n=mitest.getNeighbour(desti, source, Config.authorType);
    		    	 try{
    		    	 mitest.getNode(source).removeEdge(n);
    		    	 }catch(Exception ex){
    		    		 
    		    	 }
    		    	 break;
    		    	 
    		     case 2:
    		    	 mitest.ShowNeighbours(source,Config.confType);
    		    	 desti=miscan.nextInt();
    		    	 try{
    		    		 n=mitest.getNeighbour(desti, source, Config.termType);
        		    	 mitest.getNode(source).removeEdge(n);
    		    	 
    		    	 }catch(Exception ex){
    		    		 
    		    	 }
    		    	 break;
    		     case 3:
    		    	 mitest.ShowNeighbours(source,Config.termType);
    		    	 desti=miscan.nextInt();
    		    	 mitest.getNeighbour(desti, source, Config.termType);
    		    	 try{
    		    		 n=mitest.getNeighbour(desti, source, Config.termType);
        		    	 mitest.getNode(source).removeEdge(n);
        		    	 }catch(Exception ex){
        		    		 
        		    	 }
    		    	 break;

    		     }
    		 
    		 System.out.println("S'ha esborrat l'aresta "+mitest.getNode(source)+" "+n.getID()+" "+n.getName()+" "+n.getType());
    		 }
    		 else{
    			 
    		 
    		 System.out.println("Tria el node desti per esborrar l'aresta");
    		 mitest.ShowNeighbours(source);
    		 desti=miscan.nextInt();
    		 n=mitest.getNeighbour(source,desti);
    		 mitest.removeEdge(source, desti);
    		 }
    		 break;
    		 case 4:
    		 mitest.ShowContent();
    		 break;
    	     case 5:
    	    	 mitest.eraseAllEdges();
    	    	 System.out.println("Totes les llistes d'adjiacències estan buides");
    	    	 
    		 break;
    	     case 6:
    	    	 mitest.addallEdges();
    	    	 break;
    	     case 7:
    	    	 System.out.println("Tria el node source per consultar la llista d'adjacències");
        		 mitest.listNode();
        		 source=miscan.nextInt();
        		 if(mitest.getNode(source).getType().equals(Config.paperType)){
        			 System.out.println("tria el tipus de la llista d'adjacències \n 1 Author \n 2 Conferència \n 3 Terme ");
        		     aresta=miscan.nextInt();
        		     switch(aresta){
        		     case 1:
        		    	 mitest.ShowNeighbours(source,Config.authorType);
        		    	 
        		    	 break;
        		    	 
        		     case 2:
        		    	 mitest.ShowNeighbours(source,Config.confType);
        		    	 
        		    	 break;
        		     case 3:
        		    	 mitest.ShowNeighbours(source,Config.termType);
        		    	 
        		    	 break;

        		     }
    	    	 
    		    break;
    	 }
    	 }
    	 
	}
System.out.println("Test terminado ");
}
}
