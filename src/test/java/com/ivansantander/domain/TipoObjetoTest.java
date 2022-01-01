package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoObjetoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoObjeto.class);
        TipoObjeto tipoObjeto1 = new TipoObjeto();
        tipoObjeto1.setId(1L);
        TipoObjeto tipoObjeto2 = new TipoObjeto();
        tipoObjeto2.setId(tipoObjeto1.getId());
        assertThat(tipoObjeto1).isEqualTo(tipoObjeto2);
        tipoObjeto2.setId(2L);
        assertThat(tipoObjeto1).isNotEqualTo(tipoObjeto2);
        tipoObjeto1.setId(null);
        assertThat(tipoObjeto1).isNotEqualTo(tipoObjeto2);
    }
}
