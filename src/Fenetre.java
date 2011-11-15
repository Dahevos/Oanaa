import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	public Fenetre() throws IOException {
		BufferedImage tmp = ImageIO.read(new File("herbe.jpg"));
		Image herbe = tmp.getScaledInstance(32, 32, BufferedImage.SCALE_DEFAULT);
		
		tmp = ImageIO.read(new File("link.gif"));
		Image link = tmp.getScaledInstance(32, 32, BufferedImage.SCALE_DEFAULT);

		tmp = ImageIO.read(new File("champi.png"));
		int h = tmp.getHeight(), w = tmp.getWidth();
		Image[] champi = new Image[4];
		champi[0] = tmp.getSubimage(0, 0, w/2, h/2).getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		champi[1] = tmp.getSubimage(w/2, 0, w/2, h/2).getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		champi[2] = tmp.getSubimage(0, h/2, w/2, h/2).getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		champi[3] = tmp.getSubimage(w/2, h/2, w/2, h/2).getScaledInstance(32, 32, Image.SCALE_DEFAULT);
		
		Carte carte = new Carte(15, 10, herbe);
		carte.setNiveau(5, 5, 1, link);
		carte.setLibre(5, 5, false);
		carte.setNiveau(3, 3, 2, champi[0]);
		carte.setNiveau(4, 3, 2, champi[1]);
		carte.setNiveau(3, 4, 2, champi[2]);
		carte.setLibre(3, 4, false);
		carte.setNiveau(4, 4, 2, champi[3]);
		carte.setLibre(4, 4, false);
		
		Personnage perso = new Personnage(link, carte, 2, 3);
		
		Ecran ecran = new Ecran(perso, 20);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(ecran);
		setSize(400, 400);
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Fenetre();
	}
}
