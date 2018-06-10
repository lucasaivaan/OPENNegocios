package com.open.applic.open.create_form_profile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.open.applic.open.R;
import com.open.applic.open.create_form_profile.adaptadores.adapter_categoriaNegocio;
import com.open.applic.open.create_form_profile.adaptadores.adapter_recyclerView_CategoriasNegocios;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_cuenta;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.PuntosCuenta;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Add_info_profile extends AppCompatActivity {


    // CREate Cuenta
    private CircleImageView circleImageViewPerfil;
    private EditText editTextNombre;
    private EditText editTextEmail;
    private ProgressBar progressBar_foto;
    private Map<String, Object> MapCuentaPerfil = new HashMap<>();
    private Uri urlDescargarFoto;
    private Spinner SpinnerProfile_cuenta;
    private adapter_perfil_cuenta adapterPerfilCuenta=new adapter_perfil_cuenta();

    public  SharedPreferences misPreferencias ;



    //------------------------------- CLUD FIRESTORE -----------------------------------------------
    //--------------------- Firebase AUTH ----------------------------------------------------------
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore db =FirebaseFirestore.getInstance();

    // Datos del negocio
    private adapter_profile_negocio datos_PerfilNegocio;

    //---Double Latitud y Longitud
    public double Latitud=0;
    public  double Longitud=0;

    //----Firebase AUTH
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual


    //--EditText
    private EditText eProfile_name;
    private EditText eProfile_descripcion;
    private Spinner SpinnerProfile_Pais;
    private EditText eProfile_direccion;
    private EditText eProfile_code_postal;
    private Spinner SpinnerProfile_provincia;
    private EditText eProfile_ciudad;
    private EditText eProfile_telefono;
    private EditText eProfile_categoria;
    private EditText eProfile_sitio_web;

    //--------------------- Add Foto ---------------------------------------------------------------
    //-Storage
    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private CircleImageView circleImageViewProfile;


    ///magenView
    private ImageView imageView_Categoria;
    private TextView textview_nombre_categoria;

    //Layout View
    private LinearLayout linearLayoutCuenta;
    private LinearLayout linearLayoutInfo1;
    private LinearLayout linearLayoutInfo2;
    private LinearLayout linearLayoutCategoria;
    private LinearLayout linearLayoutDescripcion;
    private LinearLayout linearLayoutUbicacion;
    //LinealLayout Button Categoria
    private LinearLayout buttonCatMaxiKiosco;
    private LinearLayout buttonCatKiosco;
    private LinearLayout buttonCatAlmacen;
    private LinearLayout buttonCatMercado;
    private LinearLayout buttonCatPizzeria;
    private LinearLayout buttonCatCarniceria;
    private LinearLayout buttonCatVerduleria;
    private LinearLayout buttonCatPanaderia;
    private LinearLayout buttonCatPescaderia;
    private LinearLayout buttonCatFerreteria;
    private LinearLayout buttonCatHeladeria;

    //Button
    private Button button1;
    private Button button2;
    private Button button3;
    private  Button buttonContinuar;

    //AlertDialg
    private AlertDialog alertDialogClient;
    private ProgressBar progressBar;


    //---
    private String ID_NEGOCIO;
    private String sPAIS;


    ////////////////////////////////////// Views Categorias Negocios ///////////////////////////////////////////////
    public RecyclerView recyclerViewNegocios_categorias;
    public List<adapter_categoriaNegocio> adapter_categorias_negocios;
    public adapter_recyclerView_CategoriasNegocios adapterRecyclerView_categorias_negocios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_perfil);
        setTitle(R.string.perfil);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().hide();

        SharePreferencesAPP.setID_USUARIO(null,Add_info_profile.this);
        SharePreferencesAPP.setID_NEGOCIO(null,Add_info_profile.this);
        SharePreferencesAPP.setPAIS(null,Add_info_profile.this);



        //---Reference EditText
        datos_PerfilNegocio=new adapter_profile_negocio();
        progressBar=(ProgressBar) findViewById(R.id.progressBar8);
        progressBar.setVisibility(View.GONE);
        //perfil
        eProfile_name = (EditText) findViewById(R.id.eProfile_name);
        eProfile_descripcion = (EditText) findViewById(R.id.eProfile_descripcion);
        SpinnerProfile_Pais = (Spinner) findViewById(R.id.spinner_paises);
        eProfile_direccion = (EditText) findViewById(R.id.eProfile_direccion);
        eProfile_code_postal = (EditText) findViewById(R.id.eProfile_code_postal);
        SpinnerProfile_provincia = (Spinner) findViewById(R.id.spinner_provincias);
        eProfile_ciudad = (EditText) findViewById(R.id.eProfile_ciudad);
        eProfile_telefono = (EditText) findViewById(R.id.eProfile_telefono);
        eProfile_sitio_web = (EditText) findViewById(R.id.eProfile_sitio_web);
        //LinealLayout
        linearLayoutCuenta= (LinearLayout) findViewById(R.id.linearLayoutCuenta);
        linearLayoutInfo1 = (LinearLayout) findViewById(R.id.Layout_principal_info_1);
        linearLayoutInfo2 = (LinearLayout) findViewById(R.id.Layout_principal_info_2);
        linearLayoutCategoria = (LinearLayout) findViewById(R.id.Layout_categoria);
        linearLayoutDescripcion = (LinearLayout) findViewById(R.id.layout_descripcion);
        linearLayoutUbicacion = (LinearLayout) findViewById(R.id.Layout_ubicacion);
        //ImagenView
        imageView_Categoria = (ImageView) findViewById(R.id.imageView_Categoria);
        textview_nombre_categoria = (TextView) findViewById(R.id.textview_nombre_categoria);

        //Perfil cuenta
        circleImageViewPerfil = (CircleImageView) findViewById(R.id.nav_foto_perfil);
        editTextNombre = (EditText) findViewById(R.id.EditText_nombre);
        editTextEmail = (EditText) findViewById(R.id.EditText_correo);
        progressBar_foto=(ProgressBar) findViewById(R.id.progressBar_foto);
        progressBar_foto.setVisibility(View.GONE);
        SpinnerProfile_cuenta = (Spinner) findViewById(R.id.spinner_cuentas);


        //Button
        button1 = (Button) findViewById(R.id.button_1);
        button2 = (Button) findViewById(R.id.button_2);
        button3 = (Button) findViewById(R.id.button_3);


        // PERFIL DE USUARIO
        buttonContinuar=(Button) findViewById(R.id.button_perfil_usuario);
        buttonContinuar.setText(getString(R.string.continuar)+" (+5)");

        //C CATEGORIA
        Button button_1=(Button) findViewById(R.id.button_1);
        button_1.setText(getString(R.string.continuar)+" (+1)");

        // PERFIL DEL NEGOCIO
        Button button_2=(Button) findViewById(R.id.button_2);
        button_2.setText(getString(R.string.continuar)+" (+5)");

        // UBICACIÓN
        Button button_3=(Button) findViewById(R.id.button_3);
        button_3.setText(getString(R.string.continuar)+" (+5)");



        //----------------- Carga datos Usuario
        // Foto de perlil
        if (user.getPhotoUrl() != null) {
            urlDescargarFoto=user.getPhotoUrl();

            // carga la imagen
            LoadImagePerfil(urlDescargarFoto.toString());
        }
        // Nombre
        editTextNombre.setText(user.getDisplayName());

        // Email
        editTextEmail.setText(user.getEmail());
        //--------------------------------------}

        // Seleccionar pais
        Select_pais();


    }

    public void Layout_info_1(View view){
        linearLayoutInfo1.setVisibility(View.GONE);
        linearLayoutInfo2.setVisibility(View.VISIBLE);
        linearLayoutCategoria.setVisibility(View.GONE);
        linearLayoutDescripcion.setVisibility(View.GONE);
        linearLayoutUbicacion.setVisibility(View.GONE);
    }
    public void Layout_info_2(View view){
        linearLayoutInfo1.setVisibility(View.GONE);
        linearLayoutInfo2.setVisibility(View.GONE);
        linearLayoutCategoria.setVisibility(View.VISIBLE);
        linearLayoutDescripcion.setVisibility(View.GONE);
        linearLayoutUbicacion.setVisibility(View.GONE);
    }
    public void  Layout_0(View view){



        String data_categoriaCuenta=SpinnerProfile_cuenta.getSelectedItem().toString();
        String nombre=editTextNombre.getText().toString();
        String email=editTextEmail.getText().toString();

        if(!data_categoriaCuenta.equals(getResources().getString(R.string.selecciona_tipo_cuenta)) && !data_categoriaCuenta.equals("")){
            if(!nombre.equals("")){

                // Convierte la inicales en mayuscula
                char[] caracteres = nombre.toCharArray();
                caracteres[0] = Character.toUpperCase(caracteres[0]);
                // el -2 es para evitar una excepción al caernos del arreglo
                for (int i = 0; i < nombre.length()- 2; i++){
                    // Es 'palabra'
                    if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                        // Reemplazamos
                        caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
                }
                nombre=new String(caracteres);


                adapterPerfilCuenta.setNombre(nombre);
                adapterPerfilCuenta.setEmail(email);
                adapterPerfilCuenta.setUrlfotoPerfil(urlDescargarFoto.toString());
                adapterPerfilCuenta.setCuenta(data_categoriaCuenta);
                adapterPerfilCuenta.setTipocuenta(getString(R.string.administrador));


                PuntosCuenta.SumaPuntos(Add_info_profile.this,5,getString(R.string.perfil_completo)+"!");


                linearLayoutCuenta.setVisibility(View.GONE);
                linearLayoutCategoria.setVisibility(View.VISIBLE);
                linearLayoutDescripcion.setVisibility(View.GONE);
                linearLayoutUbicacion.setVisibility(View.GONE);

            }else{Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();}
        }else{ Toast.makeText(getApplicationContext(),R.string.selecciona_tipo_cuenta, Toast.LENGTH_SHORT).show();}

    }
    public void  Layout_1(View view){

        String data_categoria=textview_nombre_categoria.getText().toString();

        if(!data_categoria.equals(getResources().getString(R.string.debes_elegir_una_categoría)) && !data_categoria.equals("")){

            //---Guardar los datos en la database de firebase
            linearLayoutCategoria.setVisibility(View.GONE);
            linearLayoutDescripcion.setVisibility(View.VISIBLE);
            linearLayoutUbicacion.setVisibility(View.GONE);

            PuntosCuenta.SumaPuntos(Add_info_profile.this,1,"");



        }else{ Toast.makeText(getApplicationContext(),R.string.debes_elegir_una_categoría, Toast.LENGTH_SHORT).show();}

    }
    public void Layout_2(View view){

        //----extraccion de datos
        String data_name=eProfile_name.getText().toString();
        String data_descripcion=eProfile_descripcion.getText().toString();
        String data_telefono=eProfile_telefono.getText().toString();
        String data_sitio_web=eProfile_sitio_web.getText().toString();





        if(!data_name.equals("")){

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
            // guarda los datos del perfil
            datos_PerfilNegocio.setNombre_negocio(data_name.toUpperCase());
            datos_PerfilNegocio.setDescripcion(data_descripcion);
            datos_PerfilNegocio.setTelefono(data_telefono);
            datos_PerfilNegocio.setSitio_web(data_sitio_web);
            datos_PerfilNegocio.setId(user.getUid());

            PuntosCuenta.SumaPuntos(Add_info_profile.this,5,getString(R.string.perfil_negocio_completo)+"!");



            // Contro de visibilidadd
            linearLayoutCategoria.setVisibility(View.GONE);
            linearLayoutDescripcion.setVisibility(View.GONE);
            linearLayoutUbicacion.setVisibility(View.VISIBLE);

        }else{ Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();}

    }
    public void Layout_3(View view){
        //----extraccion de datos
        final String data_Pais= SpinnerProfile_Pais.getSelectedItem().toString();;
        final String data_direccion=eProfile_direccion.getText().toString();
        String data_code_postal=eProfile_code_postal.getText().toString();
        final String data_provincia= SpinnerProfile_provincia.getSelectedItem().toString();
        final String data_ciudad=eProfile_ciudad.getText().toString();

        //---Comprobacion que los datos no esten vacios
        if(!data_Pais.equals("") && !data_direccion.equals("")
                && !data_code_postal.equals("") && !data_provincia.equals("") && !data_ciudad.equals("")){
            //start Progressbar
            progressBar.setVisibility(View.VISIBLE);

            //Set geolocation
            datos_PerfilNegocio.setPais(data_Pais.toUpperCase());
            datos_PerfilNegocio.setDireccion(data_direccion.toUpperCase());
            datos_PerfilNegocio.setCodigo_postal(data_code_postal.toUpperCase());
            datos_PerfilNegocio.setProvincia(data_provincia.toUpperCase());
            datos_PerfilNegocio.setCiudad(data_ciudad.toUpperCase());

            try {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {



                        // Geolocalizacion
                        String value=data_Pais+" "+data_provincia+" "+data_ciudad+" "+data_direccion;
                        new Add_info_profile.GetCoordinatess().execute(value);

                    }
                }, 500);// Tiempo de espera
            } catch (Exception e) { }




        }else{ Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();}

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


    public void ViewSelectCategoria(View v){


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_categoria, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_info_profile.this);
        builder.setView(dialoglayout);
        alertDialogClient=builder.show();
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

                //Imagen
                Context context = imageView_Categoria.getContext();
                Glide.with(context)
                        .load(adapterCategoriaNegocio.getLogo())
                        .fitCenter()
                        .centerCrop()
                        .into(imageView_Categoria);

                //Textviw
                textview_nombre_categoria.setText( adapterCategoriaNegocio.getNombre() );

                // Set Categoria
                datos_PerfilNegocio.setCategoria( adapterCategoriaNegocio.getId() );


                alertDialogClient.dismiss();
            }
        });

        recyclerViewNegocios_categorias.setAdapter(adapterRecyclerView_categorias_negocios);

        if(sPAIS== null){Select_pais();}

        // Firebase
        db.collection(  getString(R.string.DB_APP)  ).document( sPAIS ).collection( getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
    public  void ButtonConfigHors(View view){

        //--.lanzadador Activity
        Intent intent2 = new Intent (Add_info_profile.this, Panel_Horarios.class);
        startActivityForResult(intent2, 0);
    }
    public  void ButtonADDFotho(View view){

        //-metodo para seleccion una imagen en la galeria
        Intent intent=new Intent(Intent.ACTION_PICK);

        //-Tipo de elementos a seleccionar
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(intent,GALLLERY_INTENT);

    }

    private class GetCoordinatess extends AsyncTask<String,Void,String> {

        ProgressDialog dialog = new ProgressDialog(Add_info_profile.this);

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

                    // guarda los puntos ganados y muestra la notificacion
                    PuntosCuenta.SumaPuntos(Add_info_profile.this,5,getString(R.string.ubicacion)+"!");

                    // Guarda la cuenta del usuario
                    DocumentReference docREfCuenta=db.collection(getString(R.string.DB_NEGOCIOS)).document(firebaseUser.getUid()).collection(getString(R.string.DB_CUENTAS)).document(firebaseUser.getEmail());
                    docREfCuenta.set(adapterPerfilCuenta, SetOptions.merge());

                    //DB
                    db.collection(getString(R.string.DB_NEGOCIOS)).document(user.getUid())
                            .set(datos_PerfilNegocio,SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void v) {


                            //stop Progressbar
                            progressBar.setVisibility(View.GONE);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {

                                    // SharePreferences commit
                                    SharePreferencesAPP.setID_NEGOCIO(firebaseUser.getUid(),Add_info_profile.this);
                                    SharePreferencesAPP.setID_USUARIO(firebaseUser.getUid(),Add_info_profile.this);
                                    SharePreferencesAPP.setPAIS(datos_PerfilNegocio.getPais().toUpperCase(),Add_info_profile.this);

                                    //--.lanzadador Activity
                                    Intent intent2 = new Intent (Add_info_profile.this, MapsActivity_profile.class);
                                    intent2.putExtra("parametroLatitud",Latitud);
                                    intent2.putExtra("parametroLongitud",Longitud);
                                    intent2.putExtra("parametroButton",false);
                                    startActivityForResult(intent2, 0);


                                    //---finaliza activity
                                    finish();

                                }
                            }, 1500);// Tiempo de espera




                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //stop Progressbar
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Add_info_profile.this,R.string.su_inforomacion_nose_guardo,Toast.LENGTH_LONG).show(); }
                    });




                }else{

                    PuntosCuenta.RestaPuntos(5,Add_info_profile.this);
                    //stop Progressbar
                    progressBar.setVisibility(View.GONE);
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
    public void LoadImagePerfil(String uri){
        try{
            Glide.with(Add_info_profile.this)
                    .load(uri)
                    .fitCenter()
                    .centerCrop()
                    .into(circleImageViewPerfil);
        }catch (Exception ex){;}
    }

    public void Select_pais(){
        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        final Dialog dialogPais=new Dialog(Add_info_profile.this);
        dialogPais.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPais.setCancelable(true);
        dialogPais.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogPais.setContentView(R.layout.view_seleccion_paises);
        dialogPais.show();
        dialogPais.setCancelable(false);

        final Spinner spinner=(Spinner) dialogPais.findViewById(R.id.spinner_paises);
        Button button=(Button) dialogPais.findViewById(R.id.button4_aceptar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!spinner.getSelectedItem().toString() .equals(getString(R.string.selecciona_tipo_cuenta))){
                    sPAIS =spinner.getSelectedItem().toString().toUpperCase();


                    SharePreferencesAPP.setPAIS(sPAIS,Add_info_profile.this);

                    dialogPais.dismiss();

                }else {
                    Toast.makeText(Add_info_profile.this,R.string.selecciona_tipo_cuenta,Toast.LENGTH_LONG).show();}

            }
        });

    }

    public void onBackPressed() {

        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(Add_info_profile.this, MainActivity_interface_principal.class);
                startActivity(myIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ////////////////// Comprobacion que se alla seleccionado una foto //////////////////////////
        if(requestCode==GALLLERY_INTENT && resultCode==RESULT_OK){

            progressBar_foto.setVisibility(View.VISIBLE); //progressBar cargando foto de perfil
            Uri uri=data.getData();

            if(uri != null ){
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
                bitmap.compress(Bitmap.CompressFormat.JPEG,30,baos);
                final byte[] foto=baos.toByteArray();

                //-Crea la carpeta dentro del Storage
                mStorage= FirebaseStorage.getInstance().getReference();
                StorageReference filePath=mStorage.child( getString(R.string.DB_NEGOCIOS) ).child(firebaseUser.getUid()).child( getString(R.string.DB_CUENTAS) ).child(firebaseUser.getUid()).child( getString(R.string.DBS_PERFIL) ).child("fotoperfil");

                // Subida de imagen al Storage
                UploadTask uploadTask= filePath.putBytes(foto);

                //Proceso de subida del archivo
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(Add_info_profile.this,R.string.foto_actualizada,Toast.LENGTH_SHORT).show();
                        progressBar_foto.setVisibility(View.GONE);

                        //-Obtiene la url de la foto
                        urlDescargarFoto=taskSnapshot.getDownloadUrl();
                        LoadImagePerfil(urlDescargarFoto.toString());

                    }
                });
            }else{
                Toast.makeText(Add_info_profile.this,"Ocurrio un error",Toast.LENGTH_SHORT).show();

            }


        }
    }
}
