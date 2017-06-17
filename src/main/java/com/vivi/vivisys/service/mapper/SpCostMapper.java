package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.SpCostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SpCost and its DTO SpCostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpCostMapper extends EntityMapper <SpCostDTO, SpCost> {
    
    
    default SpCost fromId(Long id) {
        if (id == null) {
            return null;
        }
        SpCost spCost = new SpCost();
        spCost.setId(id);
        return spCost;
    }
}
