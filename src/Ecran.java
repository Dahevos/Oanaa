import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Ecran extends JPanel {
	private Personnage joueur;
	private int largeur, hauteur;
	
	public Ecran(Personnage joueur) {
		this(joueur, 20);
	}
	
	public Ecran(Personnage joueur, int fps) {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redimensionner();
			}
		});
		redimensionner();
		this.joueur = joueur;
		addKeyListener(joueur);
		new Thread(new Rafraichissement(1000L / fps)).start();
		setFocusable(true);
	}
	
	private void redimensionner() {
		final Dimension taille = getSize();
		largeur = (taille.width + 31) / 32;
		hauteur = (taille.height + 31) / 32;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Carte carte = joueur.getCarte();
		if (carte == null) return;
		final int x = joueur.getX() - largeur / 2 + 1;
		final int y = joueur.getY() - hauteur / 2 + 1;
		carte.dessiner(g, x, y, largeur, hauteur);
	}

	private class Rafraichissement implements Runnable {
		private final long temps;
		
		public Rafraichissement(long temps) {
			this.temps = temps;
		}
		
		@Override
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(temps);
				} catch (InterruptedException e) {}
			}
		}
	}
}
