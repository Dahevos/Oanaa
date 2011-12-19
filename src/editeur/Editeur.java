package editeur;


import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
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

import ressources.Ressources;

import affichage.Carte;
import affichage.Ecran;

@SuppressWarnings("serial")
public class Editeur extends JFrame{
	//On d√©clare notre objet JSplitPane
	private JSplitPane split;
	private Carte edition;
	private JScrollPane gauche;
	private JScrollPane droite;
	private PlancheRessource planche;
	SpinnerModel niveauMap;


	public Editeur(){

		/* initialisation et param√©trage de la JFrame */
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

		/* cr√©ation de la carte */
		edition = new Carte(200, 200, Ressources.getElement("tileset.png", 0, 0));
		//edition = Carte.lire(new File("carte.dat"));

		/* contenu de droite (carte) */
		Ecran ecran = new Ecran(edition);
		droite = new JScrollPane(ecran);
		droite.getHorizontalScrollBar().setUnitIncrement(32);
		droite.getHorizontalScrollBar().setBlockIncrement(32);
		droite.getVerticalScrollBar().setBlockIncrement(32);
		droite.getVerticalScrollBar().setUnitIncrement(32);

		/* construction du s√©parateur */
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, gauche, droite);
		split.setDividerLocation(gauche.getPreferredSize().width);

		/* On le passe ensuite au contentPane de notre objet Fenetre (plac√© au centre)  */
		this.setContentPane(split);

		/* construction du menu */
		initMenu();
		droite.addMouseListener(new EcouteurSouris());



		this.setVisible(true);
	}


	/**
	 * Afficher une boite de dialogue "ouvrir un fichier"
	 * @param _title Title de la boite de dialogue
	 * @param _path RÈpertoire initiale
	 * @param _fileFilter Filtre de type *.jpg
	 * @return repertoire"/"nom du fichier
	 */
	public String LoadFromFile(String _title, String _path, String _fileFilter) {
		FileDialog fileDialog = new FileDialog(this, _title, FileDialog.LOAD);
		fileDialog.setFile(_fileFilter);
		fileDialog.setDirectory(_path);
		this.setLocationRelativeTo(null);
		fileDialog.setDirectory(_path);
		fileDialog.setVisible(true);
		return fileDialog.getDirectory() + fileDialog.getFile();
	}


	/**
	 * Afficher une boite de dialogue "Enregistrer un fichier"
	 * @param _title Title de la boite de dialogue
	 * @param _path RÈpertoire initiale
	 * @param _fileFilter Filtre de type *.jpg
	 * @return repertoire"/"nom du fichier
	 */
	public String SaveFromFile(String _title, String _path, String _fileFilter) {
		FileDialog fileDialog = new FileDialog(this, _title, FileDialog.SAVE);
		fileDialog.setFile(_fileFilter);
		fileDialog.setDirectory(_path);
		this.setLocationRelativeTo(null);
		fileDialog.setDirectory(_path);
		fileDialog.setVisible(true);
		return fileDialog.getDirectory() + fileDialog.getFile();
	}

	public void initMenu() {
		JMenuBar menubar = new JMenuBar();



		/* Menu Fichier */
		JMenu vue = new JMenu("Fichier");
		menubar.add(vue);
		vue.add(new JMenuItem(new AbstractAction("Nouvelle carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				edition = new Carte(200, 200, Ressources.getElement("tileset.png", 0, 0));
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Charger carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// rÈcupÈration du fichier sÈlectionnÈ			        
				edition = Carte.lire(new File(LoadFromFile("Charger une carte", ".\\", "*.dat")));
				edition.rafraichir(0, 0, edition.getLargeur(), edition.getHauteur());
				//droite.repaint();
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Enregistrer carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				edition.ecrire(SaveFromFile("Enregistrer une carte",".\\", "*.dat"));
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



		niveauMap = new SpinnerNumberModel(1, //initial value
				1, //min
				100, //max
				1);                //step
		JSpinner jspin = new JSpinner(niveauMap);				
		JMenu niveau = new JMenu("Niveau");

		niveau.add(jspin);


		JMenu vueEdition = new JMenu("…dition");
		menubar.add(vueEdition);
		vueEdition.add(niveau);
		
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
		
		setJMenuBar(menubar);


	}

	private class EcouteurSouris extends MouseAdapter {

		private boolean dessine;


		public void mousePressed(MouseEvent e) {
			dessine = true;
			int i = e.getX() / 32;
			int j = e.getY() / 32;
			for (int ligne = 0; ligne < planche.getElemCourants().size(); ligne++ )
				for (int colonne=0; colonne < planche.getElemCourants().get(ligne).size(); colonne++) {
					edition.getCase(i + ligne, j + colonne).setCouche((Integer) niveauMap.getValue(), planche.getElemCourants().get(ligne).get(colonne));
					edition.rafraichir(i + ligne, j + colonne, 32, 32);
				}
		}

		public void mouseReleased(MouseEvent e) {
			dessine = false;
		}	

		public void mouseMoved(MouseEvent e) {
			if(dessine) {
				System.out.println("lol");
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

	public static void main(String[] args){
		//* Utilisation du "Look & Feel" du syst√®me (si possible)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		//*/

		new Editeur();
	}     

}
