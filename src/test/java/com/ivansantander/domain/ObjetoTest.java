package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjetoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objeto.class);
        Objeto objeto1 = new Objeto();
        objeto1.setId(1L);
        Objeto objeto2 = new Objeto();
        objeto2.setId(objeto1.getId());
        assertThat(objeto1).isEqualTo(objeto2);
        objeto2.setId(2L);
        assertThat(objeto1).isNotEqualTo(objeto2);
        objeto1.setId(null);
        assertThat(objeto1).isNotEqualTo(objeto2);
    }
}
