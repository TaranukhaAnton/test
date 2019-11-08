package com.sygma.school.service;

import com.sygma.school.domain.Novel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Novel}.
 */
public interface NovelService {

    /**
     * Save a novel.
     *
     * @param novel the entity to save.
     * @return the persisted entity.
     */
    Novel save(Novel novel);

    /**
     * Get all the novels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Novel> findAll(Pageable pageable);

    /**
     * Get all the novels with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Novel> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" novel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Novel> findOne(Long id);

    /**
     * Delete the "id" novel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
