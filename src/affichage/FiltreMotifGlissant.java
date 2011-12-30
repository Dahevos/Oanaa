package affichage;

import java.awt.Image;

import evenements.Horloge;

public class FiltreMotifGlissant extends FiltreMotif {
	/**
	 * Code effectuant la translation du motif.
	 */
	private final Runnable translation = new Translation();
	
	/**
	 * Horloge responsable du déplacement.
	 */
	private Horloge horloge;
	
	/**
	 * Pas verticaux et horizontaux pour le déplacement.
	 */
	private int pasX, pasY;

	/**
	 * Construit un nouveau filtre glissant.
	 * @param motif motif de ce filtre
	 * @param alpha transparence du dessin
	 * @param horloge horloge à utiliser
	 * @param pasX pas de déplacement horizontal
	 * @param pasY pas de déplacement vertical
	 */
	public FiltreMotifGlissant(Image motif, float alpha, Horloge horloge, int pasX, int pasY) {
		super(motif, alpha);
		this.horloge = horloge;
		this.pasX = pasX;
		this.pasY = pasY;
		if (horloge != null) horloge.ajouterCode(translation);
	}

	/**
	 * Construit un nouveau filtre ayant pour motif <code>motif</code> et
	 * pour transparence <code>alpha</code>.
	 * @param motif motif de ce filtre
	 * @param alpha transparence du dessin
	 */
	public FiltreMotifGlissant(Image motif, float alpha) {
		this(motif, alpha, null, 0, 0);
	}

	/**
	 * Construit un nouveau filtre sans motif.
	 */
	public FiltreMotifGlissant() {
		this(null, 0f);
	}
	
	/**
	 * Modifie l'horloge à utiliser.
	 * @param horloge nouvelle horloge
	 */
	public void setHorloge(Horloge horloge) {
		if (this.horloge != null) this.horloge.retirerCode(translation);
		this.horloge = horloge;
		if (horloge != null) horloge.ajouterCode(translation);
	}
	
	/**
	 * Retourne l'horloge utilisée.
	 * @return l'horloge utilisée
	 */
	public Horloge getHorloge() {
		return horloge;
	}
	
	/**
	 * Modifie les pas horizontaux et verticaux du déplacement.
	 * @param pasX nouveau pas horizontal
	 * @param pasY nouveau pas vertical
	 */
	public void setPas(int pasX, int pasY) {
		this.pasX = pasX;
		this.pasY = pasY;
	}
	
	/**
	 * Retourne le pas horizontal du déplacement.
	 * @return la pas horizontal
	 */
	public int getPasX() {
		return pasX;
	}
	
	/**
	 * Retourne le pas vertical du déplacement.
	 * @return le pas vertical
	 */
	public int getPasY() {
		return pasY;
	}

	/**
	 * Classe responsable de la translation du motif.
	 */
	private class Translation implements Runnable {
		@Override
		public void run() {
			setMarges(getMargeX() - pasX, getMargeY() - pasY);
		}
	}
}
