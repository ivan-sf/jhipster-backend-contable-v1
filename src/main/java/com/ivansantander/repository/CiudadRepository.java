package com.ivansantander.repository;

import com.ivansantander.domain.Ciudad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ciudad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {}
