package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

import android.provider.ContactsContract;

import java.util.Date;

/**
 * Created by lucas on 30/10/2017.
 */

public class MensajeRecibir extends Mensaje {

    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String urlfotoPerfil, String nombre, String fotoPerfil, String type_mensaje, Long hora, Date timestamp,String categoria, String id) {
        super(mensaje, urlfotoPerfil, nombre, fotoPerfil, type_mensaje,timestamp,categoria,id);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}