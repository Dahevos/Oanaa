package affichage;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;

import javax.swing.JPanel;

/**
 * Classe contenant le composant graphique destiné à afficher une scène.
 * Celui-ci contient principalement une image "OffScreen" qui doit être gérée par une caméra.
 * Un écran est donc fortement lié à sa caméra (lorsqu'il en a une) : celle-ci est responsable
 * de toutes les tâches de dessin.
 */
@SuppressWarnings("serial")
public class Ecran extends JPanel {
	/**
	 * Caméra associée à l'écran
	 */
	private Camera camera = null;
	
	/**
	 * Liste des filtres associés à cet écran
	 */
	private final LinkedList<Filtre> filtres = new LinkedList<Filtre>();
	
	/**
	 * Construit un nouvel écran attaché à aucune caméra.
	 */
	public Ecran() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				adapterCamera();
				super.componentResized(e);
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
	 * la méthode <code>redimensionner()</code> de la classe caméra.
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
		repaint();
	}

	/**
	 * Retourne la caméra attachée à cet écran.
	 * @return la caméra attachée à cet écran
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Ajoute un filtre à cet écran, en dessous des autres.
	 * @param filtre filtre à ajouter
	 */
	public void ajouterFiltreDessous(Filtre filtre) {
		filtres.addFirst(filtre);
		filtre.ajouterEcran(this);
	}
	
	/**
	 * Ajoute un filtre à cet écran, au dessus des autres.
	 * @param filtre filtre à ajouter
	 */
	public void ajouterFiltreDessus(Filtre filtre) {
		filtres.addLast(filtre);
		filtre.ajouterEcran(this);
	}
	
	/**
	 * Retire un filtre de cet écran.
	 * @param filtre filtre à retirer
	 */
	public void retirerFiltre(Filtre filtre) {
		filtres.remove(filtre);
	}
	
	/**
	 * Retourne l'ensemble des filtres appliqués à cet écran.
	 * @return l'ensemble des filtres appliqués à cet écran
	 */
	public LinkedList<Filtre> getFiltres() {
		return filtres;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (camera == null) super.paintComponent(g);
		else camera.dessiner(g);
		for (Filtre filtre : filtres) {
			filtre.dessiner(g);
		}
	}
}
