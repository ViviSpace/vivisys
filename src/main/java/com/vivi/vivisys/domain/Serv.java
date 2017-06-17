package com.vivi.vivisys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Serv.
 */
@Entity
@Table(name = "serv")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serv")
public class Serv implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "descriptin")
    private String descriptin;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "serv")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resource> resources = new HashSet<>();

    @ManyToOne
    private Product product;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "serv_service_provider",
               joinColumns = @JoinColumn(name="servs_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="service_providers_id", referencedColumnName="id"))
    private Set<ServiceProvider> serviceProviders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "serv_agent",
               joinColumns = @JoinColumn(name="servs_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="agents_id", referencedColumnName="id"))
    private Set<Agent> agents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Serv name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptin() {
        return descriptin;
    }

    public Serv descriptin(String descriptin) {
        this.descriptin = descriptin;
        return this;
    }

    public void setDescriptin(String descriptin) {
        this.descriptin = descriptin;
    }

    public Double getPrice() {
        return price;
    }

    public Serv price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public Serv unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public Serv type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public Serv status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Serv resources(Set<Resource> resources) {
        this.resources = resources;
        return this;
    }

    public Serv addResource(Resource resource) {
        this.resources.add(resource);
        resource.setServ(this);
        return this;
    }

    public Serv removeResource(Resource resource) {
        this.resources.remove(resource);
        resource.setServ(null);
        return this;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Product getProduct() {
        return product;
    }

    public Serv product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public Serv serviceProviders(Set<ServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
        return this;
    }

    public Serv addServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProviders.add(serviceProvider);
        serviceProvider.getServs().add(this);
        return this;
    }

    public Serv removeServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProviders.remove(serviceProvider);
        serviceProvider.getServs().remove(this);
        return this;
    }

    public void setServiceProviders(Set<ServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public Serv agents(Set<Agent> agents) {
        this.agents = agents;
        return this;
    }

    public Serv addAgent(Agent agent) {
        this.agents.add(agent);
        agent.getServs().add(this);
        return this;
    }

    public Serv removeAgent(Agent agent) {
        this.agents.remove(agent);
        agent.getServs().remove(this);
        return this;
    }

    public void setAgents(Set<Agent> agents) {
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
        Serv serv = (Serv) o;
        if (serv.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serv.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Serv{" +
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
