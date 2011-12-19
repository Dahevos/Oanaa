package ressources;
import java.awt.image.BufferedImage;
import java.io.ObjectStreamException;
import java.io.Serializable;


public class Element implements Serializable {
	/**
	 * Classe représentant l'identifiant d'un <code>Element</code> dans un <code>Theme</code>.
	 * <p>Remarque : visible uniquement dans le package <code>ressources</code>.
	 */
	static class Cle implements Serializable {
		private static final long serialVersionUID = 42L;
		
		/**
		 * Numéro de la ligne
		 */
		public final int i;
		
		/**
		 * Numéro de la colonne
		 */
		public final int j;
		
		/**
		 * Hashcode de cette clé
		 */
		private final int hashCode;
		
		/**
		 * Construit une nouvelle Clé
		 * @param i numéro de la ligne
		 * @param j numéro de la colonne
		 */
		public Cle(Theme theme, int i, int j) {
			this.i = i;
			this.j = j;
			hashCode = i + theme.getLargeur() * j;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (super.equals(obj)) return true;
			if (obj instanceof Cle) return ((Cle) obj).hashCode == hashCode;
			return false;
		}
		
		@Override
		public int hashCode() {
			return hashCode;
		}
		
		@Override
		public String toString() {
			return "(" + i + ", " + j + ")";
		}
	}
	
	private static final long serialVersionUID = 42L;

	private final String nomTheme;
	private final Cle cle;
	private final transient BufferedImage image;
	
	public Element(String nomTheme, Cle cle, BufferedImage image) {
		this.nomTheme = nomTheme;
		this.cle = cle;
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		return nomTheme + cle;
	}
	
	private Object readResolve() throws ObjectStreamException {
		return Ressources.getElement(nomTheme, cle);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (image != null) System.out.println("Element " + cle + " libéré");
	}
}
