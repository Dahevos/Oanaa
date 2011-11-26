package affichage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Ecran extends JPanel {
	private Personnage perso = null;
	private Carte carte = null;
	private int xBase = 0, yBase = 0;
	private BufferedImage image = null;
	private Graphics g = null;
	private final EcouteurPerso ecouteur = new Ecouteur();

	public Ecran() {
		redimensionner();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redimensionner();
				super.componentResized(e);
			}
		});
		setFocusable(true);
	}

	public Ecran(Personnage perso) {
		this();
		setPerso(perso);
	}

	public Ecran(Carte carte) {
		this();
		setCarte(carte);
	}

	public void setPerso(Personnage perso) {
		if (perso == this.perso) return;
		if (this.perso != null) {
			this.perso.supprimerEcouteur(ecouteur);
			this.perso.setAuto(true);
		}
		this.perso = perso;
		if (perso == null) {
			xBase = 0;
			yBase = 0;
			if (carte != null) {
				carte.dessiner(g, 0, 0, 0, 0, getWidth(), getHeight());
				repaint();
			}
		} else {
			xBase = perso.getX() - getWidth() / 2 + 16;
			yBase = perso.getY() - getHeight() / 2 + 24;
			changerCarte(perso.getCarte());
			perso.setAuto(false);
			perso.ajouterEcouteur(ecouteur);
		}
	}

	public Personnage getPerso() {
		return perso;
	}

	public void setCarte(Carte carte) {
		if (perso != null) {
			perso.supprimerEcouteur(ecouteur);
			perso.setAuto(true);
		}
		perso = null;
		xBase = 0;
		yBase = 0;
		changerCarte(carte);
	}

	public Carte getCarte() {
		return carte;
	}

	private void changerCarte(Carte carte) {
		if (this.carte != null) this.carte.setEcran(null);
		this.carte = carte;
		if (carte != null) {
			carte.setEcran(this);
			if (image != null) {
				carte.dessiner(g, xBase, yBase, xBase, yBase, getWidth(), getHeight());
				repaint();
			}
		} else if (image != null) {
			g.fillRect(0, 0, getWidth(), getHeight());
			repaint();
		}
	}

	synchronized private void redimensionner() {
		final int largeur = getWidth();
		final int hauteur = getHeight();
		if (perso != null) {
			xBase = perso.getX() - largeur / 2 + 16;
			yBase = perso.getY() - hauteur / 2 + 24;
		}

		if (largeur > 0 && hauteur > 0) {
			image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
			if (g != null) g.dispose();
			g = image.createGraphics();
			g.setColor(Color.BLACK);

			if (carte != null) {
				carte.dessiner(g, xBase, yBase, xBase, yBase, largeur, hauteur);
			} else {
				g.fillRect(0, 0, largeur, hauteur);
			}
			repaint();
		} else {
			image = null;
			g = null;
		}
	}

	synchronized public void rafraichirCarte(int xMin, int yMin, int xMax, int yMax) {
		if (image == null || carte == null) return;

		xMin = xMin < xBase ? xBase : xMin;
		yMin = yMin < yBase ? yBase : yMin;

		final int xLim = xBase + getWidth() - 1;
		final int yLim = yBase + getHeight() - 1;
		xMax = xMax > xLim ? xLim : xMax;
		yMax = yMax > yLim ? yLim : yMax;

		final int largeur = xMax - xMin;
		final int hauteur = yMax - yMin;
		if (largeur <= 0 || hauteur <= 0) return;

		carte.dessiner(g, xBase, yBase, xMin, yMin, largeur, hauteur);
		repaint(xMin - xBase, yMin - yBase, largeur, hauteur);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (image == null) super.paintComponent(g);
		else g.drawImage(image, 0, 0, null);
	}

	private class Ecouteur implements EcouteurPerso {
		@Override
		public void carteChangee(Carte carte) {
			changerCarte(carte);
		}

		@Override
		public void persoBouge(Direction dir) {
			synchronized (Ecran.this) {
				final int largeur = getWidth();
				final int hauteur = getHeight();

				xBase = perso.getX() - largeur / 2 + 16;
				yBase = perso.getY() - hauteur / 2 + 24;

				if (image == null) return;

				switch (dir) {
				case BAS:
					g.copyArea(0, 8, largeur, hauteur - 8, 0, -8);
					carte.dessiner(g, xBase, yBase, xBase, yBase + hauteur - 8, largeur, 8);
					carte.dessiner(g, xBase, yBase, perso.getX(), perso.getY() - 8, 32, 56);
					break;
				case GAUCHE:
					g.copyArea(0, 0, largeur - 8, hauteur, 8, 0);
					carte.dessiner(g, xBase, yBase, xBase, yBase, 8, hauteur);
					carte.dessiner(g, xBase, yBase, perso.getX(), perso.getY(), 40, 48);
					break;
				case DROITE:
					g.copyArea(8, 0, largeur - 8, hauteur, -8, 0);
					carte.dessiner(g, xBase, yBase, xBase + largeur - 8, yBase, 8, hauteur);
					carte.dessiner(g, xBase, yBase, perso.getX() - 8, perso.getY(), 40, 48);
					break;
				case HAUT:
					g.copyArea(0, 0, largeur, hauteur - 8, 0, 8);
					carte.dessiner(g, xBase, yBase, xBase, yBase, largeur, 8);
					carte.dessiner(g, xBase, yBase, perso.getX(), perso.getY(), 32, 56);
					break;
				}
				repaint();
			}
		}
	}
}
