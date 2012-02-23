package modele;

public interface EcouteurPerso {
	public boolean carteChangee(Personnage perso, Carte carte);
	public boolean persoBouge(Personnage perso, Direction dir);
}
