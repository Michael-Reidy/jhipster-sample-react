package com.work.sample.repository;
import com.work.sample.domain.SearchResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SearchResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

}
