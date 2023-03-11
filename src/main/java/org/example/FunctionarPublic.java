package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class FunctionarPublic<T extends Utilizator> {
    private final String name;
    /* folosesc count pentru a curata fisierul de output la fiecare rulare
     *  atunci cand e 0, se curata fisierul si se va incepe sa se scrie din nou
     *  se aduna cate un 1 de fiecare daca cand se adauga o cerere rezolvata
     */
    int count = 0;

    FunctionarPublic(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    /* verifica daca doi functionari publici sunt egali */
    public boolean equals(Object o) {
        FunctionarPublic<T> that = (FunctionarPublic<T>) o;
        return Objects.equals(name, that.name);

    }

    @Override
    public String toString() {
        return "FunctionarPublic{" +
                "name='" + name + '\'' +
                '}';
    }

    void addToEmployeeList(BirouItem<T> element) {
        String fileOutput = "src/main/resources/output/"
                + "functionar_" + this.name + ".txt";
        try {
            FileWriter fw = new FileWriter(fileOutput, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String date = element.getRequest().formatter.format(element.getRequest().getDate());
            String requesterName = element.getUser().getNume();
            bw.write(date + " - " + requesterName + "\n");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void cleanUp(String oldFile) {
        File file = new File(oldFile);
        if (file.exists()) {
            try {
                new FileWriter(file, false).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
