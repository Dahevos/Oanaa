package ressources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

import javax.imageio.ImageIO;

import modele.Direction;


/**
 * Classe contenant l'apparence d'un personnage (images des différentes positions possibles).
 * @author Cédric CONNES
 * @author Sébastien LENTINI
 */
public class Apparence {
	/**
	 * Ensemble des images :
	 * <code>images[i][j]</code> = position <code>i</code> dans la direction <code>j</code>
	 */
	private final BufferedImage[][] images = new BufferedImage[4][4];
	
	/**
	 * Apparence par défaut.
	 */
	public static final Apparence APPARENCE_DEFAUT = new Apparence();
	
	/**
	 * Constructeur par défaut (utilisé uniquement pour <code>APPARENCE_DEFAUT</code>).
	 */
	private Apparence() {
		// TODO
	}
	
	/**
	 * Charge une nouvelle apparence depuis le fichier <code>fichier</code>.
	 * @param fichier image contenant les différentes positions
	 * @throws IOException si une erreur de chargement survient
	 */
	public Apparence(File fichier) throws IOException {
		BufferedImage ressource = ImageIO.read(fichier);
		int largeur = ressource.getWidth();
		int hauteur = ressource.getHeight();
		if (largeur != 128 || hauteur != 192)
			throw new InputMismatchException("Les dimensions de l'image " +
					fichier + " sont incorrectes");
		for (int j = 0; j < 4; j++) for (int i = 0; i < 4; i++)
			images[i][j] = ressource.getSubimage(32 * i, 48 * j, 32, 48);
	}
	
	/**
	 * Retourne l'image correspondant à la position spécifiée.
	 * @param dir direction du déplacement
	 * @param instant instant du déplacement (0 <= <code>instant</code> <= 3)
	 * @return l'image correspondante
	 */
	public BufferedImage getImage(Direction dir, int instant) {
		int pos;
		switch (dir) {
		case BAS: pos = 0; break;
		case GAUCHE: pos = 1; break;
		case DROITE: pos = 2; break;
		case HAUT: pos = 3; break;
		default: throw new RuntimeException("Direction invalide");
		}
		return images[instant][pos];
	}
}
