package com.vivi.vivisys.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AgentCost entity.
 */
public class AgentCostDTO implements Serializable {

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

        AgentCostDTO agentCostDTO = (AgentCostDTO) o;
        if(agentCostDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentCostDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentCostDTO{" +
            "id=" + getId() +
            "}";
    }
}
