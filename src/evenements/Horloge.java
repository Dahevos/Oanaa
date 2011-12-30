package evenements;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe définissant une horloge, capable de d'exécuter périodiquement un ensemble de traitants.
 */
public class Horloge {
	/**
	 * Période de l'horloge.
	 */
	private long periode;
	
	/**
	 * Ensemble des traitants à exécuter.
	 */
	private Set<Runnable> traitants = Collections.synchronizedSet(new HashSet<Runnable>());

	/**
	 * Construit une nouvelle horloge.
	 * @param freq fréquence de l'horloge
	 */
	public Horloge(long freq) {
		this.periode = 1000L / freq;
		new ThreadHorloge().start();
	}
	
	/**
	 * Modifie la fréquence de cette horloge.
	 * @param freq nouvelle fréquence
	 */
	public void setFreq(long freq) {
		this.periode = 1000L / freq;
	}
	
	/**
	 * Ajoute un traitant à cette horloge.
	 * @param traitant nouveau traitant
	 * @return <code>true</code> ssi le traitant a été correctement ajouté
	 */
	public boolean ajouterCode(Runnable traitant) {
		return traitant != null && traitants.add(traitant);
	}
	
	/**
	 * Retire un traitant de cette horloge.
	 * @param traitant traitant à retirer
	 * @return <code>true</code> ssi le traitant a été correctement retiré
	 */
	public boolean retirerCode(Runnable traitant) {
		return traitant != null && traitants.remove(traitant);
	}
	
	/**
	 * Thread responsable de lancement des traitants.
	 */
	private class ThreadHorloge extends Thread {
		@Override
		public void run() {
			super.run();
			while (true) {
				try {
					Thread.sleep(periode);
					for (Runnable code : traitants) {
						new Thread(code).start();
					}
				} catch (InterruptedException e) {}
			}
		}
	}
}
