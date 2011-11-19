import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public abstract class Personnage {
	private Apparence apparence;
	private Carte carte = null;
	private int i = -1, j = -1;
	private int x = -1, y = -1;
	private Direction dir, dirSuiv;
	private int instant;
	private Thread deplacement = null;
	private ArrayList<EcouteurPerso> ecouteurs = new ArrayList<EcouteurPerso>();

	public Personnage(Apparence apparence) {
		this.apparence = apparence;
	}

	public Personnage(Apparence apparence, Carte carte, int i, int j) {
		this(apparence);
		setCarte(carte, i, j);
	}
	
	public void ajouterEcouteur(EcouteurPerso ecouteur) {
		ecouteurs.add(ecouteur);
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
		// Vérification de la disponibilité
		if (carte == null || ! carte.existe(i, j) || ! carte.getCase(i, j).estLibre()) return false;

		// Réservation de la destination
		carte.getCase(i, j).setLibre(false);

		// Suppression (éventuelle) de l'ancienne position
		if (this.carte != null) {
			this.carte.getCase(i, j).setLibre(true);
			carte.getCase(i, j).setPerso(null);
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
			ecouteur.carteChangee(carte);
		}
		return true;
	}

	public boolean deplacer(Direction dir) {
		if (carte == null) return false;
		if (deplacement != null) {
			dirSuiv = dir;
			return false;
		}
		this.dir = dir;

		// Calcul des nouvelles coordonnées
		int i = this.i;
		int j = this.j;
		switch (dir) {
		case BAS: j++; break;
		case GAUCHE: i--; break;
		case DROITE: i++; break;
		case HAUT: j--; break;
		}

		// Vérification de la disponibilité
		if (! carte.existe(i, j) || ! carte.getCase(i, j).estLibre()) return false;

		// Réservation de la destination
		carte.getCase(i, j).setLibre(false);
		deplacement = new Thread(new Deplacement(this.i, this.j, i, j));

		// Mise en place de la nouvelle position
		carte.getCase(this.i, this.j).setPerso(null);
		this.i = i;
		this.j = j;
		carte.getCase(i, j).setPerso(this);

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
		private final int oldI, oldJ;
		private final int dx, dy;

		public Deplacement(int oldI, int oldJ, int newI, int newJ) {
			this.oldI = oldI;
			this.oldJ = oldJ;
			dx = (newI - oldI) * 8;
			dy = (newJ - oldJ) * 8;
		}

		@Override
		public void run() {
			for (int k = 1; k <= 4; k++) {
				synchronized (Personnage.this) {
					x += dx;
					y += dy;
					instant = k % 4;
					for (EcouteurPerso ecouteur : ecouteurs) {
						ecouteur.persoBouge(dir);
					}
					if (ecouteurs.isEmpty()) carte.rafraichir(i - 1, j - 2, i + 1, j + 1);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			carte.getCase(oldI, oldJ).setLibre(true);
			deplacement = null;
			Direction dir = dirSuiv;
			dirSuiv = null;
			if (dir != null) deplacer(dir);
		}
	}
}
