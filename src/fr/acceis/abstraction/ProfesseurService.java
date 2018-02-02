package fr.acceis.abstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.acceis.modele.*;

public class ProfesseurService
{
	public List<Professeur> lister() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT * FROM professeur");

		ResultSet result = stmt.executeQuery();

		List<Professeur> professeurs = new ArrayList<Professeur>();

		while (result.next())
		{
			String prenom = result.getString("prenom");
			String nom = result.getString("nom");
			long idProfesseur = result.getLong("id");
			Professeur professeur = new Professeur(idProfesseur, prenom, nom);

			professeurs.add(professeur);
		}

		connexion.close();
		return professeurs;
	}
}
