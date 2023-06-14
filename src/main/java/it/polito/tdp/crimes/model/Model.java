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
	
	private List<String> migliore;
	private int peso;
	
	public Model() {
		this.dao=new EventsDao();
	}
	
	public void creaGrafo(String categoria, Integer anno) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.eventi=new ArrayList<>(dao.getEventi(categoria, anno));
		this.vertici=new ArrayList<>();
		for(Event e : eventi) {
			if(!vertici.contains(e.getOffense_type_id())) {
				vertici.add(e.getOffense_type_id());
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
	
	public List<String> percorso(String partenza, String arrivo) {
		this.migliore=new ArrayList<>();
		this.peso=Integer.MAX_VALUE;
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		ricorsione( parziale, arrivo);
		return migliore;
	}

	private void ricorsione(List<String> parziale, String arrivo) {
		if(parziale.get(parziale.size()-1).compareTo(arrivo)==0) {
			if(parziale.size()==this.vertici.size() && calcolaPeso(parziale)<this.peso) {
					migliore=new ArrayList<>(parziale);
					this.peso=calcolaPeso(parziale);
			}
			return;
		}
		for(String s : this.vertici) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				ricorsione(parziale, arrivo);
				parziale.remove(s);
			}
		}
	}

	private int calcolaPeso(List<String> parziale) {
		int p = 0;
		for(int i=0;i<parziale.size()-1;i++) {
			for(Coppia c : this.archi) {
				if(c.getE1().compareTo(parziale.get(i))==0 && c.getE2().compareTo(parziale.get(i+1))==0) {
					p+=c.getN();
				}
			}
		}
		return p;
	}
}
