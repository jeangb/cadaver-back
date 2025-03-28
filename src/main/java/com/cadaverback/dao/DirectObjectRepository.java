package com.cadaverback.dao;

import com.cadaverback.model.DirectObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirectObjectRepository extends JpaRepository<DirectObject, Long> {
    List<DirectObject> findAllByUserId(final int userId);

    List<DirectObject> findAllByLibelle(final String libelle);

    boolean existsByLibelle(final String libelle);

    @Query(value = "select d.* from directobject d join phrase p on d.id=p.directobject_id where "
            + "(p.subject_id is not null and p.verb_id is not null and p.directobject_id is not null and p.circumstantialobject_id is not null)"
            + " and LTRIM(RTRIM(libelle)) != '' order by rand() limit 1", nativeQuery = true)
    DirectObject findRandom();
}
