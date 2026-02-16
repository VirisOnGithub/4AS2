package exerciceCafe3;

public abstract class Boisson {
    private String nom;
    private double prix;

    public Boisson(String nom, double prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Boisson{" +
                "nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }

    public abstract void recette();

}
