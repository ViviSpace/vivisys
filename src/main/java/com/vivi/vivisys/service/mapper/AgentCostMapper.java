package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.AgentCostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AgentCost and its DTO AgentCostDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgentCostMapper extends EntityMapper <AgentCostDTO, AgentCost> {
    
    
    default AgentCost fromId(Long id) {
        if (id == null) {
            return null;
        }
        AgentCost agentCost = new AgentCost();
        agentCost.setId(id);
        return agentCost;
    }
}
