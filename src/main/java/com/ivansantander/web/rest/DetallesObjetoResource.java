package com.ivansantander.web.rest;

import com.ivansantander.domain.DetallesObjeto;
import com.ivansantander.repository.DetallesObjetoRepository;
import com.ivansantander.service.DetallesObjetoService;
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
 * REST controller for managing {@link com.ivansantander.domain.DetallesObjeto}.
 */
@RestController
@RequestMapping("/api")
public class DetallesObjetoResource {

    private final Logger log = LoggerFactory.getLogger(DetallesObjetoResource.class);

    private static final String ENTITY_NAME = "detallesObjeto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetallesObjetoService detallesObjetoService;

    private final DetallesObjetoRepository detallesObjetoRepository;

    public DetallesObjetoResource(DetallesObjetoService detallesObjetoService, DetallesObjetoRepository detallesObjetoRepository) {
        this.detallesObjetoService = detallesObjetoService;
        this.detallesObjetoRepository = detallesObjetoRepository;
    }

    /**
     * {@code POST  /detalles-objetos} : Create a new detallesObjeto.
     *
     * @param detallesObjeto the detallesObjeto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detallesObjeto, or with status {@code 400 (Bad Request)} if the detallesObjeto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detalles-objetos")
    public ResponseEntity<DetallesObjeto> createDetallesObjeto(@RequestBody DetallesObjeto detallesObjeto) throws URISyntaxException {
        log.debug("REST request to save DetallesObjeto : {}", detallesObjeto);
        if (detallesObjeto.getId() != null) {
            throw new BadRequestAlertException("A new detallesObjeto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetallesObjeto result = detallesObjetoService.save(detallesObjeto);
        return ResponseEntity
            .created(new URI("/api/detalles-objetos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detalles-objetos/:id} : Updates an existing detallesObjeto.
     *
     * @param id the id of the detallesObjeto to save.
     * @param detallesObjeto the detallesObjeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detallesObjeto,
     * or with status {@code 400 (Bad Request)} if the detallesObjeto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detallesObjeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detalles-objetos/{id}")
    public ResponseEntity<DetallesObjeto> updateDetallesObjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetallesObjeto detallesObjeto
    ) throws URISyntaxException {
        log.debug("REST request to update DetallesObjeto : {}, {}", id, detallesObjeto);
        if (detallesObjeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detallesObjeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detallesObjetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetallesObjeto result = detallesObjetoService.save(detallesObjeto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detallesObjeto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detalles-objetos/:id} : Partial updates given fields of an existing detallesObjeto, field will ignore if it is null
     *
     * @param id the id of the detallesObjeto to save.
     * @param detallesObjeto the detallesObjeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detallesObjeto,
     * or with status {@code 400 (Bad Request)} if the detallesObjeto is not valid,
     * or with status {@code 404 (Not Found)} if the detallesObjeto is not found,
     * or with status {@code 500 (Internal Server Error)} if the detallesObjeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detalles-objetos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetallesObjeto> partialUpdateDetallesObjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetallesObjeto detallesObjeto
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetallesObjeto partially : {}, {}", id, detallesObjeto);
        if (detallesObjeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detallesObjeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detallesObjetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetallesObjeto> result = detallesObjetoService.partialUpdate(detallesObjeto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detallesObjeto.getId().toString())
        );
    }

    /**
     * {@code GET  /detalles-objetos} : get all the detallesObjetos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detallesObjetos in body.
     */
    @GetMapping("/detalles-objetos")
    public ResponseEntity<List<DetallesObjeto>> getAllDetallesObjetos(Pageable pageable) {
        log.debug("REST request to get a page of DetallesObjetos");
        Page<DetallesObjeto> page = detallesObjetoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detalles-objetos/:id} : get the "id" detallesObjeto.
     *
     * @param id the id of the detallesObjeto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detallesObjeto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detalles-objetos/{id}")
    public ResponseEntity<DetallesObjeto> getDetallesObjeto(@PathVariable Long id) {
        log.debug("REST request to get DetallesObjeto : {}", id);
        Optional<DetallesObjeto> detallesObjeto = detallesObjetoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detallesObjeto);
    }

    /**
     * {@code DELETE  /detalles-objetos/:id} : delete the "id" detallesObjeto.
     *
     * @param id the id of the detallesObjeto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detalles-objetos/{id}")
    public ResponseEntity<Void> deleteDetallesObjeto(@PathVariable Long id) {
        log.debug("REST request to delete DetallesObjeto : {}", id);
        detallesObjetoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
