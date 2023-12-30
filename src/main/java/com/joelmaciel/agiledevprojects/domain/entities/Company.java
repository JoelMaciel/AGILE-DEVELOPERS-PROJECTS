package com.joelmaciel.agiledevprojects.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long companyId;

    private String name;

    @OneToMany(mappedBy = "company")
    private List<Developer> developers;

    @OneToMany(mappedBy = "company")
    private List<Project> projects;

    @CreationTimestamp
    private OffsetDateTime creationDate;

}
