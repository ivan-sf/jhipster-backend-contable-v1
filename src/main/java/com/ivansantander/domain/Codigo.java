package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Codigo.
 */
@Entity
@Table(name = "codigo")
public class Codigo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bar_code")
    private Long barCode;

    @Column(name = "qr_code")
    private Long qrCode;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @JsonIgnoreProperties(value = { "precios" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Paquete paquete;

    @OneToOne
    @JoinColumn(unique = true)
    private Lote lote;

    @OneToOne
    @JoinColumn(unique = true)
    private Vencimiento vencimiento;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tipoObjeto", "usuario", "codigos", "sector" }, allowSetters = true)
    private Objeto objeto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Codigo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBarCode() {
        return this.barCode;
    }

    public Codigo barCode(Long barCode) {
        this.setBarCode(barCode);
        return this;
    }

    public void setBarCode(Long barCode) {
        this.barCode = barCode;
    }

    public Long getQrCode() {
        return this.qrCode;
    }

    public Codigo qrCode(Long qrCode) {
        this.setQrCode(qrCode);
        return this;
    }

    public void setQrCode(Long qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Codigo cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Codigo fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Paquete getPaquete() {
        return this.paquete;
    }

    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }

    public Codigo paquete(Paquete paquete) {
        this.setPaquete(paquete);
        return this;
    }

    public Lote getLote() {
        return this.lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Codigo lote(Lote lote) {
        this.setLote(lote);
        return this;
    }

    public Vencimiento getVencimiento() {
        return this.vencimiento;
    }

    public void setVencimiento(Vencimiento vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Codigo vencimiento(Vencimiento vencimiento) {
        this.setVencimiento(vencimiento);
        return this;
    }

    public Objeto getObjeto() {
        return this.objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public Codigo objeto(Objeto objeto) {
        this.setObjeto(objeto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Codigo)) {
            return false;
        }
        return id != null && id.equals(((Codigo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Codigo{" +
            "id=" + getId() +
            ", barCode=" + getBarCode() +
            ", qrCode=" + getQrCode() +
            ", cantidad=" + getCantidad() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
