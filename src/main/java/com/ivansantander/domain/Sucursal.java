package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Sucursal.
 */
@Entity
@Table(name = "sucursal")
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "indicativo")
    private Integer indicativo;

    @Column(name = "telefono")
    private Integer telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "logo")
    private String logo;

    @Column(name = "resolucion_facturas")
    private String resolucionFacturas;

    @Column(name = "prefijo_inicial")
    private String prefijoInicial;

    @Column(name = "prefijo_final")
    private String prefijoFinal;

    @Column(name = "prefijo_actual")
    private String prefijoActual;

    @Column(name = "descripcion")
    private Long descripcion;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @ManyToOne
    private Regimen regimen;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private Ciudad ciudad;

    @ManyToOne
    private Departamento departamento;

    @ManyToOne
    private Pais pais;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "sucursals", "regimen", "municipio", "ciudad", "departamento", "pais", "usuarios" },
        allowSetters = true
    )
    private Empresa empresa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sucursal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Sucursal nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Sucursal direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getIndicativo() {
        return this.indicativo;
    }

    public Sucursal indicativo(Integer indicativo) {
        this.setIndicativo(indicativo);
        return this;
    }

    public void setIndicativo(Integer indicativo) {
        this.indicativo = indicativo;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public Sucursal telefono(Integer telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public Sucursal email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return this.logo;
    }

    public Sucursal logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getResolucionFacturas() {
        return this.resolucionFacturas;
    }

    public Sucursal resolucionFacturas(String resolucionFacturas) {
        this.setResolucionFacturas(resolucionFacturas);
        return this;
    }

    public void setResolucionFacturas(String resolucionFacturas) {
        this.resolucionFacturas = resolucionFacturas;
    }

    public String getPrefijoInicial() {
        return this.prefijoInicial;
    }

    public Sucursal prefijoInicial(String prefijoInicial) {
        this.setPrefijoInicial(prefijoInicial);
        return this;
    }

    public void setPrefijoInicial(String prefijoInicial) {
        this.prefijoInicial = prefijoInicial;
    }

    public String getPrefijoFinal() {
        return this.prefijoFinal;
    }

    public Sucursal prefijoFinal(String prefijoFinal) {
        this.setPrefijoFinal(prefijoFinal);
        return this;
    }

    public void setPrefijoFinal(String prefijoFinal) {
        this.prefijoFinal = prefijoFinal;
    }

    public String getPrefijoActual() {
        return this.prefijoActual;
    }

    public Sucursal prefijoActual(String prefijoActual) {
        this.setPrefijoActual(prefijoActual);
        return this;
    }

    public void setPrefijoActual(String prefijoActual) {
        this.prefijoActual = prefijoActual;
    }

    public Long getDescripcion() {
        return this.descripcion;
    }

    public Sucursal descripcion(Long descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(Long descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Sucursal fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Regimen getRegimen() {
        return this.regimen;
    }

    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }

    public Sucursal regimen(Regimen regimen) {
        this.setRegimen(regimen);
        return this;
    }

    public Municipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Sucursal municipio(Municipio municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public Ciudad getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Sucursal ciudad(Ciudad ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Sucursal departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Sucursal pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    public Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Sucursal empresa(Empresa empresa) {
        this.setEmpresa(empresa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sucursal)) {
            return false;
        }
        return id != null && id.equals(((Sucursal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sucursal{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", indicativo=" + getIndicativo() +
            ", telefono=" + getTelefono() +
            ", email='" + getEmail() + "'" +
            ", logo='" + getLogo() + "'" +
            ", resolucionFacturas='" + getResolucionFacturas() + "'" +
            ", prefijoInicial='" + getPrefijoInicial() + "'" +
            ", prefijoFinal='" + getPrefijoFinal() + "'" +
            ", prefijoActual='" + getPrefijoActual() + "'" +
            ", descripcion=" + getDescripcion() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
