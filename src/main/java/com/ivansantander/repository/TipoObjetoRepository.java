package com.ivansantander.repository;

import com.ivansantander.domain.TipoObjeto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoObjeto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoObjetoRepository extends JpaRepository<TipoObjeto, Long> {}
