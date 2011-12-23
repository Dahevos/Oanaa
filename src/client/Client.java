package client;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

import modele.Carte;
import modele.Joueur;
import modele.PNJ;

import evenements.Teleporteur;
import evenements.Transformateur;

import affichage.CameraFantome;
import affichage.CameraFixe;
import affichage.CameraPerso;
import affichage.Ecran;

import ressources.Ressources;

@SuppressWarnings("serial")
public class Client extends JFrame {

	private Carte carte1, carte2;
	private Joueur joueur1, joueur2;
	private PNJ pnj;
	private CameraFixe cameraFixe;
	private CameraPerso cameraPerso;
	private CameraFantome cameraFantome;
	private Ecran ecran1, ecran2, ecran3, ecran4, ecranParam;

	public Client() throws IOException {
		Ressources.setConfig(getGraphicsConfiguration());
		
		long debut = System.currentTimeMillis();
		/*
		ressources.Theme theme = Ressources.getTheme("tileset.png");

		carte1 = new Carte(20 * theme.getLargeur(), theme.getHauteur(),
				Ressources.getElement("theme3.png", 0, 0));

		for (int j = 0; j < theme.getHauteur(); j++)
			for (int k = 0; k < 20; k++)
				for (int i = 0; i < theme.getLargeur(); i++)
					carte1.getCase(i + k * theme.getLargeur(), j).setCouche(1, theme.getElement(i, j));

		System.out.println("Début écriture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		carte1.ecrire(new File("carte2.dat"));
		System.out.println("Fin écriture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		if (true) return;
		/*/
		System.out.println("Début lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		carte1 = Carte.lire(new File("carte1.dat"));
		System.out.println("Fin lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());

		System.out.println("Début lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		carte2 = Carte.lire(new File("carte2.dat"));
		System.out.println("Fin lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		//*/

		carte1.getCase(0, 0).ajouterActionSol(new Teleporteur(carte2.getCase(3, 3)));
		carte2.getCase(0, 0).ajouterActionSol(new Teleporteur(carte1.getCase(3, 3)));
		
		carte1.getCase(0, 3).ajouterActionSol(new Transformateur(Ressources.getApparence("charset.png")));
		carte1.getCase(3, 0).ajouterActionSol(new Transformateur(Ressources.getApparence("charset3.png")));

		joueur1 = new Joueur(Ressources.getApparence("charset.png"), carte1, 3, 2);
		joueur2 = new Joueur(Ressources.getApparence("charset3.png"), carte1, 2, 3);

		//*
		for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++)
			pnj = new PNJ(Ressources.getApparence("charset2.png"), carte1, 6 + i, 6 + j, 100, 1000);
		//*/

		ecran1 = new Ecran();
		ecran2 = new Ecran();
		ecran3 = new Ecran();
		ecran4 = new Ecran();
		
		setFocusable(true);
		addKeyListener(joueur1);
		
		cameraFixe = new CameraFixe(carte2, ecran2);
		cameraPerso = new CameraPerso(joueur1, ecran1);
		cameraFantome = new CameraFantome(carte1, ecran3);
		addKeyListener(cameraFantome);

		JMenuBar menubar = new JMenuBar();
		JMenu choixEcran = new JMenu("Choix écran");
		menubar.add(choixEcran);
		ButtonGroup groupe = new ButtonGroup();
		
		JRadioButtonMenuItem radio = new JRadioButtonMenuItem(new AbstractAction("Ecran 1") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecranParam = ecran1;
			}
		});
		radio.doClick();
		groupe.add(radio);
		choixEcran.add(radio);
		
		radio = new JRadioButtonMenuItem(new AbstractAction("Ecran 2") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecranParam = ecran2;
			}
		});
		groupe.add(radio);
		choixEcran.add(radio);
		
		radio = new JRadioButtonMenuItem(new AbstractAction("Ecran 3") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecranParam = ecran3;
			}
		});
		groupe.add(radio);
		choixEcran.add(radio);
		
		radio = new JRadioButtonMenuItem(new AbstractAction("Ecran 4") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ecranParam = ecran4;
			}
		});
		groupe.add(radio);
		choixEcran.add(radio);
		
		
		JMenu choixCamera = new JMenu("Caméra");
		menubar.add(choixCamera);
		choixCamera.add(new JMenuItem(new AbstractAction("Joueur 1") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				cameraPerso.setPerso(joueur1);
				ecranParam.setCamera(cameraPerso);
			}
		}));
		choixCamera.add(new JMenuItem(new AbstractAction("Joueur 2") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				cameraPerso.setPerso(joueur2);
				ecranParam.setCamera(cameraPerso);
			}
		}));
		choixCamera.add(new JMenuItem(new AbstractAction("PNJ") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				cameraPerso.setPerso(pnj);
				ecranParam.setCamera(cameraPerso);
			}
		}));
		choixCamera.add(new JMenuItem(new AbstractAction("Carte 1") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				cameraFixe.setCarte(carte1);
				ecranParam.setCamera(cameraFixe);
			}
		}));
		choixCamera.add(new JMenuItem(new AbstractAction("Carte 2") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				cameraFixe.setCarte(carte2);
				ecranParam.setCamera(cameraFixe);
			}
		}));
		choixCamera.add(new JMenuItem(new AbstractAction("Fantome") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				ecranParam.setCamera(cameraFantome);
				addKeyListener(cameraFantome);
			}
		}));
		choixCamera.add(new JMenuItem(new AbstractAction("Rien") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(cameraFantome);
				ecranParam.setCamera(null);
			}
		}));

		JMenu controle = new JMenu("Contrôle");
		menubar.add(controle);
		controle.add(new JMenuItem(new AbstractAction("Joueur 1") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(joueur1);
				removeKeyListener(joueur2);
				addKeyListener(joueur1);
			}
		}));
		controle.add(new JMenuItem(new AbstractAction("Joueur 2") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(joueur1);
				removeKeyListener(joueur2);
				addKeyListener(joueur2);
			}
		}));
		controle.add(new JMenuItem(new AbstractAction("Rien") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeKeyListener(joueur1);
				removeKeyListener(joueur2);
			}
		}));

		JMenu outils = new JMenu("Outils");
		menubar.add(outils);
		outils.add(new JMenuItem(new AbstractAction("Exporter") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					carte1.exporterImage(new File("carte.png"), "PNG");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							Client.this, e.toString(), "Erreur", JOptionPane.ERROR_MESSAGE);
				} 
			}
		}));
		//*/

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menubar);
		
		setLayout(new GridLayout(2, 2, 5, 5));
		add(ecran1);
		add(ecran2);
		add(ecran3);
		add(ecran4);
		setExtendedState(MAXIMIZED_BOTH);

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
