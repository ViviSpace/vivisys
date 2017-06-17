package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ResourceDeployDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ResourceDeploy and its DTO ResourceDeployDTO.
 */
@Mapper(componentModel = "spring", uses = {ResourceMapper.class, OrdMapper.class, })
public interface ResourceDeployMapper extends EntityMapper <ResourceDeployDTO, ResourceDeploy> {

    @Mapping(source = "resource.id", target = "resourceId")
    @Mapping(source = "resource.name", target = "resourceName")

    @Mapping(source = "ord.id", target = "ordId")
    @Mapping(source = "ord.name", target = "ordName")
    ResourceDeployDTO toDto(ResourceDeploy resourceDeploy); 

    @Mapping(source = "resourceId", target = "resource")

    @Mapping(source = "ordId", target = "ord")
    ResourceDeploy toEntity(ResourceDeployDTO resourceDeployDTO); 
    default ResourceDeploy fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceDeploy resourceDeploy = new ResourceDeploy();
        resourceDeploy.setId(id);
        return resourceDeploy;
    }
}
