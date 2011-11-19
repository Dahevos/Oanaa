import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

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
	
	/**
	 * Images des cases composant ce thème
	 */
	private final Image images[][];

	private Boolean charge[][];
	/**
	 * Thème vide (aucune case)
	 */
	public static final Theme THEME_VIDE = new Theme();
	
	/**
	 * Constructeur par défaut (utilisé uniquement pour <code>THEME_VIDE</code>).
	 */
	private Theme() {
		largeur = 0;
		hauteur = 0;
		images = null;
		charge = null;
	}

	/**
	 * Charge une nouveau thème à partir du fichier <code>fichier</code>.
	 * @param fichier image contenant les données du thème
	 * @throws IOException si une erreur de chargement survient
	 */
	public Theme(File fichier) throws IOException {
		// Lecture de l'image
		BufferedImage ressource = ImageIO.read(fichier);
		int largeur = ressource.getWidth();
		int hauteur = ressource.getHeight();
		
		// Vérification des dimensions
		if (largeur % 32 != 0 || hauteur % 32 != 0)
			throw new InputMismatchException("Les dimensions de l'image "
					+ fichier + " sont incorrectes");

		// Découpage de l'image
		this.largeur = largeur / 32;
		this.hauteur = hauteur / 32;
		images = new Image[largeur][hauteur];
		charge = new Boolean[largeur][hauteur];
		
		for (int i = 0; i < this.largeur; i++) for (int j = 0; j < this.hauteur; j++) {
			images[i][j] = ressource.getSubimage(32 * i, 32 * j, 32, 32);	
			charge[i][j] = false;
			// images[i][j].setData(images[i][j].getData());
			// images[i][j] = new Container().createImage(new FilteredImageSource(ressource.getSource(), new CropImageFilter(32 * i, 32 * j, 32, 32)));
		}
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
	 */
	public Image getImage(int i, int j) {
		charge[i][j] = true;
		return images[i][j];
	}
	
	
	
	public void nettoyer() {
	
		for (int i = 0; i < this.largeur; i++) for (int j = 0; j < this.hauteur; j++) {
			
			if (!charge[i][j])
				images[i][j] = null;
		}	
		charge = null;
		
		System.gc();
	}

}
