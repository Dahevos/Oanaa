import java.awt.Graphics;
import java.awt.Image;

public class Carte {
	private final Case[][] cases;
	private final int largeur;
	private final int hauteur;
	
	public Carte(int largeur, int hauteur) {
		this(largeur, hauteur, null);
	}

	public Carte(int largeur, int hauteur, Image fond) {
		cases = new Case[largeur][hauteur];
		this.largeur = largeur;
		this.hauteur = hauteur;

		for (int i = 0; i < largeur; i++) for (int j = 0; j < hauteur; j++) {
			cases[i][j] = new Case();
			cases[i][j].setCouche(0, fond);
		}
	}
	
	public void setCouche(int i, int j, int couche, Image image) {
		cases[i][j].setCouche(couche, image);
	}
	
	public boolean isLibre(int i, int j) {
		return cases[i][j].isLibre();
	}
	
	public void setLibre(int i, int j, boolean libre) {
		cases[i][j].setLibre(libre);
	}
	
	public boolean existe(int i, int j) {
		return i >= 0 && j >= 0 && i < largeur && j < hauteur;
	}
	
	public void dessiner(Graphics g, int x, int y, int largeur, int hauteur) {
		int xMin = x < 0 ? 0 : x;
		int xMax = x + largeur - 1;
		xMax = xMax >= this.largeur ? this.largeur - 1 : xMax;
		
		int yMin = y < 0 ? 0 : y;
		int yMax = y + hauteur - 1;
		yMax = yMax >= this.hauteur ? this.hauteur - 1 : yMax;
		
		for (int j = yMin; j <= yMax; j++)
			for (int i = xMin; i <= xMax; i++)
				cases[i][j].dessiner(g, 32 * (i - x), 32 * (j - y));
	}
}
