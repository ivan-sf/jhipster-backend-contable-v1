package com.ivansantander.web.rest;

import com.ivansantander.domain.Paquete;
import com.ivansantander.repository.PaqueteRepository;
import com.ivansantander.service.PaqueteService;
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
 * REST controller for managing {@link com.ivansantander.domain.Paquete}.
 */
@RestController
@RequestMapping("/api")
public class PaqueteResource {

    private final Logger log = LoggerFactory.getLogger(PaqueteResource.class);

    private static final String ENTITY_NAME = "paquete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaqueteService paqueteService;

    private final PaqueteRepository paqueteRepository;

    public PaqueteResource(PaqueteService paqueteService, PaqueteRepository paqueteRepository) {
        this.paqueteService = paqueteService;
        this.paqueteRepository = paqueteRepository;
    }

    /**
     * {@code POST  /paquetes} : Create a new paquete.
     *
     * @param paquete the paquete to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paquete, or with status {@code 400 (Bad Request)} if the paquete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paquetes")
    public ResponseEntity<Paquete> createPaquete(@RequestBody Paquete paquete) throws URISyntaxException {
        log.debug("REST request to save Paquete : {}", paquete);
        if (paquete.getId() != null) {
            throw new BadRequestAlertException("A new paquete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paquete result = paqueteService.save(paquete);
        return ResponseEntity
            .created(new URI("/api/paquetes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paquetes/:id} : Updates an existing paquete.
     *
     * @param id the id of the paquete to save.
     * @param paquete the paquete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paquete,
     * or with status {@code 400 (Bad Request)} if the paquete is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paquete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paquetes/{id}")
    public ResponseEntity<Paquete> updatePaquete(@PathVariable(value = "id", required = false) final Long id, @RequestBody Paquete paquete)
        throws URISyntaxException {
        log.debug("REST request to update Paquete : {}, {}", id, paquete);
        if (paquete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paquete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paqueteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Paquete result = paqueteService.save(paquete);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paquete.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paquetes/:id} : Partial updates given fields of an existing paquete, field will ignore if it is null
     *
     * @param id the id of the paquete to save.
     * @param paquete the paquete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paquete,
     * or with status {@code 400 (Bad Request)} if the paquete is not valid,
     * or with status {@code 404 (Not Found)} if the paquete is not found,
     * or with status {@code 500 (Internal Server Error)} if the paquete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paquetes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Paquete> partialUpdatePaquete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Paquete paquete
    ) throws URISyntaxException {
        log.debug("REST request to partial update Paquete partially : {}, {}", id, paquete);
        if (paquete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paquete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paqueteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Paquete> result = paqueteService.partialUpdate(paquete);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paquete.getId().toString())
        );
    }

    /**
     * {@code GET  /paquetes} : get all the paquetes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paquetes in body.
     */
    @GetMapping("/paquetes")
    public ResponseEntity<List<Paquete>> getAllPaquetes(Pageable pageable) {
        log.debug("REST request to get a page of Paquetes");
        Page<Paquete> page = paqueteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paquetes/:id} : get the "id" paquete.
     *
     * @param id the id of the paquete to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paquete, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paquetes/{id}")
    public ResponseEntity<Paquete> getPaquete(@PathVariable Long id) {
        log.debug("REST request to get Paquete : {}", id);
        Optional<Paquete> paquete = paqueteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paquete);
    }

    /**
     * {@code DELETE  /paquetes/:id} : delete the "id" paquete.
     *
     * @param id the id of the paquete to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paquetes/{id}")
    public ResponseEntity<Void> deletePaquete(@PathVariable Long id) {
        log.debug("REST request to delete Paquete : {}", id);
        paqueteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
