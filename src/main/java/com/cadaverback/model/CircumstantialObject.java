package com.cadaverback.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "circumstantialobject")
public class CircumstantialObject
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "libelle")
    private String libelle;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    private User user;

    public CircumstantialObject()
    {
        super();
    }

    public CircumstantialObject(long id, String libelle, User user)
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

}
