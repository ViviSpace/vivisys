package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.CustomerIncomeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerIncome and its DTO CustomerIncomeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerIncomeMapper extends EntityMapper <CustomerIncomeDTO, CustomerIncome> {
    
    
    default CustomerIncome fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerIncome customerIncome = new CustomerIncome();
        customerIncome.setId(id);
        return customerIncome;
    }
}
