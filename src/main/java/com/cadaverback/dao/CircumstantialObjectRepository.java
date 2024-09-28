package com.cadaverback.dao;

import com.cadaverback.model.CircumstantialObject;
import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CircumstantialObjectRepository extends JpaRepository<CircumstantialObject, Long> {
    List<CircumstantialObject> findAllByUserId(@NotNull final int userId);

    List<CircumstantialObject> findAllByLibelle(@NotNull final String libelle);

    boolean existsByLibelle(@NotNull final String libelle);

    @Query(value = "select c.* from circumstantialobject c join phrase p on c.id=p.circumstantialobject_id where "
            + "(p.subject_id is not null and p.verb_id is not null and p.directobject_id is not null and p.circumstantialobject_id is not null)"
            + " and LTRIM(RTRIM(libelle)) != '' order by rand() limit 1", nativeQuery = true)
    CircumstantialObject findRandom();
}
