package com.ivansantander.web.rest;

import com.ivansantander.domain.Regimen;
import com.ivansantander.repository.RegimenRepository;
import com.ivansantander.service.RegimenService;
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
 * REST controller for managing {@link com.ivansantander.domain.Regimen}.
 */
@RestController
@RequestMapping("/api")
public class RegimenResource {

    private final Logger log = LoggerFactory.getLogger(RegimenResource.class);

    private static final String ENTITY_NAME = "regimen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegimenService regimenService;

    private final RegimenRepository regimenRepository;

    public RegimenResource(RegimenService regimenService, RegimenRepository regimenRepository) {
        this.regimenService = regimenService;
        this.regimenRepository = regimenRepository;
    }

    /**
     * {@code POST  /regimen} : Create a new regimen.
     *
     * @param regimen the regimen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regimen, or with status {@code 400 (Bad Request)} if the regimen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regimen")
    public ResponseEntity<Regimen> createRegimen(@RequestBody Regimen regimen) throws URISyntaxException {
        log.debug("REST request to save Regimen : {}", regimen);
        if (regimen.getId() != null) {
            throw new BadRequestAlertException("A new regimen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Regimen result = regimenService.save(regimen);
        return ResponseEntity
            .created(new URI("/api/regimen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /regimen/:id} : Updates an existing regimen.
     *
     * @param id the id of the regimen to save.
     * @param regimen the regimen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regimen,
     * or with status {@code 400 (Bad Request)} if the regimen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regimen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regimen/{id}")
    public ResponseEntity<Regimen> updateRegimen(@PathVariable(value = "id", required = false) final Long id, @RequestBody Regimen regimen)
        throws URISyntaxException {
        log.debug("REST request to update Regimen : {}, {}", id, regimen);
        if (regimen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regimen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regimenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Regimen result = regimenService.save(regimen);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regimen.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /regimen/:id} : Partial updates given fields of an existing regimen, field will ignore if it is null
     *
     * @param id the id of the regimen to save.
     * @param regimen the regimen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regimen,
     * or with status {@code 400 (Bad Request)} if the regimen is not valid,
     * or with status {@code 404 (Not Found)} if the regimen is not found,
     * or with status {@code 500 (Internal Server Error)} if the regimen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/regimen/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Regimen> partialUpdateRegimen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Regimen regimen
    ) throws URISyntaxException {
        log.debug("REST request to partial update Regimen partially : {}, {}", id, regimen);
        if (regimen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regimen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regimenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Regimen> result = regimenService.partialUpdate(regimen);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, regimen.getId().toString())
        );
    }

    /**
     * {@code GET  /regimen} : get all the regimen.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regimen in body.
     */
    @GetMapping("/regimen")
    public ResponseEntity<List<Regimen>> getAllRegimen(Pageable pageable) {
        log.debug("REST request to get a page of Regimen");
        Page<Regimen> page = regimenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /regimen/:id} : get the "id" regimen.
     *
     * @param id the id of the regimen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regimen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regimen/{id}")
    public ResponseEntity<Regimen> getRegimen(@PathVariable Long id) {
        log.debug("REST request to get Regimen : {}", id);
        Optional<Regimen> regimen = regimenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regimen);
    }

    /**
     * {@code DELETE  /regimen/:id} : delete the "id" regimen.
     *
     * @param id the id of the regimen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regimen/{id}")
    public ResponseEntity<Void> deleteRegimen(@PathVariable Long id) {
        log.debug("REST request to delete Regimen : {}", id);
        regimenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
