import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Carte implements Serializable {
	private static final long serialVersionUID = 42L;

	private static final int COUCHES_INF = 2;
	private static final int COUCHES_SUP = 2;

	private final Case[][] cases;
	private final int largeur;
	private final int hauteur;

	private transient Ecran ecran = null;

	public Carte(int largeur, int hauteur) {
		this(largeur, hauteur, null);
	}

	public Carte(int largeur, int hauteur, Element fond) {
		cases = new Case[largeur][hauteur];
		this.largeur = largeur;
		this.hauteur = hauteur;

		for (int i = 0; i < largeur; i++) for (int j = 0; j < hauteur; j++) {
			cases[i][j] = new Case(this, i, j, COUCHES_INF, COUCHES_SUP);
			cases[i][j].setCouche(0, fond);
		}
	}

	public void setEcran(Ecran ecran) {
		this.ecran = ecran;
	}

	public Ecran getEcran() {
		return ecran;
	}

	public boolean existe(int i, int j) {
		return i >= 0 && j >= 0 && i < largeur && j < hauteur;
	}

	public Case getCase(int i, int j) {
		return cases[i][j];
	}

	public void rafraichir(int iMin, int jMin, int iMax, int jMax) {
		ecran.rafraichirCarte(32 * iMin, 32 * jMin, 32 * iMax + 31, 32 * jMax + 31);
	}

	public void dessiner(Graphics g, int xBase, int yBase,
			int xMin, int yMin, int largeur, int hauteur) {
		// Calcul de la zone à dessiner
		int iMin = xMin < 0 ? (xMin - 31) / 32 : xMin / 32;
		int iMax = ((xMin + largeur - 1) + 31) / 32;

		int jMin = yMin < 0 ? (yMin - 31) / 32 : yMin / 32;
		int jMax = ((yMin + hauteur - 1) + 31) / 32;

		// Affichage des cases
		g.setColor(Color.BLACK);
		for (int j = jMin; j <= jMax; j++)
			for (int i = iMin; i <= iMax; i++)
				if (i < 0 || j < 0 || i >= this.largeur || j >= this.hauteur)
					g.fillRect(32 * i - xBase, 32 * j - yBase, 32, 32);
				else
					cases[i][j].dessiner(g, 32 * i - xBase, 32 * j - yBase);
	}
	
	@Override
	public String toString() {
		return super.toString() + " : " + largeur + "x" + hauteur;
	}

	public static void ecrire(Carte carte, File fichier) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichier));
			out.writeObject(carte);
			out.close();
		} catch (IOException e) {
			System.err.println("Le fichier " + fichier + " ne peut pas être écrit :\n" + e);
		}
	}

	public void ecrire(File fichier) {
		ecrire(this, fichier);
	}

	public static Carte lire(File fichier) {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(fichier));
		} catch (IOException e) {
			System.err.println("Le fichier " + fichier + "ne peut pas être lu.");
			return null;
		}

		Object obj = null;
		try {
			obj = in.readObject();
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		}

		if ((obj != null) && (obj instanceof Carte)) {
			return (Carte) obj;
		} else {
			System.err.println("Le fichier " + fichier + " ne contient pas une carte valide.");
			return null;
		}
	}
}
