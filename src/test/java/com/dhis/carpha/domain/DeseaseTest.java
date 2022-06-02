package com.dhis.carpha.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dhis.carpha.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeseaseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Desease.class);
        Desease desease1 = new Desease();
        desease1.setId(1L);
        Desease desease2 = new Desease();
        desease2.setId(desease1.getId());
        assertThat(desease1).isEqualTo(desease2);
        desease2.setId(2L);
        assertThat(desease1).isNotEqualTo(desease2);
        desease1.setId(null);
        assertThat(desease1).isNotEqualTo(desease2);
    }
}
