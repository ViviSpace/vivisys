package com.vivi.vivisys.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ResourceCost entity.
 */
public class ResourceCostDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceCostDTO resourceCostDTO = (ResourceCostDTO) o;
        if(resourceCostDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resourceCostDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResourceCostDTO{" +
            "id=" + getId() +
            "}";
    }
}
