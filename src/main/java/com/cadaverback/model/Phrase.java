package com.cadaverback.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Phrase
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @ManyToOne(targetEntity = Subject.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    @ManyToOne(targetEntity = Verb.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "verb_id", referencedColumnName = "id")
    private Verb verb;

    @ManyToOne(targetEntity = DirectObject.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "directobject_id", referencedColumnName = "id")
    private DirectObject directObject;

    @ManyToOne(targetEntity = CircumstantialObject.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "circumstantialobject_id", referencedColumnName = "id")
    private CircumstantialObject circumstantialObject;

    @OneToMany(targetEntity = Vote.class, mappedBy = "voteId.phrase", cascade = CascadeType.ALL, orphanRemoval = true) // , targetEntity = Vote.class
    // @JsonManagedReference
    private Set<Vote> listVotes = new HashSet<>();

    private int score;

    public Phrase()
    {
        super();
    }

    public Phrase(long id, Subject subject, Verb verb, DirectObject directObject, CircumstantialObject circumstantialObject)
    {
        super();
        this.id = id;
        this.subject = subject;
        this.verb = verb;
        this.directObject = directObject;
        this.circumstantialObject = circumstantialObject;
    }

    public Subject getSubject()
    {
        return subject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Verb getVerb()
    {
        return verb;
    }

    public void setVerb(Verb verb)
    {
        this.verb = verb;
    }

    public DirectObject getDirectObject()
    {
        return directObject;
    }

    public void setDirectObject(DirectObject directObject)
    {
        this.directObject = directObject;
    }

    public CircumstantialObject getCircumstantialObject()
    {
        return circumstantialObject;
    }

    public void setCircumstantialObject(CircumstantialObject circumstantialObject)
    {
        this.circumstantialObject = circumstantialObject;
    }

    public Set<Vote> getListVotes()
    {
        return listVotes;
    }

    public void setListVotes(Set<Vote> listVotes)
    {
        this.listVotes = listVotes;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

}
