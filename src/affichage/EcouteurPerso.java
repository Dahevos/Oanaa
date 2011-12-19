package affichage;

public interface EcouteurPerso {
	public void carteChangee(Personnage perso, Carte carte);
	public void persoBouge(Personnage perso, Direction dir);
}
