package modele;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import affichage.Camera;

import ressources.Element;
import ressources.Ressources;

public class Carte implements Serializable {
	private static final long serialVersionUID = 42L;

	private static final int COUCHES_INF = 2;
	private static final int COUCHES_SUP = 2;

	private final Case[][] cases;
	private final int largeur;
	private final int hauteur;

	private transient ArrayList<Camera> cameras = new ArrayList<Camera>();

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

	public void ajouterCamera(Camera camera) {
		cameras.add(camera);
	}

	public void retirerCamera(Camera camera) {
		cameras.remove(camera);
	}

	public boolean existe(int i, int j) {
		return i >= 0 && j >= 0 && i < largeur && j < hauteur;
	}
	
	public int getLargeur() {
		return largeur;
	}
	
	public int getHauteur() {
		return hauteur;
	}

	public Case getCase(int i, int j) {
		return cases[i][j];
	}

	public void rafraichir(int iMin, int jMin, int iMax, int jMax) {
		for (Camera camera : cameras) {
			camera.rafraichir(32 * iMin, 32 * jMin, 32 * iMax + 31, 32 * jMax + 31);
		}
	}

	public void dessiner(Graphics g, int xBase, int yBase,
			int xMin, int yMin, int largeur, int hauteur) {
		// Calcul de la zone à dessiner
		int iMin = xMin < 0 ? (xMin - 31) / 32 : xMin / 32;
		int iMax = (xMin + largeur - 1) / 32;

		int jMin = yMin < 0 ? (yMin - 31) / 32 : yMin / 32;
		int jMax = (yMin + hauteur - 1) / 32;
		
		// System.out.println("Dessin de [" + iMin + ", " + iMax + "]x[" + jMin + ", " + jMax + "]");

		// Affichage des cases
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
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fichier)));
			out.writeObject(carte);
			out.close();
		} catch (IOException e) {
			System.err.println("Le fichier " + fichier + " ne peut pas être écrit :\n" + e);
		}
	}

	public void ecrire(File fichier) {
		ecrire(this, fichier);
	}
	
	public void ecrire(String nomFichier) {
		ecrire(new File("nomFichier"));
	}

	public static Carte lire(File fichier) {
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichier)));
			// in = new ObjectInputStream(new FileInputStream(fichier));
		} catch (IOException e) {
			System.err.println("Le fichier " + fichier + "ne peut pas être lu :\n" + e);
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
	
	public void exporterImage(File fichier, String type) throws IOException {
		BufferedImage image = Ressources.getConfig().createCompatibleImage(
				32 * largeur, 32 * hauteur, Transparency.TRANSLUCENT);
		dessiner(image.getGraphics(), 0, 0, 0, 0, 32 * largeur, 32 * hauteur);
		ImageIO.write(image, type, fichier);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		cameras = new ArrayList<Camera>();
	}
}
