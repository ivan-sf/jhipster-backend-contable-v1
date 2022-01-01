package com.ivansantander.web.rest;

import com.ivansantander.domain.DetalleFactura;
import com.ivansantander.repository.DetalleFacturaRepository;
import com.ivansantander.service.DetalleFacturaService;
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
 * REST controller for managing {@link com.ivansantander.domain.DetalleFactura}.
 */
@RestController
@RequestMapping("/api")
public class DetalleFacturaResource {

    private final Logger log = LoggerFactory.getLogger(DetalleFacturaResource.class);

    private static final String ENTITY_NAME = "detalleFactura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetalleFacturaService detalleFacturaService;

    private final DetalleFacturaRepository detalleFacturaRepository;

    public DetalleFacturaResource(DetalleFacturaService detalleFacturaService, DetalleFacturaRepository detalleFacturaRepository) {
        this.detalleFacturaService = detalleFacturaService;
        this.detalleFacturaRepository = detalleFacturaRepository;
    }

    /**
     * {@code POST  /detalle-facturas} : Create a new detalleFactura.
     *
     * @param detalleFactura the detalleFactura to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detalleFactura, or with status {@code 400 (Bad Request)} if the detalleFactura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detalle-facturas")
    public ResponseEntity<DetalleFactura> createDetalleFactura(@RequestBody DetalleFactura detalleFactura) throws URISyntaxException {
        log.debug("REST request to save DetalleFactura : {}", detalleFactura);
        if (detalleFactura.getId() != null) {
            throw new BadRequestAlertException("A new detalleFactura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetalleFactura result = detalleFacturaService.save(detalleFactura);
        return ResponseEntity
            .created(new URI("/api/detalle-facturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detalle-facturas/:id} : Updates an existing detalleFactura.
     *
     * @param id the id of the detalleFactura to save.
     * @param detalleFactura the detalleFactura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleFactura,
     * or with status {@code 400 (Bad Request)} if the detalleFactura is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detalleFactura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detalle-facturas/{id}")
    public ResponseEntity<DetalleFactura> updateDetalleFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetalleFactura detalleFactura
    ) throws URISyntaxException {
        log.debug("REST request to update DetalleFactura : {}, {}", id, detalleFactura);
        if (detalleFactura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleFactura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleFacturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetalleFactura result = detalleFacturaService.save(detalleFactura);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleFactura.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detalle-facturas/:id} : Partial updates given fields of an existing detalleFactura, field will ignore if it is null
     *
     * @param id the id of the detalleFactura to save.
     * @param detalleFactura the detalleFactura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detalleFactura,
     * or with status {@code 400 (Bad Request)} if the detalleFactura is not valid,
     * or with status {@code 404 (Not Found)} if the detalleFactura is not found,
     * or with status {@code 500 (Internal Server Error)} if the detalleFactura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detalle-facturas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetalleFactura> partialUpdateDetalleFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetalleFactura detalleFactura
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetalleFactura partially : {}, {}", id, detalleFactura);
        if (detalleFactura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detalleFactura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detalleFacturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetalleFactura> result = detalleFacturaService.partialUpdate(detalleFactura);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detalleFactura.getId().toString())
        );
    }

    /**
     * {@code GET  /detalle-facturas} : get all the detalleFacturas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detalleFacturas in body.
     */
    @GetMapping("/detalle-facturas")
    public ResponseEntity<List<DetalleFactura>> getAllDetalleFacturas(Pageable pageable) {
        log.debug("REST request to get a page of DetalleFacturas");
        Page<DetalleFactura> page = detalleFacturaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detalle-facturas/:id} : get the "id" detalleFactura.
     *
     * @param id the id of the detalleFactura to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detalleFactura, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detalle-facturas/{id}")
    public ResponseEntity<DetalleFactura> getDetalleFactura(@PathVariable Long id) {
        log.debug("REST request to get DetalleFactura : {}", id);
        Optional<DetalleFactura> detalleFactura = detalleFacturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detalleFactura);
    }

    /**
     * {@code DELETE  /detalle-facturas/:id} : delete the "id" detalleFactura.
     *
     * @param id the id of the detalleFactura to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detalle-facturas/{id}")
    public ResponseEntity<Void> deleteDetalleFactura(@PathVariable Long id) {
        log.debug("REST request to delete DetalleFactura : {}", id);
        detalleFacturaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
