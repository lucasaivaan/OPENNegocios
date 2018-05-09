package com.open.applic.open.interface_principal.nav_header.galeria_fotos.adaptadores;

import java.util.Date;

public class adaptador_foto {
    public adaptador_foto() { }


    private String id;
    private String urlfoto;
    private String comentario="";
    private Date timestamp;

    public void setId(String id) { this.id = id; }
    public void setUrlfoto(String urlfoto) { this.urlfoto = urlfoto; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public String getId() { return id; }
    public String getUrlfoto() { return urlfoto; }
    public String getComentario() { return comentario; }
    public Date getTimestamp() { return timestamp; }
}
