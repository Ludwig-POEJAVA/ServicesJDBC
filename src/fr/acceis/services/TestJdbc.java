package fr.acceis.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import fr.acceis.abstraction.*;
import fr.acceis.modele.*;

public class TestJdbc
{

	public static void main(String[] args) throws Exception
	{
		//done
		listerEtudiants();
		listerProfesseurs();
		listerSalles();
		System.out.println("* Liste des cursus des étudiants 21002128 -> 21002130 :");
		cursusEtudiant("21002127");
		cursusEtudiant("21002128");
		cursusEtudiant("21002129");
		cursusEtudiant("21002130");
		System.out.println("* Fin de la liste des cursus des étudiants 21002128 -> 21002130 :");
		System.out.println();
		/*		salleCours(67);
		/*		listerCoursSalle("i57");
		/*		listerEtudiantsCours(67);
		/*		listerProfesseursCursus(10);
		/*		listerProfesseursMatiere(2);
		/*		listerProfsEtudiant("21002127");
		/*		emploiDuTempsSalle("i52");
		/*		emploiDuTempsEtudiant("21002128");
		/*		emploiDuTempsProfesseur(55);/**/
	}

	//	Liste les étudiants
	private static void listerEtudiants() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		List<Etudiant> etudiants = new ArrayList<Etudiant>();
		etudiants = new EtudiantService().lister();

		System.out.println("* Liste des étudiants :");
		for (Etudiant etudiant: etudiants)
		{
			String prenom = etudiant.getPrenom();
			String nom = etudiant.getNom();
			String numeroEtudiant = etudiant.getNumeroEtudiant();
			System.out.println("\t" + prenom + " " + nom + " (" + numeroEtudiant + ")");
		}
		System.out.println("* Fin de la liste des étudiants :");
		System.out.println();
	}

	//	Liste les professeurs
	private static void listerProfesseurs() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		List<Professeur> professeurs = new ArrayList<Professeur>();
		professeurs = new ProfesseurService().lister();

		System.out.println("* Liste des professeurs :");
		for (Professeur professeur: professeurs)
		{
			String prenom = professeur.getPrenom();
			String nom = professeur.getNom();
			Long idProfesseur = professeur.getId();
			System.out.println("\tPr. " + prenom + " " + nom + " (" + idProfesseur + ")");
		}
		System.out.println("* Fin de la liste des professeurs :");
		System.out.println();
	}

	//	Liste les salles
	private static void listerSalles() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		List<Salle> salles = new ArrayList<Salle>();
		salles = new SalleService().lister();

		System.out.println("* Liste des salles :");
		for (Salle salle: salles)
		{
			long idSalle = salle.getId();
			String nom = salle.getNom();
			System.out.println("\tSalle " + nom + " (" + idSalle + ")");
		}
		System.out.println("* Fin de la liste des salles :");
		System.out.println();
	}

	//	Affiche le nom du cursus d'un étudiant
	private static void cursusEtudiant(String numeroEtudiant) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{

		Etudiant etudiant = new EtudiantService().trouverCetEtudiantParNumero(numeroEtudiant);
		Cursus cursus = new CursusService().trouverCursusDeCetEtudiant(numeroEtudiant);

		String prenom = etudiant.getPrenom();
		String nom = etudiant.getNom();

		long idCursus = cursus.getId();
		String nomCursus = cursus.getNom();

		System.out.println("\t" + prenom + " " + nom + " (" + numeroEtudiant + ") est inscrit dans le cursus " + nomCursus + " (" + idCursus + ")");
		System.out.println();
	}

	//	Affiche le nom de la salle dans laquelle a lieu un cours
	private static void salleCours(long idCours) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT nom FROM salle " + "INNER JOIN creneau " + "ON creneau.salle_id = salle.id " + "INNER JOIN cours " + "ON cours.id = creneau.cours_id " + "WHERE cours.id=?");
		stmt.setLong(1, idCours);

		ResultSet result = stmt.executeQuery();

		while (result.next())
		{
			String nom = result.getString("nom");
			System.out.println("Le cours " + idCours + " aura lieu en salle " + nom);
		}
		System.out.println();

		connexion.close();
	}

	//	Affiche le nom des cours ayant lieu dans une salle
	private static void listerCoursSalle(String nomSalle) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT matiere.nom as nomMatiere, professeur.nom as nomProfesseur, professeur.prenom as prenomProfesseur, salle.nom as nomSalle, horaire.debut as horaireDebut, horaire.fin as horaireFin FROM horaire " + "INNER JOIN creneau " + "ON creneau.horaire_id = horaire.id " + "INNER JOIN salle " + "ON salle.id = creneau.salle_id " + "INNER JOIN cours " + "ON cours.id = creneau.cours_id " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "INNER JOIN cours_professeur " + "ON cours_professeur.cours_id = cours.id " + "INNER JOIN professeur " + "ON professeur.id = cours_professeur.professeurs_id " + "WHERE salle.nom=?");
		stmt.setString(1, nomSalle);

		ResultSet result = stmt.executeQuery();

		SimpleDateFormat formater = new SimpleDateFormat("dd\\MM\\yyyy à HH:mm");
		System.out.println("* Les cours suivants ont lieu en salle " + nomSalle + " :\n");

		while (result.next())
		{
			String nomMatiere = result.getString("nomMatiere");
			String prenomProfesseur = result.getString("prenomProfesseur");
			String nomProfesseur = result.getString("nomProfesseur");
			Time horaireDebut = result.getTime("horaireDebut");
			Time horaireFin = result.getTime("horaireFin");
			System.out.println("------------------------------");
			System.out.println("Cours de " + nomMatiere);
			System.out.println("Assuré par Pr. " + prenomProfesseur + " " + nomProfesseur);
			System.out.println("En salle " + nomSalle);
			System.out.println("Début : " + formater.format(horaireDebut));
			System.out.println("Fin : " + formater.format(horaireFin));
			System.out.println("------------------------------");
		}
		System.out.println("* Fin de la liste des cours");
		System.out.println();

		connexion.close();

	}

	//	Affiche le nom des étudiants qui assistent à un cours
	private static void listerEtudiantsCours(long idCours) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT etudiant.prenom as prenomEtudiant, etudiant.nom as nomEtudiant, etudiant.numeroEtudiant FROM cours " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "INNER JOIN cursus_matiere " + "ON cursus_matiere.matieres_id = matiere.id " + "INNER JOIN cursus " + "ON cursus.id = cursus_matiere.cursus_id " + "INNER JOIN etudiant " + "ON cursus.id = etudiant.cursus_id " + "WHERE cours.id=?");
		stmt.setLong(1, idCours);

		ResultSet result = stmt.executeQuery();

		System.out.println("* Les étudiants suivants assisteront au cours " + idCours + " :");

		while (result.next())
		{
			String prenomEtudiant = result.getString("prenomEtudiant");
			String nomEtudiant = result.getString("nomEtudiant");
			String numeroEtudiant = result.getString("numeroEtudiant");
			System.out.println(prenomEtudiant + " " + nomEtudiant + " (" + numeroEtudiant + ")");
		}
		System.out.println("* Fin de la liste des étudiants");
		System.out.println();

		connexion.close();

	}

	//	Affiche le nom des professeurss qui enseignent dans un cursus
	private static void listerProfesseursCursus(long idCursus) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");

		String nomCursus = "";

		PreparedStatement stmt = connexion.prepareStatement("SELECT nom FROM cursus WHERE cursus.id=?");
		stmt.setLong(1, idCursus);
		ResultSet result = stmt.executeQuery();
		while (result.next())
		{
			nomCursus = result.getString("nom");
		}

		stmt = connexion.prepareStatement("SELECT distinct professeur.nom as nomProfesseur, professeur.prenom as prenomProfesseur FROM professeur " + "INNER JOIN cours_professeur " + "ON professeur.id = cours_professeur.professeurs_id " + "INNER JOIN cours " + "ON cours.id = cours_professeur.cours_id " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "INNER JOIN cursus_matiere " + "ON cursus_matiere.matieres_id = matiere.id " + "INNER JOIN cursus " + "ON cursus.id = cursus_matiere.cursus_id " + "WHERE cursus.id=?");
		stmt.setLong(1, idCursus);

		result = stmt.executeQuery();

		System.out.println("* Les professeurs suivants enseignent au sein du cursus " + nomCursus + " :");
		while (result.next())
		{
			String prenomProfesseur = result.getString("prenomProfesseur");
			String nomProfesseur = result.getString("nomProfesseur");
			System.out.println("Pr. " + prenomProfesseur + " " + nomProfesseur);
		}
		System.out.println("* Fin de la liste des professeurs");
		System.out.println();

		connexion.close();

	}

	//	Affiche le nom des professeurs qui enseignent une matière
	private static void listerProfesseursMatiere(long idMatiere) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");

		String nomMatiere = "";

		PreparedStatement stmt = connexion.prepareStatement("SELECT nom FROM matiere WHERE matiere.id=?");
		stmt.setLong(1, idMatiere);
		ResultSet result = stmt.executeQuery();
		while (result.next())
		{
			nomMatiere = result.getString("nom");
		}

		stmt = connexion.prepareStatement("SELECT distinct professeur.nom as nomProfesseur, professeur.prenom as prenomProfesseur FROM professeur " + "INNER JOIN cours_professeur " + "ON professeur.id = cours_professeur.professeurs_id " + "INNER JOIN cours " + "ON cours.id = cours_professeur.cours_id " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "WHERE matiere.id=?");
		stmt.setLong(1, idMatiere);

		result = stmt.executeQuery();

		System.out.println("* Les professeurs suivants enseignent la matière \"" + nomMatiere + "\" :");
		while (result.next())
		{
			String prenomProfesseur = result.getString("prenomProfesseur");
			String nomProfesseur = result.getString("nomProfesseur");
			System.out.println("Pr. " + prenomProfesseur + " " + nomProfesseur);
		}
		System.out.println("* Fin de la liste des professeurs");
		System.out.println();

		connexion.close();

	}

	//	Affiche des profs qui enseignent à un étudiant
	private static void listerProfsEtudiant(String numeroEtudiant) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT cursus_id FROM etudiant WHERE etudiant.numeroEtudiant=?");
		stmt.setString(1, numeroEtudiant);
		ResultSet result = stmt.executeQuery();
		long idCursus = 0;
		while (result.next())
		{
			idCursus = result.getLong("cursus_id");
		}
		// hacking myself
		listerProfesseursCursus(idCursus);
	}

	//	Affiche l'emploi du temps d'une salle
	private static void emploiDuTempsSalle(String nomSalle) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");
		PreparedStatement stmt = connexion.prepareStatement("SELECT matiere.nom as nomMatiere, professeur.nom as nomProfesseur, professeur.prenom as prenomProfesseur, salle.nom as nomSalle, horaire.debut as horaireDebut, horaire.fin as horaireFin FROM horaire " + "INNER JOIN creneau " + "ON creneau.horaire_id = horaire.id " + "INNER JOIN salle " + "ON salle.id = creneau.salle_id " + "INNER JOIN cours " + "ON cours.id = creneau.cours_id " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "INNER JOIN cours_professeur " + "ON cours_professeur.cours_id = cours.id " + "INNER JOIN professeur " + "ON professeur.id = cours_professeur.professeurs_id " + "WHERE salle.nom=?");
		stmt.setString(1, nomSalle);

		ResultSet result = stmt.executeQuery();

		SimpleDateFormat formater = new SimpleDateFormat("dd\\MM\\yyyy à HH:mm");
		System.out.println("Emploi du temps de la salle " + nomSalle + " :\n");

		while (result.next())
		{
			String nomMatiere = result.getString("nomMatiere");
			String prenomProfesseur = result.getString("prenomProfesseur");
			String nomProfesseur = result.getString("nomProfesseur");
			Time horaireDebut = result.getTime("horaireDebut");
			Time horaireFin = result.getTime("horaireFin");
			System.out.println("------------------------------");
			System.out.println("Cours de " + nomMatiere);
			System.out.println("Assuré par Pr. " + prenomProfesseur + " " + nomProfesseur);
			System.out.println("En salle " + nomSalle);
			System.out.println("Début : " + formater.format(horaireDebut));
			System.out.println("Fin : " + formater.format(horaireFin));
			System.out.println("------------------------------");
		}
		System.out.println("Fin de l'emploi du temps");
		System.out.println();

		connexion.close();
	}

	//	Affiche l'emploi du temps d'un étudiant
	private static void emploiDuTempsEtudiant(String numeroEtudiant) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");

		String prenomEtudiant = "";
		String nomEtudiant = "";

		PreparedStatement stmt = connexion.prepareStatement("SELECT nom, prenom, numeroEtudiant FROM etudiant WHERE numeroEtudiant=?");
		stmt.setString(1, numeroEtudiant);
		ResultSet result = stmt.executeQuery();
		while (result.next())
		{
			nomEtudiant = result.getString("nom");
			prenomEtudiant = result.getString("prenom");
		}

		stmt = connexion.prepareStatement("SELECT matiere.nom as nomMatiere, professeur.nom as nomProfesseur, professeur.prenom as prenomProfesseur, salle.nom as nomSalle, horaire.debut as horaireDebut, horaire.fin as horaireFin, salle.nom as nomSalle FROM horaire " + "INNER JOIN creneau " + "ON creneau.horaire_id = horaire.id " + "INNER JOIN salle " + "ON salle.id = creneau.salle_id " + "INNER JOIN cours " + "ON cours.id = creneau.cours_id " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "INNER JOIN cours_professeur " + "ON cours_professeur.cours_id = cours.id " + "INNER JOIN professeur " + "ON professeur.id = cours_professeur.professeurs_id " + "INNER JOIN cursus_matiere " + "ON cursus_matiere.matieres_id = matiere.id " + "INNER JOIN cursus " + "ON cursus.id = cursus_matiere.cursus_id " + "INNER JOIN etudiant " + "ON cursus.id = etudiant.cursus_id " + "WHERE etudiant.numeroEtudiant=?");
		stmt.setString(1, numeroEtudiant);

		result = stmt.executeQuery();

		SimpleDateFormat formater = new SimpleDateFormat("dd\\MM\\yyyy à HH:mm");
		System.out.println("Emploi du temps de l'étudiant " + prenomEtudiant + " " + nomEtudiant + " (" + numeroEtudiant + ") : \n");

		while (result.next())
		{
			String nomMatiere = result.getString("nomMatiere");
			String nomSalle = result.getString("nomSalle");
			String prenomProfesseur = result.getString("prenomProfesseur");
			String nomProfesseur = result.getString("nomProfesseur");
			Time horaireDebut = result.getTime("horaireDebut");
			Time horaireFin = result.getTime("horaireFin");
			System.out.println("------------------------------");
			System.out.println("Cours de " + nomMatiere);
			System.out.println("Assuré par Pr. " + prenomProfesseur + " " + nomProfesseur);
			System.out.println("En salle " + nomSalle);
			System.out.println("Début : " + formater.format(horaireDebut));
			System.out.println("Fin : " + formater.format(horaireFin));
			System.out.println("------------------------------");
		}

		System.out.println("Fin de l'emploi du temps");
		System.out.println();

		connexion.close();
	}

	//	Affiche l'emploi du temps d'un professeur
	private static void emploiDuTempsProfesseur(long idProfesseur) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
	{
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		Connection connexion = DriverManager.getConnection("jdbc:hsqldb:data/basejpa", "sa", "");

		String prenomProfesseur = "";
		String nomProfesseur = "";

		PreparedStatement stmt = connexion.prepareStatement("SELECT nom, prenom FROM professeur WHERE id=?");
		stmt.setLong(1, idProfesseur);
		ResultSet result = stmt.executeQuery();
		while (result.next())
		{
			nomProfesseur = result.getString("nom");
			prenomProfesseur = result.getString("prenom");
		}

		stmt = connexion.prepareStatement("SELECT matiere.nom as nomMatiere, salle.nom as nomSalle, horaire.debut as horaireDebut, horaire.fin as horaireFin, salle.nom as nomSalle FROM horaire " + "INNER JOIN creneau " + "ON creneau.horaire_id = horaire.id " + "INNER JOIN salle " + "ON salle.id = creneau.salle_id " + "INNER JOIN cours " + "ON cours.id = creneau.cours_id " + "INNER JOIN matiere " + "ON matiere.id = cours.matiere_id " + "INNER JOIN cours_professeur " + "ON cours_professeur.cours_id = cours.id " + "INNER JOIN professeur " + "ON professeur.id = cours_professeur.professeurs_id " + "WHERE professeur.id=?");
		stmt.setLong(1, idProfesseur);

		result = stmt.executeQuery();

		SimpleDateFormat formater = new SimpleDateFormat("dd\\MM\\yyyy à HH:mm");
		System.out.println("Emploi du temps du Pr. " + prenomProfesseur + " " + nomProfesseur + ") : \n");

		while (result.next())
		{
			String nomMatiere = result.getString("nomMatiere");
			String nomSalle = result.getString("nomSalle");
			Time horaireDebut = result.getTime("horaireDebut");
			Time horaireFin = result.getTime("horaireFin");
			System.out.println("------------------------------");
			System.out.println("Cours de " + nomMatiere);
			System.out.println("En salle " + nomSalle);
			System.out.println("Début : " + formater.format(horaireDebut));
			System.out.println("Fin : " + formater.format(horaireFin));
			System.out.println("------------------------------");
		}

		System.out.println("Fin de l'emploi du temps");
		System.out.println();

		connexion.close();
	}

}
