import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Personnage implements KeyListener {
	private Image image;
	private Carte carte = null;
	private int x = -1, y = -1;
	
	public Personnage(Image image) {
		this.image = image;
	}
	
	public Personnage(Image image, Carte carte, int x, int y) {
		this(image);
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
			this.carte.setNiveau(this.x, this.y, 1, null);
			this.carte.setLibre(x, y, true);
		}
		
		// Mise en place de la nouvelle position
		this.carte = carte;
		this.x = x;
		this.y = y;
		carte.setNiveau(x, y, 1, image);
		carte.setLibre(x, y, false);
		return true;
	}
	
	public boolean deplacer(int dx, int dy) {
		if (carte == null) return false;
		
		// Calcul des nouvelles coordonnées
		int x = this.x + dx;
		int y = this.y + dy;
		
		// Vérification de la disponibilité
		if (! carte.existe(x, y) || ! carte.isLibre(x, y)) return false;
		
		// Suppression de l'ancienne position
		carte.setNiveau(this.x, this.y, 1, null);
		carte.setLibre(this.x, this.y, true);
		
		// Mise en place de la nouvelle position
		this.x = x;
		this.y = y;
		carte.setNiveau(this.x, this.y, 1, image);
		carte.setLibre(this.x, this.y, true);
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP: deplacer(0, -1); break;
		case KeyEvent.VK_RIGHT: deplacer(1, 0); break;
		case KeyEvent.VK_DOWN: deplacer(0, 1); break;
		case KeyEvent.VK_LEFT: deplacer(-1, 0); break;
		default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
