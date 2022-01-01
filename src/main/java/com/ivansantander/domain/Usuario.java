package com.ivansantander.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "tipo_persona")
    private String tipoPersona;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "documento")
    private String documento;

    @Column(name = "dv")
    private String dv;

    @Column(name = "estado")
    private Integer estado;

    @Column(name = "rol")
    private String rol;

    @Column(name = "rut")
    private String rut;

    @Column(name = "nombre_comercial")
    private String nombreComercial;

    @Column(name = "nit")
    private String nit;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "indicativo")
    private Integer indicativo;

    @Column(name = "telefono")
    private Integer telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "genero")
    private String genero;

    @Column(name = "edad")
    private Instant edad;

    @Column(name = "foto")
    private String foto;

    @Column(name = "descripcion")
    private Long descripcion;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @JsonIgnoreProperties(value = { "municipio", "ciudad", "departamento", "pais" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Cliente cliente;

    @JsonIgnoreProperties(value = { "trabajo" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Empleado empleado;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private Ciudad ciudad;

    @ManyToOne
    private Departamento departamento;

    @ManyToOne
    private Pais pais;

    @ManyToMany(mappedBy = "usuarios")
    @JsonIgnoreProperties(
        value = { "sucursals", "regimen", "municipio", "ciudad", "departamento", "pais", "usuarios" },
        allowSetters = true
    )
    private Set<Empresa> empresas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Usuario nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Usuario apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoPersona() {
        return this.tipoPersona;
    }

    public Usuario tipoPersona(String tipoPersona) {
        this.setTipoPersona(tipoPersona);
        return this;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public Usuario tipoDocumento(String tipoDocumento) {
        this.setTipoDocumento(tipoDocumento);
        return this;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public Usuario documento(String documento) {
        this.setDocumento(documento);
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDv() {
        return this.dv;
    }

    public Usuario dv(String dv) {
        this.setDv(dv);
        return this;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public Integer getEstado() {
        return this.estado;
    }

    public Usuario estado(Integer estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getRol() {
        return this.rol;
    }

    public Usuario rol(String rol) {
        this.setRol(rol);
        return this;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getRut() {
        return this.rut;
    }

    public Usuario rut(String rut) {
        this.setRut(rut);
        return this;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombreComercial() {
        return this.nombreComercial;
    }

    public Usuario nombreComercial(String nombreComercial) {
        this.setNombreComercial(nombreComercial);
        return this;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return this.nit;
    }

    public Usuario nit(String nit) {
        this.setNit(nit);
        return this;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Usuario direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getIndicativo() {
        return this.indicativo;
    }

    public Usuario indicativo(Integer indicativo) {
        this.setIndicativo(indicativo);
        return this;
    }

    public void setIndicativo(Integer indicativo) {
        this.indicativo = indicativo;
    }

    public Integer getTelefono() {
        return this.telefono;
    }

    public Usuario telefono(Integer telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public Usuario email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenero() {
        return this.genero;
    }

    public Usuario genero(String genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Instant getEdad() {
        return this.edad;
    }

    public Usuario edad(Instant edad) {
        this.setEdad(edad);
        return this;
    }

    public void setEdad(Instant edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return this.foto;
    }

    public Usuario foto(String foto) {
        this.setFoto(foto);
        return this;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getDescripcion() {
        return this.descripcion;
    }

    public Usuario descripcion(Long descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(Long descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Usuario fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Usuario empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    public Municipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Usuario municipio(Municipio municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public Ciudad getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public Usuario ciudad(Ciudad ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public Departamento getDepartamento() {
        return this.departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Usuario departamento(Departamento departamento) {
        this.setDepartamento(departamento);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Usuario pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    public Set<Empresa> getEmpresas() {
        return this.empresas;
    }

    public void setEmpresas(Set<Empresa> empresas) {
        if (this.empresas != null) {
            this.empresas.forEach(i -> i.removeUsuario(this));
        }
        if (empresas != null) {
            empresas.forEach(i -> i.addUsuario(this));
        }
        this.empresas = empresas;
    }

    public Usuario empresas(Set<Empresa> empresas) {
        this.setEmpresas(empresas);
        return this;
    }

    public Usuario addEmpresa(Empresa empresa) {
        this.empresas.add(empresa);
        empresa.getUsuarios().add(this);
        return this;
    }

    public Usuario removeEmpresa(Empresa empresa) {
        this.empresas.remove(empresa);
        empresa.getUsuarios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", tipoPersona='" + getTipoPersona() + "'" +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", dv='" + getDv() + "'" +
            ", estado=" + getEstado() +
            ", rol='" + getRol() + "'" +
            ", rut='" + getRut() + "'" +
            ", nombreComercial='" + getNombreComercial() + "'" +
            ", nit='" + getNit() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", indicativo=" + getIndicativo() +
            ", telefono=" + getTelefono() +
            ", email='" + getEmail() + "'" +
            ", genero='" + getGenero() + "'" +
            ", edad='" + getEdad() + "'" +
            ", foto='" + getFoto() + "'" +
            ", descripcion=" + getDescripcion() +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            "}";
    }
}
