package fr.acceis.abstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.acceis.modele.*;

public class CursusService
{

	public Cursus trouverCursusDeCetEtudiant(String numeroEtudiant) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT cursus.nom as nomCursus, cursus.id as cursusId FROM cursus " + "INNER JOIN etudiant " + "ON etudiant.cursus_id = cursus.id " + "WHERE etudiant.numeroEtudiant=?");
		stmt.setString(1, numeroEtudiant);

		ResultSet result = stmt.executeQuery();

		Cursus cursus = new Cursus();

		result.next();

		String nomCursus = result.getString("nomCursus");
		long idCursus = result.getLong("cursusId");
		cursus = new Cursus(idCursus, nomCursus);

		connexion.close();
		return cursus;
	}
}
