/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.repositorios;

import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioProveedorRepositorio extends JpaRepository<UsuarioProveedor, String> {

    @Query("select u from UsuarioProveedor u where u.profesion = :profesion")
    public List<UsuarioProveedor> buscarPorProfesion(@Param("profesion") String profesion);
    
   @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public UsuarioProveedor buscarPorEmail(@Param("email")String email);  


    }
