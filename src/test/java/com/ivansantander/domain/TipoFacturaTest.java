package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoFacturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoFactura.class);
        TipoFactura tipoFactura1 = new TipoFactura();
        tipoFactura1.setId(1L);
        TipoFactura tipoFactura2 = new TipoFactura();
        tipoFactura2.setId(tipoFactura1.getId());
        assertThat(tipoFactura1).isEqualTo(tipoFactura2);
        tipoFactura2.setId(2L);
        assertThat(tipoFactura1).isNotEqualTo(tipoFactura2);
        tipoFactura1.setId(null);
        assertThat(tipoFactura1).isNotEqualTo(tipoFactura2);
    }
}
