import java.awt.image.BufferedImage;
import java.io.ObjectStreamException;
import java.io.Serializable;


public class Element implements Serializable {
	private static final long serialVersionUID = 42L;

	private final String nomTheme;
	private final int i, j;
	private final transient BufferedImage image;
	
	public Element(String nomTheme, int i, int j, BufferedImage image) {
		this.nomTheme = nomTheme;
		this.i = i;
		this.j = j;
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	private Object readResolve() throws ObjectStreamException {
		return Ressources.getElement(nomTheme, i, j);
	}
}
