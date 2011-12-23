package evenements;

import ressources.Apparence;
import modele.Case;
import modele.Personnage;

public class Transformateur implements ActionSol {
	private static final long serialVersionUID = 42L;
	
	private Apparence apparence;
	
	public Transformateur(Apparence apparence) {
		this.apparence = apparence;
	}
	
	public Apparence getApparence() {
		return apparence;
	}
	
	public void setApparence(Apparence apparence) {
		this.apparence = apparence;
	}
	
	@Override
	public void declencher(Case source, Personnage perso) {
		perso.setApparence(apparence);
	}
}
