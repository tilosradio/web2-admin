package hu.radio.tilos.model;

import hu.radio.tilos.model.type.MixCategory;
import hu.radio.tilos.model.type.MixType;
import hu.radio.tilos.model.type.TagType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity()
@Table(name = "tag")
@NamedQuery(name = "tag.byName", query = "SELECT t FROM Tag t WHERE t.name = :name")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(length = 255)
    private String name;

    @Column
    TagType type;

    @ManyToMany
    @JoinTable(
            name = "tag_textcontent",
            joinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "textcontent_id", referencedColumnName = "id")})
    private List<TextContent> taggedTexts = new ArrayList<TextContent>();

    public List<TextContent> getTaggedTexts() {
        return taggedTexts;
    }

    public void setTaggedTexts(List<TextContent> taggedTexts) {
        this.taggedTexts = taggedTexts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }
}
