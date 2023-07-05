/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.servicios;

import org.springframework.core.io.Resource;
import com.egg.ProyectoFinalEgg.entidades.Imagen;
import com.egg.ProyectoFinalEgg.entidades.Servicio;
import com.egg.ProyectoFinalEgg.entidades.UsuarioProveedor;
import com.egg.ProyectoFinalEgg.enumeradores.Rol;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ImagenRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioProveedorRepositorio;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioProveedorServicio implements UserDetailsService {

    @Autowired
    private UsuarioProveedorRepositorio usuarioProveedorRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ServicioService servicioServicio;

    @Transactional
    public void registrarProveedor(MultipartFile archivo, String nombre, String apellido, String email, String password, String password2, String descripcion, String contacto, String profesion) throws MiException, IOException {

        validar(nombre, apellido, email, password, password2, descripcion, contacto, profesion);

        UsuarioProveedor proveedor = new UsuarioProveedor();

        proveedor.setNombre(nombre);
        proveedor.setApellido(apellido);
        proveedor.setEmail(email);
        proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
        proveedor.setRol(Rol.PROVEEDOR);
        proveedor.setDescripcion(descripcion);
        proveedor.setContacto(contacto);
        proveedor.setProfesion(profesion);

        if (archivo.isEmpty()) { // sacar false

            String pathAbsolutoCarpeta = servletContext.getRealPath("/static/img/");
            String pathAbsolutoImagen = pathAbsolutoCarpeta + "default_picture.jpg";
            String path = pathAbsolutoImagen.replace(servletContext.getRealPath("/"), "");
            Resource resource = new ClassPathResource(path);
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            Imagen imagen = new Imagen();
            imagen.setNombre("imagen_default.jpg");
            imagen.setMime("image/jpg");
            imagen.setContenido(bytes);
            imagenRepositorio.save(imagen);
            proveedor.setImagen(imagen);
        } else {
            Imagen imagen = imagenServicio.guardar(archivo);
            proveedor.setImagen(imagen);
        }

        usuarioProveedorRepositorio.save(proveedor);

    }

    @Transactional(readOnly = true)
    public List<UsuarioProveedor> listarUsuarios() {

        List<UsuarioProveedor> usuarios = new ArrayList();

        usuarios = usuarioProveedorRepositorio.findAll();

        return usuarios;
    }

    @Transactional

    public void editarUsuario(MultipartFile archivo, String nombre, String apellido, String id, String email,
            String descripcion, String contacto, String profesion) throws MiException {
        validar(nombre, apellido, email, profesion, apellido, descripcion, contacto, profesion);

        Optional<UsuarioProveedor> respuesta = usuarioProveedorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            UsuarioProveedor proveedor = respuesta.get();

            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setEmail(email);
            proveedor.setRol(Rol.PROVEEDOR);
            proveedor.setDescripcion(descripcion);
            proveedor.setContacto(contacto);
            proveedor.setProfesion(profesion);
            String idImagen = null;
            if (archivo != null && !archivo.isEmpty()) {
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                proveedor.setImagen(imagen);
                usuarioProveedorRepositorio.save(proveedor);
            }

        }

    }

    @Transactional
    public void editarUsuarioSinImagen(String nombre, String apellido, String id, String email,
            String descripcion, String contacto, String profesion) throws MiException {

        Optional<UsuarioProveedor> respuesta = usuarioProveedorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            UsuarioProveedor proveedor = respuesta.get();

            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setEmail(email);
            proveedor.setRol(Rol.PROVEEDOR);
            proveedor.setDescripcion(descripcion);
            proveedor.setContacto(contacto);
            proveedor.setProfesion(profesion);

            usuarioProveedorRepositorio.save(proveedor);

        }

    }
    
     @Transactional
    public void actualizarContraseña(String id, String password) {

        Optional<UsuarioProveedor> resp = usuarioProveedorRepositorio.findById(id);
        if (resp.isPresent()) {
            UsuarioProveedor proveedor = resp.get();
            proveedor.setPassword(new BCryptPasswordEncoder().encode(password));

            usuarioProveedorRepositorio.save(proveedor);

        }

    }
    
      
    
    

    @Transactional(readOnly = true)
    public UsuarioProveedor getOne(String id) {
        return usuarioProveedorRepositorio.getOne(id);
    }

    private void validar(String nombre, String apellido, String email, String password, String password2,
            String descripcion, String contacto, String profesion) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (nombre.isEmpty() || apellido == null) {
            throw new MiException("el apellido no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("La descripción no puede ser nulo o estar vacío");
        }
        if (contacto.isEmpty() || contacto == null) {
            throw new MiException("el contacto no puede ser nulo o estar vacío");
        }
        if (profesion.isEmpty() || profesion == null) {
            throw new MiException("La profesión no puede ser nulo o estar vacío");
        }
    }

    public int save(UsuarioProveedor u) {
        int res = 0;
        UsuarioProveedor usuario = usuarioProveedorRepositorio.save(u);
        if (!usuario.equals(null)) {
            res = 1;
        }
        return res;
    }

    @Transactional
    public void calcPromedio(String id_proveedor) {
        Optional<UsuarioProveedor> respuesta = usuarioProveedorRepositorio.findById(id_proveedor);
        int puntosTotales = 0;
        int contadorCalif = 0;
        double promedio;

        if (respuesta.isPresent()) {
            UsuarioProveedor proveedor = respuesta.get();

            List<Servicio> servicios = servicioServicio.listarServicioPorProveedor(id_proveedor);

            for (Servicio servicio : servicios) {
                Integer puntuacion = servicio.getCalificacion().getPuntucion();
                if (puntuacion != null) {
                    puntosTotales += puntuacion;
                    contadorCalif++;
                }
            }

            promedio = puntosTotales / contadorCalif;

            proveedor.setPromedio(promedio);

            usuarioProveedorRepositorio.save(proveedor);
        }
    }

    public Optional<UsuarioProveedor> buscarPorId(String id) {
        return usuarioProveedorRepositorio.findById(id);
    }

    public void eliminar(String id) {
        usuarioProveedorRepositorio.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuarioProveedor usuarioProveedor = usuarioProveedorRepositorio.buscarPorEmail(email);

        if (usuarioProveedor != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuarioProveedor.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuarioProveedor);

            return new User(usuarioProveedor.getEmail(), usuarioProveedor.getPassword(), permisos);
        } else {
            return null;
        }
    }
}
