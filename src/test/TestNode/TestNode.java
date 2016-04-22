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
    
    private void removeNode(Node n){
    	int i=0;
    	while (!n.equals(misnodos.get(i))){
    		i++;
    	}
    	misnodos.remove(i);
    }
    private void reinitTest(){
    	for(int i=0;i<=misnodos.size();i++){
    		misnodos.remove(misnodos.get(i));
    	}
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
    	Iterator<Node> itr;
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
    		System.out.println("Tenim el següent node :" +misnodos.get(i).getID()+"del tipo "+misnodos.get(i).getType()+"y su contenido es "+misnodos.get(i).getName());
    		if(!misnodos.get(i).getType().equals(Config.paperType))
    		this.ShowNeighbours(i);
    		else{
    			System.out.println("Su lista de adyacencias por tipos es ");
    			
    			this.ShowNeighbours(i,Config.authorType);
    			this.ShowNeighbours(i,Config.confType);
    			this.ShowNeighbours(i,Config.termType);
    		}
    		}
    }
    private void listNode(){
    	System.out.println("Id interno \t IdClase\tContingut\t Tipus");
    	for(int i=0;i<misnodos.size();i++){
    		System.out.println(+i+"\t \t"+misnodos.get(i).getID()+"\t "+misnodos.get(i).getName()+" "+misnodos.get(i).getType());
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
    	
    	int j=1;
    	Node n;
    	
    	switch(type){
    		 
    	case Config.authorType:
    		System.out.println("Autores");
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println("Id intern \t IdClase\tContingut");
    			System.out.print(j+"\t\t \t"+n.getID()+"\t ");
        		System.out.print(n.getName()+"\t ");
        		System.out.print(n.getType()+"\t ");
        		System.out.print("\n");
        		j++;
    		}
    		break;
    	case Config.confType:
    		System.out.println("Conferencias");
    		
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println("Id intern \t IdClase\tContingut");
    			System.out.print(i+"\t \t\t"+n.getID()+" ");
        		System.out.print(n.getName()+"\t ");
        		System.out.print(n.getType()+"\t ");
        		System.out.print("\n");
        		i++;
    		}
    		break;
    	case Config.termType:
    		System.out.println("Termes");
    		while(itr.hasNext()){
    			n=itr.next();
    			System.out.println("Id intern \t IdClase\tContingut\t ");
    			System.out.print(i+"\t \t\t"+n.getID());
        		System.out.print(n.getName()+"\t ");
        		System.out.print(n.getType()+"\t ");
        		System.out.print("\n");
        		i++;
    		}
    		break;
    		default: break;
    	
    	}
    	System.out.println("No té més adjacents");
    }
    private Node getNeighbour(int i,int id,String type){
      Iterator<Node> itr=misnodos.get(i).getNeighbours(type).iterator();
      Node n=null;
      if (itr.hasNext())
    
      for(int j=1;j<=i;j++){
       n=(Node)itr.next();
      }
      return n;
    }
    private Node getNeighbour(int i,int id){
    	 Node n=null;
    	Iterator<Node> itr=misnodos.get(i).getNeighbours().iterator();
       
        if (itr.hasNext())
      
        for(int j=1;j<=i;j++){
         n=(Node)itr.next();
        }
        return n;
      }
    private void ShowNeighbours(int i){
    	Iterator<Node> itr = misnodos.get(i).getNeighbours().iterator();
    	System.out.println("Su lista de adyacencias es ");
    	System.out.println("Id interno \t IdClase\tContenido");
    	Node n=null;
    	int j=0;
    	while(itr.hasNext()){
    		n=(Node)itr.next();
    		System.out.print(j+"\t "+n.getID()+"");
    		System.out.print("\t"+n.getName()+"\t ");
    		System.out.print(""+n.getType());
    		System.out.print("\n");
    		j++;
    	}
        System.out.println("No tiene mas adyacentes");    
    	}
  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     TestNode mitest=new TestNode();
     Node n=null;
     Scanner miscan=new Scanner(System.in);
     int option=0;
     int id,tipus,source,desti,aresta,esborrar;
     String nombre;
     
     while(option!=10){
    	 System.out.println("Tria una opcio:\n1 Crear node\n2Afegir aresta\n3Esborrar aresta \n4 Mostrar info tots els nodes creats \n5 Esborrar totes les arestes,\n6 Afegir totes les arestes\n7 Consultar adjiacencies\n8 EsborrarNode\n9Esborrar tots els nodes\n10Sortir\n");
    	 option=miscan.nextInt();
    	 
    	 switch(option){
    	 case 1:
    		 System.out.println("Introduceix una id ");
    		 id=miscan.nextInt();
    		 miscan.nextLine();
    		 System.out.println("Introdueix un nom ");
    	     nombre=miscan.nextLine();
    	     System.out.println("Tria un tipus\n 1 Paper\n 2 Author\n 3 Terme \n4Conferencia\n5 Fantasma");
    	     tipus=miscan.nextInt();
    	     switch(tipus){
    	     case 1:
    	    	 n=new Paper(id,nombre);
    	    	 mitest.afegirNode(n);
    	    	 break;
    	     case 2:
    	    	 n=new Author(id,nombre);
    	    	 mitest.afegirNode(n);
    	         break;
    	     case 3:
    	    	 n=new Term(id,nombre);
    	    	 mitest.afegirNode(n);
    	    	 break;
    	     case 4:
    	    	 n=new Conf(id,nombre);
    	    	 mitest.afegirNode(n);
    	    	 break;
    	     case 5:
    	    	 n=new Ghost(id);
    	    	 mitest.afegirNode(n);
    	    	 break;
    	    default:
    	    	break;
    	     
    	     
    	     }
    	     
    	     System.out.println("S'ha creat el node "+n.getType()+" "+n.getID()+" "+n.getName()+"\n");
    	   break;
    	 case 2:
    		 System.out.println("Tria el node source per afegir l'aresta indicant el seu id intern");
    		 mitest.listNode();
    		 source=miscan.nextInt();
    		 System.out.println("Tria el node desti per afegir l'aresta indicant el seu id intern");
    		 mitest.listNode();
    		 desti=miscan.nextInt();
    		 mitest.addEdge(source, desti);
    		 System.out.println("S'ha afegit l'aresta que va de "+mitest.getNode(source).getID()+" "+mitest.getNode(source).getName()+" "+mitest.getNode(source).getType() +" al node "+mitest.getNode(desti).getID()+""+mitest.getNode(desti).getName()+" "+mitest.getNode(desti).getType());
    		break;
    	 case 3:
    		 System.out.println("Tria el node source per esborrar l'aresta indicant el seu id intern");
    		 mitest.listNode();
    		 source=miscan.nextInt();
    		 if(mitest.getNode(source).getType().equals(Config.paperType)){
    			 System.out.println("tria el tipus d'aresta que vols eliminar \n 1 Author \n 2 Conferència \n 3 Terme ");
    		     aresta=miscan.nextInt();
    		     switch(aresta){
    		     case 1:
    		    	 mitest.ShowNeighbours(source,Config.authorType);
    		    	 desti=miscan.nextInt();
    		    	 //mitest.getNeighbour(desti, source, Config.authorType);
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
    		    	 //mitest.getNeighbour(desti, source, Config.termType);
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
    			 
    		 
    		 System.out.println("Tria el node desti per esborrar l'aresta indicant el seu id intern");
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
    	    	 System.out.println("Tria el node source per consultar la llista d'adjacències indicant el seu id intern");
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
    	     case 8: System.out.println("Tria quin node vols esborrar indicant el seu id intern\n");
    	             mitest.listNode();
    	             esborrar=miscan.nextInt();
    	             n=mitest.getNode(esborrar);
    	             System.out.println("Se ha borrado el nodo "+n.getID()+" "+n.getName()+"del tipus "+n.getType());
    	             mitest.removeNode(n);
    	             break;
    	     case 9: System.out.println("Se han destruido todos los nodos");
    	             mitest.reinitTest();
    	 }
    		 
	}
System.out.println("Test terminado ");
miscan.close();
}
}
