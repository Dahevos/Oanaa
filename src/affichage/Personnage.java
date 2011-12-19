package affichage;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ressources.Apparence;


public abstract class Personnage {
	private final Apparence apparence;
	private Carte carte = null;
	private int i = -1, j = -1;
	private int x = -1, y = -1;
	private Direction dir, dirSuiv;
	private int instant;
	private Thread deplacement = null;
	private final ArrayList<EcouteurPerso> ecouteurs = new ArrayList<EcouteurPerso>();
	private boolean auto;

	public Personnage(Apparence apparence) {
		this.apparence = apparence;
	}

	public Personnage(Apparence apparence, Carte carte, int i, int j, boolean auto) {
		this(apparence);
		setCarte(carte, i, j);
		this.auto = auto;
	}
	
	public Personnage(Apparence apparence, Carte carte, int i, int j) {
		this(apparence, carte, i, j, true);
	}

	public void ajouterEcouteur(EcouteurPerso ecouteur) {
		ecouteurs.add(ecouteur);
	}
	
	public void supprimerEcouteur(EcouteurPerso ecouteur) {
		ecouteurs.remove(ecouteur);
	}

	public Carte getCarte() {
		return carte;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean isAuto() {
		return auto;
	}
	
	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public boolean setCarte(Carte carte, int i, int j) {
		// Vérification de la validité
		if (carte == null || ! carte.existe(i, j)) return false;

		// Réservation de la destination
		if (! carte.getCase(i, j).allouer()) return false;

		// Suppression (éventuelle) de l'ancienne position
		if (this.carte != null) {
			Case c = this.carte.getCase(this.i, this.j);
			c.setPerso(null);
			c.liberer();
		}

		// Mise en place de la nouvelle position
		this.carte = carte;
		this.i = i;
		x = 32 * i;
		this.j = j;
		y = 32 * j - 16;
		dir = Direction.BAS;
		instant = 0;
		carte.getCase(i, j).setPerso(this);
		for (EcouteurPerso ecouteur : ecouteurs) {
			ecouteur.carteChangee(this, carte);
		}
		return true;
	}

	public boolean deplacer(Direction dir) {
		if (carte == null) return false;

		// Blocage des déplacements simultanés
		if (deplacement != null) {
			dirSuiv = dir;
			return false;
		}
		
		// Mise à jour de la direction
		boolean nouvDir = ! dir.equals(this.dir);
		if (nouvDir) {
			this.dir = dir;
		}

		// Calcul des nouvelles coordonnées
		int i = this.i;
		int j = this.j;
		switch (dir) {
		case BAS: j++; break;
		case GAUCHE: i--; break;
		case DROITE: i++; break;
		case HAUT: j--; break;
		}

		// Vérification de la validité
		if (! carte.existe(i, j)) {
			// La direction a changé mais on ne se déplace pas
			if (nouvDir) carte.rafraichir(this.i, this.j - 1, this.i, this.j);
			return false;
		}

		// Validation du déplacement
		synchronized (ecouteurs) {
			if ((deplacement != null) || (! carte.getCase(i, j).allouer())) {
				// La direction a changé mais on ne se déplace pas
				if (nouvDir) carte.rafraichir(this.i, this.j - 1, this.i, this.j);
				return false;
			}
			deplacement = new Thread(new Deplacement(i, j));
		}

		// Démarrage du déplacement
		deplacement.start();
		return true;
	}

	public void dessiner(Graphics g, int x, int y, int xCase, int yCase) {
		BufferedImage image = apparence.getImage(dir, instant);
		final int xMin = xCase > this.x ? xCase : this.x;
		final int yMin = yCase > this.y ? yCase : this.y;
		final int xMax = xCase + 31 < this.x + 31 ? xCase + 31 : this.x + 31;
		final int yMax = yCase + 31 < this.y + 47 ? yCase + 31 : this.y + 47;

		final int largeur = xMax - xMin + 1;
		final int hauteur = yMax - yMin + 1;
		if (largeur > 0 && hauteur > 0)
			g.drawImage(image.getSubimage(xMin - this.x, yMin - this.y, largeur, hauteur),
					x + xMin - xCase, y + yMin - yCase, null);
	}

	private class Deplacement implements Runnable {
		private final int nouvI, nouvJ;
		private final int dx, dy;

		public Deplacement(int nouvI, int nouvJ) {
			this.nouvI = nouvI;
			this.nouvJ = nouvJ;
			dx = (nouvI - Personnage.this.i) * 8;
			dy = (nouvJ - Personnage.this.j) * 8;
		}

		@Override
		public void run() {
			// Déplacement de l'image
			for (int k = 1; k <= 4; k++) {
				x += dx;
				y += dy;
				instant = k % 4;
				for (EcouteurPerso ecouteur : ecouteurs) {
					ecouteur.persoBouge(Personnage.this, dir);
				}
				if (auto) carte.rafraichir(i - 1, j - 2, i + 1, j + 1);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			
			// Mise en place de la nouvelle position
			Case orig = carte.getCase(Personnage.this.i, Personnage.this.j);
			orig.liberer();
			orig.setPerso(null);
			Personnage.this.i = nouvI;
			Personnage.this.j = nouvJ;
			carte.getCase(nouvI, nouvJ).setPerso(Personnage.this);
			
			// Libération de la ressource
			deplacement = null;
			Direction dir = dirSuiv;
			dirSuiv = null;
			if (dir != null) deplacer(dir);
		}
	}
}
