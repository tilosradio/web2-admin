package hu.tilos.radio.backend.data.types;

import hu.tilos.radio.backend.data.types.ShowSimple;
import hu.tilos.radio.backend.data.types.TextData;

/**
 * Json transfer object for episodes;
 */
public class EpisodeData {

    private int id;

    private long plannedFrom;

    private long plannedTo;

    private long realFrom;

    private long realTo;

    private ShowSimple show;

    private TextData text;

    private String m3uUrl;
    /**
     * false if generated from scheduling true if comes from real record.
     */
    private boolean persistent = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPlannedFrom() {
        return plannedFrom;
    }

    public void setPlannedFrom(long plannedFrom) {
        this.plannedFrom = plannedFrom;
    }

    public long getPlannedTo() {
        return plannedTo;
    }

    public void setPlannedTo(long plannedTo) {
        this.plannedTo = plannedTo;
    }

    public long getRealFrom() {
        return realFrom;
    }

    public void setRealFrom(long realFrom) {
        this.realFrom = realFrom;
    }

    public long getRealTo() {
        return realTo;
    }

    public void setRealTo(long realTo) {
        this.realTo = realTo;
    }

    public ShowSimple getShow() {
        return show;
    }

    public void setShow(ShowSimple show) {
        this.show = show;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public TextData getText() {
        return text;
    }

    public void setText(TextData text) {
        this.text = text;
    }

    public String getM3uUrl() {
        return m3uUrl;
    }

    public void setM3uUrl(String m3uUrl) {
        this.m3uUrl = m3uUrl;
    }
}
