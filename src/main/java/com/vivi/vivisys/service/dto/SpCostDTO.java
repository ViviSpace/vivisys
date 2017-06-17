package com.vivi.vivisys.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SpCost entity.
 */
public class SpCostDTO implements Serializable {

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

        SpCostDTO spCostDTO = (SpCostDTO) o;
        if(spCostDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spCostDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpCostDTO{" +
            "id=" + getId() +
            "}";
    }
}
