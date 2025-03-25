package com.complymatrix.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class ControlRelevantStandardsId implements Serializable {

    // It's often recommended to add a private static final long serialVersionUID
    // to every Serializable class. Lombok @Data will generate getters/setters,
    // equals, and hashCode for these fields by default.
    private static final long serialVersionUID = 1L;

    @Column(name = "control_id", nullable = false)
    private Long controlId;

    @Column(name = "standard_id", nullable = false)
    private Long standardId;

    // If you prefer explicit equals() and hashCode(), you can override them
    // manually (recommended for primary key classes). Otherwise, Lombok's
    // generated ones should suffice, as long as you trust the default
    // behavior of @Data.
}
