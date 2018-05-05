package com.open.applic.open.interface_principal.nav_header.perfil_negocio;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;

import java.util.HashMap;
import java.util.Map;

import static com.open.applic.open.Splash_Login.ID_NEGOCIO;

public class MainActivity_tarjeta_negocio extends AppCompatActivity {


    // Cloud Firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    // Perfil Tarjeta
    private  adapter_profile_negocio adapterProfileNegocio;
    private CardView cardViewNegocio;
     private ImageView imageViewIcon;
     private TextView tNombre;
    private TextView tUbicacion;
     private TextView tTelefono;
     private String sCardLayout;

     private int iPuntos;



    // Button
    private Button button_Perzonalizar;
    private Button button_Guardar;

    // Layout view
    private LinearLayout layout_styles;
    private LinearLayout layout_Cardstylo;

    // CardView Style (4)
    private CardView style_1;
    private CardView style_2;
    private CardView style_3;
    private CardView style_4;
    private CardView style_5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tarjeta_negocio2);

        setTitle(getResources().getString(R.string.mi_tarjeta_de_negocio));

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // References
        cardViewNegocio=(CardView) findViewById(R.id.cardView_nengocio);
        imageViewIcon=(ImageView) findViewById(R.id.imageViewIcon);
        tNombre=(TextView) findViewById(R.id.tNombre);
        tUbicacion=(TextView) findViewById(R.id.tUbicacion);
        tTelefono=(TextView) findViewById(R.id.tTelefono);


        button_Guardar=(Button) findViewById(R.id.button_Guardar);
        button_Guardar.setVisibility(View.GONE);
        button_Perzonalizar=(Button) findViewById(R.id.button_Perzonalizar);

        layout_styles=(LinearLayout) findViewById(R.id.layout_styles);
        layout_styles.setVisibility(View.GONE);
        layout_Cardstylo=(LinearLayout) findViewById(R.id.layout_Cardstylo);

        style_1=(CardView) findViewById(R.id.style_1);
        style_2=(CardView) findViewById(R.id.style_2);
        style_3=(CardView) findViewById(R.id.style_3);
        style_4=(CardView) findViewById(R.id.style_4);
        style_5=(CardView) findViewById(R.id.style_5);

        // stylos de las tarjetas
        final String sStyle_1="card_style_1";
        final String sStyle_2="card_style_2";
        final String sStyle_3="card_style_3";
        final String sStyle_4="card_style_4";
        final String sStyle_5="card_style_5";

        // SetOnclick
        style_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Control de visibilidad
                button_Guardar.setVisibility(View.VISIBLE);
                button_Perzonalizar.setVisibility(View.GONE);

                //Asignacion del stilo
                layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_1);
                sCardLayout=sStyle_1;

            }
        });
        style_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Control de visibilidad
                button_Guardar.setVisibility(View.VISIBLE);
                button_Perzonalizar.setVisibility(View.GONE);

                //Asignacion del stilo
                layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_2);
                sCardLayout=sStyle_2;

            }
        });
        style_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if( iPuntos >= 100 ){
                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_3);
                    // Control de visibilidad
                    button_Guardar.setVisibility(View.VISIBLE);
                    button_Perzonalizar.setVisibility(View.GONE);

                    sCardLayout=sStyle_3;
                }else {

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_3);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.GONE);
                    button_Perzonalizar.setVisibility(View.VISIBLE);

                    Toast.makeText(MainActivity_tarjeta_negocio.this,"100" + getString(R.string.puntos_necesitas_para_desbloquear_stylo),Toast.LENGTH_LONG).show();
                }



            }
        });
        style_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( iPuntos >= 100 ){

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_4);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.VISIBLE);
                    button_Perzonalizar.setVisibility(View.GONE);


                    sCardLayout=sStyle_4;
                }else {

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_4);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.GONE);
                    button_Perzonalizar.setVisibility(View.VISIBLE);

                    Toast.makeText(MainActivity_tarjeta_negocio.this,"100"+ getString(R.string.puntos_necesitas_para_desbloquear_stylo),Toast.LENGTH_LONG).show();
                }



            }
        });
        style_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( iPuntos >= 100 ){

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_5);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.VISIBLE);
                    button_Perzonalizar.setVisibility(View.GONE);


                    sCardLayout=sStyle_5;
                }else {

                    //Asignacion del stilo
                    layout_Cardstylo.setBackgroundResource(R.mipmap.card_style_5);

                    // Control de visibilidad
                    button_Guardar.setVisibility(View.GONE);
                    button_Perzonalizar.setVisibility(View.VISIBLE);

                    Toast.makeText(MainActivity_tarjeta_negocio.this,"100"+ getString(R.string.puntos_necesitas_para_desbloquear_stylo),Toast.LENGTH_LONG).show();
                }



            }
        });


        // Carga los datos de la tarjeta
        PerfilNegocio();


    }

    private void PerfilNegocio() {

        final DocumentReference documentReference = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){

                    // adaptador del perfil
                    adapterProfileNegocio=documentSnapshot.toObject(adapter_profile_negocio.class);

                    iPuntos=(adapterProfileNegocio.getPuntos() + adapterProfileNegocio.getEstrellastotal());


                    //stilo de tarjeta
                    Context context = layout_Cardstylo.getContext();
                    if(adapterProfileNegocio.getCardlayout() !=null && adapterProfileNegocio.getCardlayout() != ""){

                        if(adapterProfileNegocio.getCardlayout().equals("default")){
                            layout_Cardstylo.setBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
                        }else {
                            int id = context.getResources().getIdentifier( adapterProfileNegocio.getCardlayout(), "mipmap", context.getPackageName());
                            layout_Cardstylo.setBackgroundResource(id);

                            // nombre color
                            tNombre.setTextColor(Color.parseColor(adapterProfileNegocio.getColor()));
                        }

                    }else{
                        layout_Cardstylo.setBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
                    }


                    // Color cardView
                    cardViewNegocio.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));

                    // Icono
                    int id =getResources().getIdentifier("logo_"+ adapterProfileNegocio.getCategoria(), "mipmap", context.getPackageName());
                    imageViewIcon.setBackgroundResource(id);

                    // Nombre
                    tNombre.setText(adapterProfileNegocio.getNombre_negocio());

                    // Ubicación
                    tUbicacion.setText(adapterProfileNegocio.getProvincia()+", "+adapterProfileNegocio.getCiudad());

                    //Telefono
                    if( adapterProfileNegocio.getTelefono() != null && adapterProfileNegocio.getTelefono() != ""){
                        tTelefono.setText(adapterProfileNegocio.getTelefono());
                    }else{tTelefono.setText(R.string.sin_telefono);}

                }
            }
        });

    }


    public void Button_guardar(View view){

        // Control de visibilidad
        button_Perzonalizar.setVisibility(View.VISIBLE);
        button_Guardar.setVisibility(View.GONE);
        layout_styles.setVisibility(View.GONE);

        //Cloud Firestore
        // Guardad los datos del negocio
        final Map<String, Object> cardBusiness = new HashMap<>();
        cardBusiness.put("cardlayout",sCardLayout);

        DocumentReference documentReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
        documentReference.set(cardBusiness, SetOptions.merge());

        //Actualizando informacion en la lista de clientes de  los negocios
        CollectionReference collecNegocios=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_CLIENTES)  );
        collecNegocios.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot doc:task.getResult()){

                        if(doc.getString("nombre") != null){
                            DocumentReference docNegocio=db.collection(  getString(R.string.DB_CLIENTES)  ).document(doc.getId()).collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
                            docNegocio.set(cardBusiness, SetOptions.merge());
                        }

                    }
                }

            }
        });

    }
    public void button_Perzonalizar(View view){

        // Control de visibilidad
        layout_styles.setVisibility(View.VISIBLE);

        // Color stylos
        style_1.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        style_2.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        style_3.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        style_4.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));
        style_5.setCardBackgroundColor(Color.parseColor(adapterProfileNegocio.getColor()));

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
