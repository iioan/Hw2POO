package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class Utilizator {

    public enum RequestsType {
        INLOCUIRE_BULETIN("inlocuire buletin"),
        INREGISTRARE_VENIT_SALARIAL("inregistrare venit salarial"),
        INLOCUIRE_CARNET_SOFER("inlocuire carnet de sofer"),
        INLOCUIRE_CARNET_ELEV("inlocuire carnet de elev"),
        CREARE_ACT_CONSTITUTIV("creare act constitutiv"),
        REINNOIRE_AUTORIZATIE("reinnoire autorizatie"),
        INREGISTRARE_CUPOANE_PENSIE("inregistrare cupoane de pensie");
        public final String label;

        RequestsType(String label) {
            this.label = label;
        }
    }

    public String getNume() {
        return nume;
    }

    private final String nume;
    List<String> requestsList = new ArrayList<>();
    private final String template;

    public String getTemplate() {
        return template;
    }

    Utilizator(String nume, String template) {
        this.nume = nume;
        this.template = template;
    }

    Queue<Cerere> waitingQueue = new PriorityQueue<>();
    Queue<Cerere> aux = new PriorityQueue<>();
    Queue<Cerere> finishedQueue = new PriorityQueue<>();

    /* scrie textul cererii */
    String writeRequest(RequestsType type) throws NotMyRequestException {
        if (!isRequestValid(type)) {
            String user = this.getClass().getSimpleName().toLowerCase();
            if (user.equals("entitatejuridica"))
                user = "entitate juridica";
            String message = "Utilizatorul de tip " + user +
                    " nu poate inainta o cerere de tip " + type.label;
            throw new NotMyRequestException(message);
        } else
            switch (this.getClass().getSimpleName()) {
                case "Persoana":
                    return String.format(this.template, this.nume, type.label);
                case "Pensionar":
                    return String.format(this.template, this.nume, type.label);
                case "Angajat":
                    return String.format(this.template, this.nume, ((Angajat) this).getCompania(), type.label);
                case "Elev":
                    return String.format(this.template, this.nume, ((Elev) this).getScoala(), type.label);
                case "EntitateJuridica":
                    return String.format(this.template, ((EntitateJuridica) this).getReprezentant(), this.nume, type.label);
                default:
                    return null;
            }
    }

    /* verifica daca cererea este valida pentru tipul de utilizator */
    boolean isRequestValid(RequestsType type) {
        return requestsList.contains(type.label);
    }

    /* adauga cererea in coada de asteptare */
    Cerere addRequest(String RequestType, String date, String priority) {
        try {
            RequestsType type = getRT(RequestType);
            String templated = this.writeRequest(type);
            if (templated != null) {
                Cerere cerere = new Cerere(type, date, priority, templated);
                this.waitingQueue.add(cerere);
                return cerere;
            }
        } catch (NotMyRequestException e) {
            printException(e.getMessage());
        }
        return null;
    }

    /* returneaza tipul cererii */
    RequestsType getRT(String requestValue) {
        for (RequestsType type : RequestsType.values()) {
            if (type.label.contains(requestValue)) {
                return type;
            }
        }
        return null;
    }

    /* scrie cererile in fisier */
    void printWaitingRequests() {
        this.sortQueue(this.waitingQueue);
        String fileOutput = "src/main/resources/output/" + ManagementPrimarie.file;
        try {
            FileWriter fw = new FileWriter(fileOutput, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.nume + " - cereri in asteptare:" + "\n");
            for (Cerere cerere : this.waitingQueue) {
                bw.write(cerere.formatter.format(cerere.getDate()) + " - " + cerere.getTemplatedText() + "\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* scrie exceptia in fisier */
    void printException(String message) {
        String fileOutput = "src/main/resources/output/" + ManagementPrimarie.file;
        try {
            FileWriter fw = new FileWriter(fileOutput, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(message + "\n");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* scrie cererile finalizate in fisier */
    void printFinishedRequests() {
        String fileOutput = "src/main/resources/output/" + ManagementPrimarie.file;
        try {
            FileWriter fw = new FileWriter(fileOutput, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(this.nume + " - cereri in finalizate:" + "\n");
            for (Cerere cerere : this.finishedQueue) {
                bw.write(cerere.formatter.format(cerere.getDate()) + " - " + cerere.getTemplatedText() + "\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* scoate cererea din coada de asteptare */
    void removeRequest(String requestDate) {
        for (Cerere cerere : this.waitingQueue) {
            if (cerere.formatter.format(cerere.getDate()).equals(requestDate)) {
                this.waitingQueue.remove(cerere);
                break;
            }
        }
    }

    /* sorteaza cererile dupa data */
    void sortQueue(Queue<Cerere> queue) {
        while (!queue.isEmpty()) {
            this.aux.add(queue.poll());
        }
        while (!this.aux.isEmpty()) {
            queue.add(this.aux.poll());
        }
    }

    /* adauga cererea in coada de finalizate */
    void finishRequest(Cerere cerere) {
        this.finishedQueue.add(cerere);
    }
}
