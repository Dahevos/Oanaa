package exemples;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Exemple pour illustrer le placement des composants avec GridBagLayout.
 */
public class Placement extends JFrame {
	private static final long serialVersionUID = 42L;

	public Placement() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// On définit le gestionnaire de placement (layout manager).
		setLayout(new GridBagLayout());

		/* On construit l'objet de contrainte.
		 * Cet objet permet est utilisé à chaque fois qu'on ajoute un composent dans le conteneur afin
		 * spécifier les contraintes de placement.
		 * Ce n'est qu'une structure contenant tout un tas de paramètres, on pourra donc réutiliser
		 * la meme instance à chaque fois, en modifiant seulement les paramètres nécessaires.
		 * Pour la construction, j'ai mis les paramètres par défaut.
		 */
		GridBagConstraints gbc = new GridBagConstraints(
				/*
				 * gridx et gridy : position dans la grille
				 * Cela permet de spécifier la case du composant. On peut donner une case précise
				 * en spécifiant 2 entiers (>= 0), ou bien utiliser RELATIVE pour passer automatiquement
				 * à la case suivante (par rapport au dernier composant ajouté).
				 */
				GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
				
				/*
				 * gridwidth et gridheight : taille du composant dans la grille
				 * Cela permet de spécifier le nombre de cases occupées par le composant. On peut :
				 *  => donner une taille précise en spécifiant 2 entiers (>= 1)
				 *  => utiliser RELATIVE pour utiliser toutes les cases voisines non occupées
				 *     (dans le ligne ou la colonne)
				 *  => utiliser REMAINDER pour utiliser toutes les cases jusqu'à la fin de la ligne ou de
				 *     la colonne
				 */
				1, 1,
				
				/*
				 * gridweight et gridheight : poid du composant pour l'attribution de l'espace libre
				 * Cela permet de spécifier le comportement lorsque plus d'espace devient disponible :
				 * les lignes (ou colonnes) qui on le plus de poid prendront le plus d'espace.
				 * Par exemple, si une ligne a un poid 1 et une autre un poid 2, alors la deuxième prendra
				 * 2 fois plus d'espace que la première.
				 * Lorsque les valeurs sont nulles, cela signifie qu'aucun espace supplémentaire ne sera
				 * utilisé (la ligne ou la colonne s'ajustera simplement à la taille de ses composants).
				 */
				0, 0,
				
				/*
				 * anchor : position du composant à l'intérieur de sa case (voir Javadoc pour les différentes
				 * valeurs possibles).
				 */
				GridBagConstraints.CENTER,
				
				/*
				 * fill : redimensionnement du composant pour s'ajuster à la taille de la case.
				 * Cela permet de spécifier comment redimensionner le composant si la case est plus grande
				 * que lui (horizontalement, verticalement, les 2, aucun).
				 */
				GridBagConstraints.NONE,
				
				/*
				 * insets : marges externes du composant (en pixels)
				 * Cela permet d'écarter les cases les unes par rapport aux autres.
				 */
				new Insets(0, 0, 0, 0),
				
				/*
				 * ipadx et ipady : marges internes du composant (en pixels).
				 * Cela permet de forcer la case à être plus grande que le composant.
				 */
				0, 0
				);
		
		// On définit la politique d'utilisation de l'espace libre (par défaut, l'espace libre est ignoré).
		// Pour voir l'effet de ces 2 valeurs, il faut essayer de redimensionner la fenêtre.
		gbc.weightx = 1; gbc.weighty = 1; // On veut utiliser l'espace libre

		// On commence par remplir une ligne simple (avec RELATIVE)
		add(new JButton("TOTO"), gbc);
		
		add(new JButton("TOTO"), gbc);
		
		add(new JButton("TOTO"), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER; // C'est le dernier de la ligne
		add(new JButton("TOTO"), gbc);
		
		// Une deuxième ligne avec seulement 3 boutons
		gbc.gridwidth = 1; // Ce n'est pas le dernier !
		add(new JButton("TOTO"), gbc);
		
		gbc.gridwidth = 2; // Un composant sur 2 cases
		gbc.fill = GridBagConstraints.HORIZONTAL; // On l'adapte horizontalement
		add(new JButton("TOTO"), gbc);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.NONE;
		add(new JButton("TOTO"), gbc);
		
		// Un exemple sur 2 lignes
		gbc.gridwidth = 1;
		gbc.gridheight = 2; // Un bouton sur 2 lignes (non étiré)
		add(new JButton("TOTO"), gbc);
		
		gbc.gridx = 2; // On spécifie la position (RELATIVE ne suffit plus)
		gbc.gridy = 2;
		gbc.gridheight = 1;
		add(new JButton("TOTO"), gbc);
		
		gbc.gridx = 3; // Ici, relative aurait suffit, mais c'est pas clair
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(new JButton("TOTO"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		add(new JButton("TOTO"), gbc);

		gbc.gridx = 3;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(new JButton("TOTO"), gbc);
		
		// Un exemple d'ancrage et de remplissage
		gbc.gridx = GridBagConstraints.RELATIVE; // On repasse en relative
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1;
		gbc.gridheight = GridBagConstraints.REMAINDER; // C'est la dernière ligne
		gbc.fill = GridBagConstraints.BOTH; // On remplit tout, l'ancrage ne change rien
		add(new JButton("TOTO"), gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL; // On remplit horizontalement, ancrage vertical ?
		gbc.anchor = GridBagConstraints.PAGE_START; // On ancre en haut
		add(new JButton("TOTO"), gbc);
		
		gbc.anchor = GridBagConstraints.CENTER; // On ancre au milieu
		add(new JButton("TOTO"), gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER; // C'est le dernier composant
		gbc.anchor = GridBagConstraints.PAGE_END; // On ancre en bas
		add(new JButton("TOTO"), gbc);
		
		// On affiche la fenêtre
		setVisible(true);

		// On redimensionne la fenêtre pour s'ajuster à ses composants : cela compacte la grille
		pack();
		
		// On place la fenêtre au centre de l'écran
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Placement();
	}
}
