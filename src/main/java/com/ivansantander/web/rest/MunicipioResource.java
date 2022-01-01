package com.ivansantander.web.rest;

import com.ivansantander.domain.Municipio;
import com.ivansantander.repository.MunicipioRepository;
import com.ivansantander.service.MunicipioService;
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
 * REST controller for managing {@link com.ivansantander.domain.Municipio}.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

    private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);

    private static final String ENTITY_NAME = "municipio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MunicipioService municipioService;

    private final MunicipioRepository municipioRepository;

    public MunicipioResource(MunicipioService municipioService, MunicipioRepository municipioRepository) {
        this.municipioService = municipioService;
        this.municipioRepository = municipioRepository;
    }

    /**
     * {@code POST  /municipios} : Create a new municipio.
     *
     * @param municipio the municipio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new municipio, or with status {@code 400 (Bad Request)} if the municipio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/municipios")
    public ResponseEntity<Municipio> createMunicipio(@RequestBody Municipio municipio) throws URISyntaxException {
        log.debug("REST request to save Municipio : {}", municipio);
        if (municipio.getId() != null) {
            throw new BadRequestAlertException("A new municipio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Municipio result = municipioService.save(municipio);
        return ResponseEntity
            .created(new URI("/api/municipios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /municipios/:id} : Updates an existing municipio.
     *
     * @param id the id of the municipio to save.
     * @param municipio the municipio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipio,
     * or with status {@code 400 (Bad Request)} if the municipio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the municipio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/municipios/{id}")
    public ResponseEntity<Municipio> updateMunicipio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Municipio municipio
    ) throws URISyntaxException {
        log.debug("REST request to update Municipio : {}, {}", id, municipio);
        if (municipio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Municipio result = municipioService.save(municipio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /municipios/:id} : Partial updates given fields of an existing municipio, field will ignore if it is null
     *
     * @param id the id of the municipio to save.
     * @param municipio the municipio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated municipio,
     * or with status {@code 400 (Bad Request)} if the municipio is not valid,
     * or with status {@code 404 (Not Found)} if the municipio is not found,
     * or with status {@code 500 (Internal Server Error)} if the municipio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/municipios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Municipio> partialUpdateMunicipio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Municipio municipio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Municipio partially : {}, {}", id, municipio);
        if (municipio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, municipio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!municipioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Municipio> result = municipioService.partialUpdate(municipio);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, municipio.getId().toString())
        );
    }

    /**
     * {@code GET  /municipios} : get all the municipios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of municipios in body.
     */
    @GetMapping("/municipios")
    public ResponseEntity<List<Municipio>> getAllMunicipios(Pageable pageable) {
        log.debug("REST request to get a page of Municipios");
        Page<Municipio> page = municipioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /municipios/:id} : get the "id" municipio.
     *
     * @param id the id of the municipio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the municipio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/municipios/{id}")
    public ResponseEntity<Municipio> getMunicipio(@PathVariable Long id) {
        log.debug("REST request to get Municipio : {}", id);
        Optional<Municipio> municipio = municipioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(municipio);
    }

    /**
     * {@code DELETE  /municipios/:id} : delete the "id" municipio.
     *
     * @param id the id of the municipio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/municipios/{id}")
    public ResponseEntity<Void> deleteMunicipio(@PathVariable Long id) {
        log.debug("REST request to delete Municipio : {}", id);
        municipioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
