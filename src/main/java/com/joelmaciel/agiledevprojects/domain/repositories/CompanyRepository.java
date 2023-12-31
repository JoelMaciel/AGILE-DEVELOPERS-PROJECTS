package com.joelmaciel.agiledevprojects.domain.repositories;

import com.joelmaciel.agiledevprojects.domain.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
