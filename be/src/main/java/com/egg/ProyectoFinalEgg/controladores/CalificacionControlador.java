/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.controladores;
import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ServicioRepositorio;
import com.egg.ProyectoFinalEgg.servicios.CalificacionServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioProveedorServicio;

import com.egg.ProyectoFinalEgg.servicios.UsuarioServicio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author micae
 */
@Controller
@RequestMapping("/calificacion")
public class CalificacionControlador {

    @Autowired
    private CalificacionServicio calificacionServicio;
    @Autowired
    private UsuarioProveedorServicio usuarioProveedorServicio;
    @Autowired
    private ServicioRepositorio servicioRepositorio;

    @GetMapping("/resenia/{id}")
    public String resenia(@PathVariable String id, ModelMap modelo) {

        modelo.put("calificacion", calificacionServicio.getOne(id));

        return "calificacion.html";
    }

    @Autowired
    UsuarioServicio usuarioServicio;
    @PostMapping("/calificar/{id}")
    public String calificar(@RequestParam String id, Integer puntucion, String resenia, ModelMap modelo) throws MiException {

        try {
            Servicio servicio = servicioRepositorio.findByCalificacionId(id);

            calificacionServicio.calificar(id, puntucion, resenia, servicio.getProveedor().getId());

          //  usuarioServicio.actualizarHistorial(id);
          
            modelo.put("exito", "La calificación se realizo de forma exitosa");
            return "redirect:/inicio";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "calificacion.html";
        }
    }
}
