package com.ivansantander.repository;

import com.ivansantander.domain.Paquete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Paquete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Long> {}
