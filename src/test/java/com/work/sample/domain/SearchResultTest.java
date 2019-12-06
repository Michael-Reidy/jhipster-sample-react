package com.work.sample.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.work.sample.web.rest.TestUtil;

public class SearchResultTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SearchResult.class);
        SearchResult searchResult1 = new SearchResult();
        searchResult1.setId(1L);
        SearchResult searchResult2 = new SearchResult();
        searchResult2.setId(searchResult1.getId());
        assertThat(searchResult1).isEqualTo(searchResult2);
        searchResult2.setId(2L);
        assertThat(searchResult1).isNotEqualTo(searchResult2);
        searchResult1.setId(null);
        assertThat(searchResult1).isNotEqualTo(searchResult2);
    }
}
