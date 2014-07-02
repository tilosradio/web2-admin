package hu.radio.tilos.model;

import javax.persistence.*;

@Entity()
@Table(name = "show")
public class Show {

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
    private String definition;

    @Basic
    @Column
    private String description;

    @Basic
    @Column
    private int type;

    @Basic
    @Column
    private int status;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
