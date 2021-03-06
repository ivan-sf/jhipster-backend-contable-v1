package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lote.class);
        Lote lote1 = new Lote();
        lote1.setId(1L);
        Lote lote2 = new Lote();
        lote2.setId(lote1.getId());
        assertThat(lote1).isEqualTo(lote2);
        lote2.setId(2L);
        assertThat(lote1).isNotEqualTo(lote2);
        lote1.setId(null);
        assertThat(lote1).isNotEqualTo(lote2);
    }
}
