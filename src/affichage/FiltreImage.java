package affichage;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * Classe représentant un filtre à motif.
 */
public class FiltreImage extends Filtre {
	/**
	 * Motif de ce filtre.
	 */
	private Image motif;

	/**
	 * Dimensions du motif.
	 */
	private int largeur, hauteur;

	/**
	 * Méthode d'affichage du filtre (pour la transparence).
	 */
	private Composite comp;

	/**
	 * Construit un nouveau filtre ayant pour motif <code>motif</code> et
	 * pour transparence <code>alpha</code>.
	 * @param motif motif de ce filtre
	 * @param transparence du dessin
	 */
	public FiltreImage(Image motif, float alpha) {
		this.motif = motif;
		largeur = motif.getWidth(null);
		hauteur = motif.getHeight(null);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}

	/**
	 * Construit un nouveau filtre n'ayant aucun motif.
	 */
	public FiltreImage() {
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
		largeur = motif.getWidth(null);
		hauteur = motif.getHeight(null);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

		// Notification des écrans
		for (Ecran ecran : getEcrans()) {
			ecran.repaint();
		}
	}

	@Override
	public void dessiner(Graphics g) {
		if (motif == null) return;
		Rectangle zone = g.getClipBounds();

		final int iMin = zone.x / largeur;
		final int jMin = zone.y / hauteur;

		final int iMax = ((zone.x + zone.width - 1) + largeur - 1) / largeur;
		final int jMax = ((zone.y + zone.height - 1) + hauteur - 1) / hauteur;

		if (g instanceof Graphics2D) {
			// On utilise Graphics2D pour manipuler les composites
			Graphics2D g2d = (Graphics2D) g;
			Composite compPrec = g2d.getComposite();
			g2d.setComposite(comp);

			for (int i = iMin; i <= iMax; i++)
				for (int j = jMin; j <= jMax; j++)
					g2d.drawImage(motif, i * largeur, j * hauteur, null);

			g2d.setComposite(compPrec);
		} else {
			// On ne peut pas prendre en compte la transparence
			for (int i = iMin; i <= iMax; i++)
				for (int j = jMin; j <= jMax; j++)
					g.drawImage(motif, i * largeur, j * hauteur, null);
		}
	}
}
