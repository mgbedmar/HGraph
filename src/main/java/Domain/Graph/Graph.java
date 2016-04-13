package main.java;
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
		
	}
	
	public Node getNode(String name, String type) {
		
	}
	
	public Set<Node> getNeighbors(Node node) {
		
	}
	
	public Set<Node> getNeighbors(Node node, String type) {
		
	} 
	
	public int afegirNode(String name, String type ) {
		
	} 
	
	public void afegirNode(int id, String name, String type) {
		
	}
	
	public boolean afegirAresta(int id1, String type1, int id2, String type2) {
		
	}
	
	public boolean afegirAresta(String name1, String type1, String name2, String type2) {
		
	}
	
	public boolean esborrarNode(int id, String type) {
		
	}
	
	public boolean esborrarNode(String name, String type) {
		
	}
	
	public boolean esborrarAresta(Node node1, Node node2) {
		
	}
	
	
	

}
