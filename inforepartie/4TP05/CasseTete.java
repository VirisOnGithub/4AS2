package tp_chess;
/**
 * Représentation abstraite du jeu dont le but est
 *  d'amener tous les pions blancs à droite et les pions noirs à gauche
 */
public class CasseTete {
	/**
	 * Une ligne de pion blanc (B) ou noir (N) représenté par des caractères
	 */
	private final char[] ligne = new char[7];

	/**
	 * Initialement, trois pions blancs à droite, un espace au milieu, trois pions noirs à gauche
	 */
	public CasseTete()
	{
		"NNN BBB".getChars(0, 7, ligne, 0);
	}

	/**
	 * Retourne le pion à la case i
	 */
	public char get(int i) {
		return ligne[i];
	}

	/**
	 * Retourne l'indice de la case où le pion de la case i peut aller
	 * Un pion blanc se déplace à gauche, un pion noir à droite.
	 * Un pion se déplace d'une case ou peut sauter un seul pion de l'autre couleur.
	 */
	public int destination(int i) {
		char c = get(i);
		if ( c == ' ' ) return -1;
		int j = i;
		for ( int k = 0; k < 2; k++ ) {
			if ( c == 'B' ) j = j - 1;
			if ( c == 'N' ) j = j + 1;
			if ( j < 0 || j > 6 ) return -1;
			if ( get(j) == ' ' ) {
				return j;
			}
		}
		return -1;
	}

	/**
	 * Vrai si le pion de la case i a pu être déplacé
	 */
	public boolean jouer(int i) {
		int d = destination(i);
		if ( d < 0 ) return false;
		ligne[d] = get(i);
		ligne[i] = ' ';
		return true;
	}
	
	/**
	 * Vrai si le jeu est fini
	 */
	public boolean estFini() {
		for ( int i = 0; i < ligne.length; i++ ) {
			if ( destination(i) >= 0 ) return false;
		}
		return true;
	}

	/**
	 * Vrai si le casse-tête est résolu
	 */
	public boolean gagne() {
		return new String(ligne).equals("BBB NNN");
	}
	
	/**
	 * Représentation de la position courrante
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < ligne.length; i++)
			sb.append(get(i));
		return sb.toString();
	}
}
