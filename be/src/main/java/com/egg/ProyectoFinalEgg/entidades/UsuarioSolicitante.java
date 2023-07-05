/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.entidades;

import com.egg.ProyectoFinalEgg.enumeradores.Rol;
import java.util.List;
import javax.persistence.Entity;

import javax.persistence.Table;


/**
 *
 * @author facup
 */
@Entity
@Table(name = "solicitante")
//@PrimaryKeyJoinColumn(name = "id_usuario")
public class UsuarioSolicitante extends Usuario{
    
   
    public UsuarioSolicitante() {
    }

    public UsuarioSolicitante(String id, String nombre, String apellido, String email, String password, List<Servicio> servicios, Rol rol) {
        super(id, nombre, apellido, email, password, servicios, rol);
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