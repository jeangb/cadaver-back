package com.cadaverback.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cadaverback.model.Verb;
import com.sun.istack.NotNull;

public interface VerbRepository extends JpaRepository<Verb, Long> {
    List<Verb> findAllByUserId(@NotNull final int userId);

    List<Verb> findAllByLibelle(@NotNull final String libelle);

    boolean existsByLibelle(@NotNull final String libelle);

    @Query(value = "select v.* from verb v join phrase p on v.id=p.verb_id where "
            + "(p.subject_id is not null and p.verb_id is not null and p.directobject_id is not null and p.circumstantialobject_id is not null)"
            + "and LTRIM(RTRIM(libelle)) != '' order by rand() limit 1", nativeQuery = true)
    Verb findRandom();
}
