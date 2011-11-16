import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Personnage implements KeyListener {
	private static final int COUCHE_PERSONNAGE = 2;
	
	private Apparence apparence;
	private Carte carte = null;
	private int x = -1, y = -1;
	
	public Personnage(Apparence apparence) {
		this.apparence = apparence;
	}
	
	public Personnage(Apparence apparence, Carte carte, int x, int y) {
		this(apparence);
		setCarte(carte, x, y);
	}
	
	public Carte getCarte() {
		return carte;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean setCarte(Carte carte, int x, int y) {
		// Vérification de la disponibilité
		if (carte == null || ! carte.existe(x, y) || ! carte.isLibre(x, y)) return false;
		
		// Suppression (éventuelle) de l'ancienne position
		if (this.carte != null) {
			this.carte.setCouche(this.x, this.y, COUCHE_PERSONNAGE, null);
			this.carte.setLibre(x, y, true);
		}
		
		// Mise en place de la nouvelle position
		this.carte = carte;
		this.x = x;
		this.y = y;
		carte.setCouche(x, y, COUCHE_PERSONNAGE, apparence.getImage(Direction.BAS, 0));
		carte.setLibre(x, y, false);
		return true;
	}
	
	public boolean deplacer(Direction dir) {
		if (carte == null) return false;
		
		// Calcul des nouvelles coordonnées
		int x = this.x;
		int y = this.y;
		switch (dir) {
		case BAS: y++; break;
		case GAUCHE: x--; break;
		case DROITE: x++; break;
		case HAUT: y--; break;
		}
		
		// Vérification de la disponibilité
		if (! carte.existe(x, y) || ! carte.isLibre(x, y)) return false;
		
		// Suppression de l'ancienne position
		carte.setCouche(this.x, this.y, COUCHE_PERSONNAGE, null);
		carte.setLibre(this.x, this.y, true);
		
		// Mise en place de la nouvelle position
		this.x = x;
		this.y = y;
		carte.setCouche(this.x, this.y, COUCHE_PERSONNAGE, apparence.getImage(dir, 0));
		carte.setLibre(this.x, this.y, true);
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN: deplacer(Direction.BAS); break;
		case KeyEvent.VK_LEFT: deplacer(Direction.GAUCHE); break;
		case KeyEvent.VK_RIGHT: deplacer(Direction.DROITE); break;
		case KeyEvent.VK_UP: deplacer(Direction.HAUT); break;
		default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
