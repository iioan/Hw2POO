package org.example;

public class Angajat extends Utilizator {
    public String getCompania() {
        return compania;
    }

    void setCompania(String compania) {
        this.compania = compania;
    }

    private String compania;

    Angajat(String nume, String compania) {
        super(nume,
                "Subsemnatul %s, angajat la compania %s, va rog sa-mi aprobati urmatoarea solicitare: %s");
        requestsList.add("inlocuire buletin");
        requestsList.add("inlocuire carnet de sofer");
        requestsList.add("inregistrare venit salarial");
        this.compania = compania;
    }
}

