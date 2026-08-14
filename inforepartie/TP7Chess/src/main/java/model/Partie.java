package model;

import java.time.LocalDate;

public class Partie {
    private Integer pno;
    private Integer jno1;
    private Integer jno2;
    private LocalDate date;
    private Integer statut;
    private Integer temps;
    private Integer gagnant;

    public Partie(Integer pno, Integer jno1, Integer jno2, LocalDate date, Integer statut, Integer temps, Integer gagnant) {
        this.pno = pno;
        this.jno1 = jno1;
        this.jno2 = jno2;
        this.date = date;
        this.statut = statut;
        this.temps = temps;
        this.gagnant = gagnant;
    }

    public Integer getPno() { return pno; }
    public Integer getJno1() { return jno1; }
    public Integer getJno2() { return jno2; }
    public LocalDate getDate() { return date; }
    public Integer getStatut() { return statut; }
    public Integer getTemps() { return temps; }
    public Integer getGagnant() { return gagnant; }
}
