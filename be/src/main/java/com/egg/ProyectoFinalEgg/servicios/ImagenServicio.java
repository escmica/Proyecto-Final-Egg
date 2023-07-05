/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.servicios;

import com.egg.ProyectoFinalEgg.entidades.Imagen;
import com.egg.ProyectoFinalEgg.excepciones.MiException;
import com.egg.ProyectoFinalEgg.repositorios.ImagenRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    @Autowired
    private ServletContext servletContext;
    
    
    public Imagen guardar(MultipartFile archivo)throws MiException{
        if (archivo != null){
            try{
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getName());
                
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepositorio.save(imagen);
            } catch (Exception e){
                System.err.println(e.getMessage());
                
            }
        }
        return null;
    }
    
    public Imagen porDefecto(MultipartFile archivo)throws MiException, IOException{
       
            try{
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
                
                return imagenRepositorio.save(imagen);
            } catch (Exception e){
                System.err.println(e.getMessage());
                
            }
        
        return null;
    }
    
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException{
        if (archivo != null){
            try{
                Imagen imagen = new Imagen();
                
                if (idImagen != null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    
                    if (respuesta.isPresent()){
                        imagen = respuesta.get();
                    }
                    
                }
                
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getName());
                
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepositorio.save(imagen);
            } catch (Exception e){
                System.err.println(e.getMessage());
                
            }
        }
        return null;
    }
}
