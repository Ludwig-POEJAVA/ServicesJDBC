package fr.acceis.abstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.acceis.modele.*;

public class SalleService
{
	public List<Salle> lister() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT * FROM salle");

		ResultSet result = stmt.executeQuery();

		List<Salle> salles = new ArrayList<Salle>();

		while (result.next())
		{
			long id = result.getLong("id");
			String nom = result.getString("nom");
			Salle salle = new Salle(id, nom);

			salles.add(salle);
		}

		connexion.close();
		return salles;
	}

	public Salle trouverUneSalleEnFonctionDuCours(long idCours) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT * FROM salle");

		ResultSet result = stmt.executeQuery();

		result.next();

		long id = result.getLong("id");
		String nom = result.getString("nom");
		Salle salle = new Salle(id, nom);

		connexion.close();
		return salle;
	}
}
