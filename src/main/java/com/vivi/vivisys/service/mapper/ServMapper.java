package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ServDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Serv and its DTO ServDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, ServiceProviderMapper.class, AgentMapper.class, })
public interface ServMapper extends EntityMapper <ServDTO, Serv> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ServDTO toDto(Serv serv); 
    @Mapping(target = "resources", ignore = true)

    @Mapping(source = "productId", target = "product")
    Serv toEntity(ServDTO servDTO); 
    default Serv fromId(Long id) {
        if (id == null) {
            return null;
        }
        Serv serv = new Serv();
        serv.setId(id);
        return serv;
    }
}
