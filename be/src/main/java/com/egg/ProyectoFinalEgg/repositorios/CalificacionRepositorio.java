/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.repositorios;

import com.egg.ProyectoFinalEgg.entidades.Calificacion;
import com.egg.ProyectoFinalEgg.entidades.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author micae
 */
public interface CalificacionRepositorio extends JpaRepository<Calificacion, String>{
    
   
    
}
