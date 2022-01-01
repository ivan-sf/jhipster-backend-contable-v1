package com.ivansantander.repository;

import com.ivansantander.domain.Empresa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Empresa entity.
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    @Query(
        value = "select distinct empresa from Empresa empresa left join fetch empresa.usuarios",
        countQuery = "select count(distinct empresa) from Empresa empresa"
    )
    Page<Empresa> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct empresa from Empresa empresa left join fetch empresa.usuarios")
    List<Empresa> findAllWithEagerRelationships();

    @Query("select empresa from Empresa empresa left join fetch empresa.usuarios where empresa.id =:id")
    Optional<Empresa> findOneWithEagerRelationships(@Param("id") Long id);
}
