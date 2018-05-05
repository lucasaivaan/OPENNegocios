package com.open.applic.open.interface_principal.nav_header.tips.adaptador;

/**
 * Created by lucas on 24/10/2017.
 */

public class adapter_tips {
    public adapter_tips() {
    }
    String urlFoto;
    String titulo;
    String descripcion;
    String id;
    String urlFotoCreador;

    String nombreCreador;
    String idCreador;



    public String getIdCreador() { return idCreador; }

    public void setIdCreador(String idCreador) { this.idCreador = idCreador; }

    public String getNombreCreador() { return nombreCreador; }

    public void setNombreCreador(String nombreCreador) { this.nombreCreador = nombreCreador; }

    public String getUrlFotoCreador() { return urlFotoCreador; }

    public void setUrlFotoCreador(String urlFotoCreador) { this.urlFotoCreador = urlFotoCreador; }

    public String getUrlFoto() { return urlFoto;}

    public void setUrlFoto(String urlFoto) { this.urlFoto = urlFoto; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }




}
