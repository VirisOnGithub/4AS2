package metier;

public class Joueur {
    private int jno;
    private String pseudo;
    private String email;
    private String pwd;
    private int elo;

    public Joueur(int jno, String pseudo, String email, String pwd, int elo) {
        this.jno = jno;
        this.pseudo = pseudo;
        this.email = email;
        this.pwd = pwd;
        this.elo = elo;
    }

    public int getJno() {
        return jno;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public int getElo() {
        return elo;
    }
}