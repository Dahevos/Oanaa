package editeur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import affichage.Carte;
import affichage.Ecran;

public class Editeur extends JFrame{
	
	private static final long serialVersionUID = 42L;

	//On dÃ©clare notre objet JSplitPane
	private JSplitPane split;

	public Editeur(){

		/* initialisation et paramétrage de la JFrame */
		this.setTitle("Editeur de carte");
		this.setSize(1024, 800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* contenu de gauche (planche ressources) */
		PlancheRessource planche = new PlancheRessource("tileset.png");
		planche.setPreferredSize(new Dimension(planche.getTheme().getLargeur()*32, planche.getTheme().getHauteur()*32));
		JScrollPane gauche = new JScrollPane(planche);
		gauche.setPreferredSize(new Dimension(planche.getTheme().getLargeur()*32+18,800));
		gauche.getHorizontalScrollBar().setUnitIncrement(32);
		gauche.getHorizontalScrollBar().setBlockIncrement(32);
		gauche.getVerticalScrollBar().setBlockIncrement(32);
		gauche.getVerticalScrollBar().setUnitIncrement(32);


		/* création de la carte */
		//Carte edition = new Carte(1500, 1500);
		Carte edition = Carte.lire(new File("carte.dat"));

		/* contenu de droite (carte) */
		Ecran ecran = new Ecran(edition);
		JScrollPane droite = new JScrollPane(ecran);
		droite.getHorizontalScrollBar().setUnitIncrement(32);
		droite.getHorizontalScrollBar().setBlockIncrement(32);
		droite.getVerticalScrollBar().setBlockIncrement(32);
		droite.getVerticalScrollBar().setUnitIncrement(32);

		/* construction du séparateur */
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gauche, droite);

		/* On le passe ensuite au contentPane de notre objet Fenetre (placé au centre)  */
		this.getContentPane().add(split, BorderLayout.CENTER);

		this.setVisible(true);	 		
	}

	public static void main(String[] args){

		new Editeur();
	}     

}
