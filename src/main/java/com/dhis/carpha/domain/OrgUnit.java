package com.dhis.carpha.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrgUnit.
 */
@Entity
@Table(name = "org_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrgUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "org_unit_name")
    private String orgUnitName;

    @Column(name = "dhis_org_unit_name")
    private String dhisOrgUnitName;

    @Column(name = "dhis_org_unit_code")
    private String dhisOrgUnitCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrgUnit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgUnitName() {
        return this.orgUnitName;
    }

    public OrgUnit orgUnitName(String orgUnitName) {
        this.setOrgUnitName(orgUnitName);
        return this;
    }

    public void setOrgUnitName(String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    public String getDhisOrgUnitName() {
        return this.dhisOrgUnitName;
    }

    public OrgUnit dhisOrgUnitName(String dhisOrgUnitName) {
        this.setDhisOrgUnitName(dhisOrgUnitName);
        return this;
    }

    public void setDhisOrgUnitName(String dhisOrgUnitName) {
        this.dhisOrgUnitName = dhisOrgUnitName;
    }

    public String getDhisOrgUnitCode() {
        return this.dhisOrgUnitCode;
    }

    public OrgUnit dhisOrgUnitCode(String dhisOrgUnitCode) {
        this.setDhisOrgUnitCode(dhisOrgUnitCode);
        return this;
    }

    public void setDhisOrgUnitCode(String dhisOrgUnitCode) {
        this.dhisOrgUnitCode = dhisOrgUnitCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgUnit)) {
            return false;
        }
        return id != null && id.equals(((OrgUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgUnit{" +
            "id=" + getId() +
            ", orgUnitName='" + getOrgUnitName() + "'" +
            ", dhisOrgUnitName='" + getDhisOrgUnitName() + "'" +
            ", dhisOrgUnitCode='" + getDhisOrgUnitCode() + "'" +
            "}";
    }
}
