package com.work.sample.web.rest;

import com.work.sample.domain.SearchResult;
import com.work.sample.repository.SearchResultRepository;
import com.work.sample.web.rest.errors.BadRequestAlertException;

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
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.work.sample.domain.SearchResult}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SearchResultResource {

    private final Logger log = LoggerFactory.getLogger(SearchResultResource.class);

    private static final String ENTITY_NAME = "searchResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SearchResultRepository searchResultRepository;

    public SearchResultResource(SearchResultRepository searchResultRepository) {
        this.searchResultRepository = searchResultRepository;
    }

    /**
     * {@code POST  /search-results} : Create a new searchResult.
     *
     * @param searchResult the searchResult to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new searchResult, or with status {@code 400 (Bad Request)} if the searchResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/search-results")
    public ResponseEntity<SearchResult> createSearchResult(@RequestBody SearchResult searchResult) throws URISyntaxException {
        log.debug("REST request to save SearchResult : {}", searchResult);
        if (searchResult.getId() != null) {
            throw new BadRequestAlertException("A new searchResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SearchResult result = searchResultRepository.save(searchResult);
        return ResponseEntity.created(new URI("/api/search-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /search-results} : Updates an existing searchResult.
     *
     * @param searchResult the searchResult to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated searchResult,
     * or with status {@code 400 (Bad Request)} if the searchResult is not valid,
     * or with status {@code 500 (Internal Server Error)} if the searchResult couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/search-results")
    public ResponseEntity<SearchResult> updateSearchResult(@RequestBody SearchResult searchResult) throws URISyntaxException {
        log.debug("REST request to update SearchResult : {}", searchResult);
        if (searchResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SearchResult result = searchResultRepository.save(searchResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, searchResult.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /search-results} : get all the searchResults.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of searchResults in body.
     */
    @GetMapping("/search-results")
    public ResponseEntity<List<SearchResult>> getAllSearchResults(Pageable pageable) {
        log.debug("REST request to get a page of SearchResults");
        Page<SearchResult> page = searchResultRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /search-results/:id} : get the "id" searchResult.
     *
     * @param id the id of the searchResult to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the searchResult, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/search-results/{id}")
    public ResponseEntity<SearchResult> getSearchResult(@PathVariable Long id) {
        log.debug("REST request to get SearchResult : {}", id);
        Optional<SearchResult> searchResult = searchResultRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(searchResult);
    }

    /**
     * {@code DELETE  /search-results/:id} : delete the "id" searchResult.
     *
     * @param id the id of the searchResult to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/search-results/{id}")
    public ResponseEntity<Void> deleteSearchResult(@PathVariable Long id) {
        log.debug("REST request to delete SearchResult : {}", id);
        searchResultRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
