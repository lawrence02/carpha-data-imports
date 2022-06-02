package com.dhis.carpha.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Desease.
 */
@Entity
@Table(name = "desease")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Desease implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "desease_id")
    private String deseaseId;

    @Column(name = "case_info")
    private String caseInfo;

    @Column(name = "year")
    private String year;

    @Column(name = "week")
    private String week;

    @Column(name = "week_ending")
    private String weekEnding;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Desease id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public Desease country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeseaseId() {
        return this.deseaseId;
    }

    public Desease deseaseId(String deseaseId) {
        this.setDeseaseId(deseaseId);
        return this;
    }

    public void setDeseaseId(String deseaseId) {
        this.deseaseId = deseaseId;
    }

    public String getCaseInfo() {
        return this.caseInfo;
    }

    public Desease caseInfo(String caseInfo) {
        this.setCaseInfo(caseInfo);
        return this;
    }

    public void setCaseInfo(String caseInfo) {
        this.caseInfo = caseInfo;
    }

    public String getYear() {
        return this.year;
    }

    public Desease year(String year) {
        this.setYear(year);
        return this;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getWeek() {
        return this.week;
    }

    public Desease week(String week) {
        this.setWeek(week);
        return this;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekEnding() {
        return this.weekEnding;
    }

    public Desease weekEnding(String weekEnding) {
        this.setWeekEnding(weekEnding);
        return this;
    }

    public void setWeekEnding(String weekEnding) {
        this.weekEnding = weekEnding;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Desease)) {
            return false;
        }
        return id != null && id.equals(((Desease) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Desease{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", deseaseId='" + getDeseaseId() + "'" +
            ", caseInfo='" + getCaseInfo() + "'" +
            ", year='" + getYear() + "'" +
            ", week='" + getWeek() + "'" +
            ", weekEnding='" + getWeekEnding() + "'" +
            "}";
    }
}
