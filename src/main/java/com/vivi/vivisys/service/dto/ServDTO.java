package com.vivi.vivisys.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Serv entity.
 */
public class ServDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String descriptin;

    @NotNull
    private Double price;

    @NotNull
    private String unit;

    private String type;

    private String status;

    private Long productId;

    private String productName;

    private Set<ServiceProviderDTO> serviceProviders = new HashSet<>();

    private Set<AgentDTO> agents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptin() {
        return descriptin;
    }

    public void setDescriptin(String descriptin) {
        this.descriptin = descriptin;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Set<ServiceProviderDTO> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Set<ServiceProviderDTO> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }

    public Set<AgentDTO> getAgents() {
        return agents;
    }

    public void setAgents(Set<AgentDTO> agents) {
        this.agents = agents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServDTO servDTO = (ServDTO) o;
        if(servDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), servDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", descriptin='" + getDescriptin() + "'" +
            ", price='" + getPrice() + "'" +
            ", unit='" + getUnit() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
