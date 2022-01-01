package com.ivansantander.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A TipoFactura.
 */
@Entity
@Table(name = "tipo_factura")
public class TipoFactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "prefijo_inicial")
    private Integer prefijoInicial;

    @Column(name = "prefijo_final")
    private Integer prefijoFinal;

    @Column(name = "prefijo_actual")
    private Integer prefijoActual;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoFactura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoFactura nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrefijoInicial() {
        return this.prefijoInicial;
    }

    public TipoFactura prefijoInicial(Integer prefijoInicial) {
        this.setPrefijoInicial(prefijoInicial);
        return this;
    }

    public void setPrefijoInicial(Integer prefijoInicial) {
        this.prefijoInicial = prefijoInicial;
    }

    public Integer getPrefijoFinal() {
        return this.prefijoFinal;
    }

    public TipoFactura prefijoFinal(Integer prefijoFinal) {
        this.setPrefijoFinal(prefijoFinal);
        return this;
    }

    public void setPrefijoFinal(Integer prefijoFinal) {
        this.prefijoFinal = prefijoFinal;
    }

    public Integer getPrefijoActual() {
        return this.prefijoActual;
    }

    public TipoFactura prefijoActual(Integer prefijoActual) {
        this.setPrefijoActual(prefijoActual);
        return this;
    }

    public void setPrefijoActual(Integer prefijoActual) {
        this.prefijoActual = prefijoActual;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoFactura)) {
            return false;
        }
        return id != null && id.equals(((TipoFactura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoFactura{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", prefijoInicial=" + getPrefijoInicial() +
            ", prefijoFinal=" + getPrefijoFinal() +
            ", prefijoActual=" + getPrefijoActual() +
            "}";
    }
}
