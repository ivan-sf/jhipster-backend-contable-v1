package com.ivansantander.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ivansantander.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotaContableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotaContable.class);
        NotaContable notaContable1 = new NotaContable();
        notaContable1.setId(1L);
        NotaContable notaContable2 = new NotaContable();
        notaContable2.setId(notaContable1.getId());
        assertThat(notaContable1).isEqualTo(notaContable2);
        notaContable2.setId(2L);
        assertThat(notaContable1).isNotEqualTo(notaContable2);
        notaContable1.setId(null);
        assertThat(notaContable1).isNotEqualTo(notaContable2);
    }
}
