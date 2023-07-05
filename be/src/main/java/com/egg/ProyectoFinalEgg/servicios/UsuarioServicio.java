/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.servicios;

import com.egg.ProyectoFinalEgg.entidades.Imagen;
import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.Usuario;
import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import com.egg.ProyectoFinalEgg.enumeradores.Rol;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ServicioRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioProveedorRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioSolicitanteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public Usuario registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);

        return usuario;

    }

    public List<Usuario> listarUsuariosAsc() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        usuarios = usuarioRepositorio.ordenarPorNombreAsc();

        return usuarios;
    }

    public List<Usuario> listarUsuarios(String palabraClave) {

        if (palabraClave != null) {
            return usuarioRepositorio.findAll(palabraClave);
        }
        return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> listarUsuario(String id) {

        return usuarioRepositorio.findById(id);

    }

    @Transactional(readOnly = true)
    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede ser nulo o estar vacío, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }

    public int save(Usuario u) {
        int res = 0;
        Usuario usuario = usuarioRepositorio.save(u);

        if (!usuario.equals(null)) {
            res = 1;
        }

        return res;
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepositorio.findById(id);
    }

    public void eliminar(String id) {
        usuarioRepositorio.deleteById(id);
    }

    @Transactional
    public void cambiarRol(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.SOLICITANTE)) {
                usuario.setRol(Rol.ADMIN);
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.PROVEEDOR);
            } else if (usuario.getRol().equals(Rol.PROVEEDOR)) {
                usuario.setRol(Rol.SOLICITANTE);
            }
        }
    }
    @Autowired
    private UsuarioProveedorRepositorio usuarioProveedorRepositorio;
    @Autowired
    private UsuarioSolicitanteRepositorio usuarioSolicitanteRepositorio;
    @Autowired
    ServicioRepositorio servicioRepositorio;

    @Transactional
    public void actualizarHistorial(String id) { //el id es de una calificacion
        Servicio servicio = servicioRepositorio.findByCalificacionId(id);
        List<Servicio> servicios = new ArrayList();
        servicios.add(servicio);
        Optional<UsuarioProveedor> respuestaP = usuarioProveedorRepositorio.findById(servicio.getProveedor().getId());
        Optional<UsuarioSolicitante> respuestaS = usuarioSolicitanteRepositorio.findById(servicio.getSolicitante().getId());
        if (respuestaP.isPresent() && respuestaS.isPresent()) {
            UsuarioProveedor proveedor = respuestaP.get();
            UsuarioSolicitante solicitante = respuestaS.get();
            proveedor.setServicios(servicios);
            solicitante.setServicios(servicios);
            usuarioProveedorRepositorio.save(proveedor);
            usuarioSolicitanteRepositorio.save(solicitante);
        }
    }

//    @Transactional
//    public void actualizarContraseña(String id, String password) {
//
//        Optional<Usuario> resp = usuarioRepositorio.findById(id);
//        if (resp.isPresent()) {
//            Usuario usuario = resp.get();
//            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
//
//            usuarioRepositorio.save(usuario);
//
//        }
//
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

}
