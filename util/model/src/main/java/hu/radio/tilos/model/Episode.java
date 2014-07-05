package hu.radio.tilos.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "episode")
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

    @ManyToOne()
    @JoinColumn(name = "radioshow_id", referencedColumnName = "id")
    Show show;

    @OneToOne()
    @JoinColumn(name = "textcontent_id", referencedColumnName = "id")
    TextContent text;

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

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public TextContent getText() {
        return text;
    }

    public void setText(TextContent text) {
        this.text = text;
    }
}
