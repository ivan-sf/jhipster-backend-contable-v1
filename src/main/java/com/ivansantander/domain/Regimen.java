package com.ivansantander.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Regimen.
 */
@Entity
@Table(name = "regimen")
public class Regimen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tipo_regimen")
    private String tipoRegimen;

    @Column(name = "nombre_regimen")
    private String nombreRegimen;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Regimen id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoRegimen() {
        return this.tipoRegimen;
    }

    public Regimen tipoRegimen(String tipoRegimen) {
        this.setTipoRegimen(tipoRegimen);
        return this;
    }

    public void setTipoRegimen(String tipoRegimen) {
        this.tipoRegimen = tipoRegimen;
    }

    public String getNombreRegimen() {
        return this.nombreRegimen;
    }

    public Regimen nombreRegimen(String nombreRegimen) {
        this.setNombreRegimen(nombreRegimen);
        return this;
    }

    public void setNombreRegimen(String nombreRegimen) {
        this.nombreRegimen = nombreRegimen;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Regimen fechaRegistro(String fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regimen)) {
            return false;
        }
        return id != null && id.equals(((Regimen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Regimen{" +
            "id=" + getId() +
            ", tipoRegimen='" + getTipoRegimen() + "'" +
            ", nombreRegimen='" + getNombreRegimen() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
