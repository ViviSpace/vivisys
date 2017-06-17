package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ProblemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Problem and its DTO ProblemDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrdMapper.class, })
public interface ProblemMapper extends EntityMapper <ProblemDTO, Problem> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")

    @Mapping(source = "ord.id", target = "ordId")
    @Mapping(source = "ord.name", target = "ordName")
    ProblemDTO toDto(Problem problem); 
    @Mapping(target = "problemOrders", ignore = true)

    @Mapping(source = "customerId", target = "customer")

    @Mapping(source = "ordId", target = "ord")
    Problem toEntity(ProblemDTO problemDTO); 
    default Problem fromId(Long id) {
        if (id == null) {
            return null;
        }
        Problem problem = new Problem();
        problem.setId(id);
        return problem;
    }
}
