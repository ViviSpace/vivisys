package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ResourceCostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ResourceCost and its DTO ResourceCostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResourceCostMapper extends EntityMapper <ResourceCostDTO, ResourceCost> {
    
    
    default ResourceCost fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceCost resourceCost = new ResourceCost();
        resourceCost.setId(id);
        return resourceCost;
    }
}
