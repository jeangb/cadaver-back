package com.cadaverback.controller;

import com.cadaverback.dao.VoteRepository;
import com.cadaverback.model.Phrase;
import com.cadaverback.model.User;
import com.cadaverback.model.Vote;
import com.cadaverback.model.VoteId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VoteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);
    private final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/votes/{userId}/{phraseId}")
    public ResponseEntity<Vote> getById(@PathVariable("userId") int userId, @PathVariable("phraseId") int phraseId) {
        VoteId voteId = new VoteId(new User(userId, null, null), new Phrase(phraseId, null, null, null, null));
        Optional<Vote> vote = voteRepository.findById(voteId);
        if (vote.isPresent()) {
            return new ResponseEntity<>(vote.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/votes/exists/{userId}/{phraseId}/{voteValue}")
    public ResponseEntity<Boolean> existsByIdAndVoteValue(@PathVariable("userId") int userId, @PathVariable("phraseId") int phraseId, @PathVariable("voteValue") int voteValue) {
        VoteId voteId = new VoteId(
                new User(userId, null, null),
                new Phrase(phraseId, null, null, null, null)
        );
        if (voteRepository.existsByVoteIdAndVote(voteId, voteValue)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/votes", method = RequestMethod.POST)
    public ResponseEntity<Vote> create(@RequestBody Vote vote) {
        try {
            voteRepository.save(vote);
            return new ResponseEntity<>(vote, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/votes/{userId}/{phraseId}")
    public ResponseEntity<Vote> deleteById(@PathVariable("userId") int userId, @PathVariable("phraseId") int phraseId) {
        VoteId voteId = new VoteId(
                new User(userId, null, null),
                new Phrase(phraseId, null, null, null, null)
        );
        try {
            voteRepository.deleteById(voteId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
