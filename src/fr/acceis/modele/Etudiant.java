package fr.acceis.modele;

public class Etudiant
{

	private String	prenom;
	private String	nom;
	private String	numeroEtudiant;

	public Etudiant()
	{
	}

	public Etudiant(String prenom, String nom, String numeroEtudiant)
	{
		this.prenom = prenom;
		this.nom = nom;
		this.numeroEtudiant = numeroEtudiant;
	}

	public String getPrenom()
	{
		return this.prenom;
	}

	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}

	public String getNom()
	{
		return this.nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getNumeroEtudiant()
	{
		return this.numeroEtudiant;
	}

	public void setNumeroEtudiant(String numeroEtudiant)
	{
		this.numeroEtudiant = numeroEtudiant;
	}
}
