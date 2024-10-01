package com.cadaverback.controller;

import com.cadaverback.dao.SubjectRepository;
import com.cadaverback.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SubjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        try {
            Subject _subject = subjectRepository.save(new Subject(subject.getId(), subject.getLibelle(), subject.getUser()));
            return new ResponseEntity<>(_subject, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAll(@RequestParam(required = false) Long authorId) {
        try {
            List<Subject> listSubjects;
            if (null == authorId) {
                listSubjects = subjectRepository.findAll();
            } else {
                listSubjects = subjectRepository.findAllByUserId(authorId);
            }

            if (listSubjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listSubjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<Subject> getById(@PathVariable("id") long id) {
        Optional<Subject> sujectData = subjectRepository.findById(id);

        if (sujectData.isPresent()) {
            return new ResponseEntity<>(sujectData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<Subject> update(@PathVariable("id") long id, @RequestBody Subject subject) {
        Optional<Subject> subjectData = subjectRepository.findById(id);

        if (subjectData.isPresent()) {
            Subject mySubject = subjectData.get();
            mySubject.setLibelle(subject.getLibelle());
            mySubject.setUser(subject.getUser());
            return new ResponseEntity<>(subjectRepository.save(mySubject), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
