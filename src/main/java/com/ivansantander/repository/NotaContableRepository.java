package com.ivansantander.repository;

import com.ivansantander.domain.NotaContable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NotaContable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotaContableRepository extends JpaRepository<NotaContable, Long> {}
