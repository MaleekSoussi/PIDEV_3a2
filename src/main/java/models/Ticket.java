package models;

import java.util.Objects;
public class Ticket {

    private int idT;
    private Event event; // Reference to the associated Event
    private String qrCodeT;

    public Ticket() {
    }

    public Ticket(int idT, Event event, String qrCodeT) {
        this.idT = idT;
        this.event = event;
        this.qrCodeT = qrCodeT;
    }

    public int getIdT() {
        return idT;
    }

    public void setIdT(int idT) {
        this.idT = idT;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getQrCodeT() {
        return qrCodeT;
    }

    public void setQrCodeT(String qrCodeT) {
        this.qrCodeT = qrCodeT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return idT == ticket.idT && Objects.equals(event, ticket.event) && Objects.equals(qrCodeT, ticket.qrCodeT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idT, event, qrCodeT);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "idT=" + idT +
                ", event=" + event +
                ", qrCodeT='" + qrCodeT + '\'' +
                '}';
    }
}
