package com.joelmaciel.agiledevprojects.domain.repositories;

import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Page<Developer> findByNameContaining(Pageable pageable, String name);

    Page<Developer> findByPositionContaining(Pageable pageable, String position);

}
