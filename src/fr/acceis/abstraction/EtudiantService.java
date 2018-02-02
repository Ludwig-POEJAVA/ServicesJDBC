package fr.acceis.abstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import fr.acceis.modele.*;

public class EtudiantService
{
	public List<Etudiant> lister() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT * FROM etudiant");

		ResultSet result = stmt.executeQuery();

		List<Etudiant> etudiants = new ArrayList<Etudiant>();

		while (result.next())
		{
			String prenom = result.getString("prenom");
			String nom = result.getString("nom");
			String numeroEtudiant = result.getString("numeroEtudiant");
			Etudiant etudiant = new Etudiant(prenom, nom, numeroEtudiant);

			etudiants.add(etudiant);
		}

		connexion.close();
		return etudiants;
	}

	public Etudiant trouverCetEtudiantParNumero(String numeroEtudiant) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT * FROM etudiant WHERE etudiant.numeroEtudiant=?");
		stmt.setString(1, numeroEtudiant);

		ResultSet result = stmt.executeQuery();

		result.next();

		String prenom = result.getString("prenom");
		String nom = result.getString("nom");
		Etudiant etudiant = new Etudiant(prenom, nom, numeroEtudiant);

		connexion.close();
		return etudiant;
	}
}
