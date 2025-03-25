// src/main/java/com/complymatrix/app/repository/StandardsRepository.java
package com.complymatrix.app.repository;

import com.complymatrix.app.entity.Standards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardsRepository extends JpaRepository<Standards, Long> {
}
