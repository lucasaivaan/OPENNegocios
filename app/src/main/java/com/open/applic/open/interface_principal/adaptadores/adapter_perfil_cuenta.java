package com.open.applic.open.interface_principal.adaptadores;

/**
 * Created by ivan on 26/3/2018.
 */

public class adapter_perfil_cuenta {

    private String nombre;
    private String email;
    private String cuenta;
    private String urlfotoPerfil;
    private String tipocuenta;


    public String getTipocuenta() {return tipocuenta;}

    public void setTipocuenta(String tipocuenta) {this.tipocuenta = tipocuenta;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setEmail(String email) {this.email = email;}

    public void setCuenta(String cuenta) {this.cuenta = cuenta;}

    public void setUrlfotoPerfil(String urlfotoPerfil) {this.urlfotoPerfil = urlfotoPerfil;}

    public String getNombre() {return nombre;}

    public String getEmail() {return email;}

    public String getCuenta() {return cuenta;}

    public String getUrlfotoPerfil() {return urlfotoPerfil;}
}
