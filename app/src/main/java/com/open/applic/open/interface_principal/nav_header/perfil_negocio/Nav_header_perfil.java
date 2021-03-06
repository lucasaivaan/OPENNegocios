package com.open.applic.open.interface_principal.nav_header.perfil_negocio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.open.applic.open.MainActivity_Auth;
import com.open.applic.open.R;
import com.open.applic.open.create_form_profile.Add_info_profile;
import com.open.applic.open.create_form_profile.HttpDataHalder;
import com.open.applic.open.create_form_profile.MapsActivity_profile;
import com.open.applic.open.create_form_profile.Panel_Horarios;
import com.open.applic.open.create_form_profile.adaptadores.adapter_categoriaNegocio;
import com.open.applic.open.create_form_profile.adaptadores.adapter_recyclerView_CategoriasNegocios;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.EliminarDatosCuenta;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.metodos_funciones.icono;
import com.open.applic.open.service.ServiseNotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class Nav_header_perfil extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout LinealLayout_categoria;
    private String ID_NEGOCIO;
    private String ID_categoria;


    //---Spinner
    private adapter_profile_negocio AdapterPerfilNegocio;
    private  adapter_categoriaNegocio categoriaNegocio;

    private LinearLayout layoutSpinnerPais;
    private LinearLayout layoutSpinnerProvincia;
    private Spinner spinnerPais;
    private Spinner spinnerProvincia;


    //---Double Latitud y Longitud
    public double Latitud=0;
    public  double Longitud=0;


    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore CloudFirestoreDB=FirebaseFirestore.getInstance();
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    //-------- signOut
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private GoogleApiClient googleApiClient;


    //--EditText
    private EditText eProfile_name;
    private TextInputLayout textInputLayoutPais;
    private EditText eProfile_Pais;
    private EditText eProfile_direccion;
    private EditText eProfile_code_postal;
    private TextInputLayout textInputLayoutProvincia;
    private EditText eProfile_provincia;
    private EditText eProfile_ciudad;
    private EditText eProfile_telefono;
    private EditText eProfile_categoria;
    private EditText eProfile_sitio_web;
    private EditText geteProfile_descripcion;
    private CircleImageView CircleImageView_Title;
    private TextInputLayout textInputLayoutCategoria;

    //imagenn
    private StorageReference storageReferenceGalery;
    public int ID_Intent_Result_ImagenPerfil=3;




    //---Button
    private Button buttonEdit;
    private Button buttonUpdate;
    private Button button_categoria1;
    private LinearLayout buttonHors;

    ////////////////////////////////////// Views Categorias Negocios ///////////////////////////////////////////////
    public RecyclerView recyclerViewNegocios_categorias;
    public List<adapter_categoriaNegocio> adapter_categorias_negocios;
    public adapter_recyclerView_CategoriasNegocios adapterRecyclerView_categorias_negocios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_header_perfil);
        setTitle(R.string.perfil);

        //Datos APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);


        //-------------------------------- Toolbar -------------------------------------------------
        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///////////////////////////////////// signOut //////////////////////////////////////////////
        //----Authentication Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //---Reference EditText
        eProfile_name=(EditText) findViewById(R.id.eProfile_name);
        eProfile_Pais=(EditText) findViewById(R.id.eProfile_Pais);
        textInputLayoutPais=(TextInputLayout) findViewById(R.id.pais);
        eProfile_direccion=(EditText) findViewById(R.id.eProfile_direccion);
        eProfile_code_postal=(EditText) findViewById(R.id.eProfile_code_postal);
        eProfile_provincia=(EditText) findViewById(R.id.eProfile_provincia);
        textInputLayoutProvincia=(TextInputLayout) findViewById(R.id.provincia);
        eProfile_ciudad=(EditText) findViewById(R.id.eProfile_ciudad);
        eProfile_telefono=(EditText) findViewById(R.id.eProfile_telefono);
        eProfile_categoria=(EditText) findViewById(R.id.eProfile_categoria);
        eProfile_sitio_web=(EditText) findViewById(R.id.eProfile_sitio_web);
        geteProfile_descripcion=(EditText) findViewById(R.id.eProfile_descripcion);
        CircleImageView_Title =(CircleImageView) findViewById(R.id.nav_imagen_perfil);
        button_categoria1=(Button) findViewById(R.id.button_categoria1);
        button_categoria1.setVisibility(View.GONE);

        layoutSpinnerPais=(LinearLayout) findViewById(R.id.spinner_pais);
        layoutSpinnerProvincia=(LinearLayout) findViewById(R.id.spinner_provincia);
        spinnerPais=(Spinner) findViewById(R.id.spinner_profile_pais);
        spinnerProvincia=(Spinner) findViewById(R.id.spinner_profile_provincia);


        textInputLayoutCategoria=(TextInputLayout) findViewById(R.id.input_layout_categoria);


        //---Reference Button
        buttonEdit=(Button) findViewById(R.id.button_edit);
        buttonUpdate=(Button) findViewById(R.id.button_update);
        buttonHors=(LinearLayout) findViewById(R.id.button_cock);

        //---Desabilita EditText FocusSable
        eProfile_name.setFocusableInTouchMode(false);
        geteProfile_descripcion.setFocusableInTouchMode(false);
        eProfile_Pais.setFocusableInTouchMode(false);
        eProfile_direccion.setFocusableInTouchMode(false);
        eProfile_code_postal.setFocusableInTouchMode(false) ;
        eProfile_provincia.setFocusableInTouchMode(false);
        eProfile_ciudad.setFocusableInTouchMode(false);
        eProfile_telefono.setFocusableInTouchMode(false);
        eProfile_categoria.setFocusableInTouchMode(false) ;
        eProfile_sitio_web.setFocusableInTouchMode(false) ;


        //---Obtenemos los datos del perfil

        DocumentReference docRef = CloudFirestoreDB.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AdapterPerfilNegocio = documentSnapshot.toObject(adapter_profile_negocio.class);

                if(AdapterPerfilNegocio !=null){

                    // ID CATEGORIA
                    ID_categoria=AdapterPerfilNegocio.getCategoria();

                    eProfile_name.setText(AdapterPerfilNegocio.getNombre_negocio());
                    geteProfile_descripcion.setText(AdapterPerfilNegocio.getDescripcion());
                    eProfile_Pais.setText(AdapterPerfilNegocio.getPais());
                    eProfile_direccion.setText(AdapterPerfilNegocio.getDireccion());
                    eProfile_code_postal.setText(AdapterPerfilNegocio.getCodigo_postal());
                    eProfile_provincia.setText(AdapterPerfilNegocio.getProvincia());
                    eProfile_ciudad.setText(AdapterPerfilNegocio.getCiudad());
                    eProfile_telefono.setText(AdapterPerfilNegocio.getTelefono());

                    eProfile_sitio_web.setText(AdapterPerfilNegocio.getSitio_web());

                    // Imagen de perfil
                    CircleImageView_Title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Float DIALOG

                            //agrega item menu
                            List<String> Menu = new ArrayList<String>();
                            Menu.add(getString(R.string.actualizar_foto));
                            Menu.add(getString(R.string.cancelar));

                            //Create sequence of items
                            final CharSequence[] charSequences = Menu.toArray(new String[Menu.size()]);
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Nav_header_perfil.this);
                            dialogBuilder.setTitle( getString(R.string.imagen_de_perfil) );
                            dialogBuilder.setItems(charSequences, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    String selectedText = charSequences[item].toString();  //Selected item in listview

                                    switch (item){
                                        case 0:
                                            storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                                            final Intent intent=new Intent(Intent.ACTION_PICK);
                                            intent.setType("image/*");

                                            //Selecciona la imagen desde la galeria del telefono

                                            ID_Intent_Result_ImagenPerfil =3;
                                            startActivityForResult(intent, ID_Intent_Result_ImagenPerfil);
                                        case 1:

                                    }
                                }
                            });
                            //Create alert dialog object via builder
                            AlertDialog alertDialogObject = dialogBuilder.create();
                            //Show the dialog
                            alertDialogObject.show();

                        }
                    });

                    //Asignacion de icono de la categoria
                    // Firebase DB categorias
                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                    firebaseFirestore.collection( getString(R.string.DB_APP) ).document( AdapterPerfilNegocio.getPais().toUpperCase() ).collection( getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( AdapterPerfilNegocio.getCategoria() )
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot=task.getResult();
                                if( documentSnapshot.exists() ){

                                    // adapter
                                    categoriaNegocio=documentSnapshot.toObject(adapter_categoriaNegocio.class);

                                    // TextView nombre de la categoria actual
                                    eProfile_categoria.setText( categoriaNegocio.getNombre() );
                                    button_categoria1.setText( categoriaNegocio.getNombre() );
                                }
                            }
                        }
                    });

                    // Condición
                    if(!AdapterPerfilNegocio.getImagen_perfil().equals("default")){

                        CircleImageView_Title.setBorderColor( Color.parseColor("#FFFFFFFF") );

                        // Carga la imagen de perfil
                        Context context=CircleImageView_Title.getContext();
                        Glide.with(context)
                                .load(AdapterPerfilNegocio.getImagen_perfil())
                                .fitCenter()
                                .centerCrop()
                                .into(CircleImageView_Title);

                    }else{

                        //Asignacion de icono de la categoria
                        // Firebase DB categorias
                        FirebaseFirestore firestore_categoria=FirebaseFirestore.getInstance();
                        firestore_categoria.collection( getString(R.string.DB_APP) ).document( AdapterPerfilNegocio.getPais().toUpperCase() ).collection( getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( AdapterPerfilNegocio.getCategoria() )
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    if( documentSnapshot.exists() ){

                                        // adapter
                                        categoriaNegocio=documentSnapshot.toObject(adapter_categoriaNegocio.class);

                                        // Glide Descarga de imagen
                                        Glide.with(getBaseContext())
                                                .load(categoriaNegocio.getLogo())
                                                .fitCenter()
                                                .centerCrop()
                                                .into(CircleImageView_Title);

                                        eProfile_categoria.setText(categoriaNegocio.getNombre());
                                    }
                                }
                            }
                        });


                    }
                }

            }
        });


        //---------------------- Spinner -----------------------------------------------------------


        LinealLayout_categoria=(LinearLayout) findViewById(R.id.LinealLayout_categoria);

    }

    public  void ButtonConfigHors(View view){

        //--.lanzadador Activity
        Intent intent2 = new Intent (Nav_header_perfil.this, Panel_Horarios.class);
        startActivityForResult(intent2, 0);
    }
    public void Button_SelectCategoria(View v){


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_categoria, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Nav_header_perfil.this);
        builder.setView(dialoglayout);
        final AlertDialog alertDialogClient=builder.show();
        alertDialogClient.setCancelable(false);

        //Reference



        recyclerViewNegocios_categorias = (RecyclerView) dialoglayout.findViewById(R.id.recyclerview_categorias);
        recyclerViewNegocios_categorias.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_categorias_negocios = new ArrayList<>();
        adapterRecyclerView_categorias_negocios = new adapter_recyclerView_CategoriasNegocios(adapter_categorias_negocios,this);
        adapterRecyclerView_categorias_negocios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view2) {

                //Extrae la id de la reseña
                final adapter_categoriaNegocio adapterCategoriaNegocio=adapter_categorias_negocios.get(recyclerViewNegocios_categorias.getChildAdapterPosition(view2));


                // ID CATEGORIA
                ID_categoria=adapterCategoriaNegocio.getId();

                //Imagen
                Glide.with(getApplicationContext())
                        .load(adapterCategoriaNegocio.getLogo())
                        .fitCenter()
                        .centerCrop()
                        .into(CircleImageView_Title);

                //Textviw
                button_categoria1.setText( adapterCategoriaNegocio.getNombre() );

                alertDialogClient.dismiss();
            }
        });

        recyclerViewNegocios_categorias.setAdapter(adapterRecyclerView_categorias_negocios);

        // Firebase
        CloudFirestoreDB.collection(  getString(R.string.DB_APP)  ).document( AdapterPerfilNegocio.getPais().toUpperCase() ).collection( getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                adapter_categorias_negocios.removeAll(adapter_categorias_negocios);


                for(DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){
                        //--Adaptador del cliente
                        adapter_categoriaNegocio categoria = doc.toObject(adapter_categoriaNegocio.class);

                        if(categoria.getNombre() != null){
                            if(!categoria.getNombre().equals("")){

                                // ADD
                                adapter_categorias_negocios.add(categoria);
                                adapterRecyclerView_categorias_negocios.notifyDataSetChanged();
                            }
                        }

                    }
                }
                adapterRecyclerView_categorias_negocios.notifyDataSetChanged();


            }
        });






    }


    public void Button_Edit(View view){
        eProfile_categoria.setVisibility(View.GONE);
        textInputLayoutPais.setVisibility(View.GONE);
        textInputLayoutProvincia.setVisibility(View.GONE);
        LinealLayout_categoria.setVisibility(View.VISIBLE);
        textInputLayoutCategoria.setVisibility(View.GONE);
        buttonHors.setVisibility(View.VISIBLE);
        layoutSpinnerPais.setVisibility(View.VISIBLE);
        layoutSpinnerProvincia.setVisibility(View.VISIBLE);
        button_categoria1.setVisibility(View.VISIBLE);


        // Pais
        String[] stringArrayPaises = getResources().getStringArray(R.array.paises);

        for(int x=0;x < stringArrayPaises.length; x++){
            if(eProfile_Pais.getText().toString().toUpperCase().equals(stringArrayPaises[x].toUpperCase())){
                spinnerPais.setSelection(x);
            }
        }

        // Provincia
        String[] stringArrayProvincias = getResources().getStringArray(R.array.provincias);
        for(int x=0;x < stringArrayProvincias.length; x++){
            if(eProfile_Pais.getText().toString().toUpperCase().equals(stringArrayProvincias[x].toUpperCase())){
                spinnerProvincia.setSelection(x);
            }
        }


        //Habilitalos editText
        eProfile_name.setFocusableInTouchMode(true);
        geteProfile_descripcion.setFocusableInTouchMode(true);
        eProfile_Pais.setFocusableInTouchMode(true);
        eProfile_direccion.setFocusableInTouchMode(true);
        eProfile_code_postal.setFocusableInTouchMode(true) ;
        eProfile_provincia.setFocusableInTouchMode(true);
        eProfile_ciudad.setFocusableInTouchMode(true);
        eProfile_telefono.setFocusableInTouchMode(true);
        eProfile_categoria.setFocusableInTouchMode(true) ;
        eProfile_sitio_web.setFocusableInTouchMode(true) ;

        //---Habilita y desabilita Button
        buttonEdit.setVisibility(View.GONE);
        buttonUpdate.setVisibility(View.VISIBLE);
    }

    public void Button_Update(View view){



        //----extraccion de datos
        String data_name=eProfile_name.getText().toString();
        String data_descripcion=geteProfile_descripcion.getText().toString();
        String data_Pais=eProfile_Pais.getText().toString();
        String data_direccion=eProfile_direccion.getText().toString();
        String data_code_postal=eProfile_code_postal.getText().toString();
        String data_provincia=eProfile_provincia.getText().toString();
        String data_ciudad=eProfile_ciudad.getText().toString();
        String data_telefono=eProfile_telefono.getText().toString();
        final String data_categoria=ID_categoria;
        String data_sitio_web=eProfile_sitio_web.getText().toString();


        //---Comprobacion que los datos no esten vacios
        if(!data_name.equals("") && !data_Pais.equals("") && !data_direccion.equals("")
                && !data_code_postal.equals("") && !data_provincia.equals("") && !data_ciudad.equals("") && !data_categoria.equals("")){

            // pone la iniciales en mayuscula
            char[] caracteres = data_name.toCharArray();
            caracteres[0] = Character.toUpperCase(caracteres[0]);
            // el -2 es para evitar una excepción al caernos del arreglo
            for (int i = 0; i < data_name.length()- 2; i++){
                // Es 'palabra'
                if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                    // Reemplazamos
                    caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
            }
            data_name=new String(caracteres);

            // pone la primera inicial en mayuscula
            if(!data_descripcion.equals("")){
                data_descripcion=Character.toUpperCase(data_descripcion.charAt(0)) + data_descripcion.substring(1,data_descripcion.length());


            }

            // Adapter

            AdapterPerfilNegocio.setNombre_negocio(data_name.toUpperCase());
            AdapterPerfilNegocio.setDescripcion(data_descripcion);
            AdapterPerfilNegocio.setPais(data_Pais.toUpperCase());
            AdapterPerfilNegocio.setDireccion(data_direccion.toUpperCase());
            AdapterPerfilNegocio.setCodigo_postal(data_code_postal.toUpperCase());
            AdapterPerfilNegocio.setProvincia(data_provincia.toUpperCase());
            AdapterPerfilNegocio.setCiudad(data_ciudad.toUpperCase());
            AdapterPerfilNegocio.setTelefono(data_telefono);
            AdapterPerfilNegocio.setCategoria(data_categoria.toUpperCase());
            AdapterPerfilNegocio.setSitio_web(data_sitio_web.toUpperCase());
            AdapterPerfilNegocio.setCategoria(data_categoria.toUpperCase());

            if(AdapterPerfilNegocio.getCardlayout() == null || AdapterPerfilNegocio.getCardlayout()==""){
                AdapterPerfilNegocio.setCardlayout("default");
            }



            //Cloud Firestore
            // Guardad los datos del negocio
            DocumentReference documentReference=CloudFirestoreDB.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
            // Set
            documentReference.set(AdapterPerfilNegocio, SetOptions.merge());




            new Nav_header_perfil.GetCoordinatess().execute(data_direccion);

        }else{
            Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();
        }

    }

    public String getColorCategoria(String NameCategoria){
        // Categorias
        String[] stringArrayCategorias = getResources().getStringArray(R.array.categorias);
        String[] stringArrayCategoriasColores = getResources().getStringArray(R.array.categoriasColores);
        String idColor=null;

        for(int x=0;x < stringArrayCategorias.length; x++){
            if(NameCategoria.equals(stringArrayCategorias[x])){
                idColor= stringArrayCategoriasColores[x];
            }
        }


        return idColor;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private class GetCoordinatess extends AsyncTask<String,Void,String> {

        ProgressDialog dialog = new ProgressDialog(Nav_header_perfil.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage(getResources().getString(R.string.geolocalizando));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;

            try {
                String address = strings[0];
                HttpDataHalder http = new HttpDataHalder();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", address);
                response = http.getHTTPhalder(url);
                return response;
            } catch (Exception ex) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject= new JSONObject(s);

                Latitud= (double) ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat");
                Longitud= (double) ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng");

                if(Longitud!=0 && Latitud!=0){
                    //--.lanzadador Activity
                    Intent intent2 = new Intent (Nav_header_perfil.this, MapsActivity_profile.class);
                    intent2.putExtra("parametroLatitud",Latitud);
                    intent2.putExtra("parametroLongitud",Longitud);
                    intent2.putExtra("parametroButton",true);
                    startActivityForResult(intent2, 0);


                    //---finaliza activity
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),R.string.error_intente_denuevo, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }


                //mPlaceDetailsText.setText(String.format("coordenadas: %s  /  %s",lat,lng));
                if(dialog.isShowing()){
                    dialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),R.string.error_intente_denuevo, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }

    public void onBackPressed() {


        //---Lanzador de activity
        Intent intent = new Intent(Nav_header_perfil.this, MainActivity_interface_principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:

                //---Lanzador de activity Auth
                Intent intent = new Intent(Nav_header_perfil.this, MainActivity_interface_principal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            case  R.id.action_eliminar_cuenta:

                new AlertDialog.Builder(Nav_header_perfil.this)
                        .setIcon(R.mipmap.ic_launcher)
                        //.setTitle(R.string.pregunta_eliminar_negocio_de_lista)
                        .setMessage(R.string.confirma_eliminacion_cuenta)
                        .setPositiveButton(R.string.si, null)
                        .setNegativeButton(R.string.cancelar, null)
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                EliminarDatosCuenta.Galeriafotos(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Clientes(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Cuentas(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Horarios(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Ofertas(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Productos(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Reseñas(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Servicios(ID_NEGOCIO,Nav_header_perfil.this);
                                EliminarDatosCuenta.Cuenta(ID_NEGOCIO,Nav_header_perfil.this);


                                try {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {


                                            ///////////////////////////////// Cerrar Sesion //////////////////////////////////////////////

                                            ///////////////////////////////// signOut //////////////////////////////////////////////
                                            firebaseAuth.signOut();
                                            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(@NonNull Status status) {
                                                    if (status.isSuccess()) {

                                                        stopService(new Intent(Nav_header_perfil.this,ServiseNotify.class));

                                                        SharePreferencesAPP.setID_USUARIO(null,Nav_header_perfil.this);
                                                        SharePreferencesAPP.setID_NEGOCIO(null,Nav_header_perfil.this);
                                                        SharePreferencesAPP.setPAIS(null,Nav_header_perfil.this);

                                                        //---Lanzador de activity Auth
                                                        Intent Lanzador1=new Intent(Nav_header_perfil.this,MainActivity_Auth.class);
                                                        startActivity(Lanzador1);
                                                        finish();

                                                    } else {Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();}}});
                                            ////////////////////////////////////////////////////////////////////////////////////////

                                        }
                                    }, 3000);// Tiempo de espera
                                } catch (Exception e) { }




                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {}
                        }).show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode== ID_Intent_Result_ImagenPerfil && resultCode==RESULT_OK ){

            Uri uri=data.getData();


            //Reduce el tamaño de la imagen
            Bitmap bitmap=null;
            try {bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            }catch (IOException e){e.printStackTrace();}

            BitmapFactory.Options bmOptions= new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds=true;
            bmOptions.inSampleSize=1;
            bmOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
            bmOptions.inJustDecodeBounds=false;

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,baos); //Calidad de la imagen
            final byte[] foto=baos.toByteArray();

            // Carga la imagen de perfil
            Context context=CircleImageView_Title.getContext();
            Glide.with(context)
                    .load(foto)
                    .fitCenter()
                    .centerCrop()
                    .into(CircleImageView_Title);

            //Referencia a la direccion donde se va a guardar la foto
            StorageReference filePath= storageReferenceGalery.child(  getString(R.string.DB_NEGOCIOS)  ).child(ID_NEGOCIO).child(  getString(R.string.DBS_PERFIL)  ).child(getString(R.string.DBS_imagen_perfil));
            UploadTask uploadTask= filePath.putBytes(foto);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //-Obtiene la url de la foto
                    Uri descargarFoto=taskSnapshot.getDownloadUrl();
                    Map<String, Object> url = new HashMap<>();
                    url.put(getString(R.string.DBS_imagen_perfil),descargarFoto.toString());

                    //-Guardar el Uri de la foto en el Profile del Negocio en un String
                    CloudFirestoreDB.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).set(url,SetOptions.merge());

                }
            });

        }
    }
}
