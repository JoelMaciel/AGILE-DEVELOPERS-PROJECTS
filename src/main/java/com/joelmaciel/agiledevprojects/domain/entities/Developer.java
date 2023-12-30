package com.joelmaciel.agiledevprojects.domain.entities;

import com.joelmaciel.agiledevprojects.domain.enums.ExperienceLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Developer {


    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long developerId;

    private String name;
    private String phoneNumber;
    private Integer age;
    private String position;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    private Integer yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "developer")
    private List<Task> tasks;
}
