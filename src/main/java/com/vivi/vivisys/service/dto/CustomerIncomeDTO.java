package com.vivi.vivisys.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CustomerIncome entity.
 */
public class CustomerIncomeDTO implements Serializable {

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

        CustomerIncomeDTO customerIncomeDTO = (CustomerIncomeDTO) o;
        if(customerIncomeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerIncomeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerIncomeDTO{" +
            "id=" + getId() +
            "}";
    }
}
