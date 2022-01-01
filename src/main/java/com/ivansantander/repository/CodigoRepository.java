package com.ivansantander.repository;

import com.ivansantander.domain.Codigo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Codigo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodigoRepository extends JpaRepository<Codigo, Long> {}
