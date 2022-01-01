package com.ivansantander.web.rest;

import com.ivansantander.domain.Vencimiento;
import com.ivansantander.repository.VencimientoRepository;
import com.ivansantander.service.VencimientoService;
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
 * REST controller for managing {@link com.ivansantander.domain.Vencimiento}.
 */
@RestController
@RequestMapping("/api")
public class VencimientoResource {

    private final Logger log = LoggerFactory.getLogger(VencimientoResource.class);

    private static final String ENTITY_NAME = "vencimiento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VencimientoService vencimientoService;

    private final VencimientoRepository vencimientoRepository;

    public VencimientoResource(VencimientoService vencimientoService, VencimientoRepository vencimientoRepository) {
        this.vencimientoService = vencimientoService;
        this.vencimientoRepository = vencimientoRepository;
    }

    /**
     * {@code POST  /vencimientos} : Create a new vencimiento.
     *
     * @param vencimiento the vencimiento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vencimiento, or with status {@code 400 (Bad Request)} if the vencimiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vencimientos")
    public ResponseEntity<Vencimiento> createVencimiento(@RequestBody Vencimiento vencimiento) throws URISyntaxException {
        log.debug("REST request to save Vencimiento : {}", vencimiento);
        if (vencimiento.getId() != null) {
            throw new BadRequestAlertException("A new vencimiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vencimiento result = vencimientoService.save(vencimiento);
        return ResponseEntity
            .created(new URI("/api/vencimientos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vencimientos/:id} : Updates an existing vencimiento.
     *
     * @param id the id of the vencimiento to save.
     * @param vencimiento the vencimiento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vencimiento,
     * or with status {@code 400 (Bad Request)} if the vencimiento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vencimiento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vencimientos/{id}")
    public ResponseEntity<Vencimiento> updateVencimiento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vencimiento vencimiento
    ) throws URISyntaxException {
        log.debug("REST request to update Vencimiento : {}, {}", id, vencimiento);
        if (vencimiento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vencimiento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vencimientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vencimiento result = vencimientoService.save(vencimiento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vencimiento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vencimientos/:id} : Partial updates given fields of an existing vencimiento, field will ignore if it is null
     *
     * @param id the id of the vencimiento to save.
     * @param vencimiento the vencimiento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vencimiento,
     * or with status {@code 400 (Bad Request)} if the vencimiento is not valid,
     * or with status {@code 404 (Not Found)} if the vencimiento is not found,
     * or with status {@code 500 (Internal Server Error)} if the vencimiento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vencimientos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vencimiento> partialUpdateVencimiento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vencimiento vencimiento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vencimiento partially : {}, {}", id, vencimiento);
        if (vencimiento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vencimiento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vencimientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vencimiento> result = vencimientoService.partialUpdate(vencimiento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vencimiento.getId().toString())
        );
    }

    /**
     * {@code GET  /vencimientos} : get all the vencimientos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vencimientos in body.
     */
    @GetMapping("/vencimientos")
    public ResponseEntity<List<Vencimiento>> getAllVencimientos(Pageable pageable) {
        log.debug("REST request to get a page of Vencimientos");
        Page<Vencimiento> page = vencimientoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vencimientos/:id} : get the "id" vencimiento.
     *
     * @param id the id of the vencimiento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vencimiento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vencimientos/{id}")
    public ResponseEntity<Vencimiento> getVencimiento(@PathVariable Long id) {
        log.debug("REST request to get Vencimiento : {}", id);
        Optional<Vencimiento> vencimiento = vencimientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vencimiento);
    }

    /**
     * {@code DELETE  /vencimientos/:id} : delete the "id" vencimiento.
     *
     * @param id the id of the vencimiento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vencimientos/{id}")
    public ResponseEntity<Void> deleteVencimiento(@PathVariable Long id) {
        log.debug("REST request to delete Vencimiento : {}", id);
        vencimientoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
