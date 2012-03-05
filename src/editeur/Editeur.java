package editeur;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import editeur.PlancheRessource;
import editeur.PlancheRessource.EcouteurClavier;

import modele.Carte;

import affichage.CameraFixe;
import affichage.Ecran;
import affichage.Filtre;
import affichage.FiltreUni;

import ressources.Ressources;


@SuppressWarnings("serial")
public class Editeur extends JFrame{
	//On déclare notre objet JSplitPane
	private JSplitPane split;
	private Carte edition;
	private JScrollPane gauche;
	private JScrollPane droite;
	private PlancheRessource planche;
	private Ecran ecran;
	private CameraFixe camera;
	SpinnerModel niveauMap;


	public Editeur(){


		/* initialisation et paramétrage de la JFrame */
		this.setTitle("Editeur de carte");
		this.setSize(1024, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	

		/* contenu de gauche (planche ressources) */
		planche = new PlancheRessource("theme3.png");
		gauche = new JScrollPane(planche);
		gauche.getHorizontalScrollBar().setUnitIncrement(32);
		gauche.getHorizontalScrollBar().setBlockIncrement(32);
		gauche.getVerticalScrollBar().setBlockIncrement(32);
		gauche.getVerticalScrollBar().setUnitIncrement(32);

		/* création de la carte */
		edition = new Carte(200, 200, Ressources.getElement("tileset.png", 0, 0));
		//edition = Carte.lire(new File("carte.dat"));

		/* contenu de droite (carte) */
		camera = new CameraFixe(edition);
		ecran = new Ecran(camera);
		ecran.ajouterSolDessous(new FiltreUni(Color.BLACK)); // Mise en place du fond noir (A CHANGER ?)
		droite = new JScrollPane(ecran);
		droite.getHorizontalScrollBar().setUnitIncrement(32);
		droite.getHorizontalScrollBar().setBlockIncrement(32);
		droite.getVerticalScrollBar().setBlockIncrement(32);
		droite.getVerticalScrollBar().setUnitIncrement(32);

		/* construction du séparateur */
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, gauche, droite);
		split.setDividerLocation(gauche.getPreferredSize().width);

		/* On le passe ensuite au contentPane de notre objet Fenetre (placé au centre)  */
		this.setContentPane(split);

		/* construction du menu */
		ecran.addMouseListener(new EcouteurSouris());

		initMenu();

		addKeyListener(new EcouteurClavier());

		this.setVisible(true);
	}


	/**
	 * Afficher une boite de dialogue "ouvrir un fichier"
	 * Utilise la class privée MyFilter comme filtre
	 */
	public File LoadFromFile() {
		String filename = File.separator+"tmp";
		JFileChooser fc = new JFileChooser(new File(filename));
		try {
			// Create a File object containing the canonical path of the
			// desired directory
			File f = new File(new File(".").getCanonicalPath());
			// Set the current directory
			fc.setCurrentDirectory(f);
		} catch (IOException e) {
		}
		fc.addChoosableFileFilter(new MyFilter());

		// Show open dialog; this method does not return until the dialog is closed
		fc.showOpenDialog(this);
		File selFile = fc.getSelectedFile();
		return selFile;

	}

	private class MyFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			if (file.isDirectory()) { return true; }
			String filename = file.getName();
			return filename.endsWith(".dat");
		}
		public String getDescription() {
			return "*.dat";
		}
	}



	/**
	 * Afficher une boite de dialogue "Enregistrer un fichier"
	 */
	public File SaveFromFile() {
		String filename = File.separator+"tmp";
		JFileChooser fc = new JFileChooser(new File(filename));
		try {
			// Create a File object containing the canonical path of the
			// desired directory
			File f = new File(new File(".").getCanonicalPath());
			// Set the current directory
			fc.setCurrentDirectory(f);
		} catch (IOException e) {
		}
		fc.addChoosableFileFilter(new MyFilter());
		// Show save dialog; this method does not return until the dialog is closed
		fc.showSaveDialog(this);
		File selFile = fc.getSelectedFile();
		if(!selFile.getName().contains(".dat"))
			selFile = new File(selFile.getAbsoluteFile() + ".dat");

		return selFile;
	}




	public void initMenu() {
		JMenuBar menubar = new JMenuBar();



		/* Menu Fichier */
		JMenu vue = new JMenu("Fichier");
		menubar.add(vue);
		vue.add(new JMenuItem(new AbstractAction("Nouvelle carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DemandeNouvelleCarte choisirCarte = new DemandeNouvelleCarte(Editeur.this, "Taille de la carte", true); 
//				ArrayList<Integer> val = choisirCarte.getValCarte();
//				if (val.size() == 2) {
//					edition = new Carte(val.get(0), val.get(1), Ressources.getElement("tileset.png", 0, 0));
//					camera.setCarte(edition);
//					droite.getVerticalScrollBar().updateUI(); // maj de tout les scrollbar de l'interface
//
//				}
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Charger carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// récupération du fichier sélectionné		
				File f = LoadFromFile();
				if (f != null)  {
					edition = Carte.lire(f);
					camera.setCarte(edition);
					droite.getVerticalScrollBar().updateUI(); // maj de tout les scrollbar de l'interface

				}
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Enregistrer carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				edition.ecrire(SaveFromFile());
			}
		}));
		vue.addSeparator();
		vue.add(new JMenuItem(new AbstractAction("Exporter carte en png") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					edition.exporterImage(new File("carte.png"), "PNG");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							Editeur.this, e.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
				} 			}
		}));




		
		JMenu vueEdition = new JMenu("Édition");
		menubar.add(vueEdition);

		JMenu filtre = new JMenu("Filtre");
		filtre.add(new JMenuItem(new AbstractAction("Couleur") {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				for (int ligne = 0; ligne < edition.getLargeur(); ligne++ )
					for (int colonne=0; colonne < edition.getHauteur(); colonne++) {
						edition.getCase(ligne,colonne).setCouche((Integer) niveauMap.getValue(), planche.getElemCourants().get(ligne%planche.getElemCourants().size()).get(colonne%planche.getElemCourants().get(0).size()));
					}
				edition.rafraichir(0, 0, edition.getLargeur() - 1, edition.getHauteur() - 1);

			}
		}));
		filtre.add(new JMenuItem(new AbstractAction("Image") {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				DChoisirFiltreImage choisirFiltre = new DChoisirFiltreImage(Editeur.this, "Choisir filtre image", true); 
				
				Filtre f = choisirFiltre.getFiltre();
				if (f != null) 
					ecran.ajouterCielDessus(f);
					edition.rafraichir(0, 0, edition.getLargeur(), edition.getHauteur());

			}
		}));	
		
		vueEdition.add(filtre);

		vueEdition.add(new JMenuItem(new AbstractAction("Remplir") {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				for (int ligne = 0; ligne < edition.getLargeur(); ligne++ )
					for (int colonne=0; colonne < edition.getHauteur(); colonne++) {
						edition.getCase(ligne,colonne).setCouche((Integer) niveauMap.getValue(), planche.getElemCourants().get(ligne%planche.getElemCourants().size()).get(colonne%planche.getElemCourants().get(0).size()));
					}
				edition.rafraichir(0, 0, edition.getLargeur(), edition.getHauteur());

			}
		}));

		
		

		JLabel niveauMapNom = new JLabel("Couche n° :");
		menubar.add(niveauMapNom);
		
		niveauMap = new SpinnerNumberModel(0, //initial value
				0, //min
				100, //max
				1);                //step
		JSpinner jspin = new JSpinner(niveauMap);	
		jspin.setMaximumSize(new Dimension(40,50));
		
		menubar.add(jspin);
		
		
		
		setJMenuBar(menubar);


	}

	private class EcouteurSouris extends MouseAdapter {

		private boolean dessine;


		public void mousePressed(MouseEvent e) {
			dessine = true;
			int i = e.getX() / 32;
			int j = e.getY() / 32;
			if (i < edition.getLargeur() && j < edition.getHauteur()) {
				for (int ligne = 0; ligne < planche.getElemCourants().size(); ligne++ )
					for (int colonne=0; colonne < planche.getElemCourants().get(ligne).size(); colonne++) {
						if (i+ligne < edition.getLargeur() && j+colonne < edition.getHauteur()) {
							edition.getCase(i + ligne, j + colonne).setCouche((Integer) niveauMap.getValue(), planche.getElemCourants().get(ligne).get(colonne));
							edition.rafraichir(i + ligne, j + colonne, i + ligne, j + colonne);
						}
					}
			}
		}

		public void mouseReleased(MouseEvent e) {
			dessine = false;
		}	

		public void mouseMoved(MouseEvent e) {
			if(dessine) {
				int i = e.getX() / 32;
				int j = e.getY() / 32;
				for (int ligne = 0; ligne < planche.getElemCourants().size(); ligne++ )
					for (int colonne=0; colonne < planche.getElemCourants().get(ligne).size(); colonne++) {
						edition.getCase(i + ligne, j + colonne).setCouche((Integer) niveauMap.getValue(), planche.getElemCourants().get(ligne).get(colonne));
						edition.rafraichir(i + ligne, j + colonne, 32, 32);
					}
			}
		}

		//		@Override
		//		public void mouseClicked(MouseEvent e) {
		//			int i = e.getX() / 32;
		//			int j = e.getY() / 32;
		//			for (int ligne = 0; ligne < planche.getElemCourants().size(); ligne++ )
		//				for (int colonne=0; colonne < planche.getElemCourants().get(ligne).size(); colonne++) {
		//					edition.getCase(i + ligne, j + colonne).setCouche((Integer) niveauMap.getValue(), planche.getElemCourants().get(ligne).get(colonne));
		//					edition.rafraichir(i + ligne, j + colonne, 32, 32);
		//				}
		//		}
	}

	
	public class EcouteurClavier extends KeyAdapter {


		@Override
		public void keyTyped(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_PLUS)
				niveauMap.setValue(niveauMap.getNextValue());
			
			if (e.getKeyCode() == KeyEvent.VK_MINUS)
				niveauMap.setValue(niveauMap.getPreviousValue());
		}		
		
		

	
	}
	
	public static void main(String[] args){
		//* Utilisation du "Look & Feel" du système (si possible)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		//*/

		new Editeur();
	}     

}
