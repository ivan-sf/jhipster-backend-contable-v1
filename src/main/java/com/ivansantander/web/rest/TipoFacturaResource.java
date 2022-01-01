package com.ivansantander.web.rest;

import com.ivansantander.domain.TipoFactura;
import com.ivansantander.repository.TipoFacturaRepository;
import com.ivansantander.service.TipoFacturaService;
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
 * REST controller for managing {@link com.ivansantander.domain.TipoFactura}.
 */
@RestController
@RequestMapping("/api")
public class TipoFacturaResource {

    private final Logger log = LoggerFactory.getLogger(TipoFacturaResource.class);

    private static final String ENTITY_NAME = "tipoFactura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoFacturaService tipoFacturaService;

    private final TipoFacturaRepository tipoFacturaRepository;

    public TipoFacturaResource(TipoFacturaService tipoFacturaService, TipoFacturaRepository tipoFacturaRepository) {
        this.tipoFacturaService = tipoFacturaService;
        this.tipoFacturaRepository = tipoFacturaRepository;
    }

    /**
     * {@code POST  /tipo-facturas} : Create a new tipoFactura.
     *
     * @param tipoFactura the tipoFactura to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoFactura, or with status {@code 400 (Bad Request)} if the tipoFactura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-facturas")
    public ResponseEntity<TipoFactura> createTipoFactura(@RequestBody TipoFactura tipoFactura) throws URISyntaxException {
        log.debug("REST request to save TipoFactura : {}", tipoFactura);
        if (tipoFactura.getId() != null) {
            throw new BadRequestAlertException("A new tipoFactura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoFactura result = tipoFacturaService.save(tipoFactura);
        return ResponseEntity
            .created(new URI("/api/tipo-facturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-facturas/:id} : Updates an existing tipoFactura.
     *
     * @param id the id of the tipoFactura to save.
     * @param tipoFactura the tipoFactura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoFactura,
     * or with status {@code 400 (Bad Request)} if the tipoFactura is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoFactura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-facturas/{id}")
    public ResponseEntity<TipoFactura> updateTipoFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoFactura tipoFactura
    ) throws URISyntaxException {
        log.debug("REST request to update TipoFactura : {}, {}", id, tipoFactura);
        if (tipoFactura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoFactura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoFacturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoFactura result = tipoFacturaService.save(tipoFactura);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoFactura.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-facturas/:id} : Partial updates given fields of an existing tipoFactura, field will ignore if it is null
     *
     * @param id the id of the tipoFactura to save.
     * @param tipoFactura the tipoFactura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoFactura,
     * or with status {@code 400 (Bad Request)} if the tipoFactura is not valid,
     * or with status {@code 404 (Not Found)} if the tipoFactura is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoFactura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-facturas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoFactura> partialUpdateTipoFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoFactura tipoFactura
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoFactura partially : {}, {}", id, tipoFactura);
        if (tipoFactura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoFactura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoFacturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoFactura> result = tipoFacturaService.partialUpdate(tipoFactura);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoFactura.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-facturas} : get all the tipoFacturas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoFacturas in body.
     */
    @GetMapping("/tipo-facturas")
    public ResponseEntity<List<TipoFactura>> getAllTipoFacturas(Pageable pageable) {
        log.debug("REST request to get a page of TipoFacturas");
        Page<TipoFactura> page = tipoFacturaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-facturas/:id} : get the "id" tipoFactura.
     *
     * @param id the id of the tipoFactura to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoFactura, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-facturas/{id}")
    public ResponseEntity<TipoFactura> getTipoFactura(@PathVariable Long id) {
        log.debug("REST request to get TipoFactura : {}", id);
        Optional<TipoFactura> tipoFactura = tipoFacturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoFactura);
    }

    /**
     * {@code DELETE  /tipo-facturas/:id} : delete the "id" tipoFactura.
     *
     * @param id the id of the tipoFactura to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-facturas/{id}")
    public ResponseEntity<Void> deleteTipoFactura(@PathVariable Long id) {
        log.debug("REST request to delete TipoFactura : {}", id);
        tipoFacturaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
