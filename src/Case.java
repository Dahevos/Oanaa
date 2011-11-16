import java.awt.Graphics;
import java.awt.Image;



public class Case {
	public static final int NB_COUCHES = 5;

	private final Image couches[] = new Image[NB_COUCHES];
	private boolean libre = true;
	
	public Case() {
		for (int i = 0; i < NB_COUCHES; i++) couches[i] = null;
	}
	
	public void setCouche(int couche, Image image) {
		couches[couche] = image;
	}
	
	public boolean isLibre() {
		return libre;
	}
	
	public void setLibre(boolean libre) {
		this.libre = libre;
	}
	
	public void dessiner(Graphics g, int x, int y) {
		for (int i = 0; i < NB_COUCHES; i++)
			if (couches[i] != null) g.drawImage(couches[i], x, y, null);
	}
}
