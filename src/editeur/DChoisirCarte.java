package editeur;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DChoisirCarte extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<Integer> mapInfo = new ArrayList<Integer>();
	private JTextField x, y;
	private JLabel xNom, yNom;


	public DChoisirCarte(JFrame parent, String title, boolean modal){
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

	/**
	 * Méthode appelée pour utiliser la boîte 
	 * @return zInfo
	 */
	public ArrayList<Integer> getValCarte(){
		return this.mapInfo;		
	}

	/**
	 * Initialise le contenu de la boîte
	 */
	private void initComponent(){

		JPanel pan = new JPanel();

		// X		
		xNom = new JLabel("X : ");
		x = new JTextField();
		x.setPreferredSize(new Dimension(50, 25));
		pan.add(xNom);
		pan.add(x);

		yNom = new JLabel("Y : ");
		y = new JTextField();
		y.setPreferredSize(new Dimension(50, 25));
		pan.add(yNom);	
		pan.add(y);	

		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {		

				if (x.getText().equals("") || y.getText().equals("") ) {
					JOptionPane.showMessageDialog(null, "Vous devez spécifier des valeurs !", "Attention", JOptionPane.WARNING_MESSAGE);

				}
				else {

					try {
						mapInfo = new ArrayList<Integer>();
						mapInfo.add((Integer.parseInt(x.getText())));
						mapInfo.add((Integer.parseInt(y.getText())));
						setVisible(false);
					}
					catch (Exception e){
						JOptionPane.showMessageDialog(null, "Vous devez spécifier des entiers !", "Attention", JOptionPane.WARNING_MESSAGE);
					}
				}
			}


		});

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				mapInfo = new ArrayList<Integer>();
				setVisible(false);
			}			
		});



		okBouton.setPreferredSize(new Dimension(75, 25));
		cancelBouton.setPreferredSize(new Dimension(75, 25));
		pan.add(okBouton);
		pan.add(cancelBouton);

		this.add(pan);


	}

}
