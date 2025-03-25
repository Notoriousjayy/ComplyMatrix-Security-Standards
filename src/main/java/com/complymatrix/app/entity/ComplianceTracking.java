package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "compliance_tracking")  // Removed any schema attribute
@Data
public class ComplianceTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_id")
    private Long trackingId;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organizations organization;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standards standard;

    @ManyToOne
    @JoinColumn(name = "control_id")
    private ControlRequirements control;

    @Column(name = "compliance_status", length = 20)
    private String complianceStatus;

    @Column(name = "audit_date")
    private LocalDate auditDate;

    @Column(name = "evidence_url", length = 255)
    private String evidenceUrl;

    @Column(name = "risk_impact", length = 10)
    private String riskImpact;
}
