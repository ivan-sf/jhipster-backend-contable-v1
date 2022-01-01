package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A DetalleFactura.
 */
@Entity
@Table(name = "detalle_factura")
public class DetalleFactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "cantidad")
    private Double cantidad;

    @Column(name = "total")
    private Double total;

    @Column(name = "iva")
    private Integer iva;

    @Column(name = "valor_impuesto")
    private Double valorImpuesto;

    @Column(name = "estado")
    private Integer estado;

    @JsonIgnoreProperties(value = { "tipoObjeto", "usuario", "codigos", "sector" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Objeto objeto;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "sucursal", "tipoFactura", "notaContable", "detalleFacturas", "cliente", "empleadp" },
        allowSetters = true
    )
    private Factura factura;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetalleFactura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public DetalleFactura precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getCantidad() {
        return this.cantidad;
    }

    public DetalleFactura cantidad(Double cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return this.total;
    }

    public DetalleFactura total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getIva() {
        return this.iva;
    }

    public DetalleFactura iva(Integer iva) {
        this.setIva(iva);
        return this;
    }

    public void setIva(Integer iva) {
        this.iva = iva;
    }

    public Double getValorImpuesto() {
        return this.valorImpuesto;
    }

    public DetalleFactura valorImpuesto(Double valorImpuesto) {
        this.setValorImpuesto(valorImpuesto);
        return this;
    }

    public void setValorImpuesto(Double valorImpuesto) {
        this.valorImpuesto = valorImpuesto;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public DetalleFactura estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Objeto getObjeto() {
        return this.objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public DetalleFactura objeto(Objeto objeto) {
        this.setObjeto(objeto);
        return this;
    }

    public Factura getFactura() {
        return this.factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public DetalleFactura factura(Factura factura) {
        this.setFactura(factura);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetalleFactura)) {
            return false;
        }
        return id != null && id.equals(((DetalleFactura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetalleFactura{" +
            "id=" + getId() +
            ", precio=" + getPrecio() +
            ", cantidad=" + getCantidad() +
            ", total=" + getTotal() +
            ", iva=" + getIva() +
            ", valorImpuesto=" + getValorImpuesto() +
            ", estado=" + getEstado() +
            "}";
    }
}
