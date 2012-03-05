package editeur;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DemandeNouvelleCarte extends JDialog {
	
	public DemandeNouvelleCarte() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.insets = new Insets(0, 5, 0, 5);
		add(new JLabel("X :"), gbc);
		
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(new JTextField(4), gbc);

		gbc.weightx = 0;
		gbc.ipadx = 5;
		gbc.insets = new Insets(0, 10, 0, 5);
		add(new JLabel("Y :"), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(new JTextField(4), gbc);
		
		pack();
	}
	
	public static void main(String[] args) {
		new DemandeNouvelleCarte().setVisible(true);
	}
}
