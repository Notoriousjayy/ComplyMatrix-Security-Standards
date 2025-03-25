package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "control_relevant_standards")  // or whatever your table is called
public class ControlRelevantStandards {

    @EmbeddedId
    private ControlRelevantStandardsId id;

    // These @MapsId annotations tell JPA that each foreign key column in the 
    // embedded ID corresponds to the PK of the related entity. This ensures 
    // that JPA uses the same columns in the primary key and the foreign key relationship.

    @MapsId("controlId") // Maps this field to the 'controlId' property of the embedded ID
    @ManyToOne
    @JoinColumn(name = "control_id", nullable = false)
    private ControlRequirements control;

    @MapsId("standardId") // Maps this field to the 'standardId' property of the embedded ID
    @ManyToOne
    @JoinColumn(name = "standard_id", nullable = false)
    private Standards standard;

}
