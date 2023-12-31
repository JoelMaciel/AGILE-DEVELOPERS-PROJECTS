package com.joelmaciel.agiledevprojects.domain.repositories;

import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
