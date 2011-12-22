package evenements;

import modele.Case;
import modele.Personnage;

public class Teleporteur implements ActionSol {
	private static final long serialVersionUID = 42L;
	
	private Case destination;
	
	public Teleporteur() {
		this(null);
	}
	
	public Teleporteur(Case dest) {
		this.destination = dest;
	}
	
	public Case getDestination() {
		return destination;
	}
	
	public void setDest(Case destination) {
		this.destination = destination;
	}
	
	@Override
	public void declencher(Case source, Personnage perso) {
		if (destination == null)
			perso.setCarte(null, 0, 0);
		else
			perso.setCarte(destination.getCarte(), destination.getI(), destination.getJ());
	}
}
