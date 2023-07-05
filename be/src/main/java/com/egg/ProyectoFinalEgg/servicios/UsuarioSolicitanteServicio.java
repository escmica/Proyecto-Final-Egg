package com.egg.ProyectoFinalEgg.servicios;

import com.egg.ProyectoFinalEgg.entidades.Imagen;

import com.egg.ProyectoFinalEgg.entidades.UsuarioSolicitante;
import com.egg.ProyectoFinalEgg.enumeradores.Rol;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ImagenRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioRepositorio;
import com.egg.ProyectoFinalEgg.repositorios.UsuarioSolicitanteRepositorio;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioSolicitanteServicio implements UserDetailsService {

    @Autowired
    private UsuarioSolicitanteRepositorio usuarioSolicitanteRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    @Autowired
    private ServletContext servletContext;

    @Transactional

    public void registroSolicitante(MultipartFile archivo, String nombre, String apelido, String email, String password, String password2) throws MiException, IOException {

        validar(nombre, email, password, password2);

        UsuarioSolicitante usuario = new UsuarioSolicitante();
        usuario.setNombre(nombre);
        usuario.setApellido(apelido);

        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.SOLICITANTE);
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
            usuario.setImagen(imagen);
        } else {
            Imagen imagen = imagenServicio.guardar(archivo);
            usuario.setImagen(imagen);
        }

        usuarioSolicitanteRepositorio.save(usuario);

    }

    @Transactional
    public void editarUsuarioSolicitante(MultipartFile archivo, String idUsuario, String nombre, String apellido, String email) throws MiException {

//        validar(nombre, email, password, password2);
        Optional<UsuarioSolicitante> respuesta = usuarioSolicitanteRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            UsuarioSolicitante usuarioSolicitante = respuesta.get();
            usuarioSolicitante.setNombre(nombre);
            usuarioSolicitante.setApellido(apellido);
            usuarioSolicitante.setEmail(email);
            usuarioSolicitante.setRol(Rol.SOLICITANTE);
            String idImagen = null;
            if (archivo != null && !archivo.isEmpty()) {
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                usuarioSolicitante.setImagen(imagen);
                usuarioSolicitanteRepositorio.save(usuarioSolicitante);
            }
        }

    }

    @Transactional
    public void editarSolicitanteSinImagen(String id, String nombre, String apellido, String email) throws MiException {
        Optional<UsuarioSolicitante> resp = usuarioSolicitanteRepositorio.findById(id);
        if (resp.isPresent()) {
            UsuarioSolicitante solicitante = resp.get();
            solicitante.setApellido(apellido);
            solicitante.setEmail(email);
            solicitante.setNombre(nombre);

            usuarioSolicitanteRepositorio.save(solicitante);

        }

    }

    @Transactional
    public void actualizarContraseña(String id, String password) {

        Optional<UsuarioSolicitante> resp = usuarioSolicitanteRepositorio.findById(id);
        if (resp.isPresent()) {
            UsuarioSolicitante solicitante = resp.get();
            solicitante.setPassword(new BCryptPasswordEncoder().encode(password));

            usuarioSolicitanteRepositorio.save(solicitante);

        }

    }

    @Transactional(readOnly = true)
    public UsuarioSolicitante getOne(String id) {
        return usuarioSolicitanteRepositorio.getOne(id);
    }

    public void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
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
    }

    public int save(UsuarioSolicitante u) {
        int res = 0;
        UsuarioSolicitante usuarioSolicitante = usuarioSolicitanteRepositorio.save(u);
        if (!usuarioSolicitante.equals(null)) {
            res = 1;
        }
        return res;
    }

    public Optional<UsuarioSolicitante> buscarPorId(String id) {
        return usuarioSolicitanteRepositorio.findById(id);
    }

    public void eliminar(String id) {
        usuarioSolicitanteRepositorio.deleteById(id);
    }

    @Transactional
    public void cambiarRol(String id) {
        Optional<UsuarioSolicitante> respuesta = usuarioSolicitanteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            UsuarioSolicitante usuarioSolicitante = respuesta.get();

            if (usuarioSolicitante.getRol().equals(Rol.SOLICITANTE)) {

                usuarioSolicitante.setRol(Rol.ADMIN);

            } else if (usuarioSolicitante.getRol().equals(Rol.ADMIN)) {
                usuarioSolicitante.setRol(Rol.SOLICITANTE);
            }
        }
    }

    public List<UsuarioSolicitante> listarUsuarios() {
        List<UsuarioSolicitante> usuarios = new ArrayList();

        usuarios = usuarioSolicitanteRepositorio.findAll();

        usuarios = usuarioSolicitanteRepositorio.buscarPorRol();

        return usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UsuarioSolicitante usuarioSolicitante = usuarioSolicitanteRepositorio.buscarPorEmail(email);

        if (usuarioSolicitante != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuarioSolicitante.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuarioSolicitante);

            return new User(usuarioSolicitante.getEmail(), usuarioSolicitante.getPassword(), permisos);
        } else {
            return null;
        }
    }
}
