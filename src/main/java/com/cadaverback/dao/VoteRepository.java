package com.cadaverback.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cadaverback.model.Phrase;
import com.cadaverback.model.Vote;
import com.cadaverback.model.VoteId;
import com.sun.istack.NotNull;

public interface VoteRepository extends JpaRepository<Vote, VoteId>
{

    boolean existsByVoteId(@NotNull final VoteId voteId);

    boolean existsByVoteIdAndVote(@NotNull final VoteId voteId, @NotNull final int vote);

    // TODO
    @Query(value = "select sum(vote) from vote where phrase_id=", nativeQuery = true)
    int getScoreByPhrase(@NotNull final Phrase phrase);

}
