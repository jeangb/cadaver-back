package com.cadaverback.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cadaverback.model.Phrase;
import com.cadaverback.model.Subject;

public interface PhraseRepository extends JpaRepository<Phrase, Long> {
    List<Phrase> findAllBySubject(final Subject subject);

    List<Phrase> findAllBySubjectIsNotNullAndVerbIsNotNullAndDirectObjectIsNotNullAndCircumstantialObjectIsNotNull();

    List<Phrase> findAllBySubjectIsNullOrVerbIsNullOrDirectObjectIsNullOrCircumstantialObjectIsNull();
}
