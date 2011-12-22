package affichage;

import java.awt.Graphics;

import modele.Carte;
import modele.Direction;
import modele.EcouteurPerso;
import modele.Personnage;

public class CameraPerso extends Camera {
	private Personnage perso = null;
	private final EcouteurPerso ecouteur = new Ecouteur();

	public CameraPerso() {}

	public CameraPerso(Personnage perso) {
		setPerso(perso);
	}

	public CameraPerso(Ecran ecran) {
		setEcran(ecran);
	}

	public CameraPerso(Personnage perso, Ecran ecran) {
		setPerso(perso);
		setEcran(ecran);
	}

	public void setPerso(Personnage perso) {
		if (this.perso == perso) return;
		if (this.perso != null) this.perso.retirerEcouteur(ecouteur);
		this.perso = perso;
		if (perso != null) {
			perso.ajouterEcouteur(ecouteur);
			recalculerBase();
			super.setCarte(perso.getCarte());
		} else {
			super.setCarte(null);
		}
		redessiner();
	}

	synchronized private void recalculerBase(int largeur, int hauteur) {
		if (perso == null) return;
		xBase = perso.getX() - largeur / 2 + 16;
		xLim = xBase + largeur - 1;
		yBase = perso.getY() - hauteur / 2 + 24;
		yLim = yBase + hauteur - 1;
	}

	synchronized private void recalculerBase() {
		recalculerBase(largeur, hauteur);
	}

	@Override
	synchronized void redimensionner(int largeur, int hauteur, Graphics g) {
		recalculerBase(largeur, hauteur);
		super.redimensionner(largeur, hauteur, g);
	}
	
	private class Ecouteur implements EcouteurPerso {
		@Override
		public void carteChangee(Personnage perso, Carte carte) {
			synchronized (CameraPerso.this) {
				recalculerBase();
				CameraPerso.super.setCarte(carte);	
			}
		}

		@Override
		synchronized public void persoBouge(Personnage perso, Direction dir) {
			synchronized (CameraPerso.this) {
				recalculerBase();

				if (ecran == null || g == null) return;

				synchronized (g) {
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
				}
			}
			ecran.repaint();
		}
	}
}
