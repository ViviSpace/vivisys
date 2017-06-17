package com.vivi.vivisys.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Ord entity.
 */
public class OrdDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double quantity;

    @NotNull
    private Double price;

    private ZonedDateTime createdTime;

    private ZonedDateTime effictiveTime;

    private ZonedDateTime expriedTime;

    private String type;

    private String status;

    private Long servId;

    private String servName;

    private Long agentId;

    private String agentName;

    private Long serviceProviderId;

    private String serviceProviderName;

    private Long customerId;

    private String customerName;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getEffictiveTime() {
        return effictiveTime;
    }

    public void setEffictiveTime(ZonedDateTime effictiveTime) {
        this.effictiveTime = effictiveTime;
    }

    public ZonedDateTime getExpriedTime() {
        return expriedTime;
    }

    public void setExpriedTime(ZonedDateTime expriedTime) {
        this.expriedTime = expriedTime;
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

    public Long getServId() {
        return servId;
    }

    public void setServId(Long servId) {
        this.servId = servId;
    }

    public String getServName() {
        return servName;
    }

    public void setServName(String servName) {
        this.servName = servName;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrdDTO ordDTO = (OrdDTO) o;
        if(ordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", price='" + getPrice() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", effictiveTime='" + getEffictiveTime() + "'" +
            ", expriedTime='" + getExpriedTime() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
