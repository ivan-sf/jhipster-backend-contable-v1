package com.ivansantander.repository;

import com.ivansantander.domain.TipoFactura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoFactura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoFacturaRepository extends JpaRepository<TipoFactura, Long> {}
