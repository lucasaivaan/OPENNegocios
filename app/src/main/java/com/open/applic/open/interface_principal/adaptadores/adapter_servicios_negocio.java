package com.open.applic.open.interface_principal.adaptadores;

import android.graphics.drawable.Drawable;

/**
 * Created by lucas on 25/10/2017.
 */

public class adapter_servicios_negocio {
    public adapter_servicios_negocio() {
    }

    private  String id;
    private String titulo;
    private String descripcion;
    private String url_imagen="default";



    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getUrl_imagen() { return url_imagen; }

    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setUrl_imagen(String url_imagen) { this.url_imagen = url_imagen; }



}
