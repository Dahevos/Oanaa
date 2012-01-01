package editeur;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import evenements.Horloge;

import ressources.Ressources;

import affichage.Filtre;
import affichage.FiltreMotif;
import affichage.FiltreMotifGlissant;
import affichage.FiltreAjuste;



public class DChoisirFiltreImage extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel imageNom, alphaNom, timerNom, xNom, yNom;
	private SpinnerNumberModel niveauAlpha, niveauTimer, niveauX, niveauY;
	
	private File image;
	private Integer type = 0;
	private Filtre filtre;
	
	public DChoisirFiltreImage(JFrame parent, String title, boolean modal){
		//On appelle le construteur de JDialog correspondant
		super(parent, title, modal);
		//On spécifie une taille
		this.setSize(200, 150);
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
	public Filtre getFiltre(){
		return this.filtre;		
	}

	/**
	 * Initialise le contenu de la boîte
	 */
	
	public File LoadFromFile() {
		String filename = File.separator+"tmp";
		JFileChooser fc = new JFileChooser(new File(filename));
		try {
			// Create a File object containing the canonical path of the
			// desired directory
			File f = new File(new File(".").getCanonicalPath());
			// Set the current directory
			fc.setCurrentDirectory(f);
		} catch (IOException e) {
		}

		// Show open dialog; this method does not return until the dialog is closed
		fc.showOpenDialog(this);
		File selFile = fc.getSelectedFile();
		return selFile;

	}

	
	private void initComponent(){

		JPanel pan = new JPanel();

		// X		
		imageNom = new JLabel("Image : ");
		JButton choisirImage = new JButton("Choisir Image");
		choisirImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				image = LoadFromFile();
			}			
		});


		pan.add(imageNom);
		pan.add(choisirImage);

		alphaNom = new JLabel("Transparence alpha :");

		niveauAlpha = new SpinnerNumberModel(0.0f, //initial value
				0.0f, //min
				1.0f, //max
				0.01f);                //step
		JSpinner jspin = new JSpinner(niveauAlpha);				
		jspin.setPreferredSize(new Dimension(50, 22));
		pan.add(alphaNom);	
		pan.add(jspin);	

		//In initialization code:
	    //Create the radio buttons.
	    JRadioButton normalButton = new JRadioButton("Normal");
	    normalButton.setActionCommand("Normal");
	    normalButton.setSelected(true);

	    JRadioButton adjustButton = new JRadioButton("Adjusté");
	    adjustButton.setActionCommand("Adjusté");

	    JRadioButton defileButton = new JRadioButton("Défilé");
	    defileButton.setActionCommand("Défilé");

	  
	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(normalButton);
	    group.add(adjustButton);
	    group.add(defileButton);

	    pan.add(normalButton);
	    pan.add(adjustButton);
	    pan.add(defileButton);

	    
		timerNom = new JLabel("Minuteur : ");
		niveauTimer = new SpinnerNumberModel(0, //initial value
				0, //min
				Integer.MAX_VALUE, //max
				1);                //step
		final JSpinner jspinTimer = new JSpinner(niveauTimer);		

		
		xNom = new JLabel("Pas X : ");
		niveauX = new SpinnerNumberModel(0, //initial value
				0, //min
				Integer.MAX_VALUE, //max
				1);                //step
		final JSpinner jspinX = new JSpinner(niveauX);		
		
		
		yNom = new JLabel("Pas Y : ");
		niveauY = new SpinnerNumberModel(0, //initial value
				0, //min
				Integer.MAX_VALUE, //max
				1);                //step
		final JSpinner jspinY = new JSpinner(niveauY);		

		jspinTimer.setVisible(false);
		timerNom.setVisible(false);
		jspinX.setVisible(false);
		xNom.setVisible(false);
		jspinY.setVisible(false);
		yNom.setVisible(false);
		
		pan.add(timerNom);	
		pan.add(jspinTimer);	
		pan.add(xNom);	
		pan.add(jspinX);	
		pan.add(yNom);	
		pan.add(jspinY);	
				
	    //Register a listener for the radio buttons.
	    normalButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setSize(200, 150);
				type=0;
				jspinTimer.setVisible(false);
				timerNom.setVisible(false);
				jspinX.setVisible(false);
				xNom.setVisible(false);
				jspinY.setVisible(false);
				yNom.setVisible(false);
			}
	    });
	    adjustButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {		
				setSize(200, 150);
				type=1;
				jspinTimer.setVisible(false);
				timerNom.setVisible(false);
				jspinX.setVisible(false);
				xNom.setVisible(false);
				jspinY.setVisible(false);
				yNom.setVisible(false);
			}
	    });
	    defileButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setSize(200, 225);

				type=2;
				jspinTimer.setVisible(true);
				timerNom.setVisible(true);
				jspinX.setVisible(true);
				xNom.setVisible(true);
				jspinY.setVisible(true);
				yNom.setVisible(true);

			}
	    });

		
		
		
		JButton okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {		

					try {
						if (type == 0)
							filtre = new FiltreMotif(Ressources.getMotif(image.getCanonicalPath()), (Float)niveauAlpha.getValue());
						else if (type == 1)
							filtre = new FiltreAjuste(Ressources.getMotif(image.getCanonicalPath()), (Float)niveauAlpha.getValue());
						else
							filtre = new FiltreMotifGlissant(Ressources.getMotif(image.getCanonicalPath()),
									 (Float)niveauAlpha.getValue(), new Horloge((Integer)niveauTimer.getValue()), (Integer)niveauX.getValue(), (Integer)niveauY.getValue());
				
						setVisible(false);
					}
					catch (Exception e){
						JOptionPane.showMessageDialog(null, "Option invalide", "Attention", JOptionPane.WARNING_MESSAGE);
					}
			
			}


		});

		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				filtre = null;
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
