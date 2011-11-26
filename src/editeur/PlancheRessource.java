package editeur;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JPanel;

import ressources.*;

public class PlancheRessource extends JPanel {
	private static final long serialVersionUID = 42L;

	
	
	/** variable de classe contenant l'image à afficher en fond */
    private Theme theme;
    
    public PlancheRessource(String nom) {
    	this.theme = Ressources.getTheme(nom);
    	setPreferredSize(new Dimension(450, 110));

    }
    
    public Theme getTheme() {
    	return theme;
    }
    
    /** Surcharge de la fonction paintComponent() pour afficher notre image */
     public void paintComponent(Graphics g) {
            try {
				g.drawImage(theme.getImage(),0,0,null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    } 

}
