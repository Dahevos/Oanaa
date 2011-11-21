import java.awt.Image;
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
	
	private final File fichier;
	private BufferedImage theme;
	private final HashMap<Integer, Image> images = new HashMap<Integer, Image>();
	
	/**
	 * Thème vide (aucune case)
	 */
	public static final Theme THEME_VIDE = new Theme();
	
	/**
	 * Constructeur par défaut (utilisé uniquement pour <code>THEME_VIDE</code>).
	 */
	private Theme() {
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
	public Theme(File fichier) throws IOException {
		// Lecture de l'image
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
	 * Retourne la largeur de ce thème (nombre de cases horizontales)
	 * @return la largeur de ce thème
	 */
	public int getLargeur() {
		return largeur;
	}
	
	/**
	 * Retourne la hauteur de ce thème (nombre de cases verticales)
	 * @return la hauteur de ce thème
	 */
	public int getHauteur() {
		return hauteur;
	}
	
	/**
	 * Retourne l'image de la case (<code>i</code>, <code>j</code>) de ce thème.
	 * @param i numéro de la ligne
	 * @param j numéro de la colonne
	 * @return l'image de la case spécifiée
	 * @throws IOException si l'image ne peut pas être chargée
	 */
	public Image getImage(int i, int j) throws IOException {
		if (i >= largeur || j >= hauteur) throw new IOException(
				"Il n'y a pas de case (" + i + ", " + j +") dans le thème " + fichier);
		
		final int cle = i + largeur * j;
		Image res = images.get(cle);
		if (res == null) {
			if (theme == null) theme = ImageIO.read(fichier);
			BufferedImage image = theme.getSubimage(32 * i, 32 * j, 32, 32);
			res = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
			res.getGraphics().drawImage(image, 0, 0, null);
			images.put(cle, res);
		}
		
		return res;
	}
	
	public void nettoyer() {
		theme = null;
		System.gc();
	}
}
