package hu.radio.tilos.model;

import hu.radio.tilos.model.type.MixCategory;
import hu.radio.tilos.model.type.MixType;

import javax.persistence.*;
import java.util.Date;

@Entity()
@Table(name = "bookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Basic
    @Column(length = 160)
    private String title;


    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date realFrom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date realTo;

    @ManyToOne()
    @JoinColumn(name = "radioshow_id", referencedColumnName = "id")
    Show show;

    @ManyToOne()
    @JoinColumn(name = "episode_id", referencedColumnName = "id")
    Episode episode;

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRealFrom() {
        return realFrom;
    }

    public void setRealFrom(Date realFrom) {
        this.realFrom = realFrom;
    }

    public Date getRealTo() {
        return realTo;
    }

    public void setRealTo(Date realTo) {
        this.realTo = realTo;
    }
}
