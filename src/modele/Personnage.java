package modele;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ressources.Apparence;


public abstract class Personnage {
	private Apparence apparence;
	private Carte carte = null;
	private int i = -1, j = -1;
	private int x = -1, y = -1;
	private Direction dir, dirSuiv;
	private int instant;
	public int increment; // Déplacement par mouvement
	private long periode; // Durée d'un mouvement
	private Thread deplacement = null;
	private final ArrayList<EcouteurPerso> ecouteurs = new ArrayList<EcouteurPerso>();

	public Personnage(Apparence apparence, double vitesse) {
		this.apparence = apparence;
		setVitesse(vitesse);
	}

	public Personnage(Apparence apparence, double vitesse, Carte carte, int i, int j,
			Direction dir) {
		this(apparence, vitesse);
		setCarte(carte, i, j, dir);
	}

	public Personnage(Apparence apparence, double vitesse, Carte carte, int i, int j) {
		this(apparence, vitesse, carte, i, j, Direction.BAS);
	}
	
	public Personnage(Apparence apparence) {
		this(apparence, 2.5);
	}

	public Personnage(Apparence apparence, Carte carte, int i, int j, Direction dir) {
		this(apparence);
		setCarte(carte, i, j, dir);
	}

	public Personnage(Apparence apparence, Carte carte, int i, int j) {
		this(apparence, carte, i, j, Direction.BAS);
	}

	public Apparence getApparence() {
		return apparence;
	}

	public void setApparence(Apparence apparence) {
		if (this.apparence == apparence) return;
		this.apparence = apparence;
		if (carte != null) carte.rafraichir(i, j - 1, i, j);
	}

	public void ajouterEcouteur(EcouteurPerso ecouteur) {
		ecouteurs.add(ecouteur);
	}

	public void retirerEcouteur(EcouteurPerso ecouteur) {
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

	public boolean setCarte(Carte carte, int i, int j) {
		return setCarte(carte, i, j, this.dir);
	}

	public boolean setCarte(Carte carte, int i, int j, Direction dir) {
		// Vérification de la validité
		if (carte == null || ! carte.existe(i, j)) return false;

		// Réservation de la destination
		if (! carte.getCase(i, j).allouer()) return false;

		// Suppression (éventuelle) de l'ancienne position
		if (this.carte != null) {
			Case c = this.carte.getCase(this.i, this.j);
			c.setPerso(null);
			c.liberer();
			this.carte.rafraichir(this.i, this.j - 1, this.i, this.j);
		}

		// Mise en place de la nouvelle position
		this.carte = carte;
		this.i = i;
		x = 32 * i;
		this.j = j;
		y = 32 * j - 16;
		this.dir = dir;
		instant = 0;
		carte.getCase(i, j).setPerso(this);
		carte.rafraichir(i, j - 1, i, j);

		// Signalement du changement de carte
		for (EcouteurPerso ecouteur : ecouteurs) {
			ecouteur.carteChangee(this, carte);
		}
		return true;
	}

	public double getVitesse() {
		return (1000.0 * increment) / (periode * 32);
	}
	
	private final static long FREQ_MIN = 20;
	private final static long PERIODE_MAX = 1000 / FREQ_MIN;

	public double setVitesse(double vitesse) {
		long nouvPeriode;
		for (int nouvIncr = 1; nouvIncr <= 32; nouvIncr *= 2) {
			nouvPeriode = Math.round((1000 * nouvIncr) / (vitesse * 32));
			if (nouvIncr > 1 && nouvPeriode > PERIODE_MAX) break;
			increment = nouvIncr;
			periode = nouvPeriode;
		}

		return getVitesse();
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
			dx = (nouvI - Personnage.this.i) * increment;
			dy = (nouvJ - Personnage.this.j) * increment;
		}

		@Override
		public void run() {
			// Déplacement de l'image
			for (int k = 1; k * increment <= 32; k++) {
				x += dx;
				y += dy;
				instant = ((k * increment) / 8) % 4;

				// Signalement du déplacement
				for (EcouteurPerso ecouteur : ecouteurs) {
					ecouteur.persoBouge(Personnage.this, dir);
				}

				// Mise à jour de la carte
				switch(dir) {
				case BAS: carte.rafraichir(i, j - 1, i, j + 1); break;
				case GAUCHE: carte.rafraichir(i - 1, j - 1, i, j); break;
				case DROITE: carte.rafraichir(i, j - 1, i + 1, j); break;
				case HAUT: carte.rafraichir(i, j - 2, i, j); break;
				}
				try {
					Thread.sleep(periode);
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
