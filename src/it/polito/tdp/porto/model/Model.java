package it.polito.tdp.porto.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private PortoDAO dao;
	
	private List<Author> autori;
	private AuthorIdMap autoremap;
	
	private List<Paper> pubblicazioni;
	private PaperIdMap pubblicazionemap;
	
	private Graph<Author, DefaultEdge> grafo;
	
	public Model() {
		
		dao = new PortoDAO();
		
		autoremap = new AuthorIdMap();
		autori = dao.getTuttiAutori(autoremap);
		
		pubblicazionemap  = new PaperIdMap();
		pubblicazioni = dao.getTuttePubblicazioni(pubblicazionemap);
		
		for (Author a : autori)
			dao.getPubblicazioniFromAutore(a, pubblicazionemap);
		
		for (Paper p : pubblicazioni)
			dao.getAutoriFromPubblicazione(p, autoremap);
		
	}
	
	public void creaGrafo() {
		
		grafo = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		
		// VERTICI: TUTTI GLI AUTORI
		
		Graphs.addAllVertices(grafo, autori);
		
		// ARCHI: PRESENTE SE DUE AUTORI SONO CO-AUTORI
		
		// Versione 1
		
//		for (Author au : autori) {
//			for (Paper p : au.getPubblicazioni()) {
//				for (Author co : p.getAutori()) {
//					if (! au.equals(co) && au.getId() < co.getId())
//						grafo.addEdge(au,co); 
//				}
//			}
//		}
		
		// Versione 2
		
		for (Author au : autori) {
			List<Author> coautori = dao.getCoAutori(au, autoremap); 
			for (Author co : coautori) {
				if (! au.equals(co))
					grafo.addEdge(au, co);
			}
		}
		
	}
	
	public List<Author> getVerticiAdiacenti(Author autore) {
		return Graphs.neighborListOf(grafo, autore);
				
	}

	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Author, DefaultEdge> grafo) {
		this.grafo = grafo;
	}

}
