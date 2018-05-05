package com.open.applic.open.interface_principal.metodos_funciones;

public class Buscardor {

    //---Algoritmo de busqueda
    public static boolean Buscar(String value, String valueSeach){
        boolean resultado = false;

        if(value !=null){
            //Convierte los valores String en minuscula para facilitar la busqueda
            value=value.toLowerCase();


            String [] stringsArrayValueBuscador;
            int rdsultado2;

            //------------------   Algoritbo de busqueda --------------------------------------
            stringsArrayValueBuscador=valueSeach.split(" ");
            if(!valueSeach.equals("")){
                for (int i = 0; i < stringsArrayValueBuscador.length; i++){
                    // aqui se puede referir al objeto con arreglo[i];
                    rdsultado2 = value.indexOf(stringsArrayValueBuscador[i].toLowerCase());
                    if(rdsultado2 != -1) {
                        resultado=true;

                    }else{
                        resultado=false;
                        break;
                    }}}
        }


        return resultado;
    }
}
