package model;

public class Joueur {
    private Integer jno;
    private String pseudo;
    private String email;
    private String pwd;
    private Integer elo;

    public Joueur(Integer jno, String pseudo, String email, String pwd, Integer elo) {
        this.jno = jno;
        this.pseudo = pseudo;
        this.email = email;
        this.pwd = pwd;
        this.elo = elo;
    }

    public Integer getJno() {
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

    public Integer getElo() {
        return elo;
    }
}