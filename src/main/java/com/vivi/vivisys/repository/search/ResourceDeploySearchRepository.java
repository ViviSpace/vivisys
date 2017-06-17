package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.ResourceDeploy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ResourceDeploy entity.
 */
public interface ResourceDeploySearchRepository extends ElasticsearchRepository<ResourceDeploy, Long> {
}
