package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cerere implements Comparable<Cerere> {
    private String requestLabel;
    private Utilizator.RequestsType type;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    private Date date;
    private int priority;

    String getTemplatedText() {
        return templatedText;
    }

    void setTemplatedText(String templatedText) {
        this.templatedText = templatedText;
    }

    private String templatedText;

    Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    int getPriority() {
        return priority;
    }

    void setPriority(int priority) {
        this.priority = priority;
    }

    String getRequestLabel() {
        return requestLabel;
    }

    void setRequestLabel(String requestLabel) {
        this.requestLabel = requestLabel;
    }

    Utilizator.RequestsType getType() {
        return type;
    }

    void setType(Utilizator.RequestsType type) {
        this.type = type;
    }

    Cerere(Utilizator.RequestsType type, String date, String priority, String templatedText) {
        this.type = type;
        this.requestLabel = type.label;
        try {
            this.date = formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.priority = Integer.parseInt(priority);
        this.templatedText = templatedText;
    }

    @Override
    public int compareTo(Cerere o) {
        return this.date.compareTo(o.date);
    }

}
