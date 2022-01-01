package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Objeto.
 */
@Entity
@Table(name = "objeto")
public class Objeto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "codigo_dian")
    private String codigoDian;

    @Column(name = "detalle_objeto")
    private Long detalleObjeto;

    @Column(name = "vencimiento")
    private Instant vencimiento;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @OneToOne
    @JoinColumn(unique = true)
    private TipoObjeto tipoObjeto;

    @JsonIgnoreProperties(value = { "cliente", "empleado", "municipio", "ciudad", "departamento", "pais", "empresas" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "objeto")
    @JsonIgnoreProperties(value = { "paquete", "lote", "vencimiento", "objeto" }, allowSetters = true)
    private Set<Codigo> codigos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "objetos", "inventario" }, allowSetters = true)
    private Sector sector;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Objeto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Objeto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Objeto cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoDian() {
        return this.codigoDian;
    }

    public Objeto codigoDian(String codigoDian) {
        this.setCodigoDian(codigoDian);
        return this;
    }

    public void setCodigoDian(String codigoDian) {
        this.codigoDian = codigoDian;
    }

    public Long getDetalleObjeto() {
        return this.detalleObjeto;
    }

    public Objeto detalleObjeto(Long detalleObjeto) {
        this.setDetalleObjeto(detalleObjeto);
        return this;
    }

    public void setDetalleObjeto(Long detalleObjeto) {
        this.detalleObjeto = detalleObjeto;
    }

    public Instant getVencimiento() {
        return this.vencimiento;
    }

    public Objeto vencimiento(Instant vencimiento) {
        this.setVencimiento(vencimiento);
        return this;
    }

    public void setVencimiento(Instant vencimiento) {
        this.vencimiento = vencimiento;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Objeto fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public TipoObjeto getTipoObjeto() {
        return this.tipoObjeto;
    }

    public void setTipoObjeto(TipoObjeto tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public Objeto tipoObjeto(TipoObjeto tipoObjeto) {
        this.setTipoObjeto(tipoObjeto);
        return this;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Objeto usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Set<Codigo> getCodigos() {
        return this.codigos;
    }

    public void setCodigos(Set<Codigo> codigos) {
        if (this.codigos != null) {
            this.codigos.forEach(i -> i.setObjeto(null));
        }
        if (codigos != null) {
            codigos.forEach(i -> i.setObjeto(this));
        }
        this.codigos = codigos;
    }

    public Objeto codigos(Set<Codigo> codigos) {
        this.setCodigos(codigos);
        return this;
    }

    public Objeto addCodigo(Codigo codigo) {
        this.codigos.add(codigo);
        codigo.setObjeto(this);
        return this;
    }

    public Objeto removeCodigo(Codigo codigo) {
        this.codigos.remove(codigo);
        codigo.setObjeto(null);
        return this;
    }

    public Sector getSector() {
        return this.sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public Objeto sector(Sector sector) {
        this.setSector(sector);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Objeto)) {
            return false;
        }
        return id != null && id.equals(((Objeto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Objeto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", cantidad=" + getCantidad() +
            ", codigoDian='" + getCodigoDian() + "'" +
            ", detalleObjeto=" + getDetalleObjeto() +
            ", vencimiento='" + getVencimiento() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
