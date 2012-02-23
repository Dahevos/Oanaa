package modele;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ressources.Apparence;


public class Joueur extends Personnage implements KeyListener {

	public Joueur(Apparence apparence) {
		super(apparence);
	}
	
	public Joueur(Apparence apparence, Carte carte, int i, int j) {
		super(apparence, carte, i, j);
	}

	public Joueur(Apparence apparence, Carte carte, int i, int j, Direction dir) {
		super(apparence, carte, i, j, dir);
	}

	public Joueur(Apparence apparence, double vitesse, Carte carte, int i,
			int j, Direction dir) {
		super(apparence, vitesse, carte, i, j, dir);
	}

	public Joueur(Apparence apparence, double vitesse, Carte carte, int i, int j) {
		super(apparence, vitesse, carte, i, j);
	}

	public Joueur(Apparence apparence, double vitesse) {
		super(apparence, vitesse);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_DOWN: deplacer(Direction.BAS); break;
		case KeyEvent.VK_LEFT: deplacer(Direction.GAUCHE); break;
		case KeyEvent.VK_RIGHT: deplacer(Direction.DROITE); break;
		case KeyEvent.VK_UP: deplacer(Direction.HAUT); break;
		default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
