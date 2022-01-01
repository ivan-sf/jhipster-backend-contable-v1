package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Sector.
 */
@Entity
@Table(name = "sector")
public class Sector implements Serializable {

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

    @OneToMany(mappedBy = "sector")
    @JsonIgnoreProperties(value = { "tipoObjeto", "usuario", "codigos", "sector" }, allowSetters = true)
    private Set<Objeto> objetos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sectors" }, allowSetters = true)
    private Inventario inventario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sector id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Sector nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getValor() {
        return this.valor;
    }

    public Sector valor(Double valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Sector fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Set<Objeto> getObjetos() {
        return this.objetos;
    }

    public void setObjetos(Set<Objeto> objetos) {
        if (this.objetos != null) {
            this.objetos.forEach(i -> i.setSector(null));
        }
        if (objetos != null) {
            objetos.forEach(i -> i.setSector(this));
        }
        this.objetos = objetos;
    }

    public Sector objetos(Set<Objeto> objetos) {
        this.setObjetos(objetos);
        return this;
    }

    public Sector addObjeto(Objeto objeto) {
        this.objetos.add(objeto);
        objeto.setSector(this);
        return this;
    }

    public Sector removeObjeto(Objeto objeto) {
        this.objetos.remove(objeto);
        objeto.setSector(null);
        return this;
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Sector inventario(Inventario inventario) {
        this.setInventario(inventario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sector)) {
            return false;
        }
        return id != null && id.equals(((Sector) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sector{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", valor=" + getValor() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
