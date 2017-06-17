package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.Serv;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Serv entity.
 */
public interface ServSearchRepository extends ElasticsearchRepository<Serv, Long> {
}
