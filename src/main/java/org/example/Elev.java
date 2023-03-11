package org.example;

public class Elev extends Utilizator {
    private String scoala;

    String getScoala() {
        return scoala;
    }

    void setScoala(String scoala) {
        this.scoala = scoala;
    }

    Elev(String nume, String scoala) {
        super(nume,
                "Subsemnatul %s, elev la scoala %s, va rog sa-mi aprobati urmatoarea solicitare: %s");
        requestsList.add("inlocuire carnet de elev");
        requestsList.add("inlocuire buletin");
        this.scoala = scoala;
    }
}

