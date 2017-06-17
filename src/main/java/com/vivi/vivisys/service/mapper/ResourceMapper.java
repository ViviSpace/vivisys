package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ResourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Resource and its DTO ResourceDTO.
 */
@Mapper(componentModel = "spring", uses = {ServMapper.class, })
public interface ResourceMapper extends EntityMapper <ResourceDTO, Resource> {

    @Mapping(source = "serv.id", target = "servId")
    @Mapping(source = "serv.name", target = "servName")
    ResourceDTO toDto(Resource resource); 

    @Mapping(source = "servId", target = "serv")
    Resource toEntity(ResourceDTO resourceDTO); 
    default Resource fromId(Long id) {
        if (id == null) {
            return null;
        }
        Resource resource = new Resource();
        resource.setId(id);
        return resource;
    }
}
