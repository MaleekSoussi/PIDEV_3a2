package models;

import java.time.LocalDate;
import java.util.Objects;

public class Event {
    private int idE;
    private String nameE;
    private LocalDate dateE;
    private int durationE;
    private String typeE;
    private double entryFeeE;
    private int capacityE;
    private String imageE;


    // Default constructor
    public Event() {
    }

    public Event(String nameE, LocalDate dateE, int durationE, String typeE, double entryFeeE, int capacityE) {
        this.nameE = nameE;
        this.dateE = dateE;
        this.durationE = durationE;
        this.typeE = typeE;
        this.entryFeeE = entryFeeE;
        this.capacityE = capacityE;
    }

    // Constructor with LocalDate parameters
    public Event(int idE, String nameE, LocalDate dateE, int durationE, String typeE, double entryFeeE, int capacityE, String imageE) {
        this.idE = idE;
        this.nameE = nameE;
        this.dateE = dateE;
        this.durationE = durationE;
        this.typeE = typeE;
        this.entryFeeE = entryFeeE;
        this.capacityE = capacityE;
        this.imageE = imageE;
    }

    public Event(String nameE, LocalDate dateE, int durationE, String typeE, double entryFeeE, int capacityE, String imageE) {
        this.nameE = nameE;
        this.dateE = dateE;
        this.durationE = durationE;
        this.typeE = typeE;
        this.entryFeeE = entryFeeE;
        this.capacityE = capacityE;
        this.imageE = imageE;
    }

    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    public String getNameE() {
        return nameE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
    }

    public LocalDate getDateE() {
        return dateE;
    }

    public void setDateE(LocalDate dateE) {
        this.dateE = dateE;
    }

    public int getDurationE() {
        return durationE;
    }

    public void setDurationE(int durationE) {
        this.durationE = durationE;
    }

    public String getTypeE() {
        return typeE;
    }

    public void setTypeE(String typeE) {
        this.typeE = typeE;
    }

    public double getEntryFeeE() {
        return entryFeeE;
    }

    public void setEntryFeeE(double entryFeeE) {
        this.entryFeeE = entryFeeE;
    }

    public int getCapacityE() {
        return capacityE;
    }

    public void setCapacityE(int capacityE) {
        this.capacityE = capacityE;
    }

    public String getImageE() {
        return imageE;
    }

    public void setImageE(String imageE) {
        this.imageE = imageE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return idE == event.idE &&
                durationE == event.durationE &&
                Double.compare(event.entryFeeE, entryFeeE) == 0 &&
                capacityE == event.capacityE &&
                Objects.equals(nameE, event.nameE) &&
                Objects.equals(dateE, event.dateE) &&
                Objects.equals(typeE, event.typeE) &&
                Objects.equals(imageE, event.imageE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idE, nameE, dateE, durationE, typeE, entryFeeE, capacityE, imageE);
    }

    @Override
    public String toString() {
        return "Event{" +
                "idE=" + idE +
                ", nameE='" + nameE + '\'' +
                ", dateE=" + dateE +
                ", durationE=" + durationE +
                ", typeE='" + typeE + '\'' +
                ", entryFeeE=" + entryFeeE +
                ", capacityE=" + capacityE +
                ", imageE='" + imageE + '\'' +
                '}';
    }
}
