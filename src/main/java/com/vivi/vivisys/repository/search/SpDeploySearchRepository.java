package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.SpDeploy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SpDeploy entity.
 */
public interface SpDeploySearchRepository extends ElasticsearchRepository<SpDeploy, Long> {
}
