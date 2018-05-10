package com.open.applic.open.interface_principal.adaptadores;

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_perfil_clientes {
    public adapter_perfil_clientes() {
    }
    private String id;
    private String nombre;
    private String telefono;
    private String urlfotoPerfil="default";
    private Boolean mensaje_nuevo =false;
    private Boolean local=false;
    private String cardlayout="default";
    private Integer numreseñas=0;


    public Integer getNumreseñas() {return numreseñas;}

    public void setNumreseñas(Integer numreseñas) {this.numreseñas = numreseñas;}

    public String getCardlayout() { return cardlayout; }

    public void setCardlayout(String cardlayout) { this.cardlayout = cardlayout; }

    public Boolean getLocal() { return local; }

    public void setLocal(Boolean local) { this.local = local; }

    public Boolean getMensaje_nuevo() {return mensaje_nuevo;}

    public void setMensaje_nuevo(Boolean mensaje_nuevo) {this.mensaje_nuevo = mensaje_nuevo;}

    public String getUrlfotoPerfil() {return urlfotoPerfil;}

    public void setUrlfotoPerfil(String urlfotoPerfil) {this.urlfotoPerfil = urlfotoPerfil;}

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
