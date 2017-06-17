package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.ProblemOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProblemOrder entity.
 */
public interface ProblemOrderSearchRepository extends ElasticsearchRepository<ProblemOrder, Long> {
}
