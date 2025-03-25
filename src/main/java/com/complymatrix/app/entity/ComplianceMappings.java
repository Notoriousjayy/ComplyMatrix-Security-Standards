package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "compliance_mappings")  // Removed any schema attribute
@Data
public class ComplianceMappings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Long mappingId;

    @ManyToOne
    @JoinColumn(name = "source_standard_id")
    private Standards sourceStandard;

    @ManyToOne
    @JoinColumn(name = "source_control_id")
    private ControlRequirements sourceControl;

    @ManyToOne
    @JoinColumn(name = "target_standard_id")
    private Standards targetStandard;

    @ManyToOne
    @JoinColumn(name = "target_control_id")
    private ControlRequirements targetControl;

    @Lob
    @Column(name = "mapping_description")
    private String mappingDescription;
}
