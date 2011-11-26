package ressources;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Classe contenant une collection d'images, destinées à décorer une carte.
 * @author Cédric CONNES
 * @author Sébastien LENTINI
 */
public class Theme {
	/**
	 * Largeur de ce thème (nombre de cases horizontales)
	 */
	private final int largeur;
	
	/**
	 * Hauteur de ce thème (nombre de cases verticales)
	 */
	private final int hauteur;
	
	private final String nom;
	private final File fichier;
	private BufferedImage theme;
	private final HashMap<Integer, Element> elements = new HashMap<Integer, Element>();
	
	/**
	 * Thème vide (aucune case)
	 */
	public static final Theme THEME_VIDE = new Theme();
	
	/**
	 * Constructeur par défaut (utilisé uniquement pour <code>THEME_VIDE</code>).
	 */
	private Theme() {
		nom = "THEME_VIDE";
		fichier = null;
		theme = null;
		largeur = 0;
		hauteur = 0;
	}

	/**
	 * Charge une nouveau thème à partir du fichier <code>fichier</code>.
	 * @param fichier image contenant les données du thème
	 * @throws IOException si le thème ne peut pas être chargé
	 */
	public Theme(String nom, File fichier) throws IOException {
		// Lecture de l'image
		this.nom = nom;
		this.fichier = fichier;
		theme = ImageIO.read(fichier);
		int largeur = theme.getWidth();
		int hauteur = theme.getHeight();
		
		// Vérification des dimensions
		if (largeur % 32 != 0 || hauteur % 32 != 0)
			throw new IOException("Les dimensions de l'image " + fichier + " sont incorrectes");

		this.largeur = largeur / 32;
		this.hauteur = hauteur / 32;	
	}

	/**
	 * Retourne la largeur de ce thème (nombre de cases horizontales).
	 * @return la largeur de ce thème
	 */
	public int getLargeur() {
		return largeur;
	}
	
	/**
	 * Retourne la hauteur de ce thème (nombre de cases verticales).
	 * @return la hauteur de ce thème
	 */
	public int getHauteur() {
		return hauteur;
	}
	
	/**
	 * Retourne l'image complète de ce thème.
	 * @return l'image complète de ce thème
	 * @throws IOException si le thème ne peut pas être chargé
	 */
	public BufferedImage getImage() throws IOException {
		if (theme == null) theme = ImageIO.read(fichier);
		return theme;
	}
	
	/**
	 * Retourne l'élément (<code>i</code>, <code>j</code>) de ce thème.
	 * @param i numéro de la ligne
	 * @param j numéro de la colonne
	 * @return l'élément spécifié
	 * @throws IOException si l'élément ne peut pas être chargé
	 */
	public Element getElement(int i, int j) throws IOException {
		if (i >= largeur || j >= hauteur) throw new IOException(
				"Il n'y a pas d'élément (" + i + ", " + j +") dans le thème " + fichier);
		
		final int cle = i + largeur * j;
		Element res = elements.get(cle);
		if (res == null) {
			if (theme == null) theme = ImageIO.read(fichier);
			BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(theme.getSubimage(32 * i, 32 * j, 32, 32), 0, 0, null);
			res = new Element(nom, i, j, image);
			elements.put(cle, res);
		}
		
		return res;
	}
	
	public void nettoyer() {
		theme = null;
	}
}