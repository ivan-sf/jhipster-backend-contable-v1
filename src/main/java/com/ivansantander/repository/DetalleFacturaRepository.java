package com.ivansantander.repository;

import com.ivansantander.domain.DetalleFactura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetalleFactura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {}
