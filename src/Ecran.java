import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Ecran extends JPanel implements EcouteurPerso {
	private Joueur joueur;
	private Carte carte;
	private Direction dirEcran = null;

	public Ecran(Joueur joueur) {
		this(joueur, 20);
	}

	public Ecran(Joueur joueur, int fps) {
		this.joueur = joueur;
		joueur.ajouterEcouteur(this);
		carte = joueur.getCarte();
		carte.setEcran(this);
		addKeyListener(joueur);
		setFocusable(true);
	}

	synchronized public void rafraichirCarte(int xMin, int yMin, int xMax, int yMax) {
		final int xBase = joueur.getX() - getWidth() / 2 + 16;
		final int yBase = joueur.getY() - getHeight() / 2 + 24;
		xMin = xMin < xBase ? xBase : xMin;
		yMin = yMin < yBase ? yBase : yMin;

		final int xLim = xBase + getWidth() - 1;
		final int yLim = yBase + getHeight() - 1;
		xMax = xMax > xLim ? xLim : xMax;
		yMax = yMax > yLim ? yLim : yMax;

		final int largeur = xMax - xMin;
		final int hauteur = yMax - yMin;
		if (largeur > 0 && hauteur > 0)
			repaint(xMin - xBase, yMin - yBase, largeur, hauteur);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (carte == null) return;
		final int xBase = joueur.getX() - getWidth() / 2 + 16;
		final int yBase = joueur.getY() - getHeight() / 2 + 24;
		if (dirEcran == null) {
			Rectangle zone = g.getClipBounds();
			zone.x += xBase;
			zone.y += yBase;
			carte.dessiner(g, xBase, yBase, zone);
		} else {
			Direction dir = dirEcran;
			dirEcran = null;
			switch (dir) {
			case BAS:
				g.copyArea(0, 8, getWidth(), getHeight() - 8, 0, -8);
				carte.dessiner(g, xBase, yBase,
						new Rectangle(xBase, yBase + getHeight() - 8, getWidth(), 8));
				carte.dessiner(g, xBase, yBase,
						new Rectangle(joueur.getX(), joueur.getY() - 8, 32, 56));
				break;
			case GAUCHE:
				g.copyArea(0, 0, getWidth() - 8, getHeight(), 8, 0);
				carte.dessiner(g, xBase, yBase,
						new Rectangle(xBase, yBase, 8, getHeight()));
				carte.dessiner(g, xBase, yBase,
						new Rectangle(joueur.getX(), joueur.getY(), 40, 48));
				break;
			case DROITE:
				g.copyArea(8, 0, getWidth() - 8, getHeight(), -8, 0);
				carte.dessiner(g, xBase, yBase,
						new Rectangle(xBase + getWidth() - 8, yBase, 8, getHeight()));
				carte.dessiner(g, xBase, yBase,
						new Rectangle(joueur.getX() - 8, joueur.getY(), 40, 48));
				break;
			case HAUT:
				g.copyArea(0, 0, getWidth(), getHeight() - 8, 0, 8);
				carte.dessiner(g, xBase, yBase,
						new Rectangle(xBase, yBase, getWidth(), 8));
				carte.dessiner(g, xBase, yBase,
						new Rectangle(joueur.getX(), joueur.getY(), 32, 56));
				break;
			}
		}
	}

	@Override
	public void carteChangee(Carte carte) {
		this.carte = carte;
	}

	@Override
	synchronized public void persoBouge(Direction dir) {
		dirEcran = dir;
		repaint();
	}
}
