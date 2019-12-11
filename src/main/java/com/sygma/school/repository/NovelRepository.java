package com.sygma.school.repository;
import com.sygma.school.domain.Novel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Novel entity.
 */
@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {

    @Query(value = "select distinct novel from Novel novel left join fetch novel.authors",
        countQuery = "select count(distinct novel) from Novel novel")
    Page<Novel> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct novel from Novel novel left join fetch novel.authors")
    List<Novel> findAllWithEagerRelationships();

    @Query("select novel from Novel novel left join fetch novel.authors where novel.id =:id")
    Optional<Novel> findOneWithEagerRelationships(@Param("id") Long id);

}
