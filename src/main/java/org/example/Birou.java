package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Birou<T extends Utilizator> {
    /* coada de cereri in asteptare */
    Queue<BirouItem<T>> items;
    Queue<BirouItem<T>> aux;

    List<FunctionarPublic<T>> functionari = new ArrayList<>();

    /* element = un utilizator + o cerere */
    BirouItem<T> element;

    Birou() {
        items = new PriorityQueue<>();
        aux = new PriorityQueue<>();
    }

    /* adauga un element in coada de asteptare */
    void add(T t, Cerere cerere) {
        element = new BirouItem<>(t, cerere);
        this.items.add(element);
        sortQueue(this.items);
    }

    /* scrie in fisier cererile in asteptare */
    void printQueue(String birou) {
        String fileOutput = "src/main/resources/output/" + ManagementPrimarie.file;
        try {
            FileWriter fw = new FileWriter(fileOutput, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(birou + " - cereri in birou:" + "\n");
            for (BirouItem<T> element : this.items) {
                bw.write(element.toString() + "\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* sorteaza coada de cereri in asteptare */
    void sortQueue(Queue<BirouItem<T>> queue) {
        while (!queue.isEmpty()) {
            this.aux.add(queue.poll());
        }
        while (!this.aux.isEmpty()) {
            queue.add(this.aux.poll());
        }
    }

    /* adauga un functionar public in birou */
    void addEmployee(String nume) {
        FunctionarPublic<T> functionar = new FunctionarPublic<>(nume);
        this.functionari.add(functionar);
    }

    /* rezolva cererea primului element din coada de asteptare */
    void resolveRequest(String nume) {
        FunctionarPublic<T> functionar = new FunctionarPublic<>(nume);
        if (!this.items.isEmpty()) {
            BirouItem<T> element = this.items.poll();
            // sterge din coada de asteptare si adauga in coada de rezolvate
            element.getUser().removeRequest(element.getRequest().formatter.format(element.getRequest().getDate()));
            element.getUser().finishRequest(element.getRequest());
            sortQueue(this.items);
            // adaug cererea in fisierul functionarului
            int index = this.functionari.indexOf(functionar);
            functionar = this.functionari.get(index);
            functionar.addToEmployeeList(element);
        }
    }

    /* retrage cererea cu data si numele specificate din coada biroului*/
    void removeRequest(String requestDate, String nume) {
        for (BirouItem<T> element : this.items) {
            if (element.getRequest().formatter.format(element.getRequest().getDate()).equals(requestDate) && element.getUser().getNume().equals(nume)) {
                this.items.remove(element);
                break;
            }
        }
    }
}
