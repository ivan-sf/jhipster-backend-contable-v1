package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Inventario.
 */
@Entity
@Table(name = "inventario")
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @OneToMany(mappedBy = "inventario")
    @JsonIgnoreProperties(value = { "objetos", "inventario" }, allowSetters = true)
    private Set<Sector> sectors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Inventario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValor() {
        return this.valor;
    }

    public Inventario valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Inventario fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Sector> getSectors() {
        return this.sectors;
    }

    public void setSectors(Set<Sector> sectors) {
        if (this.sectors != null) {
            this.sectors.forEach(i -> i.setInventario(null));
        }
        if (sectors != null) {
            sectors.forEach(i -> i.setInventario(this));
        }
        this.sectors = sectors;
    }

    public Inventario sectors(Set<Sector> sectors) {
        this.setSectors(sectors);
        return this;
    }

    public Inventario addSector(Sector sector) {
        this.sectors.add(sector);
        sector.setInventario(this);
        return this;
    }

    public Inventario removeSector(Sector sector) {
        this.sectors.remove(sector);
        sector.setInventario(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventario)) {
            return false;
        }
        return id != null && id.equals(((Inventario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", valor=" + getValor() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
