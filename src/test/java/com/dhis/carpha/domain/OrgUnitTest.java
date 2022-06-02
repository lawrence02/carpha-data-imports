package com.dhis.carpha.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dhis.carpha.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgUnitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUnit.class);
        OrgUnit orgUnit1 = new OrgUnit();
        orgUnit1.setId(1L);
        OrgUnit orgUnit2 = new OrgUnit();
        orgUnit2.setId(orgUnit1.getId());
        assertThat(orgUnit1).isEqualTo(orgUnit2);
        orgUnit2.setId(2L);
        assertThat(orgUnit1).isNotEqualTo(orgUnit2);
        orgUnit1.setId(null);
        assertThat(orgUnit1).isNotEqualTo(orgUnit2);
    }
}
