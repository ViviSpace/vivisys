package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.AgentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Agent and its DTO AgentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgentMapper extends EntityMapper <AgentDTO, Agent> {
    
    @Mapping(target = "servs", ignore = true)
    Agent toEntity(AgentDTO agentDTO); 
    default Agent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Agent agent = new Agent();
        agent.setId(id);
        return agent;
    }
}
