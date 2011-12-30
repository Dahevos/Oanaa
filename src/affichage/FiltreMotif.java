package affichage;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * Classe abstraite représentant un filtre à motif.
 */
public class FiltreMotif extends Filtre {
	/**
	 * Motif de ce filtre.
	 */
	private Image motif;

	/**
	 * Marges (décalages horizontaux et verticaux pour l'affichage).
	 * Le code doit garantir que : 0 <= margeX < largeurMotif et 0 <= margeY < hauteurMotif.
	 */
	private int margeX = 0, margeY = 0;

	/**
	 * Dimensions du motif.
	 */
	private int largeurMotif, hauteurMotif;

	/**
	 * Méthode d'affichage du filtre (pour la transparence).
	 */
	private Composite comp;

	/**
	 * Construit un nouveau filtre ayant pour motif <code>motif</code> et
	 * pour transparence <code>alpha</code>.
	 * @param motif motif de ce filtre
	 * @param alpha transparence du dessin
	 */
	public FiltreMotif(Image motif, float alpha) {
		this.motif = motif;
		largeurMotif = motif.getWidth(null);
		hauteurMotif = motif.getHeight(null);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}

	/**
	 * Construit un nouveau filtre sans motif.
	 */
	public FiltreMotif() {
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
	 * Modifie le motif de ce filtre et réinitialise les marges.
	 * @param motif nouveau motif
	 */
	public void setMotif(Image motif, float alpha) {
		this.motif = motif;
		margeX = 0;
		margeY = 0;
		largeurMotif = motif.getWidth(null);
		hauteurMotif = motif.getHeight(null);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

		// Notification des écrans
		for (Ecran ecran : getEcrans()) {
			ecran.repaint();
		}
	}

	/**
	 * Retourne la marge horizontale de ce filtre.
	 * @return la marge horizontale
	 */
	public int getMargeX() {
		return margeX;
	}

	/**
	 * Retourne la marge verticale de ce filtre.
	 * @return la marge verticale
	 */
	public int getMargeY() {
		return margeY;
	}

	/**
	 * Modifie les marges de ce filtre et notifie les écrans.
	 * @param margeX nouvelle marge horizontale
	 * @param margeY nouvelle marge verticale
	 */
	public void setMarges(int margeX, int margeY) {
		margeX %= largeurMotif;
		margeY %= hauteurMotif;
		this.margeX = margeX < 0 ? largeurMotif + margeX : margeX;
		this.margeY = margeY < 0 ? hauteurMotif + margeY : margeY;

		// Notification des écrans
		for (Ecran ecran : getEcrans()) {
			ecran.repaint();
		}
	}

	@Override
	public void dessiner(Graphics g, int largeur, int hauteur) {
		if (motif == null) return;
		Rectangle zone = g.getClipBounds();

		final int iMin = (zone.x + margeX) / largeurMotif;
		final int jMin = (zone.y + margeY) / hauteurMotif;

		final int iMax = ((zone.x + margeX + zone.width - 1) + largeurMotif - 1) / largeurMotif;
		final int jMax = ((zone.y + margeY + zone.height - 1) + hauteurMotif - 1) / hauteurMotif;

		if (g instanceof Graphics2D) {
			// On utilise Graphics2D pour pouvoir manipuler les composites
			Graphics2D g2d = (Graphics2D) g;
			Composite compPrec = g2d.getComposite();
			g2d.setComposite(comp);

			for (int i = iMin; i <= iMax; i++)
				for (int j = jMin; j <= jMax; j++)
					g2d.drawImage(motif, i * largeurMotif - margeX, j * hauteurMotif - margeY, null);

			g2d.setComposite(compPrec);
		} else {
			// On ne peut pas prendre en compte la transparence
			for (int i = iMin; i <= iMax; i++)
				for (int j = jMin; j <= jMax; j++)
					g.drawImage(motif, i * largeurMotif - margeX, j * hauteurMotif - margeY, null);
		}
	}
}
