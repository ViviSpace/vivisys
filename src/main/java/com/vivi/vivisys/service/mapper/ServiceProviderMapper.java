package com.vivi.vivisys.service.mapper;

import com.vivi.vivisys.domain.*;
import com.vivi.vivisys.service.dto.ServiceProviderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ServiceProvider and its DTO ServiceProviderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceProviderMapper extends EntityMapper <ServiceProviderDTO, ServiceProvider> {
    
    @Mapping(target = "servs", ignore = true)
    ServiceProvider toEntity(ServiceProviderDTO serviceProviderDTO); 
    default ServiceProvider fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(id);
        return serviceProvider;
    }
}
