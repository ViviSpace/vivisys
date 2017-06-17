package com.vivi.vivisys.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProblemOrder.
 */
@Entity
@Table(name = "problem_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "problemorder")
public class ProblemOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Problem problem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ProblemOrder name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Problem getProblem() {
        return problem;
    }

    public ProblemOrder problem(Problem problem) {
        this.problem = problem;
        return this;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProblemOrder problemOrder = (ProblemOrder) o;
        if (problemOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), problemOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProblemOrder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
