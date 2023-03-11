package org.example;

public class Persoana extends Utilizator {

    Persoana(String nume) {
        super(nume,
                "Subsemnatul %s, va rog sa-mi aprobati urmatoarea solicitare: %s");
        requestsList.add("inlocuire carnet de sofer");
        requestsList.add("inlocuire buletin");
    }
}

