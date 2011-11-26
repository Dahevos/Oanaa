package ressources;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Classe permettant de centraliser la gestion des ressources.</br>
 * Tous les chargements de ressources doivent passer par cette classe.
 * @author Cédric CONNES
 * @author Sébastien LENTINI
 */
public class Ressources {
	/**
	 * Emplacement du répertoire contenant les ressources.
	 */
	private static String emplacement = "ressources";

	/**
	 * Sous-répertoire contenant les thèmes.
	 */
	private static final String REP_THEMES = "themes";

	/**
	 * Sous-répertoire contenant les apparences.
	 */
	private static final String REP_APPARENCES = "apparences";

	/**
	 * Ensemble des thèmes déjà chargés.
	 */
	private static final HashMap<String, Theme> themes = new HashMap<String, Theme>();

	/**
	 * Ensemble des apparences déjà chargées.
	 */
	private static final HashMap<String, Apparence> apparences = new HashMap<String, Apparence>();

	/**
	 * Constructeur par défaut (privé pour empêcher toute instanciation).
	 */
	private Ressources() {}

	/**
	 * Retourne l'emplacement du répertoire contenant les ressources.
	 * @return l'emplacement du répertoire contenant les ressources
	 */
	public static String getEmplacement() {
		return emplacement;
	}

	/**
	 * Modifie l'emplacement du répertoire contenant les ressources.
	 * @param emplacement nouvel emplacement du répertoire contenant les ressources
	 */
	public static void setEmplacement(String emplacement) {
		Ressources.emplacement = emplacement;
	}

	/**
	 * Retourne le thème nommé <code>nom</code>, ou <code>Theme.THEME_VIDE</code>
	 * s'il n'est pas disponible.
	 * @param nom nom du thème
	 * @return le thème nommé <code>nom</code>
	 */
	public static Theme getTheme(String nom) {
		Theme theme = themes.get(nom);
		if (theme == null) {
			try {
				theme = new Theme(nom, new File(emplacement + "/" + REP_THEMES + "/" + nom));
			} catch (IOException e) {
				System.err.println("Impossible de charger le thème " + nom);
				theme = Theme.THEME_VIDE;
			}
			themes.put(nom, theme);
		}
		return theme;
	}

	/**
	 * Retourne l'élément (<code>i</code>, <code>j</code>) du thème nommé <code>nom</code>, ou
	 * <code>null</code> si elle n'est pas disponible.
	 * @param nomTheme nom du thème
	 * @param i numéro de la ligne
	 * @param j numéro de la colonne
	 * @return l'élément spécifié
	 */
	public static Element getElement(String nomTheme, int i, int j) {
		try {
			return getTheme(nomTheme).getElement(i, j);
		} catch (IOException e) {
			System.err.println(e);
			return null;
		}
	}

	/**
	 * Retourne l'apparence nommée <code>nom</code>, ou <code>Apparence.APPARENCE_DEFAUT</code>
	 * si elle n'est pas disponible.
	 * @param nom nom de l'apparence
	 * @return l'apparence nommée <code>nom</code>
	 */
	public static Apparence getApparence(String nom) {
		Apparence apparence = apparences.get(nom);
		if (apparence == null) {
			try {
				apparence = new Apparence(new File(emplacement + "/" + REP_APPARENCES + "/" + nom));
			} catch (IOException e) {
				System.err.println("Impossible de charger l'apparence " + nom);
				apparence = Apparence.APPARENCE_DEFAUT;
			}
			apparences.put(nom, apparence);
		}
		return apparence;
	}

	public static void nettoyerThemes() {
		for (Theme theme : themes.values()) {
			theme.nettoyer();
		}
	}
	
	public static void supprimerThemes() {
		themes.clear();
	}
	
	public static void supprimerApparences() {
		apparences.clear();
	}
}
