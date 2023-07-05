
package com.egg.ProyectoFinalEgg.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 *
 * @author facup
 */
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String descripcion;

    @OneToOne
    private UsuarioProveedor proveedor;
    @OneToOne
    private UsuarioSolicitante solicitante;

    @OneToOne
    private Calificacion calificacion;
    
    private boolean aceptado; 

    public Servicio() {
        aceptado = false;
    }

    public Servicio(String id, String descripcion, UsuarioProveedor proveedor, UsuarioSolicitante solicitante, Calificacion calificacion) {
        this.id = id;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.solicitante = solicitante;
        this.calificacion = calificacion;
        this.aceptado = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public UsuarioProveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(UsuarioProveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }

    public UsuarioSolicitante getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(UsuarioSolicitante solicitante) {
        this.solicitante = solicitante;
    }

    public boolean getAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }
    
    


}




