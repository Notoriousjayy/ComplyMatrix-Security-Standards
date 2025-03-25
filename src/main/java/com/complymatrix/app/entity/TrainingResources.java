package com.complymatrix.app.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "training_resources")  // Removed any schema attribute
@Data
public class TrainingResources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long resourceId;

    @Column(length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "standard_id")
    private Standards standard;

    @Lob
    private String description;

    @Column(length = 50)
    private String provider;

    @Column(length = 50)
    private String type;

    @Column(length = 255)
    private String url;
}
