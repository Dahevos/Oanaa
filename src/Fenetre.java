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
		Personnage perso = new Personnage(apparence, carte, 3, 17);
		
		Ecran ecran = new Ecran(perso, 50);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(ecran);
		setSize(400, 400);
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Fenetre();
	}
}
