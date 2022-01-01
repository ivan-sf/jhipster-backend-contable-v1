package com.ivansantander.web.rest;

import com.ivansantander.domain.Codigo;
import com.ivansantander.repository.CodigoRepository;
import com.ivansantander.service.CodigoService;
import com.ivansantander.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ivansantander.domain.Codigo}.
 */
@RestController
@RequestMapping("/api")
public class CodigoResource {

    private final Logger log = LoggerFactory.getLogger(CodigoResource.class);

    private static final String ENTITY_NAME = "codigo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodigoService codigoService;

    private final CodigoRepository codigoRepository;

    public CodigoResource(CodigoService codigoService, CodigoRepository codigoRepository) {
        this.codigoService = codigoService;
        this.codigoRepository = codigoRepository;
    }

    /**
     * {@code POST  /codigos} : Create a new codigo.
     *
     * @param codigo the codigo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codigo, or with status {@code 400 (Bad Request)} if the codigo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/codigos")
    public ResponseEntity<Codigo> createCodigo(@RequestBody Codigo codigo) throws URISyntaxException {
        log.debug("REST request to save Codigo : {}", codigo);
        if (codigo.getId() != null) {
            throw new BadRequestAlertException("A new codigo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Codigo result = codigoService.save(codigo);
        return ResponseEntity
            .created(new URI("/api/codigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /codigos/:id} : Updates an existing codigo.
     *
     * @param id the id of the codigo to save.
     * @param codigo the codigo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codigo,
     * or with status {@code 400 (Bad Request)} if the codigo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codigo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/codigos/{id}")
    public ResponseEntity<Codigo> updateCodigo(@PathVariable(value = "id", required = false) final Long id, @RequestBody Codigo codigo)
        throws URISyntaxException {
        log.debug("REST request to update Codigo : {}, {}", id, codigo);
        if (codigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codigo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codigoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Codigo result = codigoService.save(codigo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codigo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /codigos/:id} : Partial updates given fields of an existing codigo, field will ignore if it is null
     *
     * @param id the id of the codigo to save.
     * @param codigo the codigo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codigo,
     * or with status {@code 400 (Bad Request)} if the codigo is not valid,
     * or with status {@code 404 (Not Found)} if the codigo is not found,
     * or with status {@code 500 (Internal Server Error)} if the codigo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/codigos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Codigo> partialUpdateCodigo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Codigo codigo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Codigo partially : {}, {}", id, codigo);
        if (codigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codigo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codigoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Codigo> result = codigoService.partialUpdate(codigo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codigo.getId().toString())
        );
    }

    /**
     * {@code GET  /codigos} : get all the codigos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codigos in body.
     */
    @GetMapping("/codigos")
    public ResponseEntity<List<Codigo>> getAllCodigos(Pageable pageable) {
        log.debug("REST request to get a page of Codigos");
        Page<Codigo> page = codigoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /codigos/:id} : get the "id" codigo.
     *
     * @param id the id of the codigo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codigo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/codigos/{id}")
    public ResponseEntity<Codigo> getCodigo(@PathVariable Long id) {
        log.debug("REST request to get Codigo : {}", id);
        Optional<Codigo> codigo = codigoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codigo);
    }

    /**
     * {@code DELETE  /codigos/:id} : delete the "id" codigo.
     *
     * @param id the id of the codigo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/codigos/{id}")
    public ResponseEntity<Void> deleteCodigo(@PathVariable Long id) {
        log.debug("REST request to delete Codigo : {}", id);
        codigoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
