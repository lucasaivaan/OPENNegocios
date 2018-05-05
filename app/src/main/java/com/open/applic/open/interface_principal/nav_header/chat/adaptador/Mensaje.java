package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

import java.util.Date;

/**
 * Created by lucas on 30/10/2017.
 */

public class Mensaje {


    private String mensaje;


    private String urlfotoPerfil;
    private String nombre;
    private String fotoPerfil;
    private String type_mensaje;
    private Date timestamp;
    private String categoria;



    public Mensaje() { }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String type_mensaje,Date timestamp,String categoria) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;
        this.timestamp=timestamp;
        this.categoria=categoria;
    }

    public Mensaje(String mensaje, String urlfotoPerfil, String nombre, String fotoPerfil, String type_mensaje, Date timestamp,String categoria) {
        this.mensaje = mensaje;
        this.urlfotoPerfil = urlfotoPerfil;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;
        this.timestamp=timestamp;
        this.categoria=categoria;
    }
    public Date getTimestamp() {return timestamp;}
    public void setTimestamp(Date timestamp) {this.timestamp = timestamp;}

    public String getMensaje() {
        return mensaje;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    public String getUrlfotoPerfil() {
        return urlfotoPerfil;
    }
    public void setUrlfotoPerfil(String urlfotoPerfil) {
        this.urlfotoPerfil = urlfotoPerfil;
    }
}