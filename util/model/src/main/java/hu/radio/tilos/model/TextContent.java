package hu.radio.tilos.model;

import javax.persistence.*;

@Entity()
@Table(name = "textcontent")
public class TextContent {

    @Id
    private int id;

    @Basic
    @Column
    private String title;

    @Basic
    @Column
    private String type;

    @Basic
    @Column
    private String format;

    @Basic
    @Column
    private String content;

    @Basic
    @Column
    private String alias;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
