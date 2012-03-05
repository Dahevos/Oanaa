package editeur;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DemandeNouvelleCarte extends JDialog {
	
	public DemandeNouvelleCarte(JFrame parent, String title, boolean modal) {
		
		//On appelle le construteur de JDialog correspondant
		super(parent, title, modal);
		//On spécifie une taille
		this.setSize(200, 100);
		//La position
		this.setLocationRelativeTo(null);
		//La boîte ne devra pas être redimensionnable
		this.setResizable(false);
		// on l'initialise
		this.initComponent();
		//Enfin on l'affiche
		this.setVisible(true);
		//Tout ceci ressemble à ce que nous faisons depuis le début avec notre JFrame...

		
	}
	
	private void initComponent() {
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 5, 5);
		add(new JLabel("X :"), gbc);
		
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, 0, 5);
		JTextField XVal = new JTextField(4);
		add(XVal, gbc);

		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 5, 5);
		add(new JLabel("Y :"), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 5, 5);
		JTextField YVal = new JTextField(4);
		add(YVal, gbc);
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.CENTER;
		gbc.weightx = 2;
		gbc.insets = new Insets(5, 5, 0, 5);
		JButton ok = new JButton("Créer");
		add(ok, gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 2;
		gbc.insets = new Insets(5, 5, 0, 5);
		JButton annuler = new JButton("Annuler");
		add(annuler, gbc);
		
		pack();
	}
	

}
