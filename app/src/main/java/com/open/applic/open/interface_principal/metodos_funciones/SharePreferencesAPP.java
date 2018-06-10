package com.open.applic.open.interface_principal.metodos_funciones;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesAPP {

    public SharePreferencesAPP() { }

    public static String getID_NEGOCIO(Context context){

        // DAtos SharePreferencesAPP
        String PREFS_KEY = "MisPreferencias";
        SharedPreferences misPreferencias;

        // STATIC VARIABLE GLOBAL
        String ID_NEGOCIO;


        // dATOS SharePreferencesAPP
        misPreferencias = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        return misPreferencias.getString("ID_NEGOCIO", null);
    }
    public static String getID_USUARIO(Context context){

        // DAtos SharePreferencesAPP
        String PREFS_KEY = "MisPreferencias";
        SharedPreferences misPreferencias;

        // STATIC VARIABLE GLOBAL
        String ID_USUARIO;

        // dATOS SharePreferencesAPP
        misPreferencias = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        return misPreferencias.getString("ID_USUARIO", null);
    }
    public static String getPAIS(Context context){

        // DAtos SharePreferencesAPP
        String PREFS_KEY = "MisPreferencias";
        SharedPreferences misPreferencias;

        // STATIC VARIABLE GLOBAL
        String sPAIS;

        // dATOS SharePreferencesAPP
        misPreferencias = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        return misPreferencias.getString("sPAIS", null);
    }

    public static void setID_NEGOCIO(String ID_NEGOCIO, Context context){
        // DAtos SharePreferencesAPP
        String PREFS_KEY = "MisPreferencias";
        SharedPreferences misPreferencias ;
        misPreferencias = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putString("ID_NEGOCIO", ID_NEGOCIO );
        editor.commit();


    }
    public static void setID_USUARIO(String ID_USUARIO, Context context){
        // DAtos SharePreferencesAPP
        String PREFS_KEY = "MisPreferencias";
        SharedPreferences misPreferencias ;
        misPreferencias = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);


        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putString("ID_USUARIO", ID_USUARIO );
        editor.commit();


    }

    public static void setPAIS(String sPais, Context context){
        // DAtos SharePreferencesAPP
        String PREFS_KEY = "MisPreferencias";
        SharedPreferences misPreferencias ;
        misPreferencias = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);


        if(sPais != null){
            SharedPreferences.Editor editor = misPreferencias.edit();
            editor.putString("sPAIS", sPais.toUpperCase() );
            editor.commit();
        }else{
            SharedPreferences.Editor editor = misPreferencias.edit();
            editor.putString("sPAIS", null );
            editor.commit();
        }


    }

}
