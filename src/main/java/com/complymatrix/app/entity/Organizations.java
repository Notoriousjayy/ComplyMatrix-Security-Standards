package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "organizations")
@Data
public class Organizations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long organizationId;

    @Column(length = 100)
    private String name;

    @Column(length = 50)
    private String industry;

    @Column(length = 50)
    private String region;

    @Column(precision = 5, scale = 2)
    private BigDecimal complianceScore;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceTracking> complianceTrackingList;

    // Remove the @ManyToMany to the "OrganizationStandards" table
    // Instead, do a OneToMany to your association entity
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<OrganizationStandards> organizationStandards;

}
