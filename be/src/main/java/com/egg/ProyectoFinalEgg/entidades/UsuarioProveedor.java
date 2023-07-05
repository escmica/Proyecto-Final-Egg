
package com.egg.ProyectoFinalEgg.entidades;

import com.egg.ProyectoFinalEgg.enumeradores.Rol;
import java.util.List;
import javax.persistence.Entity;

import javax.persistence.Table;


@Entity
@Table(name = "proveedor")
public class UsuarioProveedor extends Usuario{
        
        
    protected double promedio;
    protected String descripcion;
    protected String contacto;
    protected String foto;
    protected boolean disponible;
    protected String profesion;
    
    public UsuarioProveedor() {
    }

    public UsuarioProveedor(double promedio, String descripcion, String contacto, String foto, boolean disponible, String profesion, String id, String nombre, String apellido, String email, String password, List<Servicio> servicios, Rol rol) {
        super(id, nombre, apellido, email, password, servicios, rol);
        this.promedio = promedio;
        this.descripcion = descripcion;
        this.contacto = contacto;
        this.foto = foto;
        this.disponible = disponible;
        this.profesion = profesion;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    
}