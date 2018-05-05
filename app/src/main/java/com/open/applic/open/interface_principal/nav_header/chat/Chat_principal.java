package com.open.applic.open.interface_principal.nav_header.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.nav_header.chat.adaptador.adapter_recyclerView_Chat;

import java.util.ArrayList;
import java.util.List;

public class Chat_principal extends AppCompatActivity {

    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore CloudFirestoreDBcliente=FirebaseFirestore.getInstance();
    private FirebaseFirestore CloudFirestoreDBcliente2=FirebaseFirestore.getInstance();


    ////////////////////////////////////// CLIENTES ///////////////////////////////////////////////
    public RecyclerView recyclerViewChat;
    public List<adapter_perfil_clientes> List_perfil_cliente;
    public adapter_recyclerView_Chat adapterRecyclerViewChat;


      //----Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual


    //imagenView
    private ImageView imageView_chat;
    String valueMessage="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_principal);

        setTitle(getResources().getString(R.string.chat_clientes));

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---Reference
        imageView_chat=(ImageView) findViewById(R.id.imageView_chat);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LoadChat();
    }

    private void LoadChat(){


        ////////////////////////////////////Adaptador Navigation  Clientes //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewChat =(RecyclerView) findViewById(R.id.recyclerView_Chat_principal);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        List_perfil_cliente =new ArrayList<>();
        adapterRecyclerViewChat =new adapter_recyclerView_Chat(List_perfil_cliente,this);
        adapterRecyclerViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idClient= List_perfil_cliente.get(recyclerViewChat.getChildAdapterPosition(view)).getId();

                //---Lanzador de activity Auth
                Intent Lanzador1=new Intent(Chat_principal.this,Chat_view.class);
                Lanzador1.putExtra("parametroIdClient",idClient);
                startActivity(Lanzador1);

            }});
        recyclerViewChat.setAdapter(adapterRecyclerViewChat);

        CloudFirestoreDBcliente2
                .collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid()).collection( getString(R.string.DB_CLIENTES) )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            List_perfil_cliente.removeAll(List_perfil_cliente);
                            imageView_chat.setVisibility(View.VISIBLE) ; //Habilita la imagen de fonfo

                            for (DocumentSnapshot docChat:task.getResult()){
                                if (docChat.getString( getString(R.string.DB_ultimo_mensaje) ) != null) {
                                    //--Adaptador del cliente
                                    adapter_perfil_clientes adapter = docChat.toObject(adapter_perfil_clientes.class);
                                    //---Si es cliente del negocio carga los datos
                                    if (adapter != null ) {

                                        //Notifica si ahi o no nuevo mensaje entante
                                        Boolean value=docChat.getBoolean(  getString(R.string.DB_mensaje_nuevo)  );
                                        if(value==null){value=false;}
                                        adapter.setMensaje_nuevo(value);

                                        //Si ahi mensajes muestra la conversacion
                                        if (docChat.getString(  getString(R.string.DB_ultimo_mensaje)  ) != null) {
                                            adapter.setTelefono(docChat.getString(  getString(R.string.DB_ultimo_mensaje)  ));
                                            adapter.setId(docChat.getId());
                                            List_perfil_cliente.add(adapter);
                                            imageView_chat.setVisibility(View.GONE);

                                        }
                                    }
                                    adapterRecyclerViewChat.notifyDataSetChanged();
                                }

                            }
                        }

                    }
                });





    }
    //----Carga los datos de la listas de los clientes del negocio
    private void load_recycler(final String idCliente, final Boolean EstadoMesagge,final String messae) {

        DocumentReference docRef=CloudFirestoreDBcliente.collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if(documentSnapshot.exists()){

                    List_perfil_cliente.removeAll(List_perfil_cliente);
                    //--Adaptador del cliente
                    adapter_perfil_clientes adapter =documentSnapshot.toObject(adapter_perfil_clientes.class);

                    //---Si es cliente del negocio carga los datos
                    if (adapter != null ) {
                        //---muersta los datos
                        adapter.setId(idCliente);
                        adapter.setMensaje_nuevo(EstadoMesagge);
                        adapter.setTelefono(messae); // valor del mensaje


                        if(messae != null){
                            List_perfil_cliente.add(adapter);
                            adapterRecyclerViewChat.notifyDataSetChanged();
                            imageView_chat.setVisibility(View.GONE) ; //Desabilita la imagen de fonfo

                        }
                    }



                }
            }
        });
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
