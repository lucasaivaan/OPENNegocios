package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.menu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.MainActivity_pedidos_lista;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.MainActivity_pedidos_lista_historia;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_pedidos_configuracion extends AppCompatActivity {

    // String
    private String ID_NEGOCIO;

    // Swich
    private Switch aSwitch_Delivery;
    private Switch aSwitch_RetitoLocal;

    // FIREBASE
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedidos_configuracion);
        setTitle(getResources().getString(R.string.configuracion));

        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // DAtos aPP
        ID_NEGOCIO = SharePreferencesAPP.getID_NEGOCIO(this);

        // Refence
        aSwitch_Delivery = (Switch) findViewById(R.id.swich_delivery);
        aSwitch_RetitoLocal = (Switch) findViewById(R.id.swich_RetiroLocal);


        // Firebase
        DocumentReference documentReference_Conf_PEdido = firestore.collection(getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getResources().getString(R.string.DB_CONFIGURACION)).document(getResources().getString(R.string.DB_sistema_pedido));
        documentReference_Conf_PEdido.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc_ConfPedido = task.getResult();
                    if (doc_ConfPedido.exists()) {

                        aSwitch_Delivery.setChecked(doc_ConfPedido.getBoolean(getResources().getString(R.string.DB_delivery)));
                        aSwitch_RetitoLocal.setChecked(doc_ConfPedido.getBoolean(getResources().getString(R.string.DB_retiro_negocio)));

                    }
                }
            }
        });

        // OnClick
        aSwitch_Delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(aSwitch_Delivery.isChecked()){
                    Toast.makeText(MainActivity_pedidos_configuracion.this, getResources().getString(R.string.activado) ,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity_pedidos_configuracion.this, getResources().getString(R.string.desactivado) ,Toast.LENGTH_SHORT).show();
                }

            }
        });

        aSwitch_RetitoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(aSwitch_RetitoLocal.isChecked()){
                    Toast.makeText(MainActivity_pedidos_configuracion.this, getResources().getString(R.string.activado) ,Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity_pedidos_configuracion.this, getResources().getString(R.string.desactivado) ,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Button_Guardar(View view){

        // Firebase
        DocumentReference documentReference_Conf_PEdido=firestore.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection( getResources().getString(R.string.DB_CONFIGURACION) ).document( getResources().getString(R.string.DB_sistema_pedido) );

        // Obtenemos los datos de la configuracion
        Map<String,Object> Conf_Pedidos= new HashMap<String, Object>();
        Conf_Pedidos.put( getResources().getString(R.string.DB_delivery), aSwitch_Delivery.isChecked() );
        Conf_Pedidos.put( getResources().getString(R.string.DB_retiro_negocio),aSwitch_RetitoLocal.isChecked() );

        // Set
        documentReference_Conf_PEdido.set(  Conf_Pedidos, SetOptions.merge()  );

        // Finaliza el activiry
        finish();


    }
}
