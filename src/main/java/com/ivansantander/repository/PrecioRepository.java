package com.ivansantander.repository;

import com.ivansantander.domain.Precio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Precio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {}
