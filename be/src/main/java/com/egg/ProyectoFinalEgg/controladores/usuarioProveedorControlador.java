/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.controladores;

import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;

import com.egg.ProyectoFinalEgg.repositorios.UsuarioProveedorRepositorio;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.servicios.ServicioService;

import com.egg.ProyectoFinalEgg.servicios.UsuarioProveedorServicio;
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

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/proveedor")
public class usuarioProveedorControlador {

    @Autowired
    private UsuarioProveedorServicio usuarioProveedor;
    @Autowired
    private UsuarioProveedorRepositorio proveedorRepositorio;

    @GetMapping("/inicio")
    public String index() {
        return "index.html";
    }

    @GetMapping("/config")
    public String configUsuario(HttpSession session) {

        return "config_usuario.html";

    }
    
     @PostMapping("/editar/{id}")
    public String editar(@PathVariable String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String email,
            @RequestParam String descripcion, @RequestParam String contacto, @RequestParam String profesion, ModelMap modelo,
            MultipartFile archivo) throws MiException {

        usuarioProveedor.editarUsuario(archivo, nombre, apellido, id, email, descripcion, contacto, profesion);

        return "redirect:/proveedor/inicio";
    }

    @GetMapping("/listar")
    public String listar(ModelMap modelo) {

        List<UsuarioProveedor> usuarios = usuarioProveedor.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "listar_usuario.html";
    }

    @GetMapping("/albanileria")
    public String listarAlbanileria(ModelMap modelo) {
        List<UsuarioProveedor> proveedor = proveedorRepositorio.buscarPorProfesion("albanileria");
        modelo.addAttribute("proveedor", proveedor);
        return "tarjeta.html";
    }

    @GetMapping("/carpinteria")
    public String listarCarpinteria(ModelMap modelo) {
        List<UsuarioProveedor> proveedor = proveedorRepositorio.buscarPorProfesion("carpinteria");
        modelo.addAttribute("proveedor", proveedor);
        return "tarjeta.html";
    }

    @GetMapping("/limpieza")
    public String listarLimpieza(ModelMap modelo) {
        List<UsuarioProveedor> proveedor = proveedorRepositorio.buscarPorProfesion("limpieza");
        modelo.addAttribute("proveedor", proveedor);
        return "tarjeta.html";
    }

    @GetMapping("/herreria")
    public String listarHerreria(ModelMap modelo) {
        List<UsuarioProveedor> proveedor = proveedorRepositorio.buscarPorProfesion("herreria");
        modelo.addAttribute("proveedor", proveedor);
        return "tarjeta.html";
    }

    @GetMapping("/jardineria")
    public String listarJardineria(ModelMap modelo) {
        List<UsuarioProveedor> proveedor = proveedorRepositorio.buscarPorProfesion("jardineria");
        modelo.addAttribute("proveedor", proveedor);
        return "tarjeta.html";
    }

    @GetMapping("/plomeria")
    public String listarPlomeria(ModelMap modelo) {
        List<UsuarioProveedor> proveedor = proveedorRepositorio.buscarPorProfesion("plomeria");
        modelo.addAttribute("proveedor", proveedor);
        return "tarjeta.html";
    }

   

    @GetMapping("/tarjeta")
    public String tarjeta(ModelMap modelo) {
        List<UsuarioProveedor> usuarios = usuarioProveedor.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "tarjeta.html";
    }

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/solicitudes/{id}")
    public String mostrarServicios(@PathVariable("id") String idProveedor, Model model) {
        List<Servicio> servicios = servicioService.getServiciosByProveedorId(idProveedor);
        Iterator<Servicio> iter = servicios.iterator();
        while (iter.hasNext()) {
            Servicio servicio = iter.next();
            if (servicio.getAceptado()) {
                iter.remove();
            }
        }
        model.addAttribute("servicios", servicios);
        return "listar_servicio.html";
    }

}
