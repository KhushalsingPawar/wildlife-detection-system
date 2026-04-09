package com.example.wildlife.repository;

import com.example.wildlife.model.AuthorisedAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface AuthorisedAnimalRepository extends JpaRepository<AuthorisedAnimal, Long> {

    boolean existsByName(String name);

    // Case-insensitive delete
    @Modifying
    @Transactional
    @Query("DELETE FROM AuthorisedAnimal a WHERE LOWER(a.name) = LOWER(:name)")
    void deleteByNameIgnoreCase(@Param("name") String name);
}