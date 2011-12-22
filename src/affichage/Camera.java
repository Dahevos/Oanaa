package affichage;

import java.awt.Dimension;
import java.awt.Graphics;

import modele.Carte;

/**
 * Classe abstraite représentant un caméra.<br>
 * Cette classe est la passerelle entre une carte et un écran : elle est chargée d'afficher une vue
 * de la carte dans la zone de dessin de l'écran.
 * Une carte étant souvent trop grande pour s'afficher entièrement dans un écran, cette classe permet
 * également de spécifier la zone à afficher.<br>
 * Les différentes sous-classes doivent donc fournir différents moyen de se déplacer dans la
 * carte afin de changer la zone à afficher.
 */
public abstract class Camera {
	/**
	 * Carte affichée par cette caméra
	 */
	protected Carte carte = null;
	
	/**
	 * Ecran utilisé pour l'affichage
	 */
	protected Ecran ecran = null;
	
	/**
	 * Objet de dessin utilisé pour dessiner dans l'offscreen de l'écran
	 */
	protected Graphics g = null;
	
	/**
	 * Coordonnées de la zone à afficher (dans le repère de la carte)
	 */
	protected int xBase = 0, yBase = 0;
	
	/**
	 * Dimensions de la zone à afficher
	 */
	protected int largeur = 0, hauteur = 0;
	
	/**
	 * Dernier pixel de la zone à afficher
	 */
	protected int xLim = 0, yLim = 0;
	
	/**
	 * Taille optimale de la caméra
	 */
	private final Dimension tailleOptimale = new Dimension();
	
	/**
	 * Retourne la carte affichée par cette caméra.
	 * @return la carte affichée par cette caméra
	 */
	public Carte getCarte() {
		return carte;
	}
	
	/**
	 * Définit la carte à afficher.
	 * @param carte nouvelle carte à afficher
	 */
	protected void setCarte(Carte carte) {
		if (this.carte == carte) return;
		if (this.carte != null) this.carte.retirerCamera(this);
		this.carte = carte;
		if (carte != null) {
			carte.ajouterCamera(this);
			tailleOptimale.setSize(carte.getLargeur() * 32, carte.getHauteur() * 32);
		} else {
			tailleOptimale.setSize(0, 0);
		}
		redessiner();
	}

	/**
	 * Retourne l'écran utilisé pour l'affichage.
	 * @return l'écran utilisé pour l'affichage
	 */
	public Ecran getEcran() {
		return ecran;
	}

	/**
	 * Définit l'écran utilisé pour l'affichage
	 * @param ecran le nouvel écran à utiliser pour l'affichage
	 */
	public void setEcran(Ecran ecran) {
		if (this.ecran == ecran) return;
		if (this.ecran != null) {
			Ecran ecranPrec = this.ecran;
			this.ecran = null;
			ecranPrec.setCamera(null);
			ecranPrec.setPreferredSize(null);
		}
		this.ecran = ecran;
		if (ecran != null) {
			ecran.setCamera(this);
			ecran.setPreferredSize(tailleOptimale);
			ecran.repaint();
		} else {
			g = null;
		}
	}

	/**
	 * Redimensionne la caméra
	 * @param largeur nouvelle largeur
	 * @param hauteur nouvelle hauteur
	 * @param g nouvelle objet de dessin
	 */
	synchronized void redimensionner(int largeur, int hauteur, Graphics g) {
		this.largeur = largeur;
		xLim = xBase + largeur - 1;
		this.hauteur = hauteur;
		yLim = yBase + hauteur - 1;
		this.g = g;
		redessiner();
	}

	/**
	 * Redessine l'intégralité de la zone à afficher dans l'écran
	 */
	synchronized protected void redessiner() {
		redessiner(xBase, yBase, largeur, hauteur);
	}

	/**
	 * Redessine la zone spécifiée dans l'écran.
	 * @param xMin abscisse de la zone
	 * @param yMin ordonnée de la zone
	 * @param largeur largeur de la zone
	 * @param hauteur hauteur de la zone
	 */
	synchronized protected void redessiner(int xMin, int yMin, int largeur, int hauteur) {
		if (ecran == null || g == null) return;
		synchronized (g) {
			if (carte != null)
				carte.dessiner(g, xBase, yBase, xMin, yMin, largeur, hauteur);
			else
				g.fillRect(0, 0, largeur, hauteur);
		}
		ecran.repaint(xMin - xBase, yMin - yBase, largeur, hauteur);
	}

	/**
	 * Rafraîchit la portion de carte spécifiée.
	 * Cette méthode permet de ne mettre à jour que les zones nécessaires.
	 * @param xMin abscisse du premier pixel
	 * @param yMin ordonnée du premier pixel
	 * @param xMax abscisse du dernier pixel
	 * @param yMax ordonnée du dernier pixel
	 */
	synchronized public void rafraichir(int xMin, int yMin, int xMax, int yMax) {
		if (ecran == null) return;

		xMin = xMin < xBase ? xBase : xMin;
		yMin = yMin < yBase ? yBase : yMin;

		xMax = xMax > xLim ? xLim : xMax;
		yMax = yMax > yLim ? yLim : yMax;

		final int largeur = xMax - xMin + 1;
		final int hauteur = yMax - yMin + 1;
		if (largeur <= 0 || hauteur <= 0) return;

		redessiner(xMin, yMin, largeur, hauteur);
	}
}
