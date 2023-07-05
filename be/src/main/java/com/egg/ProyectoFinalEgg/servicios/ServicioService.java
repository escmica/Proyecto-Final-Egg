
package com.egg.ProyectoFinalEgg.servicios;

import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ServicioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioService {
    
    @Autowired
    private ServicioRepositorio servicioRepositorio;
    @Autowired

    private CalificacionServicio calificacionServicio; 
    

    @Transactional
    public void crearServicio(String descripcion, UsuarioProveedor proveedor, UsuarioSolicitante solicitante) throws MiException {
    
        validar(descripcion);
        
        Servicio servicio = new Servicio();
     
        servicio.setDescripcion(descripcion);
        servicio.setProveedor(proveedor);
        servicio.setSolicitante(solicitante);
                
        servicio.setCalificacion(calificacionServicio.crearCalif());

        servicioRepositorio.save(servicio);
    }
    
    public void editarServicio(String id, String descripcion) throws MiException {
        
        Optional<Servicio> respuesta = servicioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Servicio servicio = respuesta.get();
            servicio.setDescripcion(descripcion);
            
            servicioRepositorio.save(servicio);
        }
    }
    
    public void validar(String descripcion) throws MiException{
        
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("La descripción no puede ser nulo o estar vacío");
        }        
    }
      
    public List<Servicio> listarServicio(){
        
        List<Servicio> servicios = new ArrayList();
        
        servicios = servicioRepositorio.findAll();
        
        return servicios;        
    }
    
    public List<Servicio> listarServicioPorProveedor(String id){
        
        List<Servicio> servicios = new ArrayList();
       
        servicios = servicioRepositorio.findByProveedorId(id);
        
        return servicios;        
    }
    
    public Servicio getOne(String id){
        return servicioRepositorio.getOne(id);
    }
    
    @Transactional
    public void eliminarServicio(String id){
        Optional<Servicio> respuesta = servicioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Servicio servicio = respuesta.get();
            
            servicioRepositorio.delete(servicio);
        }
    }

    
    public List<Servicio> getServiciosByProveedorId(String idProveedor) {
        return servicioRepositorio.findByProveedorId(idProveedor);
    }
    
    public List<Servicio> getServiciosBySolicitanteId(String idSolicitante) {
        return servicioRepositorio.findBySolicitanteId(idSolicitante);
    }
    
    public Optional<Servicio> getServicioById(String id) {
        return servicioRepositorio.findById(id);
    }



}
