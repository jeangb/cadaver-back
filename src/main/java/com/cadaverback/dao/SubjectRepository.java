package com.cadaverback.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cadaverback.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByUserId(final long userId);

    List<Subject> findAllByLibelle(final String libelle);

    boolean existsByLibelle(final String libelle);

    @Query(value = "select s.* from subject s join phrase p on s.id=p.subject_id where "
            + "(p.subject_id is not null and p.verb_id is not null and p.directobject_id is not null and p.circumstantialobject_id is not null)"
            + " and LTRIM(RTRIM(libelle)) != '' order by rand() limit 1", nativeQuery = true)
    Subject findRandom();
}
