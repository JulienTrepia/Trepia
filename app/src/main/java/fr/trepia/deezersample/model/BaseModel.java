package fr.trepia.deezersample.model;

public class BaseModel {

    protected String checksum;
    protected int total;
    protected String next;

    public String getChecksum() {
        return checksum;
    }

    public int getTotal() {
        return total;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
