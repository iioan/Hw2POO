package org.example;

public class EntitateJuridica extends Utilizator {
    private String reprezentant;

    EntitateJuridica(String nume, String reprezentant) {
        super(nume,
                "Subsemnatul %s, reprezentant legal al companiei %s, va rog sa-mi aprobati urmatoarea solicitare: %s");
        requestsList.add("creare act constitutiv");
        requestsList.add("reinnoire autorizatie");
        this.reprezentant = reprezentant;
    }

    String getReprezentant() {
        return reprezentant;
    }

    void setReprezentant(String reprezentant) {
        this.reprezentant = reprezentant;
    }
}

