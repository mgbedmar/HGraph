package Domain.Graph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Dani
 *
 */
public class Graph {
	private HashMap<String,HashSet<? extends Node>> elements;
	
	
	
	
	//Constructora del Graf
	public Graph () {
		elements = new HashMap<String,HashSet<? extends Node>>();
		elements.put("Autor", (HashSet<Author>) new HashSet<Author>() );
		elements.put("Paper", (HashSet<Paper>) new HashSet<Paper>());
		elements.put("Conferencia",(HashSet<Conf>) new HashSet<Conf>());
		elements.put("Terme",(HashSet<Term>) new HashSet<Term>());		
	}
	
	public HashSet<Node> getSetOfNodes() {
		HashSet<Node> res = new HashSet<Node>();
		res.addAll(elements.get("Autor"));
		res.addAll(elements.get("Paper"));
		res.addAll(elements.get("Conferencia"));
		res.addAll(elements.get("Terme"));	
		
		return res;		
	}
	/**
	 * 
	 * @param tipus: tipus de Node
	 * @return: Tots els nodes del graf del tipus dessitjat. Null si el tipus no existeix
	 */
	
	public HashSet<? extends Node> getSetOfNodes(String tipus) {		
		String clauConsulta = "ERROR";
		switch (tipus) {
		
		case "author":
			clauConsulta = "Autor";
			break;
		case "paper":
			clauConsulta = "Paper";
			break;
		case "conf":
			clauConsulta = "Conferencia";
			break;
		case "term":
			clauConsulta = "Terme";
			break;
			
		default:
					
		}
		if (clauConsulta == "ERROR") {
			System.err.print("No s'ha insertat un tipus correcte de Node");
			return null;
		}
		else {
			return elements.get(clauConsulta);			
		}
		
	}
	/**
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	
	public Node getNode(int id, String type) {
		String clauConsulta = "ERROR";
		switch (type) {
		
		case "author":
			clauConsulta = "Autor";
			break;
		case "paper":
			clauConsulta = "Paper";
			break;
		case "conferencia":
			clauConsulta = "Conferencia";
			break;
		case "terme":
			clauConsulta = "Terme";
			break;
			
		default:
					
		}
		if (clauConsulta == "ERROR") {
			System.err.print("No s'ha insertat un tipus correcte de Node");			
		}
		else {
			for (Node nod : elements.get(clauConsulta)) {
				if (nod.getID() == id) return nod;				
			}			
		}
	    return null;
	}
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public Node getNode(String name, String type) {
		return null;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	
	public Set<Node> getNeighbours(Node node) {
		return null;
	}
	
	/**
	 * 
	 * @param node
	 * @param type
	 * @return
	 */
	
	public Set<Node> getNeighbours(Node node, String type) {
		return null;
	} 
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	
	public int afegirNode(String name, String type ) {
		return 0;
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 */
	
	public void afegirNode(int id, String name, String type) {
		Node ele = new Node(id,name)
		
	}
	
	/**
	 * 
	 * @param id1
	 * @param type1
	 * @param id2
	 * @param type2
	 * @return
	 */
	
	public boolean afegirAresta(int id1, String type1, int id2, String type2) {
		return true;
	}
	
	/**
	 * 
	 * @param name1
	 * @param type1
	 * @param name2
	 * @param type2
	 * @return
	 */
	
	public boolean afegirAresta(String name1, String type1, String name2, String type2) {
		return true;
	}
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	public boolean esborrarNode(int id, String type) {
		return true;
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public boolean esborrarNode(String name, String type) {
		return true;
	}
	
	/**
	 * 
	 * @param node1
	 * @param node2
	 * @return
	 */
	
	public boolean esborrarAresta(Node node1, Node node2) {
		return true;
	}
	
	
	

}