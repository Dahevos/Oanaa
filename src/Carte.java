import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Carte {
	private static final int COUCHES_INF = 2;
	private static final int COUCHES_SUP = 2;

	private final Case[][] cases;
	private final int largeur;
	private final int hauteur;

	private Ecran ecran = null;
	
	public Carte(int largeur, int hauteur) {
		this(largeur, hauteur, null);
	}

	public Carte(int largeur, int hauteur, Image fond) {
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

	public void dessiner(Graphics g, int xBase, int yBase, Rectangle zone) {
		// Calcul de la zone à dessiner
		int iMin = zone.x < 0 ? (zone.x - 31) / 32 : zone.x / 32;
		int iMax = ((zone.x + zone.width - 1) + 31) / 32;

		int jMin = zone.y < 0 ? (zone.y - 31) / 32 : zone.y / 32;
		int jMax = ((zone.y + zone.height - 1) + 31) / 32;

		// Affichage des cases
		g.setColor(Color.BLACK);
		for (int j = jMin; j <= jMax; j++)
			for (int i = iMin; i <= iMax; i++)
				if (i < 0 || j < 0 || i >= largeur || j >= hauteur)
					g.fillRect(32 * i - xBase, 32 * j - yBase, 32, 32);
				else
					cases[i][j].dessiner(g, 32 * i - xBase, 32 * j - yBase);
	}
}
