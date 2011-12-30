package affichage;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Classe représentant un filtre à motif ajusté (redimensionné).
 */
public class FiltreAjuste extends Filtre {
	/**
	 * Motif de ce filtre.
	 */
	private Image motif;
	
	/**
	 * Méthode d'affichage du filtre (pour la transparence).
	 */
	private Composite comp;

	/**
	 * Construit un nouveau filtre à motif ajusté.
	 * @param motif motif de ce filtre
	 * @param alpha transparence du dessin
	 * 
	 */
	public FiltreAjuste(Image motif, float alpha) {
		this.motif = motif;
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}

	/**
	 * Construit un nouveau filtre sans motif.
	 */
	public FiltreAjuste() {
		this(null, 0f);
	}

	/**
	 * Retourne le motif de ce filtre.
	 * @return le motif de ce filtre
	 */
	public Image getMotif() {
		return motif;
	}

	/**
	 * Modifie le motif de ce filtre.
	 * @param motif nouveau motif
	 */
	public void setMotif(Image motif, float alpha) {
		this.motif = motif;
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

		// Notification des écrans
		for (Ecran ecran : getEcrans()) {
			ecran.repaint();
		}
	}

	@Override
	public void dessiner(Graphics g, int largeur, int hauteur) {
		if (g instanceof Graphics2D) {
			// On utilise Graphics2D pour pouvoir manipuler les composites
			Graphics2D g2d = (Graphics2D) g;
			Composite compPrec = g2d.getComposite();
			g2d.setComposite(comp);
			g2d.drawImage(motif, 0, 0, largeur, hauteur, null);
			g2d.setComposite(compPrec);
		} else {
			// On ne peut pas prendre en compte la transparence
			g.drawImage(motif, 0, 0, largeur, hauteur, null);
		}
	}
}
