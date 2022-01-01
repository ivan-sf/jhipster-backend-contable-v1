package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaqueteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paquete.class);
        Paquete paquete1 = new Paquete();
        paquete1.setId(1L);
        Paquete paquete2 = new Paquete();
        paquete2.setId(paquete1.getId());
        assertThat(paquete1).isEqualTo(paquete2);
        paquete2.setId(2L);
        assertThat(paquete1).isNotEqualTo(paquete2);
        paquete1.setId(null);
        assertThat(paquete1).isNotEqualTo(paquete2);
    }
}
