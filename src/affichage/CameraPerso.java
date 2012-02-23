package affichage;

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

	public void setPerso(Personnage perso) {
		if (this.perso == perso) return;
		if (this.perso != null) {
			this.perso.retirerEcouteur(ecouteur);
		}
		this.perso = perso;
		if (perso != null) {
			perso.ajouterEcouteur(ecouteur);
			recalculerBase();
			super.setCarte(perso.getCarte());
		} else {
			super.setCarte(null);
		}
		rafraichir();
	}

	synchronized private void recalculerBase(int largeur, int hauteur) {
		if (perso == null) return;
		setBase(perso.getX() - largeur / 2 + 16, perso.getY() - hauteur / 2 + 24);
	}

	synchronized private void recalculerBase() {
		recalculerBase(getLargeur(), getHauteur());
	}

	@Override
	synchronized public void redimensionner(int largeur, int hauteur) {
		recalculerBase(largeur, hauteur);
		super.redimensionner(largeur, hauteur);
	}

	private class Ecouteur implements EcouteurPerso {
		@Override
		public boolean carteChangee(Personnage perso, Carte carte) {
			synchronized (CameraPerso.this) {
				CameraPerso.super.setCarte(carte);
				recalculerBase();
				rafraichir();
				return true;
			}
		}

		@Override
		public boolean persoBouge(Personnage perso, Direction dir) {
			synchronized (CameraPerso.this) {
				// Déplacement de la caméra
				deplacer(dir, perso.increment);

				// Mise à jour de la zone du personnage
				switch (dir) {
				case BAS:
					redessiner(perso.getI(), perso.getJ() - 1, perso.getI(), perso.getJ() + 1);
					break;
				case GAUCHE:
					redessiner(perso.getI() - 1, perso.getJ() - 1, perso.getI(), perso.getJ());
					break;
				case DROITE:
					redessiner(perso.getI(), perso.getJ() - 1, perso.getI() + 1, perso.getJ());
					break;
				case HAUT:
					redessiner(perso.getI(), perso.getJ() - 2, perso.getI(), perso.getJ());
					break;
				}
			}

			// Notification des écrans
			for (Ecran ecran : getEcrans()) {
				ecran.repaint();
			}
			
			return true;
		}
	}
}
