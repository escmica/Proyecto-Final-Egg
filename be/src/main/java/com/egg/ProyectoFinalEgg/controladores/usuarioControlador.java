package com.egg.ProyectoFinalEgg.controladores;

import com.egg.ProyectoFinalEgg.entidades.Usuario;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioRepositorio;
import com.egg.ProyectoFinalEgg.servicios.ImagenServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioProveedorServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioSolicitanteServicio;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/")
public class usuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioSolicitanteServicio solicitanteServicio;
    @Autowired
    private UsuarioProveedorServicio proveedorServicio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    public UsuarioRepositorio usuarioRepositorio;
    @Autowired
    public UsuarioProveedorServicio usuarioProveedorServicio;
    @Autowired
    public UsuarioSolicitanteServicio usuarioSolicitanteServicio;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {

        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "usuario_form.html";
    }

    @PostMapping("/registroP")

    public String registroPro(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2, @RequestParam String descripcion, @RequestParam String contacto,
            @RequestParam String profesion, ModelMap modelo, MultipartFile archivo) throws MiException, IOException {

        if (password.equals(password2)) {
            try {
                proveedorServicio.registrarProveedor(archivo, nombre, apellido, email,
                        password, password2, descripcion,
                        contacto, profesion);

                return "exito_usuario.html";
            } catch (MiException ex) {
                modelo.put("error", ex.getMessage());
                modelo.put("nombre", nombre);
                modelo.put("apellido", apellido);
                modelo.put("email", email);
                return "usuario_form.html";
            }
        } else {
            return "error_usuario.html";
        }

    }

    @PostMapping("/registroSoli")

    public String registroSoli(@RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, ModelMap modelo, MultipartFile archivo) throws MiException, IOException {

        if (password.equals(password2)) {
            try {
                solicitanteServicio.registroSolicitante(archivo, nombre, apellido, email, password, password2);

                return "exito_usuario.html";
            } catch (MiException ex) {
                modelo.put("error", ex.getMessage());
                modelo.put("nombre", nombre);
                modelo.put("apellido", apellido);
                modelo.put("email", email);
                return "usuario_form.html";
            }
        } else {
            return "error_usuario.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o Contraseña invalidos!");
        }
        return "login_usuario.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PROVEEDOR', 'ROLE_ADMIN', 'ROLE_SOLICITANTE')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        } else if (logueado.getRol().toString().equals("PROVEEDOR")) {
            return "redirect:/proveedor/inicio";
        } else if (logueado.getRol().toString().equals("SOLICITANTE")) {
            return "redirect:/solicitante/inicio";
        }
        return "vistaPanelAdmin.html";
    }

    @GetMapping("/config/{id}")
    public String configUsuario(HttpSession session, Model model, @PathVariable String id) {

        model.addAttribute("usuarios", usuarioServicio.getOne(id));
        model.addAttribute("usuarioP", usuarioProveedorServicio.getOne(id));
        return "config_usuario.html";

    }

    @PostMapping("/editar/{id}")

    public String editarSoli(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, String email,
            @RequestParam(required = false) String descripcion, @RequestParam(required = false) String contacto, @RequestParam(required = false) String profesion, ModelMap modelo,
            MultipartFile archivo, String password, String password2) throws MiException {

        try {
            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                System.out.println("Hola");
                if (usuario.getRol().toString().equals("SOLICITANTE")) {
                    if (archivo != null && !archivo.isEmpty()) {
                        usuarioSolicitanteServicio.editarUsuarioSolicitante(archivo, id, nombre, apellido, email);
                    } else {
                        System.out.println("Hola1");
                        usuarioSolicitanteServicio.editarSolicitanteSinImagen(id, nombre, apellido, email);
                    }

                } else {

                    if (usuario.getRol().toString().equals("PROVEEDOR")) {
                        if (archivo != null && !archivo.isEmpty()) {
                            usuarioProveedorServicio.editarUsuario(archivo, nombre, apellido, id, email, descripcion, contacto, profesion);
                        } else {
                            usuarioProveedorServicio.editarUsuarioSinImagen(nombre, apellido, id, email, descripcion, contacto, profesion);
                        }
                    }

                }
            }

            System.out.println("Hola3");

            modelo.put("exito", "El Perfil se ha editado correctamente");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "config_usuario.html";
        }

    }

    @PostMapping("/editarC/{id}")
    public String ActualizarContraseña(@PathVariable String id, @RequestParam String password, @RequestParam String password2, @RequestParam String Actual) {
       Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRol().toString().equals("SOLICITANTE")) {

                String actualEncriptada = new BCryptPasswordEncoder().encode(Actual);
                if (passwordEncoder.matches(Actual, usuario.getPassword())) {
                    if (password.equals(password2)) {
                        usuarioSolicitanteServicio.actualizarContraseña(id, password);
                    }
                }
            } else {
                if (usuario.getRol().toString().equals("PROVEEDOR")) {
                    String actualEncriptada = new BCryptPasswordEncoder().encode(Actual);
                    if (passwordEncoder.matches(Actual, usuario.getPassword())) {
                        if (password.equals(password2)) {
                            usuarioProveedorServicio.actualizarContraseña(id, password);
                        }
                    }
                }

            }
        }
        return "index.html";
    }

    //Inicio Servicios Cartas 
    @GetMapping("/albanileria")
    public String albanileria(HttpSession session
    ) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            return "redirect:/proveedor/albanileria";
        } else {
            return "login_usuario.html";
        }
    }

    @GetMapping("/carpinteria")
    public String carpinteria(HttpSession session
    ) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            return "redirect:/proveedor/carpinteria";
        } else {
            return "login_usuario.html";
        }
    }

    @GetMapping("/limpieza")
    public String limpieza(HttpSession session
    ) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            return "redirect:/proveedor/limpieza";
        } else {
            return "login_usuario.html";
        }
    }

    @GetMapping("/herreria")
    public String herreria(HttpSession session
    ) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            return "redirect:/proveedor/herreria";
        } else {
            return "login_usuario.html";
        }
    }

    @GetMapping("/jardineria")
    public String jardineria(HttpSession session
    ) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            return "redirect:/proveedor/jardineria";
        } else {
            return "login_usuario.html";
        }
    }

    @GetMapping("/plomeria")
    public String plomeria(HttpSession session
    ) {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            return "redirect:/proveedor/plomeria";
        } else {
            return "login_usuario.html";
        }
    }

}
