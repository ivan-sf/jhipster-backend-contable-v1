package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Precio.
 */
@Entity
@Table(name = "precio")
public class Precio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valor_venta")
    private Double valorVenta;

    @Column(name = "valor_compra")
    private Double valorCompra;

    @ManyToOne
    @JsonIgnoreProperties(value = { "precios" }, allowSetters = true)
    private Paquete paquete;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Precio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValorVenta() {
        return this.valorVenta;
    }

    public Precio valorVenta(Double valorVenta) {
        this.setValorVenta(valorVenta);
        return this;
    }

    public void setValorVenta(Double valorVenta) {
        this.valorVenta = valorVenta;
    }

    public Double getValorCompra() {
        return this.valorCompra;
    }

    public Precio valorCompra(Double valorCompra) {
        this.setValorCompra(valorCompra);
        return this;
    }

    public void setValorCompra(Double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public Paquete getPaquete() {
        return this.paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public Precio paquete(Paquete paquete) {
        this.setPaquete(paquete);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Precio)) {
            return false;
        }
        return id != null && id.equals(((Precio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Precio{" +
            "id=" + getId() +
            ", valorVenta=" + getValorVenta() +
            ", valorCompra=" + getValorCompra() +
            "}";
    }
}
