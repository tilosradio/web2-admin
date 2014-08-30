package hu.tilos.radio.backend.data;

import hu.tilos.radio.backend.data.types.ShowNamed;
import hu.tilos.radio.backend.data.types.ShowSimple;

import javax.persistence.*;
import java.util.Date;

public class BookmarkSimple {

    private int id;

    private String title;

    private Date realFrom;

    private Date realTo;

    private ShowNamed show;

    public ShowNamed getShow() {
        return show;
    }

    public void setShow(ShowNamed show) {
        this.show = show;
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
