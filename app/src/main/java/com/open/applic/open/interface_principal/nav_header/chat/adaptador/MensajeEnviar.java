package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.Map;

/**
 * Created by lucas on 30/10/2017.
 */

public class MensajeEnviar  extends Mensaje {
    private Map hora;

    public MensajeEnviar(String mensaje, String nombreNegocio, String fotoPerfil, String type_mensaje, FieldValue fieldValue) {
    }

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String type_mensaje, Map hora, Date timestamp,String categoria) {
        super(mensaje, nombre, fotoPerfil, type_mensaje,timestamp,categoria);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String urlFoto, String nombre, String fotoPerfil, String type_mensaje, Map hora,Date timestamp,String categoria) {
        super(mensaje, urlFoto, nombre, fotoPerfil, type_mensaje,timestamp,categoria);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}