package com.ivansantander.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Trabajo.
 */
@Entity
@Table(name = "trabajo")
public class Trabajo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "salario")
    private Integer salario;

    @Column(name = "salud")
    private String salud;

    @Column(name = "pension")
    private String pension;

    @Column(name = "observacon")
    private String observacon;

    @Column(name = "fecha_registro")
    private String fechaRegistro;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trabajo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Trabajo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return this.cargo;
    }

    public Trabajo cargo(String cargo) {
        this.setCargo(cargo);
        return this;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Integer getSalario() {
        return this.salario;
    }

    public Trabajo salario(Integer salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(Integer salario) {
        this.salario = salario;
    }

    public String getSalud() {
        return this.salud;
    }

    public Trabajo salud(String salud) {
        this.setSalud(salud);
        return this;
    }

    public void setSalud(String salud) {
        this.salud = salud;
    }

    public String getPension() {
        return this.pension;
    }

    public Trabajo pension(String pension) {
        this.setPension(pension);
        return this;
    }

    public void setPension(String pension) {
        this.pension = pension;
    }

    public String getObservacon() {
        return this.observacon;
    }

    public Trabajo observacon(String observacon) {
        this.setObservacon(observacon);
        return this;
    }

    public void setObservacon(String observacon) {
        this.observacon = observacon;
    }

    public String getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Trabajo fechaRegistro(String fechaRegistro) {
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
        if (!(o instanceof Trabajo)) {
            return false;
        }
        return id != null && id.equals(((Trabajo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trabajo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cargo='" + getCargo() + "'" +
            ", salario=" + getSalario() +
            ", salud='" + getSalud() + "'" +
            ", pension='" + getPension() + "'" +
            ", observacon='" + getObservacon() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
