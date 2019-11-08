package com.sygma.school.web.rest;

import com.sygma.school.domain.Novel;
import com.sygma.school.service.NovelService;
import com.sygma.school.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sygma.school.domain.Novel}.
 */
@RestController
@RequestMapping("/api")
public class NovelResource {

    private final Logger log = LoggerFactory.getLogger(NovelResource.class);

    private static final String ENTITY_NAME = "novel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NovelService novelService;

    public NovelResource(NovelService novelService) {
        this.novelService = novelService;
    }

    /**
     * {@code POST  /novels} : Create a new novel.
     *
     * @param novel the novel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new novel, or with status {@code 400 (Bad Request)} if the novel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/novels")
    public ResponseEntity<Novel> createNovel(@RequestBody Novel novel) throws URISyntaxException {
        log.debug("REST request to save Novel : {}", novel);
        if (novel.getId() != null) {
            throw new BadRequestAlertException("A new novel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Novel result = novelService.save(novel);
        return ResponseEntity.created(new URI("/api/novels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /novels} : Updates an existing novel.
     *
     * @param novel the novel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated novel,
     * or with status {@code 400 (Bad Request)} if the novel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the novel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/novels")
    public ResponseEntity<Novel> updateNovel(@RequestBody Novel novel) throws URISyntaxException {
        log.debug("REST request to update Novel : {}", novel);
        if (novel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Novel result = novelService.save(novel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, novel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /novels} : get all the novels.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of novels in body.
     */
    @GetMapping("/novels")
    public ResponseEntity<List<Novel>> getAllNovels(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Novels");
        Page<Novel> page;
        if (eagerload) {
            page = novelService.findAllWithEagerRelationships(pageable);
        } else {
            page = novelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /novels/:id} : get the "id" novel.
     *
     * @param id the id of the novel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the novel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/novels/{id}")
    public ResponseEntity<Novel> getNovel(@PathVariable Long id) {
        log.debug("REST request to get Novel : {}", id);
        Optional<Novel> novel = novelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(novel);
    }

    /**
     * {@code DELETE  /novels/:id} : delete the "id" novel.
     *
     * @param id the id of the novel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/novels/{id}")
    public ResponseEntity<Void> deleteNovel(@PathVariable Long id) {
        log.debug("REST request to delete Novel : {}", id);
        novelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
