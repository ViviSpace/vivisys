package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.ResourceCost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ResourceCost entity.
 */
public interface ResourceCostSearchRepository extends ElasticsearchRepository<ResourceCost, Long> {
}
