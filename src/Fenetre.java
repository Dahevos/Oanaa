import java.io.IOException;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	public Fenetre() throws IOException {
		Theme theme = Ressources.getTheme("tileset.png");
		
		Carte carte = new Carte(20 * theme.getLargeur(), theme.getHauteur(),
				Ressources.getImage("tileset.png", 0, 0));

		for (int j = 0; j < theme.getHauteur(); j++)
			for (int k = 0; k < 20; k++)
				for (int i = 0; i < theme.getLargeur(); i++)
					carte.setCouche(i + k * theme.getLargeur(), j, 3, theme.getImage(i, j));
		theme = null;
		
		Apparence apparence = Ressources.getApparence("charset.png");
		Joueur joueur = new Joueur(apparence, carte, 3, 17);
		
		new PNJ(Ressources.getApparence("charset2.png"), carte, 5, 20, 100, 1000);
		
		Ecran ecran = new Ecran(joueur, 25);
		
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
