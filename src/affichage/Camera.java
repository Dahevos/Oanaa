package affichage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import ressources.Ressources;

import modele.Carte;
import modele.Direction;

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
	private Carte carte = null;

	/**
	 * Ecran utilisé pour l'affichage
	 */
	private Set<Ecran> ecrans = new HashSet<Ecran>();

	/**
	 * Image "OffScreen", contenant la scène
	 */
	private BufferedImage image = null;

	/**
	 * Objet utilisé pour dessiner dans l'offscreen
	 */
	private Graphics g = null;

	/**
	 * Coordonnées de la première case de l'offscreen
	 */
	private int iBaseImg = 0, jBaseImg = 0;

	/**
	 * Coordonnées du premier pixel de l'offscreen
	 */
	private int xBaseImg = 0, yBaseImg = 0;

	/**
	 * Dimensions de l'offscreen (en nombre de cases)
	 */
	private int nbColImg = 0, nbLigImg = 0;

	/**
	 * Dimensions de l'offscreen (en pixels)
	 */
	private int largeurImg = 0, hauteurImg = 0;

	/**
	 * Coordonnées de la dernière case de l'offscreen
	 */
	private int iLimImg = 0, jLimImg = 0;

	/**
	 * Coordonnées du dernier pixel de l'offscreen
	 */
	private int xLimImg = 0, yLimImg = 0;

	/**
	 * Coordonnées de la zone à afficher (dans le repère de la carte)
	 */
	private int xBase = 0, yBase = 0;

	/**
	 * Dimensions de la zone à afficher
	 */
	private int largeur = 0, hauteur = 0;


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
	 * Modifie la carte à afficher et réinitialise la position de la caméra.
	 * @param carte nouvelle carte à afficher
	 */
	protected void setCarte(Carte carte) {
		if (this.carte == carte) return;
		if (this.carte != null) this.carte.retirerCamera(this);
		this.carte = carte;
		if (carte != null) {
			carte.ajouterCamera(this);
			tailleOptimale.setSize(32 * carte.getLargeur(), 32 * carte.getHauteur());
		} else {
			tailleOptimale.setSize(0, 0);
		}
		setBase(0, 0);
		rafraichir();
	}

	/**
	 * Retourne les écrans utilisés pour l'affichage.
	 * @return les écrans utilisés pour l'affichage
	 */
	public Set<Ecran> getEcrans() {
		return ecrans;
	}

	/**
	 * Ajoute un écran à utiliser pour l'affichage.
	 * @param ecran le nouvel écran à utiliser pour l'affichage
	 * @return <code>true</code> ssi l'écran a bien été ajouté
	 */
	public boolean ajouterEcran(Ecran ecran) {
		if (ecran == null || ! ecrans.add(ecran)) return false;
		ecran.setCamera(this);
		ecran.setPreferredSize(tailleOptimale);
		ecran.repaint();
		return true;
	}

	/**
	 * Retire un des écrans à utiliser pour l'affichage.
	 * @param ecran l'écran à ne plus utiliser pour l'affichage
	 * @return <code>true</code> ssi l'écran a bien été retiré
	 */
	public boolean retirerEcran(Ecran ecran) {
		if (ecran == null || ! ecrans.remove(ecran)) return false;
		ecran.setCamera(null);
		ecran.setPreferredSize(null);
		ecran.repaint();
		return true;
	}

	/**
	 * Déplace la caméra, sans notifier les écrans.
	 * @param dir direction du déplacement
	 * @param increment longueur du déplacement
	 */
	protected void deplacer(Direction dir, int increment) {
		final int iBasePrec = iBaseImg, jBasePrec = jBaseImg;

		/*
		 * On modifie la base de la caméra suivant la direction et l'incrément.
		 * Si la case de base (BaseImg) a changé, il faut translater l'image et redessiner une
		 * rangée de cases, sinon il n'y a rien à faire. 
		 */
		switch (dir) {
		case BAS:
			setBase(xBase, yBase + increment);
			if (jBaseImg == jBasePrec || g == null) return;
			synchronized (g) {
				g.copyArea(0, 32, largeurImg, hauteurImg - 32, 0, -32);
				carte.dessiner(g, iBaseImg, jBaseImg, iBaseImg, jLimImg, iLimImg, jLimImg);	
			}
			break;
		case GAUCHE:
			setBase(xBase - increment, yBase);
			if (iBaseImg == iBasePrec || g == null) return;
			synchronized (g) {
				g.copyArea(0, 0, largeurImg - 32, hauteurImg, 32, 0);
				carte.dessiner(g, iBaseImg, jBaseImg, iBaseImg, jBaseImg, iBaseImg, jLimImg);
			}
			break;
		case DROITE:
			setBase(xBase + increment, yBase);
			if (iBaseImg == iBasePrec || g == null) return;
			synchronized (g) {
				g.copyArea(32, 0, largeurImg - 32, hauteurImg, -32, 0);
				carte.dessiner(g, iBaseImg, jBaseImg, iLimImg, jBaseImg, iLimImg, jLimImg);
			}
			break;
		case HAUT:
			setBase(xBase, yBase - increment);
			if (jBaseImg == jBasePrec || g == null) return;
			synchronized (g) {
				g.copyArea(0, 0, largeurImg, hauteurImg - 32, 0, 32);
				carte.dessiner(g, iBaseImg, jBaseImg, iBaseImg, jBaseImg, iLimImg, jBaseImg);
			}
			break;
		}
	}

	/**
	 * Place la caméra à la position donnée, sans redessiner la carte.
	 * @param xBase abscisse de la nouvelle base
	 * @param yBase ordonnée de la nouvelle base
	 */
	synchronized protected void setBase(int xBase, int yBase) {
		this.xBase = xBase;
		iBaseImg = xBase < 0 ? (xBase - 31) / 32 : xBase / 32;
		xBaseImg = 32 * iBaseImg;
		iLimImg = iBaseImg + nbColImg - 1;
		xLimImg = 32 * iLimImg;
		this.yBase = yBase;
		jBaseImg = yBase < 0 ? (yBase - 31) / 32 : yBase / 32;
		yBaseImg = 32 * jBaseImg;
		jLimImg = jBaseImg + nbLigImg - 1;
		yLimImg = 32 * jLimImg;
	}

	public int getxBase() {
		return xBase;
	}

	public int getyBase() {
		return yBase;
	}

	/**
	 * Redimensionne la caméra.
	 * @param largeur nouvelle largeur
	 * @param hauteur nouvelle hauteur
	 */
	synchronized public void redimensionner(int largeur, int hauteur) {
		// Mise à jour des attributs
		this.largeur = largeur < 0 ? 0 : largeur;
		nbColImg = (this.largeur + 30) / 32 + 1;
		largeurImg = 32 * nbColImg;
		iLimImg = iBaseImg + nbColImg - 1;
		xLimImg = 32 * iLimImg;
		this.hauteur = hauteur < 0 ? 0 : hauteur;
		nbLigImg = (this.hauteur + 30) / 32 + 1;
		hauteurImg = 32 * nbLigImg;
		jLimImg = jBaseImg + nbLigImg - 1;
		yLimImg = 32 * jLimImg;

		// Remplacement de l'offscreen
		if (largeur > 0 && hauteur > 0) {
			image = Ressources.getConfig().createCompatibleImage(
					largeurImg, hauteurImg, Transparency.TRANSLUCENT);
			g = image.createGraphics();
			g.setColor(Color.BLACK);
		} else {
			image = null;
			g = null;
		}

		// Dessin de la carte dans le nouvel offscreen
		rafraichir();
	}

	/**
	 * Retourne la largeur de la caméra
	 * @return la largeur de la caméra
	 */
	public int getLargeur() {
		return largeur;
	}

	/**
	 * Retourne la hauteur de la caméra
	 * @return la hauteur de la caméra
	 */
	public int getHauteur() {
		return hauteur;
	}
	
	/**
	 * Redessine la zone spécifiée, sans notifier les écrans.
	 * @param iMin indice de colonne de la première case
	 * @param jMin indice de ligne de la première case
	 * @param iMax indice de colonne de la dernière case
	 * @param jMax indice de ligne de la dernière case
	 */
	synchronized protected void redessiner(int iMin, int jMin, int iMax, int jMax) {
		if (g == null) return;
		synchronized (g) {
			if (carte != null)
				carte.dessiner(g, iBaseImg, jBaseImg, iMin, jMin, iMax, jMax);
			else
				g.fillRect(iMin - xBaseImg, jMin - yBaseImg, iMax - iMin + 1, jMax - jMin + 1);
		}
	}

	/**
	 * Redessine l'intégralité de l'offscreen et notifie les écrans
	 */
	synchronized protected void rafraichir() {
		// Dessin de la carte dans l'offscreen
		redessiner(iBaseImg, jBaseImg, iLimImg, jLimImg);
		
		// Notification des écrans
		for (Ecran ecran : ecrans) {
			ecran.repaint();
		}
	}

	/**
	 * Redessine la zone spécifiée et notifie les écrans.
	 * @param iMin indice de colonne de la première case
	 * @param jMin indice de ligne de la première case
	 * @param iMax indice de colonne de la dernière case
	 * @param jMax indice de ligne de la dernière case
	 */
	synchronized protected void rafraichir(int iMin, int jMin, int iMax, int jMax) {
		// Dessin de la carte dans l'offscreen
		redessiner(iMin, jMin, iMax, jMax);
		
		if (ecrans.isEmpty()) return;
		
		// Calcul de la zone à afficher
		final int xMin = Math.max(xBase, 32 * iMin);
		final int xMax = Math.min(xBase + largeur - 1, 32 * iMax + 31);
		if (xMax < xMin) return;
		
		final int yMin = Math.max(yBase, 32 * jMin);
		final int yMax = Math.min(yBase + hauteur - 1, 32 * jMax + 31);
		if (yMax < yMin) return;
		
		// Notification des écrans
		for (Ecran ecran : ecrans) {
			ecran.repaint(xMin - xBase, yMin - yBase, xMax - xMin + 1, yMax - yMin + 1);
		}
	}

	/**
	 * Rafraîchit la portion de carte spécifiée.
	 * Cette méthode fait appel à <code>rafraichir()</code>, mais uniquement sur les zones nécessaires.
	 * @param iMin indice de colonne de la première case
	 * @param jMin indice de ligne de la première case
	 * @param iMax indice de colonne de la dernière case
	 * @param jMax indice de ligne de la dernière case
	 */
	synchronized public void rafraichirCarte(int iMin, int jMin, int iMax, int jMax) {
		// Clipping de la zone
		iMin = iMin < iBaseImg ? iBaseImg : iMin;
		jMin = jMin < jBaseImg ? jBaseImg : jMin;

		iMax = iMax > iLimImg ? iLimImg : iMax;
		jMax = jMax > jLimImg ? jLimImg : jMax;

		if (iMax < iMin || jMax < jMin) return;

		// Dessin dans l'offscreen
		rafraichir(iMin, jMin, iMax, jMax);
	}

	/**
	 * Dessine l'image de la caméra dans la zone de dessin fournie.
	 * @param g zone de dessin à utiliser
	 */
	synchronized public void dessiner(Graphics g) {
		if (image == null) {
			g.fillRect(0, 0, largeur, hauteur);
		} else {
			//*
			g.drawImage(image, xBaseImg - xBase, yBaseImg - yBase, null);
			/*/
			g.drawImage(image.getSubimage(xBase - xBaseImg, yBase - yBaseImg, largeur, hauteur), 0, 0, null);
			//*/
		}
	}
	
	@Override
	public String toString() {
		return super.toString() + " :\n" +
				"\txBase = " + xBase + "\n" +
				"\tyBase = " + yBase + "\n" +
				"\tiBaseImg = " + iBaseImg + "\n" +
				"\tjBaseImg = " + jBaseImg + "\n" +
				"\txBaseImg = " + xBaseImg + "\n" +
				"\tyBaseImg = " + yBaseImg + "\n" +
				"\tlargeur = " + largeur + "\n" +
				"\thauteur = " + hauteur + "\n" +
				"\tnbColImg = " + nbColImg + "\n" +
				"\tnbLigImg = " + nbLigImg + "\n" +
				"\tlargeurImg = " + largeurImg + "\n" +
				"\thauteurImg = " + hauteurImg + "\n" +
				"\tiLimImg = " + iLimImg + "\n" +
				"\tjLimImg = " + jLimImg + "\n" +
				"\txLimImg = " + xLimImg + "\n" +
				"\tyLimImg = " + yLimImg + "\n";
	}
}
