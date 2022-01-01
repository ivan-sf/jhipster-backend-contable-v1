package com.ivansantander.repository;

import com.ivansantander.domain.Objeto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Objeto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjetoRepository extends JpaRepository<Objeto, Long> {}
