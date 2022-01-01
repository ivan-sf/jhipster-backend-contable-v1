package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RegimenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regimen.class);
        Regimen regimen1 = new Regimen();
        regimen1.setId(1L);
        Regimen regimen2 = new Regimen();
        regimen2.setId(regimen1.getId());
        assertThat(regimen1).isEqualTo(regimen2);
        regimen2.setId(2L);
        assertThat(regimen1).isNotEqualTo(regimen2);
        regimen1.setId(null);
        assertThat(regimen1).isNotEqualTo(regimen2);
    }
}
