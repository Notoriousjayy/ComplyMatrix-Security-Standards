package com.complymatrix.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class OrganizationStandardsId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "standard_id")
    private Long standardId;

        // Default constructor
        public OrganizationStandardsId() {}

        // Optional convenience constructor
        public OrganizationStandardsId(Long organizationId, Long standardId) {
            this.organizationId = organizationId;
            this.standardId = standardId;
        }

}
