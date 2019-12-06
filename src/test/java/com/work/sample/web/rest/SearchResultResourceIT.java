package com.work.sample.web.rest;

import com.work.sample.JhipsterSampleReactApp;
import com.work.sample.domain.SearchResult;
import com.work.sample.repository.SearchResultRepository;
import com.work.sample.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.work.sample.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SearchResultResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleReactApp.class)
public class SearchResultResourceIT {

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private SearchResultRepository searchResultRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSearchResultMockMvc;

    private SearchResult searchResult;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SearchResultResource searchResultResource = new SearchResultResource(searchResultRepository);
        this.restSearchResultMockMvc = MockMvcBuilders.standaloneSetup(searchResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchResult createEntity(EntityManager em) {
        SearchResult searchResult = new SearchResult()
            .source(DEFAULT_SOURCE)
            .text(DEFAULT_TEXT)
            .url(DEFAULT_URL);
        return searchResult;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchResult createUpdatedEntity(EntityManager em) {
        SearchResult searchResult = new SearchResult()
            .source(UPDATED_SOURCE)
            .text(UPDATED_TEXT)
            .url(UPDATED_URL);
        return searchResult;
    }

    @BeforeEach
    public void initTest() {
        searchResult = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearchResult() throws Exception {
        int databaseSizeBeforeCreate = searchResultRepository.findAll().size();

        // Create the SearchResult
        restSearchResultMockMvc.perform(post("/api/search-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchResult)))
            .andExpect(status().isCreated());

        // Validate the SearchResult in the database
        List<SearchResult> searchResultList = searchResultRepository.findAll();
        assertThat(searchResultList).hasSize(databaseSizeBeforeCreate + 1);
        SearchResult testSearchResult = searchResultList.get(searchResultList.size() - 1);
        assertThat(testSearchResult.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testSearchResult.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSearchResult.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createSearchResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchResultRepository.findAll().size();

        // Create the SearchResult with an existing ID
        searchResult.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchResultMockMvc.perform(post("/api/search-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchResult)))
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        List<SearchResult> searchResultList = searchResultRepository.findAll();
        assertThat(searchResultList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSearchResults() throws Exception {
        // Initialize the database
        searchResultRepository.saveAndFlush(searchResult);

        // Get all the searchResultList
        restSearchResultMockMvc.perform(get("/api/search-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }
    
    @Test
    @Transactional
    public void getSearchResult() throws Exception {
        // Initialize the database
        searchResultRepository.saveAndFlush(searchResult);

        // Get the searchResult
        restSearchResultMockMvc.perform(get("/api/search-results/{id}", searchResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searchResult.getId().intValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    public void getNonExistingSearchResult() throws Exception {
        // Get the searchResult
        restSearchResultMockMvc.perform(get("/api/search-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearchResult() throws Exception {
        // Initialize the database
        searchResultRepository.saveAndFlush(searchResult);

        int databaseSizeBeforeUpdate = searchResultRepository.findAll().size();

        // Update the searchResult
        SearchResult updatedSearchResult = searchResultRepository.findById(searchResult.getId()).get();
        // Disconnect from session so that the updates on updatedSearchResult are not directly saved in db
        em.detach(updatedSearchResult);
        updatedSearchResult
            .source(UPDATED_SOURCE)
            .text(UPDATED_TEXT)
            .url(UPDATED_URL);

        restSearchResultMockMvc.perform(put("/api/search-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSearchResult)))
            .andExpect(status().isOk());

        // Validate the SearchResult in the database
        List<SearchResult> searchResultList = searchResultRepository.findAll();
        assertThat(searchResultList).hasSize(databaseSizeBeforeUpdate);
        SearchResult testSearchResult = searchResultList.get(searchResultList.size() - 1);
        assertThat(testSearchResult.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testSearchResult.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSearchResult.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingSearchResult() throws Exception {
        int databaseSizeBeforeUpdate = searchResultRepository.findAll().size();

        // Create the SearchResult

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchResultMockMvc.perform(put("/api/search-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searchResult)))
            .andExpect(status().isBadRequest());

        // Validate the SearchResult in the database
        List<SearchResult> searchResultList = searchResultRepository.findAll();
        assertThat(searchResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSearchResult() throws Exception {
        // Initialize the database
        searchResultRepository.saveAndFlush(searchResult);

        int databaseSizeBeforeDelete = searchResultRepository.findAll().size();

        // Delete the searchResult
        restSearchResultMockMvc.perform(delete("/api/search-results/{id}", searchResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SearchResult> searchResultList = searchResultRepository.findAll();
        assertThat(searchResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
