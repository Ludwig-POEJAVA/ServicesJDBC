package fr.acceis.modele;

public class Cursus
{
	private long	id;
	private String	nom;

	public Cursus()
	{
	}

	public Cursus(long id, String nom)
	{
		this.id = id;
		this.nom = nom;
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
}
