package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {

	
	public List<Author> getTuttiAutori(AuthorIdMap autoremap) {
		
		String sql = "SELECT id, lastname, firstname  FROM author";
		
		List<Author> result = new ArrayList<Author>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(autoremap.get(autore));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db in getTuttiAutori");
		}
		
	}
	
	public List<Paper> getTuttePubblicazioni(PaperIdMap pubblicazionemap) {
		
		String sql = "SELECT * FROM paper";
		
		List<Paper> result = new ArrayList<Paper>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				result.add(pubblicazionemap.get(paper));
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db in getTuttePubblicazioni");
		}
		
	}
	
	public void getPubblicazioniFromAutore(Author autore, PaperIdMap pubblicazionemap) {
		
		String sql = "SELECT p.eprintid, title, issn, publication, type, types "
					+ "FROM paper AS p, creator AS c "
					+ "WHERE c.eprintid = p.eprintid AND authorid = ?";
				
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, autore.getId());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
							rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				
				autore.getPubblicazioni().add(pubblicazionemap.get(paper));
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db in getPubblicazioniFromAutore");
		}
		
	}
	
	public void getAutoriFromPubblicazione(Paper pubblicazione, AuthorIdMap autoremap) {

		String sql = "SELECT id, lastname, firstname FROM creator, author WHERE id = authorid AND eprintid = ?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, pubblicazione.getEprintid());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author aut = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				
				pubblicazione.getAutori().add(autoremap.get(aut));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db in getAutoriFromPubblicazione");
		}

	}
	
	public List<Author> getCoAutori(Author autore, AuthorIdMap autoremap) {
		
		String sql = "SELECT DISTINCT authorid, lastname, firstname FROM creator, author "
					+ "WHERE id = authorid AND eprintid IN (SELECT eprintid FROM creator WHERE authorid = ?)";
		
		List<Author> result = new ArrayList<Author>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, autore.getId());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author coautore = new Author(rs.getInt("authorid"), rs.getString("lastname"), rs.getString("firstname"));
				result.add(autoremap.get(coautore));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db in getCoAutori");
		}
		
	}
	
	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id, AuthorIdMap autoremap) {

		final String sql = "SELECT * FROM author WHERE id = ?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autoremap.get(autore);
			}
			
			conn.close();
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db in getAutore");
		}
		
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid, PaperIdMap pubblicazionemap) {

		final String sql = "SELECT * FROM paper where eprintid = ?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return pubblicazionemap.get(paper);
			}
			
			conn.close();
			return null;

		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException("Errore Db in getArticolo");
		}
		
	}
	
}