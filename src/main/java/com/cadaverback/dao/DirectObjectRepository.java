package com.cadaverback.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cadaverback.model.DirectObject;
import com.sun.istack.NotNull;

public interface DirectObjectRepository extends JpaRepository<DirectObject, Long>
{
    List<DirectObject> findAllByUserId(@NotNull final int userId);

    List<DirectObject> findAllByLibelle(@NotNull final String libelle);

    boolean existsByLibelle(@NotNull final String libelle);

    @Query(value = "select * from directobject where LTRIM(RTRIM(libelle)) != '' order by rand() limit 1", nativeQuery = true)
    DirectObject findRandom();
}
