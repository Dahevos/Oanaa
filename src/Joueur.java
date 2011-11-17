import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Joueur extends Personnage implements KeyListener {

	public Joueur(Apparence apparence) {
		super(apparence);
	}

	public Joueur(Apparence apparence, Carte carte, int i, int j) {
		super(apparence, carte, i, j);
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