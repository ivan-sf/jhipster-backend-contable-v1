package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VencimientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vencimiento.class);
        Vencimiento vencimiento1 = new Vencimiento();
        vencimiento1.setId(1L);
        Vencimiento vencimiento2 = new Vencimiento();
        vencimiento2.setId(vencimiento1.getId());
        assertThat(vencimiento1).isEqualTo(vencimiento2);
        vencimiento2.setId(2L);
        assertThat(vencimiento1).isNotEqualTo(vencimiento2);
        vencimiento1.setId(null);
        assertThat(vencimiento1).isNotEqualTo(vencimiento2);
    }
}
