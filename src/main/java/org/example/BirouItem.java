package org.example;

public class BirouItem<T> implements Comparable<BirouItem<T>> {
    private final T user;
    private final Cerere request;

     BirouItem(T user, Cerere request) {
        this.user = user;
        this.request = request;
    }

     T getUser() {
        return user;
    }

     Cerere getRequest() {
        return request;
    }

    /* compara doua cereri dupa prioritare, iar in caz de =, dupa data*/
    @Override
    public int compareTo(BirouItem o) {
        if (this.request.getPriority() > o.request.getPriority()) {
            return -1;
        } else if (this.request.getPriority() < o.request.getPriority()) {
            return 1;
        } else {
            // if priority is equal, order by date
            if (this.request.getDate().after(o.request.getDate())) {
                return 1;
            } else if (this.request.getDate().before(o.request.getDate())) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    // format pentru a afisa cererea
    @Override
    public String toString() {
        return this.getRequest().getPriority() + " - " +
                this.getRequest().formatter.format(getRequest().getDate()) + " - " +
                this.getRequest().getTemplatedText();
    }
}
