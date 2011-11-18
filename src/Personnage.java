import java.awt.Graphics;


public abstract class Personnage {
	private Apparence apparence;
	private Carte carte = null;
	private int i = -1, j = -1;
	private int x = -1, y = -1;
	private Direction dir, dirSuiv;
	private int instant;
	private Thread deplacement = null;
	
	public Personnage(Apparence apparence) {
		this.apparence = apparence;
	}
	
	public Personnage(Apparence apparence, Carte carte, int i, int j) {
		this(apparence);
		setCarte(carte, i, j);
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
		if (carte == null || ! carte.existe(i, j) || ! carte.estLibre(i, j)) return false;

		// Réservation de la destination
		carte.setLibre(i, j, false);
		
		// Suppression (éventuelle) de l'ancienne position
		if (this.carte != null) {
			this.carte.retirerPerso(this);
			this.carte.setLibre(i, j, true);
		}
		
		// Mise en place de la nouvelle position
		this.carte = carte;
		this.i = i;
		x = 32 * i;
		this.j = j;
		y = 32 * j;
		dir = Direction.BAS;
		instant = 0;
		carte.ajouterPerso(this);
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
		if (! carte.existe(i, j) || ! carte.estLibre(i, j)) return false;
		
		// Réservation de la destination
		carte.setLibre(i, j, false);
		deplacement = new Thread(new Deplacement(this.i, this.j, i, j));
		
		// Mise en place de la nouvelle position
		this.i = i;
		this.j = j;
		
		// Démarrage du déplacement
		deplacement.start();
		return true;
	}
	
	public void dessiner(Graphics g, int xBase, int yBase) {
		g.drawImage(apparence.getImage(dir, instant), x - xBase, y - yBase - 16, null);
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
			for (int i = 1; i <= 4; i++) {
				synchronized (Personnage.this) {
					x += dx;
					y += dy;
					instant = i % 4;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			carte.setLibre(oldI, oldJ, true);
			deplacement = null;
			Direction dir = dirSuiv;
			dirSuiv = null;
			if (dir != null) deplacer(dir);
		}
	}
}
