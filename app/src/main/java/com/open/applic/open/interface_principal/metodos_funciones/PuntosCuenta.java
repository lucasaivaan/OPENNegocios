package com.open.applic.open.interface_principal.metodos_funciones;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.open.applic.open.R;

import java.util.HashMap;
import java.util.Map;

import static com.open.applic.open.Splash_Login.ID_NEGOCIO;


public class PuntosCuenta {
    private static Context contextView;


    public static void SumaPuntos(final Context context, Integer iPuntos, final String texto){

        try{
            contextView=context;

            if(iPuntos==0){iPuntos=1;}
            final Integer finalIPuntos = iPuntos;

            //////////////////////////////// Cuadro de Dialog //////////////////////////////////
            final Dialog dialog=new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.view_animacion_puntos);
            TextView tPuntos=(TextView) dialog.findViewById(R.id.textView_puntos);
            tPuntos.setText("+"+finalIPuntos);
            dialog.show();

            try {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1500);// Tiempo de espera
            } catch (Exception e) { }



            // Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference docRefNegocio=db.collection( contextView.getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO);

            docRefNegocio.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();

                        if(documentSnapshot.exists()){
                            if(documentSnapshot.getDouble("puntos") != null){

                                Double puntos=documentSnapshot.getDouble("puntos");
                                puntos+=finalIPuntos;
                                // Create a new user with a first and last name
                                Map<String, Object> PuntosValue = new HashMap<>();
                                PuntosValue.put("puntos", puntos);

                                docRefNegocio.set(PuntosValue, SetOptions.merge());

                            }else{

                                // Create a new user with a first and last name
                                Map<String, Object> PuntosValue = new HashMap<>();
                                PuntosValue.put("puntos", finalIPuntos);

                                docRefNegocio.set(PuntosValue, SetOptions.merge());

                            }
                        }else {

                            // Create a new user with a first and last name
                            Map<String, Object> PuntosValue = new HashMap<>();
                            PuntosValue.put("puntos", finalIPuntos);

                            docRefNegocio.set(PuntosValue, SetOptions.merge());

                        }
                    }
                }
            });

        }catch (Exception ex){}


    }

    public static void RestaPuntos(Integer iPuntos){

        try{

            if(iPuntos==0){iPuntos=1;}
            final Integer finalIPuntos = iPuntos;


            // Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference docRefNegocio=db.collection( contextView.getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO);

            docRefNegocio.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot=task.getResult();

                        if(documentSnapshot.exists()){
                            if(documentSnapshot.getDouble("puntos") != null){

                                Double puntos=documentSnapshot.getDouble("puntos");
                                puntos-=finalIPuntos;
                                // Create a new user with a first and last name
                                Map<String, Object> PuntosValue = new HashMap<>();
                                PuntosValue.put("puntos", puntos);

                                docRefNegocio.set(PuntosValue, SetOptions.merge());

                            }else{ }
                        }

                    }

                }
            });

        }catch (Exception ex){ }



    }
}
