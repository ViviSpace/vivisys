package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ProblemOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProblemOrder and its DTO ProblemOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {ProblemMapper.class, })
public interface ProblemOrderMapper extends EntityMapper <ProblemOrderDTO, ProblemOrder> {

    @Mapping(source = "problem.id", target = "problemId")
    @Mapping(source = "problem.name", target = "problemName")
    ProblemOrderDTO toDto(ProblemOrder problemOrder); 

    @Mapping(source = "problemId", target = "problem")
    ProblemOrder toEntity(ProblemOrderDTO problemOrderDTO); 
    default ProblemOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProblemOrder problemOrder = new ProblemOrder();
        problemOrder.setId(id);
        return problemOrder;
    }
}
