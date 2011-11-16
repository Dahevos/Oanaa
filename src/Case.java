import java.awt.Graphics;
import java.awt.Image;



public class Case {
	private final Image couches[];
	private boolean libre = true;
	
	public Case(int nbCouches) {
		couches = new Image[nbCouches];
		for (int i = 0; i < nbCouches; i++) couches[i] = null;
	}
	
	public void setCouche(int couche, Image image) {
		couches[couche] = image;
	}
	
	public boolean estLibre() {
		return libre;
	}
	
	public void setLibre(boolean libre) {
		this.libre = libre;
	}
	
	public void dessinerCouche(int couche, Graphics g, int x, int y) {
		if (couches[couche] != null) g.drawImage(couches[couche], x, y, null);
	}
}
