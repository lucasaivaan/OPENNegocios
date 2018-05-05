package com.open.applic.open.interface_principal.nav_header.cuentas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Clientes;
import com.open.applic.open.interface_principal.metodos_funciones.Buscardor;

import java.util.ArrayList;
import java.util.List;

import static com.open.applic.open.Splash_Login.ID_NEGOCIO;

public class Cuenta_principal_list extends AppCompatActivity {

    private TextView textView_message;
    private ImageView imageView_fondo_img;

    private String idCliente;

    public TextView textView_no_cliente;

    // --- Url
    String url_play_store=null;


    //----Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    ////////////////////////////////////// CLIENT Recycler//////////////////////////////////////////
    // Firestore
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //Adapter
    public RecyclerView recyclerViewClientes;
    public List<adapter_perfil_clientes> adapterClientesNegocios;
    public adapter_recyclerView_Clientes adapterRecyclerViewClientes;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////// CLIENTES  Alert Dialog///////////////////////////////////
    //Adapter
    public RecyclerView recyclerViewClientesAlertDialog;
    public List<adapter_perfil_clientes> adapterClientesNegociosAlerDialog;
    public adapter_recyclerView_Clientes adapterRecyclerViewClientesAlertDialog;
    public EditText edittext_seachCliente;
    //AlertDialg
    private AlertDialog alertDialogClient;
    //---TextView
    public TextView textViewNotificacionCliente;
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_launch_coordinatorlayour);
        setTitle(R.string.cuentas);

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //__________________________________________________________________________________________
        //Reference
        textView_message=(TextView) findViewById(R.id.textView_no_ahi_cuentas);
        imageView_fondo_img=(ImageView) findViewById(R.id.imageView_fondo_img);


        //----------------------------- AlertDdialog Clientes ----------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_floating_crear_cuenta);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.layout_clientes, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Cuenta_principal_list.this);
                builder.setView(dialoglayout);
                textView_no_cliente=(TextView) dialoglayout.findViewById(R.id.notificacion_no_cliente_lista);

                alertDialogClient=builder.show();

                ////////////////////////////////////////////////////////////////////////////////////
                LoadClientesAlertView(dialoglayout);
                ////////////////////////////////////////////////////////////////////////////////////
            }});

        //------------------------------------------------------------------------------------------
    }
    ////////////// AlertView carga los clientes ////////////////////////////////////////////////////
    public void LoadClientesAlertView(View inflater){

        //////////////////////////////////// AlertDialog Clientes //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        // Reference
        CardView cardViewSeach=(CardView) inflater.findViewById(R.id.cardview_seah);
        //cardViewSeach.setVisibility(View.GONE); // Desabilita la vista del buscador
        edittext_seachCliente=(EditText) inflater.findViewById(R.id.edittext_seachCliente);
        recyclerViewClientesAlertDialog =(RecyclerView) inflater.findViewById(R.id.recyclerView_clientes);

        recyclerViewClientesAlertDialog.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterClientesNegociosAlerDialog =new ArrayList<>();
        adapterRecyclerViewClientesAlertDialog =new adapter_recyclerView_Clientes(adapterClientesNegociosAlerDialog,this);
        adapterRecyclerViewClientesAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idCliente= adapterClientesNegociosAlerDialog.get(recyclerViewClientesAlertDialog.getChildAdapterPosition(view)).getId();
                //---Lanzador de activity Cuentas
                Intent Lanzador1=new Intent(Cuenta_principal_list.this,Cuenta_launchCliente.class);
                Lanzador1.putExtra("parametroIdCliente",idCliente);
                startActivity(Lanzador1);
                //Cierra AlertDialogClient
                alertDialogClient.dismiss();
            }});
        recyclerViewClientesAlertDialog.setAdapter(adapterRecyclerViewClientesAlertDialog);

        // ONCLICK
        edittext_seachCliente.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {

                if(!edittext_seachCliente.getText().toString().equals("")){

                    // Buscador
                    carga_Clientes(edittext_seachCliente.getText().toString());
                }else {
                    // Buscador
                    carga_Clientes ("");
                }
            }
        });

        // BASE DE DATOS
        CollectionReference collectClientes=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  );
        collectClientes.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adapterClientesNegociosAlerDialog.removeAll(adapterClientesNegociosAlerDialog);
                textView_no_cliente.setVisibility(View.VISIBLE); //Habilita la notificacion de que no tiene cliente en su lista


                for(DocumentSnapshot doc:documentSnapshots){


                    if(doc.getString(  getString(R.string.DB_nombrecliente)  ) != null){

                        adapter_perfil_clientes adapterClient = new adapter_perfil_clientes();
                        adapterClient.setId(doc.getId());
                        adapterClient.setNombre(doc.getString(  getString(R.string.DB_nombrecliente)  ));
                        adapterClient.setMensaje_nuevo(false);
                        adapterClient.setUrlfotoPerfil(doc.getString(  getString(R.string.DB_urlfotoPerfil)  ));

                        adapterClientesNegociosAlerDialog.add(adapterClient);
                        adapterRecyclerViewClientesAlertDialog.notifyDataSetChanged();

                        textView_no_cliente.setVisibility(View.GONE); //Desabilita notificacion

                    }else{
                        //--Adaptador del cliente
                        adapter_perfil_clientes adapter = doc.toObject(adapter_perfil_clientes.class);
                        adapter.setId(doc.getId());
                        //---muersta los datos
                        adapterClientesNegociosAlerDialog.add(adapter);

                        textView_no_cliente.setVisibility(View.GONE); //Desabilita notificacion
                    }
                }
                adapterRecyclerViewClientesAlertDialog.notifyDataSetChanged();

            }
        });

    }
    public void carga_Clientes(final String valueSeach) {

        /////////////////////////////////////  BASE DE DATOS ///////////////////////////////////////

        db.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_CLIENTES)  )
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                adapterClientesNegociosAlerDialog.removeAll(adapterClientesNegociosAlerDialog);

                for(DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){

                        //--Adaptador del cliente
                        adapter_perfil_clientes aCliente = doc.toObject(adapter_perfil_clientes.class);
                        aCliente.setId(doc.getId());

                        if(   valueSeach.equals("")  || Buscardor.Buscar( aCliente.getNombre(),valueSeach)){

                            // ADD
                            adapterClientesNegociosAlerDialog.add(aCliente);
                            adapterRecyclerViewClientesAlertDialog.notifyDataSetChanged();
                            // Clear
                            aCliente=null;
                        }

                    }
                }
                adapterRecyclerViewClientesAlertDialog.notifyDataSetChanged();

            }
        });



    }

    ////////////////////////////////// Carga los clientes que tengan cuentas ///////////////////////
    public void LoasClientCuent(){

        ////////////////////////////////////Adaptador Recycler  Clientes //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewClientes =(RecyclerView) findViewById(R.id.recyclerview_clientes_con_cuenta);
        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterClientesNegocios =new ArrayList<>();
        adapterRecyclerViewClientes =new adapter_recyclerView_Clientes(adapterClientesNegocios,this);
        adapterRecyclerViewClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idCliente= adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view)).getId();
                //---Lanzador de activity Cuentas
                Intent Lanzador1=new Intent(Cuenta_principal_list.this,Cuenta_launchCliente.class);
                Lanzador1.putExtra("parametroIdCliente",idCliente);
                startActivity(Lanzador1);
                //Cierra AlertDialogClient
            }});
        recyclerViewClientes.setAdapter(adapterRecyclerViewClientes);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////  Firestore  //////////////////////////////////////////
        CollectionReference collectionReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  );
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot doc:task.getResult()){

                        if(doc.getBoolean("cuenta") != null ){
                            if(doc.getString(  getString(R.string.DB_nombrecliente)  ) != null){
                                adapter_perfil_clientes adapterClient = new adapter_perfil_clientes();
                                adapterClient.setId(doc.getId());
                                adapterClient.setNombre(doc.getString(  getString(R.string.DB_nombrecliente)  ));
                                adapterClient.setMensaje_nuevo(false);
                                adapterClient.setUrlfotoPerfil(doc.getString(  getString(R.string.DB_urlfotoPerfil)  ));

                                adapterClientesNegocios.add(adapterClient);
                                adapterRecyclerViewClientes.notifyDataSetChanged();

                                //Visibilidad Notificacion
                                textView_message.setVisibility(View.GONE);
                                imageView_fondo_img.setVisibility(View.GONE);

                            }else{
                                //--Adaptador del cliente
                                adapter_perfil_clientes adapter = doc.toObject(adapter_perfil_clientes.class);
                                adapter.setId(doc.getId());
                                adapterClientesNegocios.add(adapter);

                                adapterRecyclerViewClientes.notifyDataSetChanged();


                                //Visibilidad Notificacion
                                textView_message.setVisibility(View.GONE);
                                imageView_fondo_img.setVisibility(View.GONE);
                            }
                        }



                    }

                    adapterRecyclerViewClientes.notifyDataSetChanged();
                }
            }
        });


    }

    /////////////////////////////////  BUTTON  /////////////////////////////////////////////////////
    public void Button_ADD_Cliente(View view){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_add_cliente, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuenta_principal_list.this);
        builder.setView(dialoglayout);
        alertDialogClient=builder.show();

        // Url Play Store
        DocumentReference docUrl=db.collection(  getString(R.string.DB_APP)  ).document(  getString(R.string.DB_INFORMACION)  );
        docUrl.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        url_play_store=doc.getString("url_open_store");
                    }
                }
            }
        });

        // Button add cliente
        Button buttonAddClient=(Button) dialoglayout.findViewById(R.id.button13);
        buttonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(url_play_store != null){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");

                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.info_compartir_mendaje_para_cliente));
                    intent.putExtra(Intent.EXTRA_TEXT, url_play_store);
                    startActivity(Intent.createChooser(intent, "Comparta con un cliente para que se contacte contigo por medio de OPEN "));
                    alertDialogClient.dismiss();
                }

            }
        });
    }
    public void Button_ADD_ClienteLocal(View view){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_add_client_local, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuenta_principal_list.this);
        builder.setView(dialoglayout);
        alertDialogClient=builder.show();

        Button buttonAddClient=(Button) dialoglayout.findViewById(R.id.button14);
        final EditText editTextClientName=(EditText)  dialoglayout.findViewById(R.id.eClient_name);
        buttonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editTextClientName.getText().equals("")){

                    // ADAPTADOR
                    adapter_perfil_clientes ClienteLocal = new adapter_perfil_clientes();
                    ClienteLocal.setLocal(true);
                    ClienteLocal.setNombre(editTextClientName.getText().toString());

                    // BASE DE DATOS
                    DocumentReference docClientLocal=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_CLIENTES)  ).document();
                    docClientLocal.set(ClienteLocal);

                }
                alertDialogClient.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Carga los clientes que tengan una cuenta creada
        LoasClientCuent();
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
