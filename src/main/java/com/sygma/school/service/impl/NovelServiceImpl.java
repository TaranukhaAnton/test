package com.sygma.school.service.impl;

import com.sygma.school.service.NovelService;
import com.sygma.school.domain.Novel;
import com.sygma.school.repository.NovelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Novel}.
 */
@Service
@Transactional
public class NovelServiceImpl implements NovelService {

    private final Logger log = LoggerFactory.getLogger(NovelServiceImpl.class);

    private final NovelRepository novelRepository;

    public NovelServiceImpl(NovelRepository novelRepository) {
        this.novelRepository = novelRepository;
    }

    /**
     * Save a novel.
     *
     * @param novel the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Novel save(Novel novel) {
        log.debug("Request to save Novel : {}", novel);
        return novelRepository.save(novel);
    }

    /**
     * Get all the novels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Novel> findAll(Pageable pageable) {
        log.debug("Request to get all Novels");
        return novelRepository.findAll(pageable);
    }

    /**
     * Get all the novels with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Novel> findAllWithEagerRelationships(Pageable pageable) {
        return novelRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one novel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Novel> findOne(Long id) {
        log.debug("Request to get Novel : {}", id);
        return novelRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the novel by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Novel : {}", id);
        novelRepository.deleteById(id);
    }
}
