package hu.radio.tilos.model;

import javax.persistence.*;

@Entity()
@Table(name = "author")
public class Author {

    @Id
    private int id;

    @Basic
    @Column
    private String name;

    @Basic
    @Column
    private String alias;

    @Basic
    @Column
    private String photo;

    @Basic
    @Column
    private String avatar;

    @Basic
    @Column
    private String introduction;

    @Basic
    @Column
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
