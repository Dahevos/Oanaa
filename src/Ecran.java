import java.awt.Graphics;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Ecran extends JPanel {
	private Joueur joueur;
	
	public Ecran(Joueur joueur) {
		this(joueur, 20);
	}
	
	public Ecran(Joueur joueur, int fps) {
		this.joueur = joueur;
		addKeyListener(joueur);
		new Thread(new Rafraichissement(1000L / fps)).start();
		setFocusable(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Carte carte = joueur.getCarte();
		if (carte == null) return;
		final int largeur = getWidth();
		final int hauteur = getHeight();
		synchronized (joueur) {
			final int xBase = joueur.getX() - largeur / 2 + 16;
			final int yBase = joueur.getY() - hauteur / 2 + 24;
			carte.dessiner(g, xBase, yBase, largeur, hauteur);
		}
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
