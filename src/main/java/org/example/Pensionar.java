package org.example;

public class Pensionar extends Utilizator {
    Pensionar(String nume) {
        super(nume,
                "Subsemnatul %s, va rog sa-mi aprobati urmatoarea solicitare: %s");
        requestsList.add("inlocuire carnet de sofer");
        requestsList.add("inlocuire buletin");
        requestsList.add("inregistrare cupoane de pensie");
    }
}

