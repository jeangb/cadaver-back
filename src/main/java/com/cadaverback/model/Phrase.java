package com.cadaverback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "phrase")
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

    @JsonIgnore
    public String getContenu()
    {
        return beautifyPhrase(new StringBuilder(this.subject.getLibelle()).append(" ").append(this.verb.getLibelle()).append(" ").append(this.directObject.getLibelle()).append(" ")
                .append(this.circumstantialObject.getLibelle()));
    }

    @JsonIgnore
    public String getMailsAuteursSepparatedByComma()
    {
        final List<User> authors = new ArrayList<>();
        authors.add(this.getSubject().getUser());
        authors.add(this.getVerb().getUser());
        authors.add(this.getDirectObject().getUser());
        authors.add(this.getCircumstantialObject().getUser());

        final List<String> distinctsUsersMails = authors.stream().map(User::getEmail).filter(m -> StringUtils.isNotEmpty(m)).distinct().collect(Collectors.toList());
        return String.join(",", distinctsUsersMails);
    }

    @JsonIgnore
    public List<String> getMailsAuteurs() {
        final List<User> authors = new ArrayList<>();
        authors.add(this.getSubject().getUser());
        authors.add(this.getVerb().getUser());
        authors.add(this.getDirectObject().getUser());
        authors.add(this.getCircumstantialObject().getUser());
        return authors.stream().map(User::getEmail).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
    }

    @JsonIgnore
    public String getAuteursUsernamesSepparatedByComma()
    {
        final List<User> authors = new ArrayList<>();
        authors.add(this.getSubject().getUser());
        authors.add(this.getVerb().getUser());
        authors.add(this.getDirectObject().getUser());
        authors.add(this.getCircumstantialObject().getUser());

        final List<String> distinctsUsernames = authors.stream().map(User::getUsername).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        return String.join(", ", distinctsUsernames);
    }

    /**
     * Ajoute une majuscule au début de la phrase ainsi qu'un point à la fin.
     *
     * @param phrase
     * @return
     */
    private String beautifyPhrase(StringBuilder phrase)
    {
        phrase.append(".");
        return phrase.substring(0, 1).toUpperCase() + phrase.substring(1);
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
