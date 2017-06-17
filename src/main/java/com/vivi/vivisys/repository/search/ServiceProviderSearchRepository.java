package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.ServiceProvider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ServiceProvider entity.
 */
public interface ServiceProviderSearchRepository extends ElasticsearchRepository<ServiceProvider, Long> {
}
