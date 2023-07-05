
package com.egg.ProyectoFinalEgg.repositorios;

import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioSolicitanteRepositorio extends JpaRepository<UsuarioSolicitante, String> {
    
    @Query("select u from Usuario u where u.nombre = :nombre")
    public UsuarioSolicitante buscarPorNombre(@Param("nombre") String nombre);
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public UsuarioSolicitante buscarPorEmail(@Param("email")String email);    
    @Query("select u from Usuario u order by u.nombre asc")
    public List<UsuarioSolicitante> ordenarPorNombreAsc();
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'SOLICITANTE'")
    public List<UsuarioSolicitante> buscarPorRol();    
}