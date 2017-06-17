package com.vivi.vivisys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ord.
 */
@Entity
@Table(name = "ord")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ord")
public class Ord implements Serializable {

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

    @OneToMany(mappedBy = "ord")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceDeploy> resourceDeploys = new HashSet<>();

    @OneToMany(mappedBy = "ord")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SpDeploy> spDeploys = new HashSet<>();

    @ManyToOne
    private Serv serv;

    @ManyToOne
    private Agent agent;

    @ManyToOne
    private ServiceProvider serviceProvider;

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Ord name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Ord quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Ord price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public Ord createdTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getEffictiveTime() {
        return effictiveTime;
    }

    public Ord effictiveTime(ZonedDateTime effictiveTime) {
        this.effictiveTime = effictiveTime;
        return this;
    }

    public void setEffictiveTime(ZonedDateTime effictiveTime) {
        this.effictiveTime = effictiveTime;
    }

    public ZonedDateTime getExpriedTime() {
        return expriedTime;
    }

    public Ord expriedTime(ZonedDateTime expriedTime) {
        this.expriedTime = expriedTime;
        return this;
    }

    public void setExpriedTime(ZonedDateTime expriedTime) {
        this.expriedTime = expriedTime;
    }

    public String getType() {
        return type;
    }

    public Ord type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public Ord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<ResourceDeploy> getResourceDeploys() {
        return resourceDeploys;
    }

    public Ord resourceDeploys(Set<ResourceDeploy> resourceDeploys) {
        this.resourceDeploys = resourceDeploys;
        return this;
    }

    public Ord addResourceDeploy(ResourceDeploy resourceDeploy) {
        this.resourceDeploys.add(resourceDeploy);
        resourceDeploy.setOrd(this);
        return this;
    }

    public Ord removeResourceDeploy(ResourceDeploy resourceDeploy) {
        this.resourceDeploys.remove(resourceDeploy);
        resourceDeploy.setOrd(null);
        return this;
    }

    public void setResourceDeploys(Set<ResourceDeploy> resourceDeploys) {
        this.resourceDeploys = resourceDeploys;
    }

    public Set<SpDeploy> getSpDeploys() {
        return spDeploys;
    }

    public Ord spDeploys(Set<SpDeploy> spDeploys) {
        this.spDeploys = spDeploys;
        return this;
    }

    public Ord addSpDeploy(SpDeploy spDeploy) {
        this.spDeploys.add(spDeploy);
        spDeploy.setOrd(this);
        return this;
    }

    public Ord removeSpDeploy(SpDeploy spDeploy) {
        this.spDeploys.remove(spDeploy);
        spDeploy.setOrd(null);
        return this;
    }

    public void setSpDeploys(Set<SpDeploy> spDeploys) {
        this.spDeploys = spDeploys;
    }

    public Serv getServ() {
        return serv;
    }

    public Ord serv(Serv serv) {
        this.serv = serv;
        return this;
    }

    public void setServ(Serv serv) {
        this.serv = serv;
    }

    public Agent getAgent() {
        return agent;
    }

    public Ord agent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public Ord serviceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        return this;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Ord customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ord ord = (Ord) o;
        if (ord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ord{" +
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
