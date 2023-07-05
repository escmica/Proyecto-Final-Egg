package com.egg.ProyectoFinalEgg.controladores;

import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ServicioRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioProveedorRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioSolicitanteRepositorio;
import com.egg.ProyectoFinalEgg.servicios.ServicioService;
import com.egg.ProyectoFinalEgg.servicios.UsuarioProveedorServicio;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/servicio")
public class ServicioControlador {

    @Autowired
    private ServicioService servicioService;
    @Autowired
    private UsuarioProveedorServicio usuarioProveedorServicio;
    @Autowired
    private UsuarioProveedorRepositorio usuarioProveedorRepositorio;
    @Autowired
    private UsuarioSolicitanteRepositorio usuarioSolicitanteRepositorio;

    @GetMapping("/solicitar/{id}")
    public String solicitarServicio(@PathVariable String id, ModelMap modelo) {

        modelo.put("proveedor", usuarioProveedorServicio.getOne(id));

        return "servicio_form.html";
    }

    @PostMapping("/solicitar/{id}")
    public String crearServicio(HttpSession session, @RequestParam String id_proveedor, @RequestParam String id_solicitante, 
            @RequestParam String descripcion, ModelMap modelo) throws MiException {

        try {
            Optional<UsuarioSolicitante> respuestaSoli = usuarioSolicitanteRepositorio.findById(id_solicitante);
            UsuarioSolicitante solicitante = respuestaSoli.get();

            Optional<UsuarioProveedor> respuesta = usuarioProveedorRepositorio.findById(id_proveedor);
            UsuarioProveedor proveedor = respuesta.get();

            servicioService.crearServicio(descripcion, proveedor, solicitante);

            modelo.put("exito", "La solicitud ha sido realizada");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("descripcion", descripcion);
            return "servicio_form.html";
        }
        
    }

    @GetMapping("/editar/{id}")
    public String editarServicio(@PathVariable String id, ModelMap modelo) {

        modelo.put("servicio", servicioService.getOne(id));

        return "servicio_mod.html";
    }

    @PostMapping("/editar/{id}")
    public String editarServicio(@PathVariable String id, @RequestParam String descripcion, ModelMap modelo) throws MiException {
        try {
            servicioService.editarServicio(id, descripcion);

            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("descripcion", descripcion);
            return "servicio_mod.html";
        }
        
    }

    @GetMapping("/listar")
    public String mostrarServicio(ModelMap modelo) {

        List<Servicio> servicios = servicioService.listarServicio();

        modelo.addAttribute("servicios", servicios);

        return "listar_servicio.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarServicio(@PathVariable String id) {

        servicioService.eliminarServicio(id);

        return "listar_servico.html";
    }
    
    @Autowired
    ServicioRepositorio servicioRepositorio;
    
    @PostMapping("/aceptar/{id}")
    public String aceptarServicio(@PathVariable String id) {
        Optional<Servicio> respuesta = servicioRepositorio.findById(id);
        String idProv = respuesta.get().getProveedor().getId();
        String redirect = "redirect:/proveedor/solicitudes/" + idProv;

        if (respuesta.isPresent()) {
            Servicio servicio= respuesta.get();
            servicio.setAceptado(true);
            servicioRepositorio.save(servicio);
        }
        return redirect;  
    }
    
    @PostMapping("/rechazar/{id}")
    public String rechazarServicio(@PathVariable String id) {
        Optional<Servicio> respuesta = servicioRepositorio.findById(id);
        String idProv = respuesta.get().getProveedor().getId();
        String redirect = "redirect:/proveedor/solicitudes/" + idProv;

        if (respuesta.isPresent()) {
            Servicio servicio= respuesta.get();
            
            servicioRepositorio.delete(servicio);
        }
        return redirect;  
    }
    
    


    

}
