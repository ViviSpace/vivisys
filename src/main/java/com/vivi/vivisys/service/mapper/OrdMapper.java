package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.OrdDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Ord and its DTO OrdDTO.
 */
@Mapper(componentModel = "spring", uses = {ServMapper.class, AgentMapper.class, ServiceProviderMapper.class, CustomerMapper.class, })
public interface OrdMapper extends EntityMapper <OrdDTO, Ord> {

    @Mapping(source = "serv.id", target = "servId")
    @Mapping(source = "serv.name", target = "servName")

    @Mapping(source = "agent.id", target = "agentId")
    @Mapping(source = "agent.name", target = "agentName")

    @Mapping(source = "serviceProvider.id", target = "serviceProviderId")
    @Mapping(source = "serviceProvider.name", target = "serviceProviderName")

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    OrdDTO toDto(Ord ord); 
    @Mapping(target = "resourceDeploys", ignore = true)
    @Mapping(target = "spDeploys", ignore = true)

    @Mapping(source = "servId", target = "serv")

    @Mapping(source = "agentId", target = "agent")

    @Mapping(source = "serviceProviderId", target = "serviceProvider")

    @Mapping(source = "customerId", target = "customer")
    Ord toEntity(OrdDTO ordDTO); 
    default Ord fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ord ord = new Ord();
        ord.setId(id);
        return ord;
    }
}
