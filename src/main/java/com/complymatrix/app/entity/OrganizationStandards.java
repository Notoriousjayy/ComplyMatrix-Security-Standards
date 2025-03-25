package com.complymatrix.app.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Table(name = "organization_standards")
@Data
public class OrganizationStandards {

    @EmbeddedId
    private OrganizationStandardsId id;

    /**
     * Use @MapsId to tie the PK fields in the embedded ID to 
     * this entity's references. 
     */
    @ManyToOne
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id", nullable = false)
    private Organizations organization;

    @ManyToOne
    @MapsId("standardId")
    @JoinColumn(name = "standard_id", nullable = false)
    private Standards standard;

    // You can add additional fields here if your join table has extra columns
    // For example:
    // private LocalDate createdDate;
    // private String someMetadata;
    
    // Constructors if needed
    public OrganizationStandards() {
        // Empty constructor required by JPA
    }

    public OrganizationStandards(Organizations org, Standards std) {
        this.organization = org;
        this.standard = std;
        this.id = new OrganizationStandardsId();
        this.id.setOrganizationId(org.getOrganizationId());
        this.id.setStandardId(std.getStandardId());
    }

    // If you prefer, you can let Lombok handle getters/setters, but be careful
    // with keeping id in sync when you set organization/standard.
}
