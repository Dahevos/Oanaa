package affichage;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;

import javax.swing.JPanel;

/**
 * Classe contenant le composant graphique destiné à afficher une caméra.<br>
 * Un écran est donc composé d'une caméra ainsi que d'un ensemble de sols et de ciels, sous la forme
 * de filtres.
 * Les sols sont affichés sous la caméra, qui est elle même affichée sous les ciels.
 * <p>Attention : le premier sol doit absolument être entièrement opaque.
 */
@SuppressWarnings("serial")
public class Ecran extends JPanel {
	/**
	 * Caméra associée à l'écran
	 */
	private Camera camera = null;
	
	/**
	 * Liste des sols associés à cet écran
	 */
	private final LinkedList<Filtre> sols = new LinkedList<Filtre>();
	
	/**
	 * Liste des ciels associés à cet écran
	 */
	private final LinkedList<Filtre> ciels = new LinkedList<Filtre>();
	
	/**
	 * Construit un nouvel écran attaché à aucune caméra.
	 */
	public Ecran() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				adapterCamera();
			}
		});
		setFocusable(true);
	}
	
	/**
	 * Construit un nouvel écran attaché à la caméra <code>camera</code>.
	 * @param camera caméra à utiliser
	 */
	public Ecran(Camera camera) {
		this();
		setCamera(camera);
	}

	/**
	 * Attache cet écran à la caméra <code>camera</code>.
	 * <br>Remarque : cette méthode adapte automatiquement la caméra à cet écran.
	 * Si plusieurs écrans sont attachés à la même caméra, c'est le dernier qui imposera donc
	 * sa taille à celle-ci. Pour corriger ceci, méthode <code>adapterCamera()</code> permet de forcer la caméra
	 * à se ré-adapter à cet écran.
	 * <br>Pour mieux contrôler la taille de la caméra, il est également possible d'utiliser
	 * la méthode <code>redimensionner()</code> de la classe <code>Camera</code>.
	 * @param camera caméra à rattacher à cet écran
	 */
	public void setCamera(Camera camera) {
		if (this.camera == camera) return;
		if (this.camera != null) {
			Camera cameraPrec = this.camera;
			this.camera = null;
			cameraPrec.retirerEcran(this);
		}
		this.camera = camera;
		if (camera != null) camera.ajouterEcran(this);
		adapterCamera();
	}
	
	/**
	 * Redimensionne la caméra de cet écran pour s'y adapter parfaitement.
	 */
	private void adapterCamera() {
		if (camera != null) camera.redimensionner(getWidth(), getHeight());
	}

	/**
	 * Retourne la caméra attachée à cet écran.
	 * @return la caméra attachée à cet écran
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Ajoute un ciel à cet écran, en dessous des autres.
	 * @param ciel ciel à ajouter
	 */
	public void ajouterCielDessous(Filtre ciel) {
		ciels.addFirst(ciel);
		ciel.ajouterEcran(this);
	}
	
	/**
	 * Ajoute un ciel à cet écran, au dessus des autres.
	 * @param ciel ciel à ajouter
	 */
	public void ajouterCielDessus(Filtre ciel) {
		ciels.addLast(ciel);
		ciel.ajouterEcran(this);
	}
	
	/**
	 * Retire un ciel de cet écran.
	 * @param ciel ciel à retirer
	 */
	public void retirerCiel(Filtre ciel) {
		ciels.remove(ciel);
	}
	
	/**
	 * Retourne l'ensemble des ciels appliqués à cet écran.
	 * @return l'ensemble des ciels appliqués à cet écran
	 */
	public LinkedList<Filtre> getCiels() {
		return ciels;
	}
	
	/**
	 * Ajoute un sol à cet écran, en dessous des autres.
	 * @param sol sol à ajouter
	 */
	public void ajouterSolDessous(Filtre sol) {
		sols.addFirst(sol);
		sol.ajouterEcran(this);
	}
	
	/**
	 * Ajoute un sol à cet écran, au dessus des autres.
	 * @param sol sol à ajouter
	 */
	public void ajouterSolDessus(Filtre sol) {
		sols.addLast(sol);
		sol.ajouterEcran(this);
	}
	
	/**
	 * Retire un sol de cet écran.
	 * @param sol sol à retirer
	 */
	public void retirerSol(Filtre sol) {
		sols.remove(sol);
	}
	
	/**
	 * Retourne l'ensemble des sols appliqués à cet écran.
	 * @return l'ensemble des sols appliqués à cet écran
	 */
	public LinkedList<Filtre> getSols() {
		return sols;
	}

	@Override
	protected void paintComponent(Graphics g) {
		for (Filtre sol : sols) {
			sol.dessiner(g, getWidth(), getHeight());
		}
		if (camera != null) camera.dessiner(g);
		for (Filtre ciel : ciels) {
			ciel.dessiner(g, getWidth(), getHeight());
		}
	}
}
