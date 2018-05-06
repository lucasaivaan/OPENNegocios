package com.open.applic.open.interface_principal.nav_header.administrador_cuenta;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_cuenta;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Cuentas;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class nav_adminitrador_cuenta extends AppCompatActivity {

    // Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Lista Cuentas
    public RecyclerView recyclerViewCuentas;
    public List<adapter_perfil_cuenta> adapterListaCuentas;
    public adapter_recyclerView_Cuentas adapterRecyclerViewCuentas;
    private Uri urlDescargarFoto;
    //-Storage
    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private CircleImageView circleImageViewProfile;


    //banner
    private CircleImageView circleImageViewFotoPerfil;
    private TextView textViewNombre;

    // Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual
    private String ID_NEGOCIO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_adminitrador_cuenta);

        //-------------------------------- Toolbar -------------------------------------------------
        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCuenta);
        setSupportActionBar(toolbar);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Datos APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);


        // ---Reference
        circleImageViewFotoPerfil=(CircleImageView) findViewById(R.id.foto_perfil_user);  // foto perfil usuario actual
        textViewNombre=(TextView) findViewById(R.id.nombre_user); // nombre del usuario actual

        CargarPerfilActual();
        CargarRecyclerView();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //-Button retroceso
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;}

        return super.onOptionsItemSelected(item);
    }


    private void CargarRecyclerView(){
        ////////////////////////////////////Adaptador Navigation  Horarios //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewCuentas =(RecyclerView) findViewById(R.id.REcyclerView_Cuentas);
        recyclerViewCuentas.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterListaCuentas =new ArrayList<>();
        adapterRecyclerViewCuentas =new adapter_recyclerView_Cuentas(adapterListaCuentas);
        adapterRecyclerViewCuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // Adaptador de perfil de cuenta
                final adapter_perfil_cuenta Peril_cuenta=adapterListaCuentas.get(recyclerViewCuentas.getChildAdapterPosition(view));

                //Crear AlertDialog Hors
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.view_create_cuenta_usuario, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(nav_adminitrador_cuenta.this);
                builder.setView(dialoglayout);

                //vista flotante
                final AlertDialog ViewDialog_createCuenta;
                ViewDialog_createCuenta=builder.show();

                // Reference
                circleImageViewProfile = (CircleImageView) dialoglayout.findViewById(R.id.nav_foto_perfil);
                final EditText editTextNombre = (EditText) dialoglayout.findViewById(R.id.EditText_nombre);
                final EditText editTextEmail = (EditText) dialoglayout.findViewById(R.id.EditText_correo);
                final Spinner SpinnerProfile_cuenta = (Spinner) dialoglayout.findViewById(R.id.spinner_cuentas);
                final Spinner SpinnerProfile_cuenta_permisos = (Spinner) dialoglayout.findViewById(R.id.spinner_permisos);

                Button buttonUpdate=(Button) dialoglayout.findViewById(R.id.button_crear);
                buttonUpdate.setText(R.string.actualizar);
                Button buttonDElete=(Button) dialoglayout.findViewById(R.id.button17_delete);
                buttonDElete.setVisibility(View.VISIBLE);

                String[] aTipoCuenta = getResources().getStringArray(R.array.cuentas);
                String[] aTipoPermiso = getResources().getStringArray(R.array.permisos);


                // Set

                // Imagen de perfil
                if(!Peril_cuenta.getUrlfotoPerfil().equals("default")){ Glide.with(dialoglayout.getContext()).load(Peril_cuenta.getUrlfotoPerfil()).fitCenter().centerCrop().into(circleImageViewProfile); }

                //Spinner
                for(int x=0;x < aTipoCuenta.length; x++){ if(Peril_cuenta.getCuenta().equals(aTipoCuenta[x])){ SpinnerProfile_cuenta.setSelection(x); } }
                for(int x=0;x < aTipoPermiso.length; x++){ if(Peril_cuenta.getTipocuenta().equals(aTipoPermiso[x])){ SpinnerProfile_cuenta_permisos.setSelection(x); } }

                // Nombre y email
                editTextNombre.setText(Peril_cuenta.getNombre());
                editTextEmail.setText(Peril_cuenta.getEmail());

                // Button update
                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String data_categoriaCuenta=SpinnerProfile_cuenta.getSelectedItem().toString();
                        String data_permisos=SpinnerProfile_cuenta_permisos.getSelectedItem().toString();
                        String nombre=editTextNombre.getText().toString();
                        String email=editTextEmail.getText().toString();
                        if(!nombre.equals("") && !email.equals("")){
                            if(!data_categoriaCuenta.equals(getResources().getString(R.string.selecciona_tipo_cuenta)) && !data_categoriaCuenta.equals("")){
                                if(!data_permisos.equals(getResources().getString(R.string.debe_elejir_tipo_permiso))){


                                    adapter_perfil_cuenta adapterPerfilCuenta=new adapter_perfil_cuenta();
                                    //setter
                                    adapterPerfilCuenta.setNombre(nombre);
                                    adapterPerfilCuenta.setEmail(email);

                                    if(urlDescargarFoto == null){adapterPerfilCuenta.setUrlfotoPerfil("default");}
                                    else { adapterPerfilCuenta.setUrlfotoPerfil(urlDescargarFoto.toString());}

                                    adapterPerfilCuenta.setCuenta(data_categoriaCuenta);
                                    adapterPerfilCuenta.setTipocuenta(data_permisos);

                                    // Firestore
                                    DocumentReference docREfCuenta=db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO).collection( getString(R.string.DB_CUENTAS) ).document(email);
                                    docREfCuenta.set(adapterPerfilCuenta, SetOptions.merge());

                                    ViewDialog_createCuenta.dismiss();


                                }else { Toast.makeText(getApplicationContext(),R.string.debe_elejir_tipo_permiso, Toast.LENGTH_SHORT).show();}

                            }else{ Toast.makeText(getApplicationContext(),R.string.selecciona_tipo_cuenta, Toast.LENGTH_SHORT).show();}

                        }else{ Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();}

                    }
                });

                // Button para eliminar la cuenta
                buttonDElete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Email=Peril_cuenta.getEmail();
                        DocumentReference docRedCuenta=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_CUENTAS)).document(Email);
                        docRedCuenta.delete();

                        ViewDialog_createCuenta.dismiss();
                    }
                });



            }});
        recyclerViewCuentas.setAdapter(adapterRecyclerViewCuentas);


        CollectionReference collecHorario = db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO).collection( getString(R.string.DB_CUENTAS) );
        collecHorario.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                //---vacia la lista  recyclerView
                adapterListaCuentas.removeAll(adapterListaCuentas);

                for(DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists() ){
                        //---Carga todos los datos en el adaptador de Servicios al adaptador
                        adapter_perfil_cuenta adapterServicios = doc.toObject(adapter_perfil_cuenta.class);

                        if(!doc.getId().equals(firebaseUser.getEmail())){
                            adapterListaCuentas.add(adapterServicios);
                        }

                    }

                }
                adapterRecyclerViewCuentas.notifyDataSetChanged();

            }
        });
    }
    private void CargarPerfilActual() {

        final DocumentReference docRefUser=db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO).collection( getString(R.string.DB_CUENTAS) ).document(firebaseUser.getEmail());
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docUser=task.getResult();
                    if(docUser.exists()){
                        adapter_perfil_cuenta cuentaUser=docUser.toObject(adapter_perfil_cuenta.class);

                        // Nombre
                        textViewNombre.setText(cuentaUser.getNombre());

                        // Imagen de perfil
                        Glide.with(nav_adminitrador_cuenta.this)
                                .load(cuentaUser.getUrlfotoPerfil())
                                .fitCenter()
                                .centerCrop()
                                .into(circleImageViewFotoPerfil);
                    }
                }
            }
        });

    }


    //______________________________________________________________________________________________
    //--- Button
    public void ButtonAgregarCuenta(View view){


        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_create_cuenta_usuario, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(nav_adminitrador_cuenta.this);
        builder.setView(dialoglayout);

        //vista flotante
        final AlertDialog ViewDialog_createCuenta;
        ViewDialog_createCuenta=builder.show();

        // Reference
        //Perfil cuenta
        circleImageViewProfile = (CircleImageView) dialoglayout.findViewById(R.id.nav_foto_perfil);
        circleImageViewProfile.setVisibility(View.GONE);
        final EditText editTextNombre = (EditText) dialoglayout.findViewById(R.id.EditText_nombre);
        final EditText editTextEmail = (EditText) dialoglayout.findViewById(R.id.EditText_correo);
        final Spinner SpinnerProfile_cuenta = (Spinner) dialoglayout.findViewById(R.id.spinner_cuentas);
        final Spinner SpinnerProfile_cuenta_permisos = (Spinner) dialoglayout.findViewById(R.id.spinner_permisos);

        Button buttonCrear=(Button) dialoglayout.findViewById(R.id.button_crear);
        buttonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data_categoriaCuenta=SpinnerProfile_cuenta.getSelectedItem().toString();
                String data_permisos=SpinnerProfile_cuenta_permisos.getSelectedItem().toString();
                String nombre=editTextNombre.getText().toString();
                String email=editTextEmail.getText().toString();
                if(!nombre.equals("") && !email.equals("")){
                    if(!data_categoriaCuenta.equals(getResources().getString(R.string.selecciona_tipo_cuenta)) && !data_categoriaCuenta.equals("")){
                        if(!data_permisos.equals(getResources().getString(R.string.debe_elejir_tipo_permiso))){


                            adapter_perfil_cuenta adapterPerfilCuenta=new adapter_perfil_cuenta();
                            //setter
                            adapterPerfilCuenta.setNombre(nombre);
                            adapterPerfilCuenta.setEmail(email);

                            if(urlDescargarFoto == null){adapterPerfilCuenta.setUrlfotoPerfil("default");}
                            else { adapterPerfilCuenta.setUrlfotoPerfil(urlDescargarFoto.toString());}

                            adapterPerfilCuenta.setCuenta(data_categoriaCuenta);
                            adapterPerfilCuenta.setTipocuenta(data_permisos);

                            // Firestore
                            DocumentReference docREfCuenta=db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO).collection( getString(R.string.DB_CUENTAS) ).document(email);
                            docREfCuenta.set(adapterPerfilCuenta, SetOptions.merge());

                            ViewDialog_createCuenta.dismiss();


                        }else { Toast.makeText(getApplicationContext(),R.string.debe_elejir_tipo_permiso, Toast.LENGTH_SHORT).show();}

                    }else{ Toast.makeText(getApplicationContext(),R.string.selecciona_tipo_cuenta, Toast.LENGTH_SHORT).show();}

                }else{ Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();}

        }
        });

    }

}
