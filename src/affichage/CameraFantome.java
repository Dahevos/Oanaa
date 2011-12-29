package affichage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import modele.Carte;
import modele.Direction;

public class CameraFantome extends Camera implements KeyListener {
	private int xBaseMax = 0, yBaseMax = 0;
	private int increment = 8;

	public CameraFantome() {}

	public CameraFantome(Carte carte) {
		setCarte(carte);
	}

	public CameraFantome(int increment) {
		this.increment = increment;
	}

	public CameraFantome(Carte carte, int increment) {
		setCarte(carte);
		this.increment = increment;
	}

	public void setCarte(Carte carte) {
		super.setCarte(carte);
		recalculerBaseMax();
	}

	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
	}

	synchronized private void recalculerBaseMax() {
		recalculerBaseMax(getLargeur(), getHauteur());
	}

	synchronized private void recalculerBaseMax(int largeur, int hauteur) {
		final Carte carte = getCarte();
		if (carte != null) {
			xBaseMax = carte.getLargeur() * 32 - largeur;
			yBaseMax = carte.getHauteur() * 32 - hauteur;
		} else {
			xBaseMax = 0;
			yBaseMax = 0;
		}
	}

	@Override
	synchronized public void redimensionner(int largeur, int hauteur) {
		recalculerBaseMax(largeur, hauteur);
		super.redimensionner(largeur, hauteur);
	}

	synchronized public void keyPressed(KeyEvent e) {
		Direction dir;
		int incrementMax;
		
		// Analyse de la touche
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN:
			dir = Direction.BAS;
			incrementMax = yBaseMax - getyBase();
			break;
		case KeyEvent.VK_LEFT:
			dir = Direction.GAUCHE;
			incrementMax = getxBase();
			break;
		case KeyEvent.VK_RIGHT:
			dir = Direction.DROITE;
			incrementMax = xBaseMax - getxBase();
			break;
		case KeyEvent.VK_UP:
			dir = Direction.HAUT;
			incrementMax = getyBase();
			break;
		default: return;
		}
		
		// Déplacement de la caméra
		deplacer(dir, increment > incrementMax ? incrementMax : increment);
		
		// Notification des écrans
		for (Ecran ecran : getEcrans()) {
			ecran.repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
