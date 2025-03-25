package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "standards")
@Data
@ToString(exclude = {
    "controlRequirements",
    "complianceTrackingList",
    "trainingResources",
    "organizations",
    "relevantControls",
    "sourceMappings",
    "targetMappings"
})
public class Standards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "standard_id")
    private Long standardId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "category", length = 50)
    private String category;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "publisher", length = 50)
    private String publisher;

    @Column(name = "focus_area", length = 50)
    private String focusArea;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "standard_version", length = 20)
    private String standardVersion;

    @Column(name = "region", length = 50)
    private String region;

    @OneToMany(mappedBy = "standard", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ControlRequirements> controlRequirements;

    @OneToMany(mappedBy = "standard", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceTracking> complianceTrackingList;

    @OneToMany(mappedBy = "standard", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<TrainingResources> trainingResources;

    // Remove @ManyToMany on "OrganizationStandards":
    @OneToMany(mappedBy = "standard", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<OrganizationStandards> organizationStandards;

    @ManyToMany
    @JoinTable(
        name = "ControlRelevantStandards",
        joinColumns = @JoinColumn(name = "standardID"),
        inverseJoinColumns = @JoinColumn(name = "controlID")
    )
    private List<ControlRequirements> relevantControls;

    @OneToMany(mappedBy = "sourceStandard", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceMappings> sourceMappings;

    @OneToMany(mappedBy = "targetStandard", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ComplianceMappings> targetMappings;
}
