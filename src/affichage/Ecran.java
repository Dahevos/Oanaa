package affichage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

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
	 * Image "OffScreen", contenant la scène
	 */
	private BufferedImage image = null;
	
	/**
	 * Objet utilisé pour dessiner dans l'offscreen
	 */
	private Graphics g = null;

	/**
	 * Construit un nouvel écran, prêt à être attaché à une caméra.
	 */
	public Ecran() {
		redimensionner();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				redimensionner();
				super.componentResized(e);
			}
		});
		setFocusable(true);
	}

	/**
	 * Attache cet écran à la caméra <code>camera</code>.
	 * <p>Cette méthode est fortement liée à la méthode <code>setEcran()</code> de la classe
	 * <code>Camera</code>.
	 * @param camera caméra à rattacher à cet écran
	 */
	public void setCamera(Camera camera) {
		if (this.camera == camera) return;
		if (this.camera != null) {
			Camera cameraPrec = this.camera;
			this.camera = null;
			cameraPrec.setEcran(null);
		}
		this.camera = camera;
		if (camera != null) camera.setEcran(this);
		adapterCamera();
	}
	
	/**
	 * Paramètre la caméra de cet écran pour afficher correctement ses données dans celui-ci.
	 * Cela permet de transmettre les dimensions de l'écran ainsi que l'objet de dessin.
	 */
	private void adapterCamera() {
		if (camera != null) {
			camera.redimensionner(getWidth(), getHeight(), g);
		} else if (g != null) {
			synchronized (g) {
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		}
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
	 * Redimensionne l'offscreen et ré-adapte la caméra.
	 */
	private void redimensionner() {
		final int largeur = getWidth();
		final int hauteur = getHeight();

		if (largeur > 0 && hauteur > 0) {
			// Transparency.OPAQUE provoque des bug... Pourquoi ?
			image = getGraphicsConfiguration().createCompatibleImage(largeur, hauteur, Transparency.TRANSLUCENT);
			g = image.createGraphics();
			g.setColor(Color.BLACK);
			adapterCamera();
		} else {
			image = null;
			g = null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (image == null || this.g == null) super.paintComponent(g);
		else synchronized (this.g) {
			g.drawImage(image, 0, 0, null);
		}
	}
}
