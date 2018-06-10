package com.open.applic.open.interface_principal.metodos_funciones;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.open.applic.open.R;
import com.open.applic.open.create_form_profile.adaptadores.adapter_categoriaNegocio;

public class Categoria {
    private static adapter_categoriaNegocio categoriaNegocio;


    public static adapter_categoriaNegocio getCategoria(String idCategoria , String sPais, Context context){

        // Adapter


        // Firebase
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection( context.getResources().getString(R.string.DB_APP) ).document( sPais ).collection( context.getResources().getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( idCategoria )
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if( documentSnapshot.exists() ){

                        // adapter
                        categoriaNegocio=documentSnapshot.toObject(adapter_categoriaNegocio.class);


                    }
                }
            }
        });

        return categoriaNegocio;
    }
}
