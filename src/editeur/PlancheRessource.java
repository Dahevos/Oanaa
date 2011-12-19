package editeur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ressources.*;

@SuppressWarnings("serial")
public class PlancheRessource extends JPanel {
	/** variable de classe contenant l'image à afficher en fond */
	private Theme theme;
	private int i;
	private int j;

	public PlancheRessource(String nom) {
		this.theme = Ressources.getTheme(nom);
		setPreferredSize(new Dimension(theme.getLargeur() * 32, theme.getHauteur() * 32));
		i = 0;
		j = 0;
		addMouseListener(new EcouteurSouris());
	}

	public Theme getTheme() {
		return theme;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}


	// Couleurs de la grille
	private final static Color[] couleursGrille = new Color[] {Color.LIGHT_GRAY, Color.GRAY};

	private void dessinerGrille(Graphics g) {
		// Calcul de la zone à dessiner
		Rectangle zone = g.getClipBounds();
		final int iMin = zone.x / 8;
		final int jMin = zone.y / 8;
		final int iMax = ((zone.x + zone.width - 1) + 7) / 8;
		final int jMax = ((zone.y + zone.height - 1) + 7) / 8;

		// Dessin de la grille
		for (int i = iMin; i <= iMax; i++) {
			for (int j = jMin; j <= jMax; j++) {
				g.setColor(couleursGrille[(i + j) % 2]);
				g.fillRect(8*i, 8*j, 8, 8);
			}
		}
	}

	/** Redéfinition de la fonction paintComponent() pour afficher notre image */
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		dessinerGrille(g);
		try {
			g.drawImage(theme.getImage(), 0, 0, null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Impossible de (re)charger le thème " + theme.getNom() + " :\n" + e,
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
		g.setColor(Color.RED);
		g.draw3DRect(32 * i, 32 * j, 31, 31, true);
	}

	private class EcouteurSouris extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			final int iPrec = i, jPrec = j;
			i = e.getX() / 32;
			j = e.getY() / 32;
			// On repaint seulement les zone modifiées (à conserver ?)
			repaint(32 * i, 32 * j, 32, 32);
			repaint(32 * iPrec, 32 * jPrec, 32, 32);
			//repaint();
		}
	}
}
