package com.cadaverback.dao;

import com.cadaverback.model.Phrase;
import com.cadaverback.model.Vote;
import com.cadaverback.model.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, VoteId> {
    boolean existsByVoteId(final VoteId voteId);
    boolean existsByVoteIdAndVote(final VoteId voteId, final int vote);
    // TODO
    @Query(value = "select sum(vote) from vote where phrase_id=", nativeQuery = true)
    int getScoreByPhrase(final Phrase phrase);
}
