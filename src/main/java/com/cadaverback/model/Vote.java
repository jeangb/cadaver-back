package com.cadaverback.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Vote
{

    @EmbeddedId
    private VoteId voteId;

    @Column(name = "vote")
    private int vote;

    public Vote()
    {
        super();
    }

    public Vote(VoteId voteId, int vote)
    {
        super();
        this.voteId = voteId;
        this.vote = vote;
    }

    public VoteId getVoteId()
    {
        return voteId;
    }

    public void setVoteId(VoteId voteId)
    {
        this.voteId = voteId;
    }

    public int getVote()
    {
        return vote;
    }

    public void setVote(int vote)
    {
        this.vote = vote;
    }

}
