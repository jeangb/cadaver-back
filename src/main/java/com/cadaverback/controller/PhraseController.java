package com.cadaverback.controller;

import com.cadaverback.dao.*;
import com.cadaverback.model.*;
import com.cadaverback.service.IPhraseService;
import com.cadaverback.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PhraseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhraseController.class);
    private final PhraseRepository phraseRepository;
    private final SubjectRepository subjectRepository;
    private final VerbRepository verbRepository;
    private final DirectObjectRepository directObjectRepository;
    private final CircumstantialObjectRepository circumstantialObjectRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final IPhraseService phraseGeneratorService;

    static final String MSG_ERR_CONFLIT_PHRASE = "Action interdite : 2 mots qui se suivent ont été entrés par le même joueur.";

    public PhraseController(PhraseRepository phraseRepository, SubjectRepository subjectRepository, VerbRepository verbRepository, DirectObjectRepository directObjectRepository, CircumstantialObjectRepository circumstantialObjectRepository, UserRepository userRepository, MailService mailService, IPhraseService phraseGeneratorService) {
        this.phraseRepository = phraseRepository;
        this.subjectRepository = subjectRepository;
        this.verbRepository = verbRepository;
        this.directObjectRepository = directObjectRepository;
        this.circumstantialObjectRepository = circumstantialObjectRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.phraseGeneratorService = phraseGeneratorService;
    }

    @PostMapping("/phrases")
    public ResponseEntity<Phrase> create(@RequestBody Phrase phrase) {
        try {
            Subject subject = null;
            Verb verb = null;
            DirectObject directObject = null;
            CircumstantialObject circumstantialObject = null;

            // Subject
            if (null != phrase.getSubject()) {
                if (phrase.getSubject().getId() != 0) {
                    Optional<Subject> subjectData = subjectRepository.findById(phrase.getSubject().getId());
                    if (subjectData.isPresent()) {
                        subject = subjectData.get();
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else {
                    subject = phrase.getSubject();
                    final User consolidateUser = consolidateUser(subject.getUser());
                    subject.setUser(consolidateUser);
                }
            }

            // Verb
            if (null != phrase.getVerb()) {
                if (phrase.getVerb().getId() != 0) {
                    Optional<Verb> verbData = verbRepository.findById(phrase.getVerb().getId());
                    if (verbData.isPresent()) {
                        verb = verbData.get();
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else {
                    verb = phrase.getVerb();
                    final User consolidateUser = consolidateUser(subject.getUser());
                    verb.setUser(consolidateUser);
                }
            }

            // Direct Object
            if (null != phrase.getDirectObject()) {
                if (phrase.getDirectObject().getId() != 0) {
                    Optional<DirectObject> directObjectData = directObjectRepository.findById(phrase.getDirectObject().getId());
                    if (directObjectData.isPresent()) {
                        directObject = directObjectData.get();
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else {
                    directObject = phrase.getDirectObject();
                    final User consolidateUser = consolidateUser(subject.getUser());
                    directObject.setUser(consolidateUser);
                }
            }

            // Circumstantial Object
            if (null != phrase.getCircumstantialObject()) {
                if (phrase.getCircumstantialObject().getId() != 0) {
                    Optional<CircumstantialObject> circumstantialObjectData = circumstantialObjectRepository.findById(phrase.getCircumstantialObject().getId());
                    if (circumstantialObjectData.isPresent()) {
                        circumstantialObject = circumstantialObjectData.get();
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                    }
                } else {
                    circumstantialObject = phrase.getCircumstantialObject();
                    final User consolidateUser = consolidateUser(subject.getUser());
                    circumstantialObject.setUser(consolidateUser);
                }
            }

            Phrase myPhrase = phraseRepository.save(new Phrase(phrase.getId(), subject, verb, directObject, circumstantialObject));
            return new ResponseEntity<>(myPhrase, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User consolidateUser(User user) {
        return userRepository.findById(user.getId()).orElse(null);
    }

    @GetMapping("/phrases")
    public ResponseEntity<List<Phrase>> getAll(@RequestParam(required = false) Boolean full, @RequestParam(required = false) boolean withScore) {
        try {
            List<Phrase> phrases;
            if (null != full && full) {
                phrases = phraseRepository.findAllBySubjectIsNotNullAndVerbIsNotNullAndDirectObjectIsNotNullAndCircumstantialObjectIsNotNull();
            } else {
                phrases = phraseRepository.findAllBySubjectIsNullOrVerbIsNullOrDirectObjectIsNullOrCircumstantialObjectIsNull();
            }

            if (withScore) {
                // Calculer le score sur chaque phrase
                for (Phrase phrase : phrases) {
                    phrase.setScore(calculScoreOnPhrase(phrase));
                }
            }

            if (phrases.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(phrases, HttpStatus.OK);
        } catch (Exception e) {
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
    private int calculScoreOnPhrase(Phrase phrase) {
        return phrase.getListVotes().stream().mapToInt(Vote::getVote).sum();
    }

    @GetMapping("/phrases/{id}")
    public ResponseEntity<Phrase> getById(@PathVariable("id") long id, @RequestParam(required = false) boolean withScore) {
        Optional<Phrase> phraseData = phraseRepository.findById(id);

        if (phraseData.isPresent()) {
            final Phrase phrase = phraseData.get();
            if (withScore) {
                // Calculer le score sur la phrase
                phrase.setScore(calculScoreOnPhrase(phrase));
            }

            return new ResponseEntity<>(phrase, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/phrases/generaterandom")
    public ResponseEntity getRandomlyGeneratedPhrase() {
        try {
            final String generatedPhrase = phraseGeneratorService.getRandomlyGeneratedPhrase();
            return new ResponseEntity<>("\" " + generatedPhrase + " \"", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/phrases/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Phrase phrase) {
        Optional<Phrase> phraseData = phraseRepository.findById(id);

        if (phraseData.isPresent()) {
            Phrase myPhrase = phraseData.get();

            if (!StringUtils.equals(null == myPhrase.getSubject() ? null : myPhrase.getSubject().getLibelle(),
                    null == phrase.getSubject() ? null : phrase.getSubject().getLibelle())) {
                myPhrase.setSubject(phrase.getSubject());
            }
            if (!StringUtils.equals(null == myPhrase.getVerb() ? null : myPhrase.getVerb().getLibelle(), null == phrase.getVerb() ? null : phrase.getVerb().getLibelle())) {
                if (phrase.getVerb().getUser().getId() == myPhrase.getSubject().getUser().getId()) {
                    return new ResponseEntity<>(MSG_ERR_CONFLIT_PHRASE, HttpStatus.UNAUTHORIZED);
                }
                myPhrase.setVerb(phrase.getVerb());
            }
            if (!StringUtils.equals(null == myPhrase.getDirectObject() ? null : myPhrase.getDirectObject().getLibelle(),
                    null == phrase.getDirectObject() ? null : phrase.getDirectObject().getLibelle())) {
                if (phrase.getDirectObject().getUser().getId() == myPhrase.getVerb().getUser().getId()) {
                    return new ResponseEntity<>(MSG_ERR_CONFLIT_PHRASE, HttpStatus.UNAUTHORIZED);
                }
                myPhrase.setDirectObject(phrase.getDirectObject());
            }
            if (!StringUtils.equals(null == myPhrase.getCircumstantialObject() ? null : myPhrase.getCircumstantialObject().getLibelle(),
                    null == phrase.getCircumstantialObject() ? null : phrase.getCircumstantialObject().getLibelle())) {
                if (phrase.getCircumstantialObject().getUser().getId() == myPhrase.getDirectObject().getUser().getId()) {
                    return new ResponseEntity<>(MSG_ERR_CONFLIT_PHRASE, HttpStatus.UNAUTHORIZED);
                }

                myPhrase.setCircumstantialObject(phrase.getCircumstantialObject());
                // phrase finie : envoyer mail
                mailService.sendCompletePhraseByMailToUsers(myPhrase);
            }
            return new ResponseEntity<>(phraseRepository.save(myPhrase), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
