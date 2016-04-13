package Domain.Graph;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph {
	private HashMap<String,HashSet<Node>> elements;
	
	
	
	
	//Constructora del Graf
	public Graph () {
		elements = new HashMap<String,HashSet<Node>>();
		elements.put("Autor", null);
		elements.put("Paper", null);
		elements.put("Conferencia",null);
		elements.put("Terme",null);		
	}
	
	public HashSet<Node> getSetOfNodes() {
		HashSet<Node> res = new HashSet<Node>();
		res.addAll(elements.get("Autor"));
		res.addAll(elements.get("Paper"));
		res.addAll(elements.get("Conferencia"));
		res.addAll(elements.get("Terme"));
		return res;		
	}
	
	public HashSet<Node> getSetOfNodes(String tipus) {
		HashSet res = new HashSet<Node>();
		switch (tipus) {
		case "author": case "paper": case "conferencia": case "terme":
			res = elements.get(tipus);
			break;
			
		default:
			System.err.print("No s'ha insertat un tipus correcte de Node");		
		}
		return res;
	}
	
	public Node getNode(int id, String type) {
	    return null;
	}
	
	public Node getNode(String name, String type) {
		return null;
	}
	
	public Set<Node> getNeighbours(Node node) {
		return null;
	}
	
	public Set<Node> getNeighbours(Node node, String type) {
		return null;
	} 
	
	public int afegirNode(String name, String type ) {
		return 0;
	} 
	
	public void afegirNode(int id, String name, String type) {
		
	}
	
	public boolean afegirAresta(int id1, String type1, int id2, String type2) {
		return true;
	}
	
	public boolean afegirAresta(String name1, String type1, String name2, String type2) {
		return true;
	}
	
	public boolean esborrarNode(int id, String type) {
		return true;
	}
	
	public boolean esborrarNode(String name, String type) {
		return true;
	}
	
	public boolean esborrarAresta(Node node1, Node node2) {
		return true;
	}
	
	
	

}
