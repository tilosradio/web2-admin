package hu.tilos.radio.backend.data;

public class MixRequest {

    private int id;

    private String author;

    private String title;

    private String file;

    private EntitySelector show;

    private int type;

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

    public EntitySelector getShow() {
        return show;
    }

    public void setShow(EntitySelector show) {
        this.show = show;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
