import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	public Fenetre() throws IOException {
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
		*/
		System.out.println("Début lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		Carte carte = Carte.lire(new File("carte.dat"));
		System.out.println("Fin lecture (" + (System.currentTimeMillis() - debut) + ") : " + getMemoire());
		
		Apparence apparence = Ressources.getApparence("charset.png");
		Joueur joueur = new Joueur(apparence, carte, 3, 3);

		for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++)
			new PNJ(Ressources.getApparence("charset2.png"), carte, 5 + i, 20 + j, 100, 1000);
		
		Ecran ecran = new Ecran(joueur);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		new Fenetre();
	}
}
