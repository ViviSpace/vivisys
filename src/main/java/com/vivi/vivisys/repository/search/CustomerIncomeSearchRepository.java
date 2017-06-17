package com.vivi.vivisys.repository.search;

import com.vivi.vivisys.domain.CustomerIncome;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerIncome entity.
 */
public interface CustomerIncomeSearchRepository extends ElasticsearchRepository<CustomerIncome, Long> {
}
