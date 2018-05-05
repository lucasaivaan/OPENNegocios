package com.open.applic.open.interface_principal.adaptadores;

import android.graphics.drawable.Drawable;

/**
 * Created by lucas on 25/10/2017.
 */

public class adapter_servicios_negocio {
    public adapter_servicios_negocio() {
    }

    private String titulo;
    private String descripcion;
    private Drawable ic_servico;
    private String ic_name;
    private  String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public String getIc_name() {
        return ic_name;
    }

    public void setIc_name(String ic_name) {
        this.ic_name = ic_name;
    }



    public Drawable getIc_servico() {
        return ic_servico;
    }

    public void setIc_servico(Drawable ic_servico) {
        this.ic_servico = ic_servico;
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
