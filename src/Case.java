import java.awt.Graphics;
import java.awt.Image;



public class Case {
	public static final int NB_NIVEAUX = 3;

	private final Image niveaux[] = new Image[NB_NIVEAUX];
	private boolean libre = true;
	
	public Case() {
		for (int i = 0; i < NB_NIVEAUX; i++) niveaux[i] = null;
	}
	
	public void setNiveau(int niveau, Image image) {
		niveaux[niveau] = image;
	}
	
	public boolean isLibre() {
		return libre;
	}
	
	public void setLibre(boolean libre) {
		this.libre = libre;
	}
	
	public void dessiner(Graphics g, int x, int y) {
		for (int i = 0; i < NB_NIVEAUX; i++)
			if (niveaux[i] != null) g.drawImage(niveaux[i], x, y, null);
	}
}
