package com.ivansantander.web.rest;

import com.ivansantander.domain.Objeto;
import com.ivansantander.repository.ObjetoRepository;
import com.ivansantander.service.ObjetoService;
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
 * REST controller for managing {@link com.ivansantander.domain.Objeto}.
 */
@RestController
@RequestMapping("/api")
public class ObjetoResource {

    private final Logger log = LoggerFactory.getLogger(ObjetoResource.class);

    private static final String ENTITY_NAME = "objeto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjetoService objetoService;

    private final ObjetoRepository objetoRepository;

    public ObjetoResource(ObjetoService objetoService, ObjetoRepository objetoRepository) {
        this.objetoService = objetoService;
        this.objetoRepository = objetoRepository;
    }

    /**
     * {@code POST  /objetos} : Create a new objeto.
     *
     * @param objeto the objeto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objeto, or with status {@code 400 (Bad Request)} if the objeto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/objetos")
    public ResponseEntity<Objeto> createObjeto(@RequestBody Objeto objeto) throws URISyntaxException {
        log.debug("REST request to save Objeto : {}", objeto);
        if (objeto.getId() != null) {
            throw new BadRequestAlertException("A new objeto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Objeto result = objetoService.save(objeto);
        return ResponseEntity
            .created(new URI("/api/objetos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /objetos/:id} : Updates an existing objeto.
     *
     * @param id the id of the objeto to save.
     * @param objeto the objeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objeto,
     * or with status {@code 400 (Bad Request)} if the objeto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/objetos/{id}")
    public ResponseEntity<Objeto> updateObjeto(@PathVariable(value = "id", required = false) final Long id, @RequestBody Objeto objeto)
        throws URISyntaxException {
        log.debug("REST request to update Objeto : {}, {}", id, objeto);
        if (objeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Objeto result = objetoService.save(objeto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, objeto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /objetos/:id} : Partial updates given fields of an existing objeto, field will ignore if it is null
     *
     * @param id the id of the objeto to save.
     * @param objeto the objeto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objeto,
     * or with status {@code 400 (Bad Request)} if the objeto is not valid,
     * or with status {@code 404 (Not Found)} if the objeto is not found,
     * or with status {@code 500 (Internal Server Error)} if the objeto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/objetos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Objeto> partialUpdateObjeto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Objeto objeto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Objeto partially : {}, {}", id, objeto);
        if (objeto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objeto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!objetoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Objeto> result = objetoService.partialUpdate(objeto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, objeto.getId().toString())
        );
    }

    /**
     * {@code GET  /objetos} : get all the objetos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objetos in body.
     */
    @GetMapping("/objetos")
    public ResponseEntity<List<Objeto>> getAllObjetos(Pageable pageable) {
        log.debug("REST request to get a page of Objetos");
        Page<Objeto> page = objetoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /objetos/:id} : get the "id" objeto.
     *
     * @param id the id of the objeto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objeto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/objetos/{id}")
    public ResponseEntity<Objeto> getObjeto(@PathVariable Long id) {
        log.debug("REST request to get Objeto : {}", id);
        Optional<Objeto> objeto = objetoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(objeto);
    }

    /**
     * {@code DELETE  /objetos/:id} : delete the "id" objeto.
     *
     * @param id the id of the objeto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/objetos/{id}")
    public ResponseEntity<Void> deleteObjeto(@PathVariable Long id) {
        log.debug("REST request to delete Objeto : {}", id);
        objetoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
