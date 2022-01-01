package com.ivansantander.repository;

import com.ivansantander.domain.DetallesObjeto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetallesObjeto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetallesObjetoRepository extends JpaRepository<DetallesObjeto, Long> {}
