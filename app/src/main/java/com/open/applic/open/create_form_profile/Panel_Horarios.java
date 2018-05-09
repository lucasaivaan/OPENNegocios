package com.open.applic.open.create_form_profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_horario;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_horario;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;

import java.util.ArrayList;
import java.util.List;



public class Panel_Horarios extends AppCompatActivity {

    private  String ID=null;
    private TextView textViewNoti_sinHorario;

    // FIRESTORE FB
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    //----Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual


    //Referencia para acceso de DB)
    private DatabaseReference DataBaseRef= FirebaseDatabase.getInstance().getReference();

    ////////////////////////////////////// Horarios ///////////////////////////////////////////////

    public RecyclerView recyclerViewHorarios;
    public List<adapter_horario> adapterHorarios;
    public adapter_recyclerView_horario adapterRecyclerViewHorarios;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private String ID_NEGOCIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_horarios);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Datos APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);

        //Reference
        textViewNoti_sinHorario= findViewById(R.id.textViewNoti_sinHorario);



        if(!ID_NEGOCIO.equals("null")){ID=firebaseUser.getUid();}
        else {ID=ID_NEGOCIO;}
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Carga los datos en el el recyclerView
        LoadDateFirebase();
    }

    public void LoadDateFirebase() {

        ////////////////////////////////////Adaptador Navigation  Horarios //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewHorarios = (RecyclerView) findViewById(R.id.recyclerView_Horarios);
        recyclerViewHorarios.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterHorarios = new ArrayList<>();
        adapterRecyclerViewHorarios = new adapter_recyclerView_horario(adapterHorarios);
        adapterRecyclerViewHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                //----------------------------------------Alert Dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Panel_Horarios.this);
                builder1.setMessage(R.string.pregunta_eliminar_horario);
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference databaseReferenceFelete = FirebaseDatabase.getInstance().getReference();


                        //////////////////  Eliminada horario seleccionanda ////////////////////////////////////



                        final String valueIDitemHOra = adapterHorarios.get(recyclerViewHorarios.getChildAdapterPosition(view)).getId();
                        databaseReferenceFelete.child( getString(R.string.DB_NEGOCIOS) ).child( ID ).child( getString(R.string.DB_HORARIOS) ).child(valueIDitemHOra).removeValue();
                        ////////////////////////////////////////////////////////////////////////////////////////////
                        dialog.cancel();
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
                //----------------------------------------------Fin Alert Dialog


            }
        });
        recyclerViewHorarios.setAdapter(adapterRecyclerViewHorarios);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference collecHorario = db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID).collection( getString(R.string.DB_HORARIOS) );
        collecHorario.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //---vacia la lista  recyclerView
                    adapterHorarios.removeAll(adapterHorarios);
                    textViewNoti_sinHorario.setVisibility(View.VISIBLE);

                    for(DocumentSnapshot doc:task.getResult()){
                        if(doc.exists()){
                            //---Carga todos los datos en el adaptador de Servicios al adaptador
                            adapter_horario adapterServicios = doc.toObject(adapter_horario.class);

                            adapterServicios.setId(doc.getId());
                            adapterHorarios.add(adapterServicios);
                            textViewNoti_sinHorario.setVisibility(View.GONE);
                        }

                    }
                    adapterRecyclerViewHorarios.notifyDataSetChanged();
                }

            }
        });


    }

    public void ButtonNewHors(View view){
        //---Launch activity
        Intent Lanzador1=new Intent(Panel_Horarios.this,Configuracion_Horario.class);
        startActivity(Lanzador1);

    }
    public void ButtonFinish(View view){
        //Finish Activity
        finish();
    }
    public void onBackPressed() {
        //---Finish Activity
        finish();
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
