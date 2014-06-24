package hu.radio.tilos.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Episode {
    @Id
    private int id;

    @Basic
    @Column
    private Timestamp plannedFrom;

    @Basic
    @Column
    private Timestamp plannedTo;

    @Basic
    @Column
    private Timestamp realFrom;

    @Basic
    @Column
    private Timestamp realTo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getPlannedFrom() {
        return plannedFrom;
    }

    public void setPlannedFrom(Timestamp plannedFrom) {
        this.plannedFrom = plannedFrom;
    }

    public Timestamp getPlannedTo() {
        return plannedTo;
    }

    public void setPlannedTo(Timestamp plannedTo) {
        this.plannedTo = plannedTo;
    }

    public Timestamp getRealFrom() {
        return realFrom;
    }

    public void setRealFrom(Timestamp realFrom) {
        this.realFrom = realFrom;
    }

    public Timestamp getRealTo() {
        return realTo;
    }

    public void setRealTo(Timestamp realTo) {
        this.realTo = realTo;
    }
}
