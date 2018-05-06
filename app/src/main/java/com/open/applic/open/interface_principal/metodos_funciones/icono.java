package com.open.applic.open.interface_principal.metodos_funciones;

import android.content.Context;

public class icono {
    public icono() { }

    public static int getIconLocationCategoria(String sCategoria, Context context){

        return context.getResources().getIdentifier("loc_"+sCategoria.toLowerCase(), "mipmap", context.getPackageName());
    }
    public static int getIconLogoCategoria(String sCategoria, Context context){

        return context.getResources().getIdentifier("logo_"+sCategoria.toLowerCase(), "mipmap", context.getPackageName());
    }
}
