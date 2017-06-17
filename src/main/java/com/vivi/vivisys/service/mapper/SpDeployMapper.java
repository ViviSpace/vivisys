package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.SpDeployDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SpDeploy and its DTO SpDeployDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceProviderMapper.class, OrdMapper.class, })
public interface SpDeployMapper extends EntityMapper <SpDeployDTO, SpDeploy> {

    @Mapping(source = "serviceProvider.id", target = "serviceProviderId")
    @Mapping(source = "serviceProvider.name", target = "serviceProviderName")

    @Mapping(source = "ord.id", target = "ordId")
    @Mapping(source = "ord.name", target = "ordName")
    SpDeployDTO toDto(SpDeploy spDeploy); 

    @Mapping(source = "serviceProviderId", target = "serviceProvider")

    @Mapping(source = "ordId", target = "ord")
    SpDeploy toEntity(SpDeployDTO spDeployDTO); 
    default SpDeploy fromId(Long id) {
        if (id == null) {
            return null;
        }
        SpDeploy spDeploy = new SpDeploy();
        spDeploy.setId(id);
        return spDeploy;
    }
}
