package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.Ord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ord entity.
 */
public interface OrdSearchRepository extends ElasticsearchRepository<Ord, Long> {
}
