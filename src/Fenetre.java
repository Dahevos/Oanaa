import java.io.IOException;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	public Fenetre() throws IOException {
		Theme theme = Ressources.getTheme("tileset.png");

		Carte carte = new Carte(20 * theme.getLargeur(), theme.getHauteur(),
				Ressources.getImage("tileset.png", 0, 0));

		for (int j = 0; j < 10/*theme.getHauteur()/**/; j++)
			for (int k = 0; k < 20; k++)
				for (int i = 0; i < theme.getLargeur(); i++)
					carte.getCase(i + k * theme.getLargeur(), j).setCouche(1, theme.getImage(i, j));

		Apparence apparence = Ressources.getApparence("charset.png");
		Joueur joueur = new Joueur(apparence, carte, 10, 17);

		for (int i = 0; i < 10; i++) for (int j = 0; j < 10; j++)
			new PNJ(Ressources.getApparence("charset2.png"), carte, 5 + i, 20 + j, 100, 1000);

		Ecran ecran = new Ecran(joueur);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(ecran);
		setSize(400, 400);

		Ressources.nettoyerThemes();
		
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Fenetre();
	}
}
