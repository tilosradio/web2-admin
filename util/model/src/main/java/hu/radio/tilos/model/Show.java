package hu.radio.tilos.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity()
@Table(name = "radioshow")
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

    @OneToMany(mappedBy = "show")
    private Set<Mix> mixes = new HashSet();

    @OneToMany(mappedBy = "show")
    private Set<Contribution> contributors = new HashSet();

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

    public Set<Mix> getMixes() {
        return mixes;
    }

    public void setMixes(Set<Mix> mixes) {
        this.mixes = mixes;
    }

    public Set<Contribution> getContributors() {
        return contributors;
    }

    public void setContributors(Set<Contribution> contributors) {
        this.contributors = contributors;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
