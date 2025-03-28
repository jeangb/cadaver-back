package com.cadaverback.model;


import jakarta.persistence.*;

@Entity(name = "directobject")
public class DirectObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "libelle")
    private String libelle;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "authorId", referencedColumnName = "id")
    private User user;

    public DirectObject()
    {
        super();
    }

    public DirectObject(long id, String libelle, User user)
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
