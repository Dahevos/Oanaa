package editeur;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import ressources.Ressources;

import affichage.Carte;
import affichage.Ecran;

@SuppressWarnings("serial")
public class Editeur extends JFrame{
	//On déclare notre objet JSplitPane
	private JSplitPane split;
	private Carte edition;
	JScrollPane gauche;
	JScrollPane droite;

	public Editeur(){

		/* initialisation et paramétrage de la JFrame */
		this.setTitle("Editeur de carte");
		this.setSize(1024, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* contenu de gauche (planche ressources) */
		PlancheRessource planche = new PlancheRessource("theme3.png");
		gauche = new JScrollPane(planche);
		gauche.getHorizontalScrollBar().setUnitIncrement(32);
		gauche.getHorizontalScrollBar().setBlockIncrement(32);
		gauche.getVerticalScrollBar().setBlockIncrement(32);
		gauche.getVerticalScrollBar().setUnitIncrement(32);

		/* création de la carte */
		edition = new Carte(200, 200, Ressources.getElement("tileset.png", 0, 0));
		//edition = Carte.lire(new File("carte.dat"));

		/* contenu de droite (carte) */
		Ecran ecran = new Ecran(edition);
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
		initMenu();

		this.setVisible(true);
	}


	public void initMenu() {
		JMenuBar menubar = new JMenuBar();

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
				edition = Carte.lire(new File("carte.dat"));
				droite.repaint();
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Enregistrer carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				edition.ecrire("save.dat");
			}
		}));
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

		setJMenuBar(menubar);


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
