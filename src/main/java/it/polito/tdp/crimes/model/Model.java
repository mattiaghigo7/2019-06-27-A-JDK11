package it.polito.tdp.crimes.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	
	private Graph<String,DefaultWeightedEdge> grafo;
	private List<Event> eventi;
	private List<String> vertici;
	private List<Coppia> archi;
	
	public Model() {
		this.dao=new EventsDao();
	}
	
	public void creaGrafo(String categoria, Integer anno) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.eventi=new ArrayList<>(dao.getEventi(categoria, anno));
		this.vertici=new ArrayList<>();
		for(Event e : eventi) {
			if(!vertici.contains(e.getOffense_type_id())) {
				vertici.add(e.getOffense_category_id());
			}
		}
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		this.archi=new ArrayList<>(dao.getCoppie(categoria, anno));
		for(Coppia c : archi) {
			Graphs.addEdgeWithVertices(this.grafo, c.getE1(), c.getE2(), c.getN());
		}
	}
	
	public List<Coppia> getArchiMax(){
		List<Coppia> l = new ArrayList<>();
		int grado = this.gradoMassimo();
		for(Coppia c : archi) {
			if(c.getN()==grado) {
				l.add(c);
			}
		}
		return l;
	}
	
	private int gradoMassimo() {
		int grado = 0;
		for(DefaultWeightedEdge d : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(d)>grado) {
				grado=(int) grafo.getEdgeWeight(d);
			}
		}
		return grado;
	}
	
	public List<String> percorso(String partenza, String arrivo) {
	DijkstraShortestPath<String, DefaultWeightedEdge> sp = new DijkstraShortestPath<>(grafo);
	GraphPath<String,DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo);
	return gp.getVertexList();
}
	
	public List<String> getCategorie(){
		return dao.allCategorie();
	}
	
	public List<Integer> getAnni(){
		return dao.allAnni();
	}
	
	public int getVerticiSize() {
		return this.grafo.vertexSet().size();
	}
	
	public int getArchiSize() {
		return this.grafo.edgeSet().size();
	}
}
