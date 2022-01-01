package com.ivansantander.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A DetallesObjeto.
 */
@Entity
@Table(name = "detalles_objeto")
public class DetallesObjeto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private Long descripcion;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetallesObjeto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public DetallesObjeto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getDescripcion() {
        return this.descripcion;
    }

    public DetallesObjeto descripcion(Long descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(Long descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public DetallesObjeto fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetallesObjeto)) {
            return false;
        }
        return id != null && id.equals(((DetallesObjeto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetallesObjeto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion=" + getDescripcion() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
