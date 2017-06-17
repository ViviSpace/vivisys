package com.vivi.vivisys.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SpDeploy.
 */
@Entity
@Table(name = "sp_deploy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "spdeploy")
public class SpDeploy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "created_time")
    private ZonedDateTime createdTime;

    @Column(name = "effictive_time")
    private ZonedDateTime effictiveTime;

    @Column(name = "expried_time")
    private ZonedDateTime expriedTime;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "status")
    private String status;

    @ManyToOne
    private ServiceProvider serviceProvider;

    @ManyToOne
    private Ord ord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public SpDeploy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public SpDeploy quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public SpDeploy price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public SpDeploy createdTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getEffictiveTime() {
        return effictiveTime;
    }

    public SpDeploy effictiveTime(ZonedDateTime effictiveTime) {
        this.effictiveTime = effictiveTime;
        return this;
    }

    public void setEffictiveTime(ZonedDateTime effictiveTime) {
        this.effictiveTime = effictiveTime;
    }

    public ZonedDateTime getExpriedTime() {
        return expriedTime;
    }

    public SpDeploy expriedTime(ZonedDateTime expriedTime) {
        this.expriedTime = expriedTime;
        return this;
    }

    public void setExpriedTime(ZonedDateTime expriedTime) {
        this.expriedTime = expriedTime;
    }

    public String getType() {
        return type;
    }

    public SpDeploy type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public SpDeploy status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public SpDeploy serviceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        return this;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Ord getOrd() {
        return ord;
    }

    public SpDeploy ord(Ord ord) {
        this.ord = ord;
        return this;
    }

    public void setOrd(Ord ord) {
        this.ord = ord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpDeploy spDeploy = (SpDeploy) o;
        if (spDeploy.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spDeploy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpDeploy{" +
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
