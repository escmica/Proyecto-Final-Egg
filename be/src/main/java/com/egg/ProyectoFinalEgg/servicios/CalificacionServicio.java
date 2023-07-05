/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.servicios;

import com.egg.ProyectoFinalEgg.entidades.Calificacion;
import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.CalificacionRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.ServicioRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioProveedorRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioSolicitanteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author micae
 */
@Service
public class CalificacionServicio {

    @Autowired
    CalificacionRepositorio calificacionRepositorio;
    @Autowired
    UsuarioProveedorRepositorio proveedorRepositorio;
    @Autowired
    UsuarioSolicitanteRepositorio solicitanteRepositorio;
    @Autowired
    ServicioRepositorio servicioRepositorio;

   
    @Transactional
    public Calificacion crearCalif() {
        Calificacion calificacion = new Calificacion();
        calificacionRepositorio.save(calificacion);

        return calificacion;
    }

    @Transactional
    public void calificar(String id, Integer puntucion, String resenia, String id_proveedor) throws MiException {
        validar(puntucion, resenia);
        
        Optional<Calificacion> respuesta = calificacionRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Calificacion calificacion = respuesta.get();
            calificacion.setPuntucion(puntucion);
            calificacion.setResenia(resenia);

            calificacionRepositorio.save(calificacion);

        }
        calculoPromedio(id_proveedor);
    }

    @Transactional
    public void calculoPromedio(String id_proveedor) {

        double puntosTotales = 0;
        double contadorCalif = 0;
        double promedio;

        Optional<UsuarioProveedor> respuesta = proveedorRepositorio.findById(id_proveedor);

        if (respuesta.isPresent()) {
            UsuarioProveedor proveedor = respuesta.get();

            List<Servicio> servicios = servicioRepositorio.findByProveedorId(id_proveedor);

            for (Servicio servicio : servicios) {
                Integer puntuacion = servicio.getCalificacion().getPuntucion();
                if (puntuacion != null) {
                    puntosTotales += puntuacion;
                    contadorCalif++;
                }
            }

            promedio = Math.round((puntosTotales / contadorCalif) * 10.0) / 10.0;

            proveedor.setPromedio(promedio);

            proveedorRepositorio.save(proveedor);
        }
    }

    public void editarServicio(String id, String promedio, String resenia) throws MiException {
        Optional<Calificacion> resp = calificacionRepositorio.findById(id);
        if (resp.isPresent()) {
            Calificacion calificacion = resp.get();
            calificacion.setResenia(resenia);

            calificacionRepositorio.save(calificacion);
        }
    }
    
    public void validar(Integer puntuacion, String resenia) throws MiException{
        
//        if (puntuacion == null) {
//            throw new MiException("La puntuacion no puede estar vacío");
//        }   
        if (resenia.isEmpty() || resenia == null) {
            throw new MiException("La reseña no puede ser nulo o estar vacío");
        } 
    }

    public List<Calificacion> listarCalifiaciones() {
        List<Calificacion> Calificaciones = new ArrayList();
        Calificaciones = calificacionRepositorio.findAll();
        return Calificaciones;
    }

    public void eliminarCalifiacion(String id) {
        Optional<Calificacion> resp = calificacionRepositorio.findById(id);

        if (resp.isPresent()) {
            Calificacion calificacion = resp.get();

            calificacionRepositorio.delete(calificacion);
        }
    }

    @Transactional(readOnly = true)
    public Calificacion getOne(String id) {
        return calificacionRepositorio.getOne(id);
    }

}
