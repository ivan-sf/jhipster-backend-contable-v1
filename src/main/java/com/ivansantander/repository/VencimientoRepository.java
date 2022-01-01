package com.ivansantander.repository;

import com.ivansantander.domain.Vencimiento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vencimiento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VencimientoRepository extends JpaRepository<Vencimiento, Long> {}
