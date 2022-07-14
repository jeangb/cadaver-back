package com.cadaverback.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Subject
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "libelle")
    private String libelle;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    private User user;

    public Subject()
    {
        super();
    }

    public Subject(long id, String libelle, User user)
    {
        super();
        this.id = id;
        this.libelle = libelle;
        this.setUser(user);
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getLibelle()
    {
        return libelle;
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Subject [id=");
        builder.append(id);
        builder.append(", libelle=");
        builder.append(libelle);
        builder.append(", user=");
        builder.append(user);
        builder.append("]");
        return builder.toString();
    }

}
