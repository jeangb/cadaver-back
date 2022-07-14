package com.cadaverback.controller;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cadaverback.dao.CircumstantialObjectRepository;
import com.cadaverback.dao.DirectObjectRepository;
import com.cadaverback.dao.PhraseRepository;
import com.cadaverback.dao.SubjectRepository;
import com.cadaverback.dao.UserRepository;
import com.cadaverback.dao.VerbRepository;
import com.cadaverback.model.CircumstantialObject;
import com.cadaverback.model.DirectObject;
import com.cadaverback.model.Phrase;
import com.cadaverback.model.Subject;
import com.cadaverback.model.User;
import com.cadaverback.model.Verb;
import com.cadaverback.model.Vote;
import com.cadaverback.service.IPhraseRandomGeneratorService;
import com.cadaverback.service.MailService;

//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class PhraseController
{

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    VerbRepository verbRepository;

    @Autowired
    DirectObjectRepository directObjectRepository;

    @Autowired
    CircumstantialObjectRepository circumstantialObjectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailService mailService;

    @Autowired
    IPhraseRandomGeneratorService phraseGeneratorService;

    @PostMapping("/phrases")
    public ResponseEntity<Phrase> create(@RequestBody Phrase phrase)
    {
        try
        {
            Subject subject = null;
            Verb verb = null;
            DirectObject directObject = null;
            CircumstantialObject circumstantialObject = null;

            // Subject
            if (null != phrase.getSubject())
            {
                if (0 != phrase.getSubject().getId())
                {
                    Optional<Subject> subjectData = subjectRepository.findById(phrase.getSubject().getId());
                    if (subjectData.isPresent())
                    {
                        subject = subjectData.get();
                    } else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else
                {
                    subject = phrase.getSubject();
                    subject.setUser(getUserFromWord(subject.getUser()));
                }
            }

            // Verb
            if (null != phrase.getVerb())
            {
                if (0 != phrase.getVerb().getId())
                {
                    Optional<Verb> verbData = verbRepository.findById(phrase.getVerb().getId());
                    if (verbData.isPresent())
                    {
                        verb = verbData.get();
                    } else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else
                {
                    verb = phrase.getVerb();
                    verb.setUser(getUserFromWord(verb.getUser()));
                }
            }

            // Direct Object
            if (null != phrase.getDirectObject())
            {
                if (0 != phrase.getDirectObject().getId())
                {
                    Optional<DirectObject> directObjectData = directObjectRepository.findById(phrase.getDirectObject().getId());
                    if (directObjectData.isPresent())
                    {
                        directObject = directObjectData.get();
                    } else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else
                {
                    directObject = phrase.getDirectObject();
                    directObject.setUser(getUserFromWord(directObject.getUser()));
                }
            }

            // Circumstantial Object
            if (null != phrase.getCircumstantialObject())
            {
                if (0 != phrase.getCircumstantialObject().getId())
                {
                    Optional<CircumstantialObject> circumstantialObjectData = circumstantialObjectRepository.findById(phrase.getCircumstantialObject().getId());
                    if (circumstantialObjectData.isPresent())
                    {
                        circumstantialObject = circumstantialObjectData.get();
                    } else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else
                {
                    circumstantialObject = phrase.getCircumstantialObject();
                    circumstantialObject.setUser(getUserFromWord(circumstantialObject.getUser()));
                }
            }

            Phrase myPhrase = phraseRepository.save(new Phrase(phrase.getId(), subject, verb, directObject, circumstantialObject));
            return new ResponseEntity<>(myPhrase, HttpStatus.CREATED);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User getUserFromWord(User user)
    {
        if (null == user)
        {
            return null;
        } else
        {
            return userRepository.findById(user.getId());
        }
    }

    @GetMapping("/phrases")
    public ResponseEntity<List<Phrase>> getAll(@RequestParam(required = false) Boolean full, @RequestParam(required = false) boolean withScore)
    {

        try
        {
            List<Phrase> phrases;
            if (null != full && full)
            {
                phrases = phraseRepository.findAllBySubjectIsNotNullAndVerbIsNotNullAndDirectObjectIsNotNullAndCircumstantialObjectIsNotNull();
            } else
            {
                phrases = phraseRepository.findAllBySubjectIsNullOrVerbIsNullOrDirectObjectIsNullOrCircumstantialObjectIsNull();
            }

            if (withScore)
            {
                // Calculer le score sur chaque phrase
                for (Phrase phrase : phrases)
                {
                    phrase.setScore(calculScoreOnPhrase(phrase));
                }
            }

            if (phrases.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(phrases, HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Additionne les valeurs des votes d'une phrase pour calculer le score.
     * 
     * @param phrase
     *            {@link Phrase}
     * @return
     */
    private int calculScoreOnPhrase(Phrase phrase)
    {
        return phrase.getListVotes().stream().mapToInt(Vote::getVote).sum();
    }

    @GetMapping("/phrases/{id}")
    public ResponseEntity<Phrase> getById(@PathVariable("id") long id, @RequestParam(required = false) boolean withScore)
    {
        Optional<Phrase> phraseData = phraseRepository.findById(id);

        if (phraseData.isPresent())
        {
            final Phrase phrase = phraseData.get();
            if (withScore)
            {
                // Calculer le score sur la phrase
                phrase.setScore(calculScoreOnPhrase(phrase));
            }

            return new ResponseEntity<>(phrase, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/phrases/generaterandom")
    public ResponseEntity getRandomlyGeneratedPhrase()
    {
        final String generatedPhrase;

        try
        {
            generatedPhrase = phraseGeneratorService.getRandomlyGeneratedPhrase();
            return new ResponseEntity<>("\" " + generatedPhrase + " \"", HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/phrases/{id}")
    public ResponseEntity<Phrase> update(@PathVariable("id") long id, @RequestBody Phrase phrase)
    {
        Optional<Phrase> phraseData = phraseRepository.findById(id);

        if (phraseData.isPresent())
        {
            Phrase myPhrase = phraseData.get();

            if (!StringUtils.equals(null == myPhrase.getSubject() ? null : myPhrase.getSubject().getLibelle(),
                    null == phrase.getSubject() ? null : phrase.getSubject().getLibelle()))
            {
                // phrase.getSubject().setUser(getUserFromWord(phrase.getSubject()));
                myPhrase.setSubject(phrase.getSubject());
            }
            if (!StringUtils.equals(null == myPhrase.getVerb() ? null : myPhrase.getVerb().getLibelle(), null == phrase.getVerb() ? null : phrase.getVerb().getLibelle()))
            {
                myPhrase.setVerb(phrase.getVerb());
            }
            if (!StringUtils.equals(null == myPhrase.getDirectObject() ? null : myPhrase.getDirectObject().getLibelle(),
                    null == phrase.getDirectObject() ? null : phrase.getDirectObject().getLibelle()))
            {
                myPhrase.setDirectObject(phrase.getDirectObject());
            }
            if (!StringUtils.equals(null == myPhrase.getCircumstantialObject() ? null : myPhrase.getCircumstantialObject().getLibelle(),
                    null == phrase.getCircumstantialObject() ? null : phrase.getCircumstantialObject().getLibelle()))
            {
                myPhrase.setCircumstantialObject(phrase.getCircumstantialObject());
                // phrase finie : envoyer mail
                mailService.sendCompletePhraseByMailToUsers(myPhrase);

            }
            return new ResponseEntity<>(phraseRepository.save(myPhrase), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
