package affichage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Classe représentant un filtre uni.
 */
public class FiltreUni extends Filtre {
	/**
	 * Couleur du filtre
	 */
	private Color couleur;
	
	/**
	 * Construit une nouveau filtre uni.
	 * @param couleur couleur du filtre
	 */
	public FiltreUni(Color couleur) {
		this.couleur = couleur;
	}
	
	/**
	 * Construit une nouveau filtre noir.
	 */
	public FiltreUni() {
		this(Color.BLACK);
	}
	
	/**
	 * Retourne la couleur de ce filtre.
	 * @return la couleur de ce filtre
	 */
	public Color getCouleur() {
		return couleur;
	}
	
	/**
	 * Modifie la couleur de ce filtre.
	 * @param couleur nouvelle couleur
	 */
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
		
		// Notification des écrans
		for (Ecran ecran : getEcrans()) {
			ecran.repaint();
		}
	}
	
	@Override
	public void dessiner(Graphics g) {
		final Color couleurPrec = g.getColor();
		Rectangle zone = g.getClipBounds();
		g.setColor(couleur);
		g.fillRect(zone.x, zone.y, zone.width, zone.height);
		g.setColor(couleurPrec);
	}
}
