import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Carte {
	private static final int COUCHES_INF = 2;
	private static final int COUCHES_SUP = 2;
	private static final int NB_COUCHES = COUCHES_INF + COUCHES_SUP;

	private final Case[][] cases;
	private final int largeur;
	private final int hauteur;

	private final ArrayList<Personnage> persos = new ArrayList<Personnage>();

	private static final Comparator<Personnage> comparateurPersos = new Comparator<Personnage>() {
		@Override
		public int compare(Personnage p1, Personnage p2) {
			return Integer.valueOf(p1.getJ()).compareTo(Integer.valueOf(p2.getJ()));
		}
	};
	
	public Carte(int largeur, int hauteur) {
		this(largeur, hauteur, null);
	}

	public Carte(int largeur, int hauteur, Image fond) {
		cases = new Case[largeur][hauteur];
		this.largeur = largeur;
		this.hauteur = hauteur;

		for (int i = 0; i < largeur; i++) for (int j = 0; j < hauteur; j++) {
			cases[i][j] = new Case(NB_COUCHES);
			cases[i][j].setCouche(0, fond);
		}
	}

	public void setCouche(int i, int j, int couche, Image image) {
		cases[i][j].setCouche(couche, image);
	}

	public boolean estLibre(int i, int j) {
		return cases[i][j].estLibre();
	}

	public void setLibre(int i, int j, boolean libre) {
		cases[i][j].setLibre(libre);
	}

	public boolean existe(int i, int j) {
		return i >= 0 && j >= 0 && i < largeur && j < hauteur;
	}

	public void dessiner(Graphics g, int xBase, int yBase, int largeur, int hauteur) {
		// Calcul de la zone à dessiner
		int iMin = (xBase < 0 ? 0 : xBase) / 32;
		int iMax = ((xBase + largeur - 1) + 31) / 32;
		iMax = iMax >= this.largeur ? this.largeur - 1 : iMax;

		int jMin = (yBase < 0 ? 0 : yBase) / 32;
		int jMax = ((yBase + hauteur - 1) + 31) / 32;
		jMax = jMax >= this.hauteur ? this.hauteur - 1 : jMax;

		// Affichage des couches inférieures
		for (int j = jMin; j <= jMax; j++)
			for (int i = iMin; i <= iMax; i++)
				for (int couche = 0; couche < COUCHES_INF; couche++)
					cases[i][j].dessinerCouche(couche, g, 32 * i - xBase, 32 * j - yBase);

		// Affichage des personnages
		Collections.sort(persos, comparateurPersos);
		for (Personnage perso : persos) {
			perso.dessiner(g, xBase, yBase);
		}
		
		// Affichage des couches supérieures
		for (int j = jMin; j <= jMax; j++)
			for (int i = iMin; i <= iMax; i++)
				for (int couche = COUCHES_INF; couche < NB_COUCHES; couche++)
					cases[i][j].dessinerCouche(couche, g, 32 * i - xBase, 32 * j - yBase);

	}

	public void ajouterPerso(Personnage perso) {
		persos.add(perso);
	}

	public void retirerPerso(Personnage perso) {
		persos.remove(perso);
	}
}
