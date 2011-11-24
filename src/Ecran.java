import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Ecran extends JPanel implements EcouteurPerso {
	private Joueur joueur;
	private Carte carte;
	private int xBase, yBase;
	private BufferedImage image;
	private Graphics g;

	public Ecran(Joueur joueur) {
		this.joueur = joueur;
		joueur.ajouterEcouteur(this);
		carte = joueur.getCarte();
		carte.setEcran(this);
		redimensionner();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redimensionner();
				super.componentResized(e);
			}
		});
		addKeyListener(joueur);
		setFocusable(true);
	}

	synchronized private void redimensionner() {
		final int largeur = getWidth();
		final int hauteur = getHeight();
		xBase = joueur.getX() - largeur / 2 + 16;
		yBase = joueur.getY() - hauteur / 2 + 24;
		if (largeur > 0 && hauteur > 0) {
			image = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
			g = image.createGraphics();
			carte.dessiner(g, xBase, yBase, xBase, yBase, largeur, hauteur);
			repaint();
		} else {
			g = null;
			image = null;
		}
	}
	
	synchronized public void rafraichirCarte(int xMin, int yMin, int xMax, int yMax) {
		if (image == null) return;
		
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
		if (image == null) return;
		g.drawImage(image, 0, 0, null);
	}

	@Override
	public void carteChangee(Carte carte) {
		this.carte = carte;
		carte.dessiner(g, xBase, yBase, xBase, yBase, getWidth(), getHeight());
		repaint();
	}

	@Override
	synchronized public void persoBouge(Direction dir) {
		final int largeur = getWidth();
		final int hauteur = getHeight();
		
		xBase = joueur.getX() - largeur / 2 + 16;
		yBase = joueur.getY() - hauteur / 2 + 24;
		
		if (image == null) return;
		
		switch (dir) {
		case BAS:
			g.copyArea(0, 8, largeur, hauteur - 8, 0, -8);
			carte.dessiner(g, xBase, yBase, xBase, yBase + hauteur - 8, largeur, 8);
			carte.dessiner(g, xBase, yBase, joueur.getX(), joueur.getY() - 8, 32, 56);
			break;
		case GAUCHE:
			g.copyArea(0, 0, largeur - 8, hauteur, 8, 0);
			carte.dessiner(g, xBase, yBase, xBase, yBase, 8, hauteur);
			carte.dessiner(g, xBase, yBase, joueur.getX(), joueur.getY(), 40, 48);
			break;
		case DROITE:
			g.copyArea(8, 0, largeur - 8, hauteur, -8, 0);
			carte.dessiner(g, xBase, yBase, xBase + largeur - 8, yBase, 8, hauteur);
			carte.dessiner(g, xBase, yBase, joueur.getX() - 8, joueur.getY(), 40, 48);
			break;
		case HAUT:
			g.copyArea(0, 0, largeur, hauteur - 8, 0, 8);
			carte.dessiner(g, xBase, yBase, xBase, yBase, largeur, 8);
			carte.dessiner(g, xBase, yBase, joueur.getX(), joueur.getY(), 32, 56);
			break;
		}
		repaint();
	}
}
