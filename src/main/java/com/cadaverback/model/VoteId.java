package com.cadaverback.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Embeddable
public class VoteId implements Serializable {
    private static final long serialVersionUID = 8504876001894002888L;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(targetEntity = Phrase.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "phrase_id", referencedColumnName = "id")
    // @JsonBackReference
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Phrase phrase;

    public VoteId()
    {
        super();
    }

    public VoteId(User user, Phrase phrase)
    {
        super();
        this.setUser(user);
        this.setPhrase(phrase);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPhrase(), getUser());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VoteId other = (VoteId) obj;
        return Objects.equals(getPhrase(), other.getPhrase()) && Objects.equals(getUser(), other.getUser());
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Phrase getPhrase()
    {
        return phrase;
    }

    public void setPhrase(Phrase phrase)
    {
        this.phrase = phrase;
    }

}
