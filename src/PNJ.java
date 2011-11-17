
public class PNJ extends Personnage {
	public static final int DUREE_MIN_DEFAUT = 500;
	public static final int DUREE_MAX_DEFAUT = 2500;
	
	private final int dureeMin;
	private final int dureeMax;

	public PNJ(Apparence apparence, int dureeMin, int dureeMax) {
		super(apparence);
		this.dureeMin = dureeMin;
		this.dureeMax = dureeMax;
		new Thread(new Generateur()).start();
	}

	public PNJ(Apparence apparence) {
		this(apparence, DUREE_MIN_DEFAUT, DUREE_MAX_DEFAUT);
	}
	
	public PNJ(Apparence apparence, Carte carte, int i, int j, int dureeMin, int dureeMax) {
		super(apparence, carte, i, j);
		this.dureeMin = dureeMin;
		this.dureeMax = dureeMax;
		new Thread(new Generateur()).start();
	}
	
	public PNJ(Apparence apparence, Carte carte, int i, int j) {
		this(apparence, carte, i, j, DUREE_MIN_DEFAUT, DUREE_MAX_DEFAUT);
	}

	private class Generateur implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(Math.round(dureeMin + (dureeMax - dureeMin) * Math.random()));
				} catch (InterruptedException e) {}
				deplacer(Direction.values()[(int) Math.floor(4 * Math.random())]);
			}
		}
	}
}
