package com.ivansantander.web.rest;

import com.ivansantander.domain.NotaContable;
import com.ivansantander.repository.NotaContableRepository;
import com.ivansantander.service.NotaContableService;
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
 * REST controller for managing {@link com.ivansantander.domain.NotaContable}.
 */
@RestController
@RequestMapping("/api")
public class NotaContableResource {

    private final Logger log = LoggerFactory.getLogger(NotaContableResource.class);

    private static final String ENTITY_NAME = "notaContable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotaContableService notaContableService;

    private final NotaContableRepository notaContableRepository;

    public NotaContableResource(NotaContableService notaContableService, NotaContableRepository notaContableRepository) {
        this.notaContableService = notaContableService;
        this.notaContableRepository = notaContableRepository;
    }

    /**
     * {@code POST  /nota-contables} : Create a new notaContable.
     *
     * @param notaContable the notaContable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notaContable, or with status {@code 400 (Bad Request)} if the notaContable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nota-contables")
    public ResponseEntity<NotaContable> createNotaContable(@RequestBody NotaContable notaContable) throws URISyntaxException {
        log.debug("REST request to save NotaContable : {}", notaContable);
        if (notaContable.getId() != null) {
            throw new BadRequestAlertException("A new notaContable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotaContable result = notaContableService.save(notaContable);
        return ResponseEntity
            .created(new URI("/api/nota-contables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nota-contables/:id} : Updates an existing notaContable.
     *
     * @param id the id of the notaContable to save.
     * @param notaContable the notaContable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaContable,
     * or with status {@code 400 (Bad Request)} if the notaContable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notaContable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nota-contables/{id}")
    public ResponseEntity<NotaContable> updateNotaContable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NotaContable notaContable
    ) throws URISyntaxException {
        log.debug("REST request to update NotaContable : {}, {}", id, notaContable);
        if (notaContable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notaContable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notaContableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotaContable result = notaContableService.save(notaContable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notaContable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nota-contables/:id} : Partial updates given fields of an existing notaContable, field will ignore if it is null
     *
     * @param id the id of the notaContable to save.
     * @param notaContable the notaContable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notaContable,
     * or with status {@code 400 (Bad Request)} if the notaContable is not valid,
     * or with status {@code 404 (Not Found)} if the notaContable is not found,
     * or with status {@code 500 (Internal Server Error)} if the notaContable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nota-contables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotaContable> partialUpdateNotaContable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NotaContable notaContable
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotaContable partially : {}, {}", id, notaContable);
        if (notaContable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notaContable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notaContableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotaContable> result = notaContableService.partialUpdate(notaContable);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notaContable.getId().toString())
        );
    }

    /**
     * {@code GET  /nota-contables} : get all the notaContables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notaContables in body.
     */
    @GetMapping("/nota-contables")
    public ResponseEntity<List<NotaContable>> getAllNotaContables(Pageable pageable) {
        log.debug("REST request to get a page of NotaContables");
        Page<NotaContable> page = notaContableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nota-contables/:id} : get the "id" notaContable.
     *
     * @param id the id of the notaContable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notaContable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nota-contables/{id}")
    public ResponseEntity<NotaContable> getNotaContable(@PathVariable Long id) {
        log.debug("REST request to get NotaContable : {}", id);
        Optional<NotaContable> notaContable = notaContableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notaContable);
    }

    /**
     * {@code DELETE  /nota-contables/:id} : delete the "id" notaContable.
     *
     * @param id the id of the notaContable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nota-contables/{id}")
    public ResponseEntity<Void> deleteNotaContable(@PathVariable Long id) {
        log.debug("REST request to delete NotaContable : {}", id);
        notaContableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
