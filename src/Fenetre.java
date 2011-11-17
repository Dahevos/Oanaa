import java.io.IOException;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	public Fenetre() throws IOException {
		Theme theme = Ressources.getTheme("tileset.png");
		
		Carte carte = new Carte(theme.getLargeur(), theme.getHauteur(),
				Ressources.getImage("tileset.png", 0, 0));

		for (int j = 0; j < theme.getHauteur(); j++)
			for (int i = 0; i < theme.getLargeur(); i++)
				carte.setCouche(i, j, 3, theme.getImage(i, j));
		
		Apparence apparence = Ressources.getApparence("charset.png");
		Joueur joueur = new Joueur(apparence, carte, 3, 17);
		
		new PNJ(Ressources.getApparence("charset2.png"), carte, 5, 20, 100, 1000);
		
		Ecran ecran = new Ecran(joueur, 50);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(ecran);
		setSize(400, 400);
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Fenetre();
	}
}
