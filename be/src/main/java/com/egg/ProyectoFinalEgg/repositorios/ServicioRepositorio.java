
package com.egg.ProyectoFinalEgg.repositorios;

import com.egg.ProyectoFinalEgg.entidades.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ServicioRepositorio extends JpaRepository<Servicio, String>{

  
    
    @Query("SELECT s FROM Servicio s WHERE s.proveedor.id = :idProveedor")
    List<Servicio> findByProveedorId(@Param("idProveedor") String idProveedor);
    
    @Query("SELECT s FROM Servicio s WHERE s.solicitante.id = :idSolicitante")
    List<Servicio> findBySolicitanteId(@Param("idSolicitante") String idSolicitante);

    @Query("SELECT s FROM Servicio s WHERE s.calificacion.id = :idCalificacion")
    public Servicio findByCalificacionId(@Param("idCalificacion") String idCalificacion);

    
}
