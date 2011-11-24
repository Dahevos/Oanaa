import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Case implements Serializable {
	private static final long serialVersionUID = 42L;
	
	private final Carte carte;
	private final int i, j;
	
	private final int couchesInf, nbCouches;
	private final Element couches[];
	private boolean bloquee = false;
	private transient Personnage perso = null;
	private transient boolean libre = ! bloquee;
	
	public Case(Carte carte, int i, int j, int couchesInf, int couchesSup) {
		this.carte = carte;
		this.i = i;
		this.j = j;
		this.couchesInf = couchesInf;
		nbCouches = couchesInf + couchesSup;
		couches = new Element[nbCouches];
		for (int k = 0; k < nbCouches; k++) couches[k] = null;
	}
	
	public void setCouche(int couche, Element element) {
		couches[couche] = element;
	}
	
	public Personnage getPerso() {
		return perso;
	}
	
	public void setPerso(Personnage perso) {
		this.perso = perso;
	}
	
	public boolean estBloquee() {
		return bloquee;
	}

	public void setBloquee(boolean bloquee) {
		this.bloquee = bloquee;
	}
	
	public boolean estLibre() {
		return libre;
	}
	
	public void setLibre(boolean libre) {
		this.libre = libre;
	}
	
	public void dessiner(Graphics g, int x, int y) {
		// Dessin des couches inférieures
		for (int couche = 0; couche < couchesInf; couche++)
			if (couches[couche] != null) g.drawImage(couches[couche].getImage(), x, y, null);
		
		// Dessin des personnages éventuels
		Personnage perso;
		for (int j = this.j - 1; j <= this.j + 2; j++)
			for (int i = this.i - 1; i <= this.i + 1; i++)
				if (carte.existe(i, j)) {
					perso = carte.getCase(i, j).getPerso();
					if (perso != null) perso.dessiner(g, x, y, 32 * this.i, 32 * this.j);
				}
		
		// Dessin des couches supérieures
		for (int couche = couchesInf; couche < nbCouches; couche++)
			if (couches[couche] != null) g.drawImage(couches[couche].getImage(), x, y, null);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		libre = ! bloquee;
	}
}
