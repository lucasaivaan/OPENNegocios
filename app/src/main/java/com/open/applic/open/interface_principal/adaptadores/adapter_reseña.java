package com.open.applic.open.interface_principal.adaptadores;

import java.util.Date;

/**
 * Created by ivan on 15/3/2018.
 */

public class adapter_reseña {

    public adapter_reseña() {}

    private String urlfotoPerfil;
    private String nombre;
    private String id;
    private Date timestamp;
    private String reseña;
    private String fotoPerfil;
    private String idReseña;
    private Integer estrellas;



    public Integer getEstrellas() {return estrellas;}

    public void setEstrellas(Integer estrellas) {this.estrellas = estrellas;}

    public String getIdReseña() {return idReseña;}

    public void setIdReseña(String idReseña) {this.idReseña = idReseña;}

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public void setUrlfotoPerfil(String urlfotoPerfil) {this.urlfotoPerfil = urlfotoPerfil;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setId(String id) {this.id = id;}

    public void setTimestamp(Date timestamp) {this.timestamp = timestamp;}

    public void setReseña(String reseña) {this.reseña = reseña;}

    public String getUrlfotoPerfil() {return urlfotoPerfil;}

    public String getNombre() {return nombre;}

    public String getId() {return id;}

    public Date getTimestamp() {return timestamp;}

    public String getReseña() {return reseña;}
}
