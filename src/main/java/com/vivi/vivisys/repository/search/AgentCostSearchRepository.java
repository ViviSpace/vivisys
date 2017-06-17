package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.AgentCost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AgentCost entity.
 */
public interface AgentCostSearchRepository extends ElasticsearchRepository<AgentCost, Long> {
}
