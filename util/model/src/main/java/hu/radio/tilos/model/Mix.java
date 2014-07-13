package hu.radio.tilos.model;

import javax.persistence.*;
import java.util.Date;

@Entity()
@Table(name = "mix")
public class Mix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Basic
    @Column
    private String author;


    @Basic
    @Column
    private String title;


    @Basic
    @Column
    private String file;

    @ManyToOne
    @JoinColumn(name = "show_id")
    private Show show;

    @Temporal(TemporalType.DATE)
    @Column
    private Date date;

    @Column
    private MixType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MixType getType() {
        return type;
    }

    public void setType(MixType type) {
        this.type = type;
    }
}
