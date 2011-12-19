package evenements;

import java.io.Serializable;

import affichage.Case;
import affichage.Personnage;

public interface ActionSol extends Serializable {
	public void declencher(Case source, Personnage perso);
}
