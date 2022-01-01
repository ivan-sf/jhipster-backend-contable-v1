package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Paquete.
 */
@Entity
@Table(name = "paquete")
public class Paquete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @OneToMany(mappedBy = "paquete")
    @JsonIgnoreProperties(value = { "paquete" }, allowSetters = true)
    private Set<Precio> precios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paquete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Paquete cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Set<Precio> getPrecios() {
        return this.precios;
    }

    public void setPrecios(Set<Precio> precios) {
        if (this.precios != null) {
            this.precios.forEach(i -> i.setPaquete(null));
        }
        if (precios != null) {
            precios.forEach(i -> i.setPaquete(this));
        }
        this.precios = precios;
    }

    public Paquete precios(Set<Precio> precios) {
        this.setPrecios(precios);
        return this;
    }

    public Paquete addPrecio(Precio precio) {
        this.precios.add(precio);
        precio.setPaquete(this);
        return this;
    }

    public Paquete removePrecio(Precio precio) {
        this.precios.remove(precio);
        precio.setPaquete(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paquete)) {
            return false;
        }
        return id != null && id.equals(((Paquete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paquete{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            "}";
    }
}
