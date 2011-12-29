package affichage;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe abstraite définissant un filtre.
 */
public abstract class Filtre {
	/**
	 * Liste des écrans utilisés pour l'affichage.
	 */
	private Set<Ecran> ecrans = new HashSet<Ecran>();
	
	/**
	 * Ajoute un écran à utiliser pour l'affichage.
	 * @param ecran le nouvel écran à utiliser pour l'affichage
	 * @return <code>true</code> ssi l'écran a bien été ajouté
	 */
	boolean ajouterEcran(Ecran ecran) {
		return ecran != null && ecrans.add(ecran);
	}

	/**
	 * Retire un des écrans à utiliser pour l'affichage.
	 * @param ecran l'écran à ne plus utiliser pour l'affichage
	 * @return <code>true</code> ssi l'écran a bien été retiré
	 */
	boolean retirerEcran(Ecran ecran) {
		return ecran != null && ecrans.remove(ecran);
	}
	
	/**
	 * Retourne l'ensemble des écrans utilisés pour l'affichage.
	 * @return l'ensemble des écrans utilisés pour l'affichage
	 */
	public Set<Ecran> getEcrans() {
		return ecrans;
	}
	
	abstract public void dessiner(Graphics g);
}
