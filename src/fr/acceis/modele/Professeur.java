package fr.acceis.modele;

public class Professeur
{
	private long	id;
	private String	nom;
	private String	prenom;

	public Professeur()
	{
	}

	public Professeur(long id, String nom, String prenom)
	{
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getNom()
	{
		return this.nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getPrenom()
	{
		return this.prenom;
	}

	public void setPrenom(String prenom)
	{
		this.prenom = prenom;
	}
}
