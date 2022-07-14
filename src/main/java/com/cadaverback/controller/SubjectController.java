package com.cadaverback.controller;

import java.util.List;
import java.util.Optional;

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

import com.cadaverback.dao.SubjectRepository;
import com.cadaverback.model.Subject;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class SubjectController
{

    @Autowired
    SubjectRepository subjectRepository;

    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject)
    {
        try
        {
            Subject _subject = subjectRepository.save(new Subject(subject.getId(), subject.getLibelle(), subject.getUser()));
            return new ResponseEntity<>(_subject, HttpStatus.CREATED);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAll(@RequestParam(required = false) Long authorId)
    {
        try
        {
            List<Subject> listSubjects;
            if (null == authorId)
            {
                listSubjects = subjectRepository.findAll();
            } else
            {
                listSubjects = subjectRepository.findAllByUserId(authorId);
            }

            if (listSubjects.isEmpty())
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(listSubjects, HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subjects/{id}")
    public ResponseEntity<Subject> getById(@PathVariable("id") long id)
    {
        Optional<Subject> sujectData = subjectRepository.findById(id);

        if (sujectData.isPresent())
        {
            return new ResponseEntity<>(sujectData.get(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/subjects/{id}")
    public ResponseEntity<Subject> update(@PathVariable("id") long id, @RequestBody Subject subject)
    {
        Optional<Subject> subjectData = subjectRepository.findById(id);

        if (subjectData.isPresent())
        {
            Subject mySubject = subjectData.get();
            mySubject.setLibelle(subject.getLibelle());
            mySubject.setUser(subject.getUser());
            return new ResponseEntity<>(subjectRepository.save(mySubject), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
