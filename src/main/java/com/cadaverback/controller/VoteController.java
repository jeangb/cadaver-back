package com.cadaverback.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cadaverback.dao.VoteRepository;
import com.cadaverback.model.Phrase;
import com.cadaverback.model.User;
import com.cadaverback.model.Vote;
import com.cadaverback.model.VoteId;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class VoteController
{
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/votes/{userId}/{phraseId}")
    public ResponseEntity<Vote> getById(@PathVariable("userId") int userId, @PathVariable("phraseId") int phraseId)
    {
        VoteId voteId = new VoteId(new User(userId, null, null, null), new Phrase(phraseId, null, null, null, null));
        Optional<Vote> vote = voteRepository.findById(voteId);

        if (vote.isPresent())
        {
            return new ResponseEntity<>(vote.get(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/votes/exists/{userId}/{phraseId}/{voteValue}")
    public ResponseEntity<Boolean> existsByIdAndVoteValue(@PathVariable("userId") int userId, @PathVariable("phraseId") int phraseId, @PathVariable("voteValue") int voteValue)
    {
        VoteId voteId = new VoteId(new User(userId, null, null, null), new Phrase(phraseId, null, null, null, null));

        if (voteRepository.existsByVoteIdAndVote(voteId, voteValue))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/votes", method = RequestMethod.POST)
    public ResponseEntity<Vote> create(@RequestBody Vote vote)
    {
        try
        {
            voteRepository.save(vote);
            return new ResponseEntity<>(vote, HttpStatus.CREATED);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/votes/{userId}/{phraseId}")
    public ResponseEntity<Vote> deleteById(@PathVariable("userId") int userId, @PathVariable("phraseId") int phraseId)
    {
        VoteId voteId = new VoteId(new User(userId, null, null, null), new Phrase(phraseId, null, null, null, null));

        try
        {
            voteRepository.deleteById(voteId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
