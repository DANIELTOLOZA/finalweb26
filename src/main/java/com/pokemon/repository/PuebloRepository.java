package com.pokemon.repository;

import com.pokemon.model.Pueblo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PuebloRepository extends JpaRepository<Pueblo, Integer> {
    Optional<Pueblo> findByUuid(String uuid);
}
