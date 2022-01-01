package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "caja")
    private String caja;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "iva_19")
    private Double iva19;

    @Column(name = "base_iva_19")
    private Double baseIva19;

    @Column(name = "iva_5")
    private Double iva5;

    @Column(name = "base_iva_5")
    private Double baseIva5;

    @Column(name = "iva_0")
    private Double iva0;

    @Column(name = "base_iva_0")
    private Double baseIva0;

    @Column(name = "total")
    private Integer total;

    @Column(name = "pago")
    private Double pago;

    @Column(name = "saldo")
    private Double saldo;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private Instant fechaActualizacion;

    @JsonIgnoreProperties(value = { "regimen", "municipio", "ciudad", "departamento", "pais", "empresa" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Sucursal sucursal;

    @OneToOne
    @JoinColumn(unique = true)
    private TipoFactura tipoFactura;

    @OneToOne
    @JoinColumn(unique = true)
    private NotaContable notaContable;

    @OneToMany(mappedBy = "factura")
    @JsonIgnoreProperties(value = { "objeto", "factura" }, allowSetters = true)
    private Set<DetalleFactura> detalleFacturas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "municipio", "ciudad", "departamento", "pais" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne
    @JsonIgnoreProperties(value = { "trabajo" }, allowSetters = true)
    private Empleado empleadp;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Factura id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Factura numero(Integer numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCaja() {
        return this.caja;
    }

    public Factura caja(String caja) {
        this.setCaja(caja);
        return this;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public Factura estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Double getIva19() {
        return this.iva19;
    }

    public Factura iva19(Double iva19) {
        this.setIva19(iva19);
        return this;
    }

    public void setIva19(Double iva19) {
        this.iva19 = iva19;
    }

    public Double getBaseIva19() {
        return this.baseIva19;
    }

    public Factura baseIva19(Double baseIva19) {
        this.setBaseIva19(baseIva19);
        return this;
    }

    public void setBaseIva19(Double baseIva19) {
        this.baseIva19 = baseIva19;
    }

    public Double getIva5() {
        return this.iva5;
    }

    public Factura iva5(Double iva5) {
        this.setIva5(iva5);
        return this;
    }

    public void setIva5(Double iva5) {
        this.iva5 = iva5;
    }

    public Double getBaseIva5() {
        return this.baseIva5;
    }

    public Factura baseIva5(Double baseIva5) {
        this.setBaseIva5(baseIva5);
        return this;
    }

    public void setBaseIva5(Double baseIva5) {
        this.baseIva5 = baseIva5;
    }

    public Double getIva0() {
        return this.iva0;
    }

    public Factura iva0(Double iva0) {
        this.setIva0(iva0);
        return this;
    }

    public void setIva0(Double iva0) {
        this.iva0 = iva0;
    }

    public Double getBaseIva0() {
        return this.baseIva0;
    }

    public Factura baseIva0(Double baseIva0) {
        this.setBaseIva0(baseIva0);
        return this;
    }

    public void setBaseIva0(Double baseIva0) {
        this.baseIva0 = baseIva0;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Factura total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Double getPago() {
        return this.pago;
    }

    public Factura pago(Double pago) {
        this.setPago(pago);
        return this;
    }

    public void setPago(Double pago) {
        this.pago = pago;
    }

    public Double getSaldo() {
        return this.saldo;
    }

    public Factura saldo(Double saldo) {
        this.setSaldo(saldo);
        return this;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Factura fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Instant getFechaActualizacion() {
        return this.fechaActualizacion;
    }

    public Factura fechaActualizacion(Instant fechaActualizacion) {
        this.setFechaActualizacion(fechaActualizacion);
        return this;
    }

    public void setFechaActualizacion(Instant fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Sucursal getSucursal() {
        return this.sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Factura sucursal(Sucursal sucursal) {
        this.setSucursal(sucursal);
        return this;
    }

    public TipoFactura getTipoFactura() {
        return this.tipoFactura;
    }

    public void setTipoFactura(TipoFactura tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public Factura tipoFactura(TipoFactura tipoFactura) {
        this.setTipoFactura(tipoFactura);
        return this;
    }

    public NotaContable getNotaContable() {
        return this.notaContable;
    }

    public void setNotaContable(NotaContable notaContable) {
        this.notaContable = notaContable;
    }

    public Factura notaContable(NotaContable notaContable) {
        this.setNotaContable(notaContable);
        return this;
    }

    public Set<DetalleFactura> getDetalleFacturas() {
        return this.detalleFacturas;
    }

    public void setDetalleFacturas(Set<DetalleFactura> detalleFacturas) {
        if (this.detalleFacturas != null) {
            this.detalleFacturas.forEach(i -> i.setFactura(null));
        }
        if (detalleFacturas != null) {
            detalleFacturas.forEach(i -> i.setFactura(this));
        }
        this.detalleFacturas = detalleFacturas;
    }

    public Factura detalleFacturas(Set<DetalleFactura> detalleFacturas) {
        this.setDetalleFacturas(detalleFacturas);
        return this;
    }

    public Factura addDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFacturas.add(detalleFactura);
        detalleFactura.setFactura(this);
        return this;
    }

    public Factura removeDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFacturas.remove(detalleFactura);
        detalleFactura.setFactura(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Factura cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Empleado getEmpleadp() {
        return this.empleadp;
    }

    public void setEmpleadp(Empleado empleado) {
        this.empleadp = empleado;
    }

    public Factura empleadp(Empleado empleado) {
        this.setEmpleadp(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", caja='" + getCaja() + "'" +
            ", estado=" + getEstado() +
            ", iva19=" + getIva19() +
            ", baseIva19=" + getBaseIva19() +
            ", iva5=" + getIva5() +
            ", baseIva5=" + getBaseIva5() +
            ", iva0=" + getIva0() +
            ", baseIva0=" + getBaseIva0() +
            ", total=" + getTotal() +
            ", pago=" + getPago() +
            ", saldo=" + getSaldo() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", fechaActualizacion='" + getFechaActualizacion() + "'" +
            "}";
    }
}
