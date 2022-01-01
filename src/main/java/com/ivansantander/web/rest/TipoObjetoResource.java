package com.ivansantander.web.rest;

import com.ivansantander.domain.TipoObjeto;
import com.ivansantander.repository.TipoObjetoRepository;
import com.ivansantander.service.TipoObjetoService;
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
 * REST controller for managing {@link com.ivansantander.domain.TipoObjeto}.
 */
@RestController
@RequestMapping("/api")
public class TipoObjetoResource {

    private final Logger log = LoggerFactory.getLogger(TipoObjetoResource.class);

    private static final String ENTITY_NAME = "tipoObjeto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoObjetoService tipoObjetoService;

    private final TipoObjetoRepository tipoObjetoRepository;

    public TipoObjetoResource(TipoObjetoService tipoObjetoService, TipoObjetoRepository tipoObjetoRepository) {
        this.tipoObjetoService = tipoObjetoService;
        this.tipoObjetoRepository = tipoObjetoRepository;
    }

    /**
     * {@code POST  /tipo-objetos} : Create a new tipoObjeto.
     *
     * @param tipoObjeto the tipoObjeto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoObjeto, or with status {@code 400 (Bad Request)} if the tipoObjeto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-objetos")
    public ResponseEntity<TipoObjeto> createTipoObjeto(@RequestBody TipoObjeto tipoObjeto) throws URISyntaxException {
        log.debug("REST request to save TipoObjeto : {}", tipoObjeto);
        if (tipoObjeto.getId() != null) {
            throw new BadRequestAlertException("A new tipoObjeto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoObjeto result = tipoObjetoService.save(tipoObjeto);
        return ResponseEntity
            .created(new URI("/api/tipo-objetos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-objetos/:id} : Updates an existing tipoObjeto.
     *
     * @param id the id of the tipoObjeto to save.
     * @param tipoObjeto the tipoObjeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoObjeto,
     * or with status {@code 400 (Bad Request)} if the tipoObjeto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoObjeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-objetos/{id}")
    public ResponseEntity<TipoObjeto> updateTipoObjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoObjeto tipoObjeto
    ) throws URISyntaxException {
        log.debug("REST request to update TipoObjeto : {}, {}", id, tipoObjeto);
        if (tipoObjeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoObjeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoObjetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoObjeto result = tipoObjetoService.save(tipoObjeto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoObjeto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-objetos/:id} : Partial updates given fields of an existing tipoObjeto, field will ignore if it is null
     *
     * @param id the id of the tipoObjeto to save.
     * @param tipoObjeto the tipoObjeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoObjeto,
     * or with status {@code 400 (Bad Request)} if the tipoObjeto is not valid,
     * or with status {@code 404 (Not Found)} if the tipoObjeto is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoObjeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-objetos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoObjeto> partialUpdateTipoObjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TipoObjeto tipoObjeto
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoObjeto partially : {}, {}", id, tipoObjeto);
        if (tipoObjeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoObjeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoObjetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoObjeto> result = tipoObjetoService.partialUpdate(tipoObjeto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoObjeto.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-objetos} : get all the tipoObjetos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoObjetos in body.
     */
    @GetMapping("/tipo-objetos")
    public ResponseEntity<List<TipoObjeto>> getAllTipoObjetos(Pageable pageable) {
        log.debug("REST request to get a page of TipoObjetos");
        Page<TipoObjeto> page = tipoObjetoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-objetos/:id} : get the "id" tipoObjeto.
     *
     * @param id the id of the tipoObjeto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoObjeto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-objetos/{id}")
    public ResponseEntity<TipoObjeto> getTipoObjeto(@PathVariable Long id) {
        log.debug("REST request to get TipoObjeto : {}", id);
        Optional<TipoObjeto> tipoObjeto = tipoObjetoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoObjeto);
    }

    /**
     * {@code DELETE  /tipo-objetos/:id} : delete the "id" tipoObjeto.
     *
     * @param id the id of the tipoObjeto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-objetos/{id}")
    public ResponseEntity<Void> deleteTipoObjeto(@PathVariable Long id) {
        log.debug("REST request to delete TipoObjeto : {}", id);
        tipoObjetoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
