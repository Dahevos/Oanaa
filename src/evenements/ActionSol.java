package evenements;

import java.io.Serializable;

import modele.Case;
import modele.Personnage;


public interface ActionSol extends Serializable {
	public void declencher(Case source, Personnage perso);
}
