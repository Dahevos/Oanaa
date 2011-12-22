package affichage;

import modele.Carte;

public class CameraFixe extends Camera {
	public CameraFixe() {}
	
	public CameraFixe(Carte carte) {
		setCarte(carte);
	}
	
	public CameraFixe(Ecran ecran) {
		setEcran(ecran);
	}
	
	public CameraFixe(Carte carte, Ecran ecran) {
		setCarte(carte);
		setEcran(ecran);
	}
	
	public void setCarte(Carte carte) {
		super.setCarte(carte);
	}
}
