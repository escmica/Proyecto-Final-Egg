/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.ProyectoFinalEgg.entidades;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Table;

/**
 *
 * @author facup
 */
@Entity
@Table(name = "calificaciones")
public class Calificacion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String resenia;

    private Integer puntucion;

    public Calificacion() {
    }

    public Calificacion(String id, String resenia, Integer puntucion) {
        this.id = id;
        this.resenia = resenia;
        this.puntucion = puntucion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResenia() {
        return resenia;
    }

    public void setResenia(String resenia) {
        this.resenia = resenia;
    }

    public Integer getPuntucion() {
        return puntucion;
    }

    public void setPuntucion(Integer puntucion) {
        this.puntucion = puntucion;
    }

}
