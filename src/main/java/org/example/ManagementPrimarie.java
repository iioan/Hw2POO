package org.example;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ManagementPrimarie {
    Map<String, Utilizator> map = new HashMap<>();

    /* numele fisierului */
    public static String file;
    Birou<Elev> elevBirou;
    Birou<Persoana> persoanaBirou;
    Birou<EntitateJuridica> entitateJuridicaBirou;
    Birou<Pensionar> persionarBirou;
    Birou<Angajat> angajatBirou;

    ManagementPrimarie(String[] args) {
        file = args[0];
        this.elevBirou = new Birou<>();
        this.persoanaBirou = new Birou<>();
        this.entitateJuridicaBirou = new Birou<>();
        this.persionarBirou = new Birou<>();
        this.angajatBirou = new Birou<>();
    }

    public static void main(String[] args) throws IOException, ParseException {
        ManagementPrimarie managementPrimarie = new ManagementPrimarie(args);
        /* se curata fisierul de output pentru adaugarea informatiilor */
        // managementPrimarie.cleanUp();
        managementPrimarie.readFile();
    }

    /* citeste fisierul de input si apeleaza metodele corespunzatoare dupa caz */
    void readFile() throws IOException, ParseException {
        String fileName = "src/main/resources/input/" + file;
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String sv = line.split("; ")[0];
                switch (sv) {
                    case "adauga_utilizator":
                        this.addUser2Map(line);
                        break;
                    case "cerere_noua":
                        this.addRequest2User(line);
                        break;
                    case "afiseaza_cereri_in_asteptare":
                        this.showWaitingRequests(line);
                        break;
                    case "retrage_cerere":
                        this.removeRequest(line);
                        break;
                    case "afiseaza_cereri":
                        this.showBirouRequests(line);
                        break;
                    case "adauga_functionar":
                        this.addEmployee(line);
                        break;
                    case "rezolva_cerere":
                        this.resolveRequest(line);
                        break;
                    case "afiseaza_cereri_finalizate":
                        this.showResolvedRequests(line);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* sterge cererea din coada utilizatorului si din coada biroului */
    void removeRequest(String line) {
        String[] sv = line.split("; ");
        String nume = sv[1];
        String requestDate = sv[2];
        this.map.get(nume).removeRequest(requestDate);
        String birou = this.map.get(nume).getClass().getSimpleName();
        switch (birou) {
            case "Elev":
                this.elevBirou.removeRequest(requestDate, nume);
                break;
            case "Persoana":
                this.persoanaBirou.removeRequest(requestDate, nume);
                break;
            case "EntitateJuridica":
                this.entitateJuridicaBirou.removeRequest(requestDate, nume);
                break;
            case "Pensionar":
                this.persionarBirou.removeRequest(requestDate, nume);
                break;
            case "Angajat":
                this.angajatBirou.removeRequest(requestDate, nume);
                break;
        }
    }

    /* adauga un utilizator nou in map */
    void addUser2Map(String line) {
        String[] sv = line.split("; ");
        switch (sv[1]) {
            case "persoana":
                this.map.put(sv[2], new Persoana(sv[2]));
                break;
            case "angajat":
                this.map.put(sv[2], new Angajat(sv[2], sv[3]));
                break;
            case "pensionar":
                this.map.put(sv[2], new Pensionar(sv[2]));
                break;
            case "elev":
                this.map.put(sv[2], new Elev(sv[2], sv[3]));
                break;
            case "entitate juridica":
                this.map.put(sv[2], new EntitateJuridica(sv[2], sv[3]));
                break;
        }
    }

    /* adauga o cerere noua in coada utilizatorului si in coada biroului */
    public void addRequest2User(String line) {
        String[] sv = line.split("; ");
        String nume = sv[1];
        String requestType = sv[2];
        String date = sv[3];
        String priority = sv[4];
        Utilizator utilizator = this.map.get(nume);
        Cerere cerere = utilizator.addRequest(requestType, date, priority);
        String clasa = utilizator.getClass().getSimpleName();
        if (cerere != null) {
            switch (clasa) {
                case "Elev":
                    this.elevBirou.add((Elev) utilizator, cerere);
                    break;
                case "Persoana":
                    this.persoanaBirou.add((Persoana) utilizator, cerere);
                    break;
                case "EntitateJuridica":
                    this.entitateJuridicaBirou.add((EntitateJuridica) utilizator, cerere);
                    break;
                case "Pensionar":
                    this.persionarBirou.add((Pensionar) utilizator, cerere);
                    break;
                case "Angajat":
                    this.angajatBirou.add((Angajat) utilizator, cerere);
                    break;
            }
        }
    }

    /* afiseaza cererile din coada de asteptare */
    void showWaitingRequests(String line) {
        String[] sv = line.split("; ");
        String nume = sv[1];
        this.map.get(nume).printWaitingRequests();
    }

    /* curata fisierele de output */
    void cleanUp() {
        File file = new File("src/main/resources/output/" + ManagementPrimarie.file);
        if (file.exists()) {
            try {
                new FileWriter(file, false).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* afiseaza cererile din coada biroului */
    void showBirouRequests(String line) {
        String[] sv = line.split("; ");
        String birou = sv[1];
        this.getBirou(birou).printQueue(birou);
    }

    /* adauga un functionar in birou */
    void addEmployee(String line) {
        String[] sv = line.split("; ");
        String birou = sv[1];
        String nume = sv[2];
        this.getBirou(birou).addEmployee(nume);
    }

    /* rezolva o cerere din coada biroului si coada utilizatorului */
    void resolveRequest(String line) {
        String[] sv = line.split("; ");
        String birou = sv[1];
        String nume = sv[2];
        this.getBirou(birou).resolveRequest(nume);
    }

    /* returneaza biroul corespunzator dupa string*/
    Birou<? extends Utilizator> getBirou(String birou) {
        switch (birou) {
            case "elev":
                return this.elevBirou;
            case "persoana":
                return this.persoanaBirou;
            case "entitate juridica":
                return this.entitateJuridicaBirou;
            case "pensionar":
                return this.persionarBirou;
            case "angajat":
                return this.angajatBirou;
        }
        return null;
    }

    /* afiseaza cererile finalizate */
    void showResolvedRequests(String line) {
        String[] sv = line.split("; ");
        String nume = sv[1];
        this.map.get(nume).printFinishedRequests();
    }
}