package com.egg.ProyectoFinalEgg.controladores;

import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.Usuario;
import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.servicios.ServicioService;
import com.egg.ProyectoFinalEgg.servicios.UsuarioServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioSolicitanteServicio;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/solicitante")
public class SolicitanteControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioSolicitanteServicio usuarioSolicitanteServicio;

    @GetMapping("/inicio")
    public String index() {
        return "index.html";
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {

        List<UsuarioSolicitante> usuarios = usuarioSolicitanteServicio.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "listar_usuario.html";
    }

//    @GetMapping("/configuracion/{id}")
//    public String configUsuario(@PathVariable String id, ModelMap model) {
//           model.put("solicitante", usuarioSolicitanteServicio.getOne(id));
//        
//        return "config_solicitante.html";
//
//    }
//
//    @PostMapping("/editar/{id}")
//    public String editarSoli(@RequestParam MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email) throws MiException {
//
//        usuarioSolicitanteServicio.editarUsuarioSolicitante(archivo, id, nombre, apellido, email);
//
//        return "index.html";
//    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        usuarioSolicitanteServicio.eliminar(id);
        return "redirect:/";
    }

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/inicio/calificar/{id}")
    public String mostrarServicios(@PathVariable("id") String idSolicitante, Model model) {
        List<Servicio> servicios = servicioService.getServiciosBySolicitanteId(idSolicitante);
        Iterator<Servicio> iter = servicios.iterator();
        while (iter.hasNext()) {
            Servicio servicio = iter.next();
            if (!servicio.getAceptado() || servicio.getCalificacion().getPuntucion() != null) {
                iter.remove();
            }
        }
        model.addAttribute("servicios", servicios);
        return "listar_calificacion.html";
    }

}
