package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.SpCost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SpCost entity.
 */
public interface SpCostSearchRepository extends ElasticsearchRepository<SpCost, Long> {
}
