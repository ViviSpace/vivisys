package com.vivi.vivisys.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ProblemOrder entity.
 */
public class ProblemOrderDTO implements Serializable {

    private Long id;

    private Long problemId;

    private String problemName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProblemOrderDTO problemOrderDTO = (ProblemOrderDTO) o;
        if(problemOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), problemOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProblemOrderDTO{" +
            "id=" + getId() +
            "}";
    }
}
