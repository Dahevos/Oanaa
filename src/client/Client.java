package client;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import affichage.Carte;
import affichage.Ecran;
import affichage.Joueur;
import affichage.PNJ;

import ressources.Ressources;

@SuppressWarnings("serial")
public class Client extends JFrame {

	private Joueur joueur1, joueur2;
	private PNJ pnj;
	private Carte carte;
	private Ecran ecran;

	public Client() throws IOException {
		long debut = System.currentTimeMillis();
		/*
		Theme theme = Ressources.getTheme("tileset.png");

		Carte carte = new Carte(20 * theme.getLargeur(), theme.getHauteur(),
				Ressources.getElement("theme3.png", 0, 0));

		for (int j = 0; j < theme.getHauteur(); j++)
			for (int k = 0; k < 20; k++)
				for (int i = 0; i < theme.getLargeur(); i++)
					carte.getCase(i + k * theme.getLargeur(), j).setCouche(1, theme.getElement(i, j));

		System.out.println("Début écriture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		carte.ecrire(new File("carte.dat"));
		System.out.println("Fin écriture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		/**/
		System.out.println("Début lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		carte = Carte.lire(new File("carte.dat"));
		System.out.println("Fin lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());

		joueur1 = new Joueur(Ressources.getApparence("charset.png"), carte, 3, 3);
		joueur2 = new Joueur(Ressources.getApparence("charset3.png"), carte, 5, 5);

		for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++)
			pnj = new PNJ(Ressources.getApparence("charset2.png"), carte, 5 + i, 20 + j, 100, 1000);

		ecran = new Ecran(joueur1);
		ecran.addKeyListener(joueur1);

		JMenuBar menubar = new JMenuBar();

		JMenu vue = new JMenu("Vue");
		menubar.add(vue);
		vue.add(new JMenuItem(new AbstractAction("Joueur 1") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.setPerso(joueur1);
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Joueur 2") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.setPerso(joueur2);
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("PNJ") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.setPerso(pnj);
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Carte") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.setCarte(carte);
			}
		}));
		vue.add(new JMenuItem(new AbstractAction("Rien") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.setCarte(null);
			}
		}));

		JMenu controle = new JMenu("Contrôle");
		menubar.add(controle);
		controle.add(new JMenuItem(new AbstractAction("Joueur 1") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.removeKeyListener(joueur1);
				ecran.removeKeyListener(joueur2);
				ecran.addKeyListener(joueur1);
			}
		}));
		controle.add(new JMenuItem(new AbstractAction("Joueur 2") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecran.removeKeyListener(joueur1);
				ecran.removeKeyListener(joueur2);
				ecran.addKeyListener(joueur2);
			}
		}));

		JMenu outils = new JMenu("Outils");
		menubar.add(outils);
		outils.add(new JMenuItem(new AbstractAction("Exporter") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					carte.exporterImage(new File("carte.png"), "PNG");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							Client.this, e.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
				} 
			}
		}));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menubar);
		setContentPane(ecran);
		setSize(400, 400);

		System.out.println("Avant nettoyage : " + getMemoire());
		Ressources.nettoyerThemes();
		System.gc();
		System.out.println("Après nettoyage : " + getMemoire());

		setVisible(true);
	}

	public static String getMemoire() {
		final Runtime r = Runtime.getRuntime();
		final long total = r.totalMemory();
		final long libre = r.freeMemory();
		return ((total - libre) / 1024) + " ko / " + (total / 1024) + " ko";
	}

	public static void main(String[] args) throws IOException {
		//* Utilisation du "Look & Feel" du système (si possible)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		//*/

		new Client();
	}
}
