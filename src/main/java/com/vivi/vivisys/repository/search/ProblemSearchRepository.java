package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.Problem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Problem entity.
 */
public interface ProblemSearchRepository extends ElasticsearchRepository<Problem, Long> {
}
