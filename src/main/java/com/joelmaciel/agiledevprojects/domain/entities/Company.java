package com.joelmaciel.agiledevprojects.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
