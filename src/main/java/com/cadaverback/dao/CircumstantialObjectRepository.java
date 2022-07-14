package com.cadaverback.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cadaverback.model.CircumstantialObject;
import com.sun.istack.NotNull;

public interface CircumstantialObjectRepository extends JpaRepository<CircumstantialObject, Long>
{
    List<CircumstantialObject> findAllByUserId(@NotNull final int userId);

    List<CircumstantialObject> findAllByLibelle(@NotNull final String libelle);

    boolean existsByLibelle(@NotNull final String libelle);

    @Query(value = "select * from circumstantialObject where LTRIM(RTRIM(libelle)) != '' order by rand() limit 1", nativeQuery = true)
    CircumstantialObject findRandom();
}
