package editeur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.lwjgl.test.opengl.awt.DemoBox.ClearColorDemoBox;

import ressources.*;

public class PlancheRessource extends JPanel implements MouseListener {
	private static final long serialVersionUID = 42L;

	
	
	/** variable de classe contenant l'image à afficher en fond */
    private Theme theme;
    private int X;
    private int Y;
    private int largeurSelection;
    private int hauteurSelection;
    
    public PlancheRessource(String nom) {
    	this.theme = Ressources.getTheme(nom);
    	setPreferredSize(new Dimension(450, 110));
    	X=0;
    	Y=0;
    }
    
    public Theme getTheme() {
    	return theme;
    }
    
    /** Surcharge de la fonction paintComponent() pour afficher notre image */
     public void paintComponent(Graphics g) {
            try {
				g.drawImage(theme.getImage(),0,0,null);
				g.setColor(Color.RED);
				g.draw3DRect(X*32, Y*32, 32, 32, true);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		X = e.getX()/32;
		Y = e.getY()/32;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub


	}




	

	

}
