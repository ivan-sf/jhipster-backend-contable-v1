package com.ivansantander.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A TipoObjeto.
 */
@Entity
@Table(name = "tipo_objeto")
public class TipoObjeto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    @Column(name = "codigo_dian")
    private String codigoDian;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoObjeto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public TipoObjeto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public TipoObjeto fechaRegistro(String fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigoDian() {
        return this.codigoDian;
    }

    public TipoObjeto codigoDian(String codigoDian) {
        this.setCodigoDian(codigoDian);
        return this;
    }

    public void setCodigoDian(String codigoDian) {
        this.codigoDian = codigoDian;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoObjeto)) {
            return false;
        }
        return id != null && id.equals(((TipoObjeto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoObjeto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", codigoDian='" + getCodigoDian() + "'" +
            "}";
    }
}
