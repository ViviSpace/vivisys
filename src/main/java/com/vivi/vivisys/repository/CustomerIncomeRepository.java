package com.vivi.vivisys.repository;

import com.vivi.vivisys.domain.CustomerIncome;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CustomerIncome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerIncomeRepository extends JpaRepository<CustomerIncome,Long> {
    
}
