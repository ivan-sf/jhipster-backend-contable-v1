package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetallesObjetoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetallesObjeto.class);
        DetallesObjeto detallesObjeto1 = new DetallesObjeto();
        detallesObjeto1.setId(1L);
        DetallesObjeto detallesObjeto2 = new DetallesObjeto();
        detallesObjeto2.setId(detallesObjeto1.getId());
        assertThat(detallesObjeto1).isEqualTo(detallesObjeto2);
        detallesObjeto2.setId(2L);
        assertThat(detallesObjeto1).isNotEqualTo(detallesObjeto2);
        detallesObjeto1.setId(null);
        assertThat(detallesObjeto1).isNotEqualTo(detallesObjeto2);
    }
}
