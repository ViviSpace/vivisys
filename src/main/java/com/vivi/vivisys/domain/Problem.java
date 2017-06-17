package com.vivi.vivisys.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Problem.
 */
@Entity
@Table(name = "problem")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "problem")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "problem")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProblemOrder> problemOrders = new HashSet<>();

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Ord ord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ProblemOrder> getProblemOrders() {
        return problemOrders;
    }

    public Problem problemOrders(Set<ProblemOrder> problemOrders) {
        this.problemOrders = problemOrders;
        return this;
    }

    public Problem addProblemOrder(ProblemOrder problemOrder) {
        this.problemOrders.add(problemOrder);
        problemOrder.setProblem(this);
        return this;
    }

    public Problem removeProblemOrder(ProblemOrder problemOrder) {
        this.problemOrders.remove(problemOrder);
        problemOrder.setProblem(null);
        return this;
    }

    public void setProblemOrders(Set<ProblemOrder> problemOrders) {
        this.problemOrders = problemOrders;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Problem customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Ord getOrd() {
        return ord;
    }

    public Problem ord(Ord ord) {
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
        Problem problem = (Problem) o;
        if (problem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), problem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Problem{" +
            "id=" + getId() +
            "}";
    }
}
