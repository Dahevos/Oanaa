package editeur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ressources.*;

@SuppressWarnings("serial")
public class PlancheRessource extends JPanel {
	/** variable de classe contenant l'image à afficher en fond */
	private Theme theme;
	private int i;
	private int j;
	private ArrayList<ArrayList<Element>> elemCourants; 
	private boolean ctrl;

	public PlancheRessource(String nom) {
		this.theme = Ressources.getTheme(nom);
		setPreferredSize(new Dimension(theme.getLargeur() * 32, theme.getHauteur() * 32));
		i = 0;
		j = 0;
		addKeyListener(new EcouteurClavier());
		addMouseListener(new EcouteurSouris());
		setFocusable(true);

		elemCourants = new ArrayList<ArrayList<Element>>();
		elemCourants.clear();
		
		ArrayList<Element> elemEnCour = new ArrayList<Element>();
		try {
			elemEnCour.add(theme.getElement(0, 0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		elemCourants.add(elemEnCour);
		
	}

	public Theme getTheme() {
		return theme;
	}

	public  ArrayList<ArrayList<Element>> getElemCourants() {
		return elemCourants;
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
		g.draw3DRect(32 * i, 32 * j, 31*elemCourants.size(), 31*elemCourants.get(0).size(), true);
	}

	private class EcouteurSouris extends MouseAdapter {		
	
		public void mouseClicked(MouseEvent e) {

			int largeur = elemCourants.size();
			int hauteur = elemCourants.get(0).size();
			elemCourants.clear();

			if (!ctrl) {

				final int iPrec = i, jPrec = j;
				i = e.getX() / 32;
				j = e.getY() / 32;
				// On repaint seulement les zone modifiées (à conserver ?)
				repaint(32 * i, 32 * j, 32, 32);
				repaint(32 * iPrec, 32 * jPrec, 32*largeur, 32*hauteur);

				try {
					ArrayList<Element> elemEnCour = new ArrayList<Element>();
					elemEnCour.add(theme.getElement(i, j));
					elemCourants.add(elemEnCour);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				
				int newI =  e.getX()/32;
				int newJ = e.getY()/32;
				
				if (newI < i) {
					int temp = i;
					i=newI;
					newI = temp;
				}
	
				if (newJ < j) {
					int temp = j;
					j=newJ;
					newJ = temp;
				}
				
				
				for(int iCour = i; iCour < newI +1; iCour++) {
					ArrayList<Element> elemEnCour = new ArrayList<Element>();
					for (int jCour = j; jCour < newJ +1; jCour++) {
						try {
							System.out.println("iCour = " + iCour +"; jCour = " + jCour);
							elemEnCour.add(theme.getElement(iCour, jCour ));
							System.out.println(theme.getElement(iCour, jCour));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					elemCourants.add(elemEnCour);
					System.out.println("---------------");
					for(int p=0; p < elemCourants.size(); p++)
						for (int t=0; t < elemCourants.get(p).size(); t++)
							System.out.println(elemCourants.get(p).get(t));
					System.out.println("---------------");

				}
				repaint(32 * i, 32 * j, elemCourants.size()*32, elemCourants.get(0).size()*32);

				
			}
		}

	
	}
	
	private class EcouteurClavier extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
				ctrl = true;	
		}
		
	

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)
				ctrl = false;
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}		
		
		

	
	}
	
}
