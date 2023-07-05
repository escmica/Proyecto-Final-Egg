/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg;

import com.egg.ProyectoFinalEgg.servicios.UsuarioProveedorServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioServicio;
import com.egg.ProyectoFinalEgg.servicios.UsuarioSolicitanteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author micae
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class seguridadWeb extends WebSecurityConfigurerAdapter {

    @Autowired
    public UsuarioServicio usuarioServicio;
    @Autowired
    public UsuarioSolicitanteServicio usuarioSolicitanteServicio;
    @Autowired
    public UsuarioProveedorServicio usuarioProveedorServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(usuarioSolicitanteServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(usuarioProveedorServicio)
                .passwordEncoder(new BCryptPasswordEncoder());        
    }

     @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Autowired
//    public void configureGlobalS(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(usuarioSolicitanteServicio)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//    @Autowired
//    public void configureGlobalP(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(usuarioProveedorServicio)
//                .passwordEncoder(new BCryptPasswordEncoder());
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        .antMatchers("/admin/*").hasRole("ADMIN")
//                        .antMatchers("/proveedor/*").hasRole("PROVEEDOR")
//                        .antMatchers("/solicitante/*").hasRole("SOLICITANTE")
                        .antMatchers("/css/*", "/js/*", "/img/*", "/**")
                        .permitAll()
                .and().formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/inicio")
                        .permitAll()
                .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                .and().csrf()
                        .disable();

    }

}