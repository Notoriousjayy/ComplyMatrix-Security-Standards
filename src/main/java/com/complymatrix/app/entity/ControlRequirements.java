package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "control_requirements")  // Removed any schema attribute
@Data
public class ControlRequirements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "control_id")
    private Long controlId;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standards standard;

    @Column(length = 100)
    private String name;

    @Lob
    private String description;

    @Column(length = 50)
    private String category;

    private Boolean isMandatory;

    @Column(length = 10)
    private String riskLevel;

    @Column(length = 20)
    private String complianceType;

    @Column(length = 50)
    private String applicableIndustry;

    @OneToMany(mappedBy = "sourceControl", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceMappings> sourceMappings;

    @OneToMany(mappedBy = "targetControl", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceMappings> targetMappings;

    @OneToMany(mappedBy = "control", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceTracking> complianceTrackingList;

    @ManyToMany
    @JoinTable(
        name = "ControlRelevantStandards",
        joinColumns = @JoinColumn(name = "controlID"),
        inverseJoinColumns = @JoinColumn(name = "standard_id")
    )
    private List<Standards> relevantStandards;
}
