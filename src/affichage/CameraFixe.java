package affichage;

import modele.Carte;

public class CameraFixe extends Camera {
	public CameraFixe() {}
	
	public CameraFixe(Carte carte) {
		setCarte(carte);
	}

	public void setCarte(Carte carte) {
		super.setCarte(carte);
	}
}
