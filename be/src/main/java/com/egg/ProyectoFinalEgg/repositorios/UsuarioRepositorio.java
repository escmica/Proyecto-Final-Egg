/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.repositorios;

import com.egg.ProyectoFinalEgg.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    @Query("select u from Usuario u where u.nombre = :nombre")

    public Usuario buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    
    public Usuario buscarPorEmail(@Param("email") String email);

    @Query("select u from Usuario u order by u.nombre asc")
    public List<Usuario> ordenarPorNombreAsc();

    @Query("SELECT u from Usuario u WHERE u.nombre LIKE %?1%"
            + " OR u.email LIKE %?1%"
            + " OR u.rol LIKE %?1%")
    public List<Usuario> findAll(String palabraClave);

}