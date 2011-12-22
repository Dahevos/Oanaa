package affichage;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import modele.Carte;

public class CameraFantome extends Camera {
	private final EcouteurClavier ecouteur = new EcouteurClavier();
	private int xBaseMax = 0, yBaseMax = 0;
	private int increment = 8;

	public CameraFantome() {}

	public CameraFantome(Carte carte) {
		setCarte(carte);
	}

	public CameraFantome(Ecran ecran) {
		setEcran(ecran);
	}

	public CameraFantome(Carte carte, Ecran ecran) {
		setCarte(carte);
		setEcran(ecran);
	}
	
	public CameraFantome(int increment) {
		this.increment = increment;
	}

	public CameraFantome(Carte carte, int increment) {
		setCarte(carte);
		this.increment = increment;
	}

	public CameraFantome(Ecran ecran, int increment) {
		setEcran(ecran);
		this.increment = increment;
	}

	public CameraFantome(Carte carte, Ecran ecran, int increment) {
		setCarte(carte);
		setEcran(ecran);
		this.increment = increment;
	}

	@Override
	public void setEcran(Ecran ecran) {
		if (this.ecran != null) this.ecran.removeKeyListener(ecouteur);
		super.setEcran(ecran);
		if (ecran != null) ecran.addKeyListener(ecouteur);
	}

	public void setCarte(Carte carte) {
		super.setCarte(carte);
		xBase = 0;
		yBase = 0;
		recalculerBaseMax();
	}
	
	public int getIncrement() {
		return increment;
	}
	
	public void setIncrement(int increment) {
		this.increment = increment;
	}

	synchronized private void recalculerBaseMax() {
		recalculerBaseMax(largeur, hauteur);
	}

	synchronized private void recalculerBaseMax(int largeur, int hauteur) {
		if (carte != null) {
			xBaseMax = carte.getLargeur() * 32 - largeur;
			yBaseMax = carte.getHauteur() * 32 - hauteur;
		} else {
			xBaseMax = 0;
			yBaseMax = 0;
		}
	}

	@Override
	synchronized void redimensionner(int largeur, int hauteur, Graphics g) {
		recalculerBaseMax(largeur, hauteur);
		super.redimensionner(largeur, hauteur, g);
	}

	private class EcouteurClavier extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			synchronized (CameraFantome.this) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					if (yBase < yBaseMax) {
						yBase += increment;
						yBase = yBase > yBaseMax ? yBaseMax : yBase;
						yLim = yBase + largeur - 1;
						if (ecran == null || g == null) return;
						synchronized (g) {
							g.copyArea(0, increment, largeur, hauteur - increment, 0, -increment);
							carte.dessiner(g, xBase, yBase, xBase, yBase + hauteur - increment, largeur, increment);	
						}
					}
					break;
				case KeyEvent.VK_LEFT:
					if (xBase > 0) {
						xBase -= increment;
						xBase = xBase < 0 ? 0 : xBase;
						xLim = xBase + hauteur - 1;
						if (ecran == null || g == null) return;
						synchronized (g) {
							g.copyArea(0, 0, largeur - increment, hauteur, increment, 0);
							carte.dessiner(g, xBase, yBase, xBase, yBase, increment, hauteur);
						}
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (xBase < xBaseMax) {
						xBase += increment;
						xBase = xBase > xBaseMax ? xBaseMax : xBase;
						xLim = xBase + hauteur - 1;
						if (ecran == null || g == null) return;
						synchronized (g) {
							g.copyArea(increment, 0, largeur - increment, hauteur, -increment, 0);
							carte.dessiner(g, xBase, yBase, xBase + largeur - increment, yBase, increment, hauteur);
						}
					}
					break;
				case KeyEvent.VK_UP:
					if (yBase > 0) {
						yBase = yBase < 0 ? 0 : yBase;
						yLim = yBase + largeur - 1;
						if (ecran == null || g == null) return;
						synchronized (g) {
							g.copyArea(0, 0, largeur, hauteur - increment, 0, increment);
							carte.dessiner(g, xBase, yBase, xBase, yBase, largeur, increment);
						}
					}
					break;
				}
			}
			ecran.repaint();
		}
	}
}
