package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Empresa.
 */
@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "nombre_comercial")
    private String nombreComercial;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "documento")
    private String documento;

    @Column(name = "dv")
    private String dv;

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

    @Column(name = "estado")
    private Integer estado;

    @OneToMany(mappedBy = "empresa")
    @JsonIgnoreProperties(value = { "regimen", "municipio", "ciudad", "departamento", "pais", "empresa" }, allowSetters = true)
    private Set<Sucursal> sucursals = new HashSet<>();

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

    @ManyToMany
    @JoinTable(
        name = "rel_empresa__usuario",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnoreProperties(value = { "cliente", "empleado", "municipio", "ciudad", "departamento", "pais", "empresas" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empresa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public Empresa razonSocial(String razonSocial) {
        this.setRazonSocial(razonSocial);
        return this;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return this.nombreComercial;
    }

    public Empresa nombreComercial(String nombreComercial) {
        this.setNombreComercial(nombreComercial);
        return this;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public Empresa tipoDocumento(String tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public Empresa documento(String documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDv() {
        return this.dv;
    }

    public Empresa dv(String dv) {
        this.setDv(dv);
        return this;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Empresa direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getIndicativo() {
        return this.indicativo;
    }

    public Empresa indicativo(Integer indicativo) {
        this.setIndicativo(indicativo);
        return this;
    }

    public void setIndicativo(Integer indicativo) {
        this.indicativo = indicativo;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public Empresa telefono(Integer telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public Empresa email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return this.logo;
    }

    public Empresa logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getResolucionFacturas() {
        return this.resolucionFacturas;
    }

    public Empresa resolucionFacturas(String resolucionFacturas) {
        this.setResolucionFacturas(resolucionFacturas);
        return this;
    }

    public void setResolucionFacturas(String resolucionFacturas) {
        this.resolucionFacturas = resolucionFacturas;
    }

    public String getPrefijoInicial() {
        return this.prefijoInicial;
    }

    public Empresa prefijoInicial(String prefijoInicial) {
        this.setPrefijoInicial(prefijoInicial);
        return this;
    }

    public void setPrefijoInicial(String prefijoInicial) {
        this.prefijoInicial = prefijoInicial;
    }

    public String getPrefijoFinal() {
        return this.prefijoFinal;
    }

    public Empresa prefijoFinal(String prefijoFinal) {
        this.setPrefijoFinal(prefijoFinal);
        return this;
    }

    public void setPrefijoFinal(String prefijoFinal) {
        this.prefijoFinal = prefijoFinal;
    }

    public String getPrefijoActual() {
        return this.prefijoActual;
    }

    public Empresa prefijoActual(String prefijoActual) {
        this.setPrefijoActual(prefijoActual);
        return this;
    }

    public void setPrefijoActual(String prefijoActual) {
        this.prefijoActual = prefijoActual;
    }

    public Long getDescripcion() {
        return this.descripcion;
    }

    public Empresa descripcion(Long descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(Long descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Empresa fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public Empresa estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Set<Sucursal> getSucursals() {
        return this.sucursals;
    }

    public void setSucursals(Set<Sucursal> sucursals) {
        if (this.sucursals != null) {
            this.sucursals.forEach(i -> i.setEmpresa(null));
        }
        if (sucursals != null) {
            sucursals.forEach(i -> i.setEmpresa(this));
        }
        this.sucursals = sucursals;
    }

    public Empresa sucursals(Set<Sucursal> sucursals) {
        this.setSucursals(sucursals);
        return this;
    }

    public Empresa addSucursal(Sucursal sucursal) {
        this.sucursals.add(sucursal);
        sucursal.setEmpresa(this);
        return this;
    }

    public Empresa removeSucursal(Sucursal sucursal) {
        this.sucursals.remove(sucursal);
        sucursal.setEmpresa(null);
        return this;
    }

    public Regimen getRegimen() {
        return this.regimen;
    }

    public void setRegimen(Regimen regimen) {
        this.regimen = regimen;
    }

    public Empresa regimen(Regimen regimen) {
        this.setRegimen(regimen);
        return this;
    }

    public Municipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Empresa municipio(Municipio municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public Ciudad getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Empresa ciudad(Ciudad ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Empresa departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Empresa pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Empresa usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Empresa addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getEmpresas().add(this);
        return this;
    }

    public Empresa removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getEmpresas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empresa)) {
            return false;
        }
        return id != null && id.equals(((Empresa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empresa{" +
            "id=" + getId() +
            ", razonSocial='" + getRazonSocial() + "'" +
            ", nombreComercial='" + getNombreComercial() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", dv='" + getDv() + "'" +
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
            ", estado=" + getEstado() +
            "}";
    }
}
