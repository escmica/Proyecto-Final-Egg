/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.controladores;

import com.egg.ProyectoFinalEgg.entidades.Usuario;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioRepositorio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioProveedorServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioSolicitanteServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    public UsuarioServicio usuarioServicio;
    @Autowired
    public UsuarioRepositorio usuarioRepositorio;
    @Autowired
    public UsuarioProveedorServicio usuarioProveedorServicio;
    @Autowired
    public UsuarioSolicitanteServicio usuarioSolicitanteServicio;

    @GetMapping("/dashboard")
    public String inicio() {

        return "index.html";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model, @Param("palabraClave") String palabraClave) {

        List<Usuario> usuarios = usuarioServicio.listarUsuarios(palabraClave);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("palabraClave", palabraClave);

        return "listar_usuario.html";
    }

    @GetMapping("/new")
    public String agregarUsuarios(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario_form.html";
    }

    @PostMapping("/save")
    public String save(@Validated Usuario u, Model model) {
        usuarioServicio.save(u);

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, ModelMap model) {
                
//        model.put("usuarioP", usuarioProveedorServicio.getOne(id));
//        model.put("usuarioS", usuarioSolicitanteServicio.getOne(id));
//        
//        return "modulo_ususario";
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            if (usuario.getRol().toString().equals("SOLICITANTE")){
                
                model.put("usuario", usuarioSolicitanteServicio.getOne(id));
                
                return "solicitante_edit.html";                
            } else {
                if (usuario.getRol().toString().equals("PROVEEDOR")){
                
                    model.put("usuario", usuarioProveedorServicio.getOne(id));
                    
                    return "proveedor_edit.html";
                }
            }
        }
            
        return "redirect:/admin/usuarios";
        
    }
    
    @PostMapping("/editarPro/{id}")
    public String editarPro(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, 
            @RequestParam String descripcion, @RequestParam String contacto, @RequestParam String profesion, ModelMap modelo, 
            MultipartFile archivo) throws MiException {

        usuarioProveedorServicio.editarUsuario(archivo, nombre, apellido, id, email, descripcion, contacto, profesion);
        
        return "redirect:/admin/usuarios";     
    }
    
    @PostMapping("/editarSoli/{id}")
    public String editarSoli(@RequestParam(required = false) MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
         ModelMap modelo) throws MiException {

        usuarioSolicitanteServicio.editarUsuarioSolicitante(archivo, id, nombre, apellido, email);
        
        return "redirect:/admin/usuarios";     
    }
    
    @PostMapping("/editar/{id}")
    public String editar(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email, 
            @RequestParam(required = false) String descripcion, @RequestParam(required = false) String contacto, @RequestParam(required = false) String profesion, ModelMap modelo, 
            MultipartFile archivo) throws MiException {
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            if (usuario.getRol().toString().equals("SOLICITANTE")){
                
                usuarioSolicitanteServicio.editarUsuarioSolicitante(archivo, id, nombre, apellido, email);     
            } else {
                if (usuario.getRol().toString().equals("PROVEEDOR")){
                    usuarioProveedorServicio.editarUsuario(archivo, nombre, apellido, id, email, descripcion, contacto, profesion);
                }
            }
        }
        
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(Model model, @PathVariable String id) {
        usuarioServicio.eliminar(id);

        return "redirect:/admin/usuarios";

    }
    
    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);


        return "redirect:/admin/usuarios";

    }  
}

