package com.open.applic.open.interface_principal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.open.applic.open.MainActivity_Auth;
import com.open.applic.open.R;
import com.open.applic.open.create_form_profile.Add_info_profile;
import com.open.applic.open.create_form_profile.Configuracion_Horario;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.adaptadores.adapter_horario;
import com.open.applic.open.interface_principal.adaptadores.adapter_ofertas_negocio;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_cuenta;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Clientes;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Ofertas;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Reseñas;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Servicios;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Servicios_all;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_horario;
import com.open.applic.open.interface_principal.adaptadores.adapter_reseña;
import com.open.applic.open.interface_principal.adaptadores.adapter_servicios_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.Buscardor;
import com.open.applic.open.interface_principal.metodos_funciones.PuntosCuenta;
import com.open.applic.open.interface_principal.metodos_funciones.OnSwipeTouchListener;
import com.open.applic.open.interface_principal.nav_header.perfil_negocio.MainActivity_tarjeta_negocio;
import com.open.applic.open.interface_principal.nav_header.perfil_negocio.Nav_header_perfil;
import com.open.applic.open.interface_principal.nav_header.administrador_cuenta.nav_adminitrador_cuenta;
import com.open.applic.open.interface_principal.nav_header.administrador_cuenta.nav_cuenta_perfil;
import com.open.applic.open.interface_principal.nav_header.chat.Chat_principal;
import com.open.applic.open.interface_principal.nav_header.chat.Chat_view;
import com.open.applic.open.interface_principal.nav_header.cuentas.Cuenta_principal_list;
import com.open.applic.open.interface_principal.nav_header.cuentas.Cuenta_launchCliente;
import com.open.applic.open.interface_principal.nav_header.informacion.nav_informacion;
import com.open.applic.open.interface_principal.nav_header.productos.MainActivity_productos;
import com.open.applic.open.interface_principal.nav_header.tips.nav_tips;
import com.open.applic.open.service.ServiseNotify;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.open.applic.open.Splash_Login.ID_NEGOCIO;
import static com.open.applic.open.Splash_Login.ID_PAIS;
import static com.open.applic.open.Splash_Login.ID_USUARIO;

/**
 * Created by lucas on 23/10/2017.
 */
public class MainActivity_interface_principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {


    //------------------------------- CLUD FIRESTORE -----------------------------------------------
    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private FirebaseFirestore dbFirestore =FirebaseFirestore.getInstance();
    private FirebaseFirestore dbFirestoreNegocioPerfil =FirebaseFirestore.getInstance();
    private FirebaseFirestore dbFirestoreOfertas =FirebaseFirestore.getInstance();
    private FirebaseFirestore dbFirestoreServicios =FirebaseFirestore.getInstance();
    private FirebaseFirestore dbFirestoreClientes =FirebaseFirestore.getInstance();


    public  SharedPreferences misPreferencias ;
    //-- Broadcastreceiver
    public  static  final String MENSAJE="MENSAJE";

    //Animacion Loadiing
    private LottieAnimationView lottieAnimationView_load;

    //No ahi conexion a internet
    public  TextView textViewNo_Conexion_Internet;
    private  LottieAnimationView animation_view_no_internet;


    //############################### Views Navigation #############################################

    //--Views
    private View include_home ;
    private View include_ofertas ;
    private View include_servicios;
    private View include_clientes;
    //Navegador de vistas
    private NavigationView navigationView;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    // Toolbar
    private  Toolbar toolbar;
    private EditText edittext_seachCliente;

    ////////////////////////////////////// Views OFERTAS ///////////////////////////////////////////////
    public RecyclerView recyclerViewOfertas;
    public List<adapter_ofertas_negocio> adapterProfileNegociosOfertas;
    public adapter_recyclerView_Ofertas adapterRecyclerViewOfertas;

    private TextView notificacion_no_oferta_creada;
    private static final int idIntent_addImagenOferta =2;
    public byte[] fotoOFerta;
    public ImageView circleImageViewFoto;


    ////////////////////////////////////// Views SERVICIOS ///////////////////////////////////////////////
    public RecyclerView recyclerViewServicios;
    public List<adapter_servicios_negocio> adapterServiciosNegocios;
    public adapter_recyclerView_Servicios adapterRecyclerViewServicios;

    // FloatService ADD
    private RecyclerView view_recyclerview_Servicios;
    private List<adapter_servicios_negocio> view_adapterServiciosNegocios;
    private adapter_recyclerView_Servicios_all view_adapter_recyclerView_Servicios_all;
    private adapter_servicios_negocio view_Item_Negocio=new adapter_servicios_negocio();

    private TextView notificacion_no_servicio_creada;
    //values
    private String ic_name="default";



    ////////////////////////////////////// Views CLIENTES ///////////////////////////////////////////////
    public RecyclerView recyclerViewClientes;
    public List<adapter_perfil_clientes> adapterClientesNegocios;
    public adapter_recyclerView_Clientes adapterRecyclerViewClientes;


    //---TextView
    public TextView textViewNotificacionCliente;
    //AlertDialg Add client
    private AlertDialog alertDialogClient;
    private Button buttonAddClient;
    private int intValue=0;
    private Boolean value;
    private String url_play_store;

    ////////////////////////////////////// NEGOCIO /////////////////////////////////////////////////

    private  int valueContClient;

    // Inten Result
    private static int ID_Intent_Result_ImagenPerfil =3;

    // Adaptadores de Perfiles
    public adapter_profile_negocio adaptert_Profile;
    public adapter_perfil_cuenta perfilCuenta;

    //Contenido de informacion del negocio
    private ScrollView scrollvie_contenido_Informacion;
    private LinearLayout LinealLayout_contenido_fondo_negocio;
    private CardView cardViewCountClient;

    //Title Store
    private CircleImageView CircleImageView_Title;
    private TextView textViewTitle;
    public String sCategoria;

    // Horarios
    public RecyclerView recyclerViewHorarios;
    public List<adapter_horario> adapterHorarios;
    public adapter_recyclerView_horario adapterRecyclerViewHorarios;
    //vista flotante
    private AlertDialog alertDialogHors;


    //---Galeria de fotos
    private ImageView imageViewFoto1;
    private ImageView imageViewFoto2;
    private ImageView imageViewFoto3;
    private ImageView imageViewFoto4;
    private ImageView imageViewFoto5;
    private StorageReference storageReferenceGalery;
    private static int ID_Intent_Result_addFoto =1;
    private String PhotoValueDirectUri;
    private LottieAnimationView lottieAnimationViewLoad_Galery;
    private LinearLayout Lineallayout_galery;
    private ProgressBar progressbar_load_photo;

    // Reseñas
    private TextView tNotReseña;
    private int iEstrellasTotalReseñas;

    //Fondo de layout
    private ImageView imageView_home;
    public TextView notificacion_no_cliente;

    //--Descripcion del negocio
    private TextView textViewDescripcion;


    //--TextView PERFIL
    public TextView textViewPais;
    public TextView textViewProvincia;
    public TextView textViewCiudad;
    public TextView textViewDireccion;
    public TextView textViewCodigoPostal;
    public TextView textViewTelefono;
    public TextView textViewWeb;

    //barra de información de número de clientes
    public  TextView textView_Puntos;
    public  TextView textView_NumClient;


    ///////////////////////////////////// Google Maps   ////////////////////////////////////////////
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    //Bitmap iconMaker
    private BitmapDescriptor iconKiosco;
    private BitmapDescriptor iconAlmacen;
    private BitmapDescriptor iconPizzeria;
    private Boolean Latitud;
    private Boolean Longitud;


    //////////////////////////////////////// FIREBASE  /////////////////////////////////////////////
    /* Client for accessing Google APIs */

    //Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //Referencia Firebase Database

    //-signOut- Authentication user
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    // Storage
    // Create a storage reference from our app
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface_principal_adinistardor);


        ////////////////////////////// Authentication GOOGLE ///////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { }
                else { goLogInScreen();}}};



        //-------------------------------- Toolbar -------------------------------------------------
        // (Barra de herramientas)
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CircleImageView_Title =(CircleImageView) toolbar.findViewById(R.id.imagen_perfil);
        textViewTitle=(TextView) toolbar.findViewById(R.id.title_name_store);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //------------------------------- DrawerLayout ---------------------------------------------
        //(Barra lateral de menu)
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_administrador);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ///////////////////////////// Bottom bar navigation ////////////////////////////////////////
        //(barra de botones de vistas)
        //---Reference include
        include_home = (View) findViewById(R.id.include_negocio);
        include_ofertas = (View) findViewById(R.id.include_ofertas);
        include_servicios = (View) findViewById(R.id.include_servicios);
        include_clientes = (View) findViewById(R.id.include_clientes);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        fragmentManager = getSupportFragmentManager();

        //Bottom bar navigation OnClick
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.navigation_home:
                        //Vista Informacion del Negocio
                        include_home.setVisibility(View.VISIBLE);
                        include_ofertas.setVisibility(View.GONE);
                        include_servicios.setVisibility(View.GONE);
                        include_clientes.setVisibility(View.GONE);

                        break;
                    case R.id.navigation_ofertas:
                        //Vista de ofertas
                        include_home.setVisibility(View.GONE);
                        include_ofertas.setVisibility(View.VISIBLE);
                        include_servicios.setVisibility(View.GONE);
                        include_clientes.setVisibility(View.GONE);
                        //Check_conexionInternet();

                        break;
                    case R.id.navigation_servicios:
                        //Vistas de servicios
                        include_home.setVisibility(View.GONE);
                        include_ofertas.setVisibility(View.GONE);
                        include_servicios.setVisibility(View.VISIBLE);
                        include_clientes.setVisibility(View.GONE);
                        //Check_conexionInternet();

                        break;
                    case R.id.navigation_clientes:
                        //Vista de Clientes
                        include_home.setVisibility(View.GONE);
                        include_ofertas.setVisibility(View.GONE);
                        include_servicios.setVisibility(View.GONE);
                        include_clientes.setVisibility(View.VISIBLE);
                        //Check_conexionInternet();
                        break;
                }
                return true;
            }});


        //################################# NEGOCIO LAYOUT #########################################

        //--Reference
        //Title Bar
        cardViewCountClient=(CardView) findViewById(R.id.CatdVuew_Count_Client);
        textView_NumClient=(TextView) findViewById(R.id.textView_NumClient);
        textView_Puntos=(TextView) findViewById(R.id.textView39_puntos);

        // Descripcion
        textViewDescripcion=(TextView) findViewById(R.id.textView_descripcion);

        // PROFILE Store
        textViewPais=(TextView) findViewById(R.id.textView_pais);
        textViewProvincia=(TextView) findViewById(R.id.textView_provincia);
        textViewCiudad=(TextView) findViewById(R.id.textView_ciudad);
        textViewDireccion=(TextView) findViewById(R.id.textView_direccion);
        textViewCodigoPostal=(TextView) findViewById(R.id.textView_codigo_postal);
        textViewTelefono=(TextView) findViewById(R.id.textView_telefono);
        textViewWeb=(TextView) findViewById(R.id.textView_web);


        // Galery
        imageViewFoto1=(ImageView) findViewById(R.id.button_galary_1);
        imageViewFoto2=(ImageView) findViewById(R.id.button_galary_2);
        imageViewFoto3=(ImageView) findViewById(R.id.button_galary_3);
        imageViewFoto4=(ImageView) findViewById(R.id.button_galary_4);
        imageViewFoto5=(ImageView) findViewById(R.id.button_galary_5);
        lottieAnimationViewLoad_Galery=(LottieAnimationView) findViewById(R.id.animation_view_load_galery);
        Lineallayout_galery=(LinearLayout) findViewById(R.id.Lineallayout_galery);


        //Anim Loading
        lottieAnimationView_load=(LottieAnimationView) findViewById(R.id.animation_view_load);

        //not there internet connection
        textViewNo_Conexion_Internet=(TextView) findViewById(R.id.textView_No_conexion);
        animation_view_no_internet=(LottieAnimationView) findViewById(R.id.animation_view_no_internet);



        //Image and text fond
        imageView_home =(ImageView) findViewById(R.id.imageView_home);
        imageView_home.setVisibility(View.GONE);


        //Contenido del fondo si la lista de negocio esta vacía
        scrollvie_contenido_Informacion=(ScrollView) findViewById(R.id.scrollvie_contenido_Informacion);
        LinealLayout_contenido_fondo_negocio=(LinearLayout) findViewById(R.id.LinealLayout_contenido_fondo_negocio_no_cliente);



        //----------------------------------Maps----------------------------------------------------
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity_interface_principal.this);







        ////////////////////////////////////CLIENTES ///////////////////////////////////////////////

        //------------------------------ Touch event update-----------------------------------------------
        //Deslizar dedo de arriba hacia abajo
        RecyclerView recyclerView_home=(RecyclerView) findViewById(R.id.recyclerView_clientes);
        recyclerView_home.setOnTouchListener(new OnSwipeTouchListener(MainActivity_interface_principal.this) {
            public void onSwipeBottom() {
                //---Funcion para actualizar los datos
                carga_Clientes("");
                //message
                Toast.makeText(MainActivity_interface_principal.this,R.string.actualizado, Toast.LENGTH_SHORT).show();
            }});


        //Reference
        textViewNotificacionCliente=(TextView) findViewById(R.id.textView_no_cliente);
        edittext_seachCliente=(EditText )findViewById(R.id.edittext_seachCliente);

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



        //-- Inicializar el servicio
        startService(new Intent(this, ServiseNotify.class));


        ///////////////////////////////////////  Load Data  ////////////////////////////////////////

        verificarCuenta();
        LoadReseñas();
        LoadImageGalery();
        LoadGaleryPhoto();
        cargar_Ofertas();
        carga_Servicios();
        carga_perfilNegocio();
        carga_Clientes("");

    }

    public void verificarCuenta(){

        DocumentReference docRefUsuario=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_CUENTAS)  ).document(firebaseUser.getEmail());
        docRefUsuario.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){

                    if(documentSnapshot.exists()){

                        perfilCuenta=documentSnapshot.toObject(adapter_perfil_cuenta.class);


                        if(perfilCuenta.getTipocuenta().equals(getString(R.string.administrador))){

                            // infla el navigation menu
                            navigationView = (NavigationView) findViewById(R.id.nav_view_administrador);
                            navigationView.getMenu().clear();
                            navigationView.inflateMenu(R.menu.drawer_menu_administrador);
                            View hview = navigationView.getHeaderView(0);

                            //------------- Cuenta del usuario
                            // Referencer
                            CircleImageView circleImageViewPerfil = (CircleImageView) hview.findViewById(R.id.nav_fotoPerfil);
                            TextView navEmail = (TextView) hview.findViewById(R.id.nav_email);

                            // Carga la imagen de perfil
                            if(!perfilCuenta.getUrlfotoPerfil().equals("default")){
                                try {
                                    Glide.with(MainActivity_interface_principal.this).load(perfilCuenta.getUrlfotoPerfil()).fitCenter().centerCrop().into(circleImageViewPerfil);
                                }catch (Exception ex){}
                             }else {circleImageViewPerfil.setImageResource(R.mipmap.ic_user2);}

                            //Carga el correo del usuario
                            navEmail.setText(perfilCuenta.getEmail());

                            NavigationDrawelPerfilNegocio(hview);

                        }else {
                            if(perfilCuenta.getTipocuenta().equals(getString(R.string.estandar))) {

                                // infla el navigation menu
                                navigationView = (NavigationView) findViewById(R.id.nav_view_administrador);
                                navigationView.getMenu().clear();
                                navigationView.inflateMenu(R.menu.drawer_menu_estandar);
                                View hview = navigationView.getHeaderView(0);

                                //------------- Cuenta del usuario
                                // Referencer
                                CircleImageView circleImageViewPerfil = (CircleImageView) hview.findViewById(R.id.nav_fotoPerfil);
                                TextView navEmail = (TextView) hview.findViewById(R.id.nav_email);

                                // Carga la imagen de perfil
                                if(!perfilCuenta.getUrlfotoPerfil().equals("default")){
                                    Glide.with(MainActivity_interface_principal.this).load(perfilCuenta.getUrlfotoPerfil()).fitCenter().centerCrop().into(circleImageViewPerfil);
                                }else {circleImageViewPerfil.setImageResource(R.mipmap.ic_user2);}

                                //Carga el correo del usuario
                                navEmail.setText(perfilCuenta.getEmail());

                                // carga el pergil del negocio en el nav
                                NavigationDrawelPerfilNegocio(hview);
                            }
                        }


                    }
                }

            }
        });
    }

    ///////////////////////////// Navigation ///////////////////////////////////////////////////////
    public void NavigationDrawelPerfilNegocio(View view){
        //------------------  carga los daros en el navigation -----------------------------

        //-Refrence
        final TextView navUsername = (TextView) view.findViewById(R.id.nav_id_name);
         final CircleImageView CircleImageView_nav_imagen=(CircleImageView) view.findViewById(R.id.nav_photoImageView);
         final  TextView nav_puntos=(TextView) view.findViewById(R.id.textView8_puntos);

        //--------------------------- Database -----------------------------------------------------


        DocumentReference docRef = dbFirestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){
                    //Adapter Profile store
                    adaptert_Profile = documentSnapshot.toObject(adapter_profile_negocio.class);

                    if(adaptert_Profile != null){
                        if(adaptert_Profile.getNombre_negocio() != null){

                            //ASIGNA VARIABLE GRLOBAL
                            ID_PAIS=adaptert_Profile.getPais().toUpperCase();


                            //nombre del negocio
                            navUsername.setText(adaptert_Profile.getNombre_negocio());

                            int iPuntos=0;
                            if(adaptert_Profile.getEstrellastotal() != null){ iPuntos=adaptert_Profile.getEstrellastotal() + adaptert_Profile.getPuntos(); }
                            else if(adaptert_Profile.getPuntos() != null){ iPuntos=adaptert_Profile.getPuntos(); }

                            // Puntos
                            nav_puntos.setText( Integer.toString(iPuntos) );


                            // Condición
                            if(!adaptert_Profile.getImagen_perfil().equals("default")){

                                CircleImageView_nav_imagen.setBorderColor( Color.parseColor("#FFFFFFFF") );

                                // Carga la imagen de perfil
                                Context context=CircleImageView_nav_imagen.getContext();
                                Glide.with(context)
                                        .load(adaptert_Profile.getImagen_perfil())
                                        .fitCenter()
                                        .centerCrop()
                                        .into(CircleImageView_nav_imagen);

                            }else{
                              //Asignacion de icono de la categoria
                                Context context = CircleImageView_nav_imagen.getContext();
                                int id = context.getResources().getIdentifier("logo_"+ adaptert_Profile.getCategoria(), "mipmap", context.getPackageName());
                                CircleImageView_nav_imagen.setImageResource(id);
                                CircleImageView_nav_imagen.setBorderColor( Color.parseColor("#FFFFFFFF") );


                            }


                        }else{}
                    }
                }

            }
        });



        navigationView.setNavigationItemSelectedListener(this);
        //------------------------------------------------------------------------------------
    }

    //////////////////////////// Accion Retroceso //////////////////////////////////////////////////
    @Override
    public void onBackPressed() { moveTaskToBack(true); }
    //////////////////////////// MENU Options //////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_interface_principal, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cerrar_sesion) {
            ///////////////////////////////// Cerrar Sesión ////////////////////////////////////////
            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {

                        // References
                        misPreferencias= getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = misPreferencias.edit();
                        editor.putString("email",null);
                        editor.putString("ID_NEGOCIO", null);
                        ID_NEGOCIO=null;
                        ID_USUARIO=null;
                        editor.commit();


                        // Al cerrar la sescion detiene los servicios
                        stopService(new Intent(MainActivity_interface_principal.this, ServiseNotify.class));


                        //---Lanzador de activity Auth
                        Intent Lanzador1=new Intent(MainActivity_interface_principal.this,MainActivity_Auth.class);
                        startActivity(Lanzador1);
                        finish();
                    } else {Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();}}});
            ////////////////////////////////////////////////////////////////////////////////////////
        }

        return super.onOptionsItemSelected(item);
    }
    //------------------------ comprobacion de conexion a internet ---------------------------------
    public boolean Check_conexionInternet() {
        try
        {
            if(VerificaConectividad()){
                //---Carga dataos
                carga_perfilNegocio();
                return true;
            }
            else{
                //Toast.makeText(this,"Valida tu conexión WIFI o Red de Datos y vuelve a intentarlo.", Toast.LENGTH_LONG).show();
                ControlVisivilidadContenido("NoInternet");
                return false;
            }
        }
        catch (Exception ex) { //Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
        }

        return true;
    }
    private boolean VerificaConectividad() {
        boolean _resultado = false;

        try {
            NetworkInfo _informacionRed = (((ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();

            boolean _es3g = _informacionRed.getType() == ConnectivityManager.TYPE_MOBILE;
            boolean _esWiFi = _informacionRed.getType() == ConnectivityManager.TYPE_WIFI;
            boolean _conexionEstablecida = _informacionRed.isConnectedOrConnecting();

            _resultado = ((_es3g || _esWiFi) && _conexionEstablecida) ? true : false;
        } catch (Exception ex) {

            throw ex;
        } finally {
            return _resultado;
        }
    }
////////////////////////////// Navigation  /////////////////////////////////////////////////////////

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tarjeta) {
            //--.lanzadador Activity
            Intent intent = new Intent (MainActivity_interface_principal.this,MainActivity_tarjeta_negocio.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.nav_perfil) {
            //--.lanzadador Activity
            Intent intent = new Intent (MainActivity_interface_principal.this,Nav_header_perfil.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.nav_productos) {

            //---Lanzador de activity Cuentas
            Intent intent=new Intent(MainActivity_interface_principal.this,MainActivity_productos.class);
            startActivity(intent);

        } else if (id == R.id.nav_cuentas) {

            //---Lanzador de activity Cuentas
            Intent intent=new Intent(MainActivity_interface_principal.this,Cuenta_principal_list.class);
            startActivity(intent);

        } else if (id == R.id.nav_chat) {
            //---Lanzador de activity Auth
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,Chat_principal.class);
            startActivity(Lanzador1);

        } else if (id == R.id.nav_revoke) {

            ///////////////////////////////// signOut //////////////////////////////////////////////
            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        //---Lanzador de activity Auth
                        Intent Lanzador1=new Intent(MainActivity_interface_principal.this,MainActivity_Auth.class);
                        startActivity(Lanzador1);
                        finish();
                    } else {Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();}}});
            ////////////////////////////////////////////////////////////////////////////////////////

        } else if (id == R.id.nav_info) {
        //---Lanzador de activity Auth
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,nav_informacion.class);
            startActivity(Lanzador1);
        } else if (id == R.id.nav_adminnistar_cuenta) {
            //---Lanzador de activity Auth
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,nav_adminitrador_cuenta.class);
            startActivity(Lanzador1);
        }else if (id == R.id.nav_perfil_cuenta) {
            //---Lanzador de activity Auth
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,nav_cuenta_perfil.class);
            startActivity(Lanzador1);
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //- Comprobación de conexión a internet
        Check_conexionInternet();
    }
    @Override
    protected void onPause() {
        //onPause cuendo la app se minimiza
        super.onPause();
    }
    @Override
    protected void onResume() {
        //onResume se ejecuta ante que la app empieze a correr despues de ser minimizada
        super.onResume();

    }

    //-------------------------------- SigOut ------------------------------------------------------
    private void goLogInScreen() {
        Intent intent = new Intent(this, MainActivity_Auth.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //-------------------------------- Carga Datos --------------------------------------------------------
    public void carga_perfilNegocio(){

        // FIRESTORE
        DocumentReference docRefprofile=db.collection( getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO );
        docRefprofile.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if(snapshot.exists()){

                    // Adaptador perfil de negocio
                    final adapter_profile_negocio adapterNegocioPerfil= snapshot.toObject(adapter_profile_negocio.class);


                    if(adapterNegocioPerfil.getNombre_negocio()!=null ){

                        // Control de Visibilidad
                        ControlVisivilidadContenido("perfilCompleto");
                        lottieAnimationView_load.setVisibility(View.GONE);
                        imageView_home.setVisibility(View.GONE);

                        // Puntos
                        if(adapterNegocioPerfil.getPuntos() != null){
                            if(adapterNegocioPerfil.getEstrellastotal() ==null){ adapterNegocioPerfil.setEstrellastotal(0); }
                            int valuePuntos=adapterNegocioPerfil.getPuntos() +adapterNegocioPerfil.getEstrellastotal() ;
                            textView_Puntos.setText(Integer.toString(valuePuntos));
                        }

                        // Ubicacion
                        textViewPais.setText(adapterNegocioPerfil.getPais());
                        textViewProvincia.setText(adapterNegocioPerfil.getProvincia()+",");
                        textViewCiudad.setText(adapterNegocioPerfil.getCiudad()+",");
                        textViewDireccion.setText(adapterNegocioPerfil.getDireccion());
                        textViewCodigoPostal.setText(adapterNegocioPerfil.getCodigo_postal());

                        // Descripcion
                        if(!adapterNegocioPerfil.getDescripcion().equals("")){
                            textViewDescripcion.setText(adapterNegocioPerfil.getDescripcion());
                        }else{textViewDescripcion.setText(R.string.sin_descripcion);}
                        // Telefono
                        if(!adapterNegocioPerfil.getTelefono().equals("")){
                            textViewTelefono.setText(adapterNegocioPerfil.getTelefono());
                        }else{textViewTelefono.setText(R.string.sin_telefono);}
                        // Sitio web
                        if(!adapterNegocioPerfil.getSitio_web().equals("")){
                            textViewWeb.setText(adapterNegocioPerfil.getSitio_web());
                        }else{textViewWeb.setText(R.string.sin_pagina_web);}


                        ////////////////// Asigna el icono ssegun la categoria del negocio /////////////
                        sCategoria =adapterNegocioPerfil.getCategoria();
                        if(sCategoria ==null){
                            sCategoria ="null";
                        }

                        //-------- Toolbar

                        // Nombre del negocio
                        textViewTitle.setText(adapterNegocioPerfil.getNombre_negocio());

                        // Imagen de perfil
                        if(!adapterNegocioPerfil.getImagen_perfil().equals("default")){

                            CircleImageView_Title.setBorderColor( Color.parseColor("#FFFFFFFF") );

                            // Carga la imagen de perfil
                            Context context=CircleImageView_Title.getContext();
                            Glide.with(context)
                                    .load(adapterNegocioPerfil.getImagen_perfil())
                                    .fitCenter()
                                    .centerCrop()
                                    .into(CircleImageView_Title);

                        }else{
                            //Asignacion de icono de la categoria
                            try{

                                int id = getResources().getIdentifier("logo_"+ sCategoria.toLowerCase(), "mipmap", getPackageName());
                                CircleImageView_Title.setImageResource(id);
                                CircleImageView_Title.setBorderColor( Color.parseColor("#FFFFFFFF") );

                            }catch (Exception ex){ Toast.makeText(MainActivity_interface_principal.this,ex.toString(),Toast.LENGTH_LONG).show(); }
                        }

                        CircleImageView_Title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Float DIALOG

                                List<String> Menu = new ArrayList<String>();
                                Menu.removeAll(Menu);
                                Menu.add(getString(R.string.actualizar_foto));
                                if(!adapterNegocioPerfil.getImagen_perfil().equals("default")){ Menu.add(getString(R.string.ver_foto)); }
                                Menu.add(getString(R.string.cancel));

                                //Create sequence of items
                                final CharSequence[] charSequences = Menu.toArray(new String[Menu.size()]);
                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity_interface_principal.this);
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
                                                break;
                                            case 1:
                                                //Crear AlertDialog Hors
                                                LayoutInflater inflater = getLayoutInflater();
                                                View dialoglayout = inflater.inflate(R.layout.view_galery_foto, null);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
                                                builder.setView(dialoglayout);
                                                alertDialogHors=builder.show();
                                                progressbar_load_photo=(ProgressBar) dialoglayout.findViewById(R.id.progressbar_load_photo);
                                                ImageView imageViewFoto=(ImageView) dialoglayout.findViewById(R.id.imageView_galery_foto);
                                                Button buttonDelete =(Button) dialoglayout.findViewById(R.id.button_view_foto_delete);
                                                buttonDelete.setVisibility(View.GONE);
                                                Button buttonCancel =(Button) dialoglayout.findViewById(R.id.button_view_foto_cancel);
                                                buttonCancel.setVisibility(View.GONE);

                                                Glide.with(MainActivity_interface_principal.this)
                                                        .load(adapterNegocioPerfil.getImagen_perfil())
                                                        .fitCenter()
                                                        .centerCrop()
                                                        .into(imageViewFoto);
                                                break;
                                            case 2:
                                                break;
                                        }
                                    }
                                });
                                //Create alert dialog object via builder
                                AlertDialog alertDialogObject = dialogBuilder.create();
                                //Show the dialog
                                alertDialogObject.show();

                            }
                        });



                    }else {
                        //---Esconde objeto el perfil del negocio esta incompleto
                        //Muestra el button (completar perfil)
                        ControlVisivilidadContenido("PerfilIncompleto");
                    }
                }else {
                    //---Esconde objeto el perfil del negocio esta incompleto
                    //Muestra el button (completar perfil)
                    ControlVisivilidadContenido("PerfilIncompleto");
                }


            }
        });

    }
    public void cargar_Ofertas(){

        //////////////////////////////////// Adapter Navigation Oferta //////////////////////////////////////////////////////
        notificacion_no_oferta_creada=(TextView) findViewById(R.id.notificacion_no_oferta_creada);

        //---Click en el item seleccionado
        recyclerViewOfertas =(RecyclerView) findViewById(R.id.recyclerView_ofertas);
        recyclerViewOfertas.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterProfileNegociosOfertas =new ArrayList<>();
        adapterRecyclerViewOfertas =new adapter_recyclerView_Ofertas(adapterProfileNegociosOfertas);

        adapterRecyclerViewOfertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //----------------------------------------Alert Dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity_interface_principal.this);
                builder1.setMessage(R.string.eliminar_oferta);
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Resta puntos
                        PuntosCuenta.RestaPuntos(2);

                        //---extrae la id de la Oferta
                        String Id=adapterProfileNegociosOfertas.get(recyclerViewOfertas.getChildAdapterPosition(view)).getId();

                        //////////////////  Eliminada Oferta seleccionanda ////////////////////////////////////
                        db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_OFERTAS)  ).document(Id).delete();

                        // Storage
                        StorageReference desertRef = storageRef.child(getString(R.string.DB_NEGOCIOS)).child(ID_NEGOCIO).child(getString(R.string.DB_OFERTAS)).child(Id);
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                PuntosCuenta.RestaPuntos(1);
                            }
                        });

                        dialog.cancel();
                    }});
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {dialog.cancel();}});
                AlertDialog alert11 = builder1.create();
                alert11.show();
                //----------------------------------------------Fin Alert Dialog
            }});
        recyclerViewOfertas.setAdapter(adapterRecyclerViewOfertas);


        /////////////////////////////////////  OFERTAS ///////////////////////////////////
        dbFirestoreOfertas.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_OFERTAS)  )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        adapterProfileNegociosOfertas.removeAll(adapterProfileNegociosOfertas);
                        notificacion_no_oferta_creada.setVisibility(View.VISIBLE);

                        for(DocumentSnapshot doc:documentSnapshots){

                            notificacion_no_oferta_creada.setVisibility(View.GONE);

                            //---Carga todos los datos en el adaptador Ofertas
                            adapter_ofertas_negocio ContructorItemRecycleviewOferta=doc.toObject(adapter_ofertas_negocio.class);
                            ContructorItemRecycleviewOferta.setId(doc.getId());

                            adapterProfileNegociosOfertas.add(ContructorItemRecycleviewOferta);
                        }
                        adapterRecyclerViewOfertas.notifyDataSetChanged();
                    }
                });

    }
    public void carga_Servicios(){

        ////////////////////////////////////Adaptador Navigation Servicios //////////////////////////////////////////////////////
        notificacion_no_servicio_creada=(TextView) findViewById(R.id.notificacion_no_servicio_creada);
        //---Click en el item seleccionado
        recyclerViewServicios =(RecyclerView) findViewById(R.id.recyclerView_servicios);
        recyclerViewServicios.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterServiciosNegocios =new ArrayList<>();
        adapterRecyclerViewServicios =new adapter_recyclerView_Servicios(adapterServiciosNegocios);

        adapterRecyclerViewServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //----------------------------------------Alert Dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity_interface_principal.this);
                builder1.setMessage(R.string.eliminar_servicio);
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Resta puntos
                        PuntosCuenta.RestaPuntos(5);

                        //Extrae la id del servicio
                        String Id=adapterServiciosNegocios.get(recyclerViewServicios.getChildAdapterPosition(view)).getId();

                        //////////////////  Eliminada Oferta seleccionanda ////////////////////////////////////
                        db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_SERVICIOS)  ).document(Id).delete();

                        dialog.cancel();}});
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {dialog.cancel();}});
                AlertDialog alert11 = builder1.create();
                alert11.show();
                //----------------------------------------------Fin Alert Dialog

            }});
        recyclerViewServicios.setAdapter(adapterRecyclerViewServicios);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////////////// SERVICIOS //////////////////////////////////////
        dbFirestoreServicios.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_SERVICIOS)  )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        adapterServiciosNegocios.removeAll(adapterServiciosNegocios);

                        notificacion_no_servicio_creada.setVisibility(View.VISIBLE);
                        for (DocumentSnapshot doc : documentSnapshots) {
                            notificacion_no_servicio_creada.setVisibility(View.GONE);
                            //---Carga todos los datos en el adaptador de Servicios al adaptador
                            adapter_servicios_negocio adapterServicios = doc.toObject(adapter_servicios_negocio.class);

                            adapterServicios.setId(doc.getId());  // ID del item
                            adapterServiciosNegocios.add(adapterServicios);
                        }
                        adapterRecyclerViewServicios.notifyDataSetChanged();
                    }
                });

    }
    public void carga_Clientes(final String valueSeach) {

        ///////////////////////////////Adaptador Navigation  Clientes //////////////////////////////
        notificacion_no_cliente = (TextView) findViewById(R.id.textView_no_cliente);

        //---Clickn el item seleccionado
        recyclerViewClientes = (RecyclerView) findViewById(R.id.recyclerView_clientes);
        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterClientesNegocios = new ArrayList<>();
        adapterRecyclerViewClientes = new adapter_recyclerView_Clientes(adapterClientesNegocios,this);

        adapterRecyclerViewClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view2) {

                //Adaptador perfil del cliente
                adapter_perfil_clientes adapterPerfilClientes = adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view2));

                //////////////////////////////// Cuadro de Dialog //////////////////////////////////
                final Dialog dialog = new Dialog(MainActivity_interface_principal.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.view_perfil_cliente);
                dialog.show();

                // Reference
                LinearLayout linearLayoutPhone = (LinearLayout) dialog.findViewById(R.id.lineallayout_phone);
                Button buttonChat = (Button) dialog.findViewById(R.id.button_chat);
                Button buttonCuenta = (Button) dialog.findViewById(R.id.button_cuenta);
                Button buttonDelete = (Button) dialog.findViewById(R.id.button15);
                buttonDelete.setVisibility(View.GONE);
                CircleImageView imagePerfil = (CircleImageView) dialog.findViewById(R.id.imageView_perfilcliente);
                TextView textView_Nombre = (TextView) dialog.findViewById(R.id.textView_nombre);
                final TextView textView_Telefono = (TextView) dialog.findViewById(R.id.textView_telefono);
                TextView tReseña=(TextView) dialog.findViewById(R.id.textView22_reseñasa);
                ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar2);
                LinearLayout layout_Cardstylo=(LinearLayout) dialog.findViewById(R.id.layout_stylo);
                LinearLayout linearLayoutReseña=(LinearLayout)dialog.findViewById(R.id.layout_reseñas);

                // Reseñas
                if(adapterPerfilClientes.getNumreseñas() != null){
                    linearLayoutReseña.setVisibility(View.VISIBLE);

                    int iNumeroReseña= adapterPerfilClientes.getNumreseñas().intValue();

                    tReseña.setText( iNumeroReseña +" "+getResources().getString(R.string.reseñas));

                }else{
                    linearLayoutReseña.setVisibility(View.GONE);

                }

                //stilo de tarjeta
                Context context = layout_Cardstylo.getContext();
                if(adapterPerfilClientes.getCardlayout() !=null && adapterPerfilClientes.getCardlayout() != ""){

                    if(adapterPerfilClientes.getCardlayout().equals("default")){
                        //layout_Cardstylo.setBackgroundColor(Color.parseColor(aPerfilUsuario.getColor()));
                    }else {
                        int id = context.getResources().getIdentifier( adapterPerfilClientes.getCardlayout(), "mipmap", context.getPackageName());
                        layout_Cardstylo.setBackgroundResource(id);

                        // nombre color
                        //editTextNombre.setTextColor(Color.parseColor(aPerfilUsuario.getColor()));
                    }

                }else{
                    //layout_Cardstylo.setBackgroundColor(Color.parseColor(aPerfilUsuario.getColor()));
                }






                if (adapterPerfilClientes.getLocal() == false) {

                    //------------------------------- Usuario Cliente


                    // TextVie Nombre
                    // pone la iniciales en mayuscula
                    String name = adapterPerfilClientes.getNombre();
                    char[] caracteres = name.toCharArray();
                    caracteres[0] = Character.toUpperCase(caracteres[0]);
                    // el -2 es para evitar una excepción al caernos del arreglo
                    for (int i = 0; i < name.length() - 2; i++) {
                        // Es 'palabra'
                        if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                            // Reemplazamos
                            caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
                    }
                    name = new String(caracteres);
                    textView_Nombre.setText(name);// Set EditText


                    //---- Telefono
                    if (!adapterPerfilClientes.getTelefono().equals("")) {
                        textView_Telefono.setText(adapterPerfilClientes.getTelefono());

                        // Onclick telefono
                        linearLayoutPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(Intent.ACTION_DIAL);
                                i.setData(Uri.parse("tel:" + textView_Telefono.getText().toString()));
                                startActivity(i);
                            }
                        });

                    } else { textView_Telefono.setText(getString(R.string.sin_telefono)); }

                    // Imagen
                    if (!adapterPerfilClientes.getUrlfotoPerfil().equals("default")) {

                        Glide.with(MainActivity_interface_principal.this)
                                .load(adapterPerfilClientes.getUrlfotoPerfil())
                                .fitCenter()
                                .centerCrop()
                                .into(imagePerfil);
                    } else { progressBar.setVisibility(View.GONE); }


                    buttonChat.setOnClickListener(new View.OnClickListener() {

                        // Cliente
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            //--.lanzadador Activity
                            Intent intent2 = new Intent(MainActivity_interface_principal.this, Chat_view.class);
                            intent2.putExtra("parametroIdClient", adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view2)).getId());
                            startActivityForResult(intent2, 0);
                        }
                    });

                    buttonCuenta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            //--.lanzadador Activity
                            Intent intent2 = new Intent(MainActivity_interface_principal.this, Cuenta_launchCliente.class);
                            intent2.putExtra("parametroIdCliente", adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view2)).getId());
                            startActivityForResult(intent2, 0);
                        }
                    });
                } else {

                    // ------------------------------ Cliente Local

                    // TextVie Nombre
                    textView_Nombre.setText(adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view2)).getNombre());
                    linearLayoutPhone.setVisibility(View.GONE);
                    // Cliente local
                    buttonChat.setVisibility(View.GONE);
                    buttonDelete.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                    buttonCuenta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            //--.lanzadador Activity
                            Intent intent2 = new Intent(MainActivity_interface_principal.this, Cuenta_launchCliente.class);
                            intent2.putExtra("parametroIdCliente", adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view2)).getId());
                            startActivityForResult(intent2, 0);
                        }
                    });

                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                            DocumentReference documentReference = db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO)
                                    .collection(getString(R.string.DB_CLIENTES)).document(adapterClientesNegocios.get(recyclerViewClientes.getChildAdapterPosition(view2)).getId());
                            documentReference.delete();
                        }}
                    );
                }
            }
        });
        recyclerViewClientes.setAdapter(adapterRecyclerViewClientes);


        /////////////////////////////////////  BASE DE DATOS ///////////////////////////////////////

        dbFirestoreClientes.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_CLIENTES)  )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        adapterClientesNegocios.removeAll(adapterClientesNegocios);
                        notificacion_no_cliente.setVisibility(View.VISIBLE); //Habilita la notificacion de que no tiene cliente en su lista

                        //Contador de clientes
                        valueContClient=0;
                        textView_NumClient.setText("0");


                        for(DocumentSnapshot doc:documentSnapshots){
                            if(doc.exists()){

                                notificacion_no_cliente.setVisibility(View.GONE); //Desabilita notificacion
                                //Contador de cliente
                                valueContClient+=1;
                                textView_NumClient.setText(String.valueOf(valueContClient));

                                //--Adaptador del cliente
                                adapter_perfil_clientes aCliente = doc.toObject(adapter_perfil_clientes.class);
                                aCliente.setId(doc.getId());

                                if(   valueSeach.equals("")  || Buscardor.Buscar( aCliente.getNombre(),valueSeach)){

                                    // ADD
                                    adapterClientesNegocios.add(aCliente);
                                    adapterRecyclerViewClientes.notifyDataSetChanged();
                                    // Clear
                                    aCliente=null;
                                }

                            }
                        }
                        adapterRecyclerViewClientes.notifyDataSetChanged();
                    }
                });



    }

    /////////////////////////////////// Load Galery photo //////////////////////////////////////////
    public void LoadGaleryPhoto(){

        storageReferenceGalery= FirebaseStorage.getInstance().getReference();
        final Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");


        imageViewFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefGalery=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto1");
                docRefGalery.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            PhotoValueDirectUri="foto1";
                            ViewImagenGalery(documentSnapshot.getString("id") );
                        }else{
                            //Selecciona la imagen desde la galeria del telefono
                            ID_Intent_Result_addFoto =1;
                            PhotoValueDirectUri="foto1";
                            startActivityForResult(intent, ID_Intent_Result_addFoto);
                        }
                    }
                });

            }});
         imageViewFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto2");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            ViewImagenGalery(documentSnapshot.getString("id") );
                            PhotoValueDirectUri="foto2";
                        }else{
                            //Selecciona la imagen desde la galeria del telefono
                            PhotoValueDirectUri="foto2";
                            ID_Intent_Result_addFoto =1;
                            startActivityForResult(intent, ID_Intent_Result_addFoto);
                        }
                    }
                });
            }});
        imageViewFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto3");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            PhotoValueDirectUri="foto3";
                            ViewImagenGalery(documentSnapshot.getString("id") );
                        }else{
                            //Selecciona la imagen desde la galeria del telefono
                            ID_Intent_Result_addFoto =1;
                            PhotoValueDirectUri="foto3";
                            startActivityForResult(intent, ID_Intent_Result_addFoto);
                        }
                    }
                });
            }});
        imageViewFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto4");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            PhotoValueDirectUri="foto4";
                            ViewImagenGalery(documentSnapshot.getString("id"));
                        }else{
                            //Selecciona la imagen desde la galeria del telefono
                            ID_Intent_Result_addFoto =1;
                            PhotoValueDirectUri="foto4";
                            startActivityForResult(intent, ID_Intent_Result_addFoto);
                        }
                    }
                });
            }});
        imageViewFoto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto5");
                docRefFoto2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){
                            //Genera la vista ampliada de la imagen
                            PhotoValueDirectUri="foto5";
                            ViewImagenGalery(documentSnapshot.getString("id") );
                        }else{
                            //Selecciona la imagen desde la galeria del telefono
                            ID_Intent_Result_addFoto =1;
                            PhotoValueDirectUri="foto5";
                            startActivityForResult(intent, ID_Intent_Result_addFoto);
                        }
                    }
                });
            }});



    }
    public void ViewImagenGalery(String uri){
        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_galery_foto, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
        builder.setView(dialoglayout);
        alertDialogHors=builder.show();
        progressbar_load_photo=(ProgressBar) dialoglayout.findViewById(R.id.progressbar_load_photo);
        ImageView imageViewFoto=(ImageView) dialoglayout.findViewById(R.id.imageView_galery_foto);
        Button buttonDelete =(Button) dialoglayout.findViewById(R.id.button_view_foto_delete);
        Button buttonCancel =(Button) dialoglayout.findViewById(R.id.button_view_foto_cancel);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //////////////////  Eliminada el Servicio seleccionando ////////////////////////////////////
                db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document(PhotoValueDirectUri).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                PuntosCuenta.RestaPuntos(1);
                         }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { }
                });


                if(PhotoValueDirectUri.equals("foto1")){
                    imageViewFoto1.setImageResource(R.mipmap.ic_add1);
                }else if(PhotoValueDirectUri.equals("foto2")){
                    imageViewFoto2.setImageResource(R.mipmap.ic_add1);
                }else if(PhotoValueDirectUri.equals("foto3")){
                    imageViewFoto3.setImageResource(R.mipmap.ic_add1);
                }else if(PhotoValueDirectUri.equals("foto4")){
                    imageViewFoto4.setImageResource(R.mipmap.ic_add1);
                }else if(PhotoValueDirectUri.equals("foto5")){
                    imageViewFoto5.setImageResource(R.mipmap.ic_add1);
                }

                alertDialogHors.dismiss();//Finaliza el ViewFhoto

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogHors.dismiss();//Finaliza el ViewFhoto
            }
        });


        // Carga la imagen
        try{

            //progressbar_load_photo.setVisibility(View.GONE);

            Glide.with(MainActivity_interface_principal.this)
                    .load(uri)
                    .fitCenter()
                    .centerCrop()
                    .into(imageViewFoto);
        }catch (Exception ex){}
    }
    public void LoadImageGalery(){

        //---Foto 1
        DocumentReference docRefFoto1=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto1");
        docRefFoto1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    try{


                        Glide.with(MainActivity_interface_principal.this)
                                .load(snapshot.getString("id"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto1);
                    }catch (Exception ex){}
                }

            }});

        //---Foto 2
        DocumentReference docRefFoto2=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto2");
        docRefFoto2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    try{

                        Glide.with(MainActivity_interface_principal.this)
                                .load(snapshot.getString("id"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto2);
                    }catch (Exception ex){}
                }

            }});

        //---Foto 3
        DocumentReference docRefFoto3=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto3");
        docRefFoto3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    try{
                        Glide.with(MainActivity_interface_principal.this)
                                .load(snapshot.getString("id"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto3);
                    }catch (Exception ex){}
                }

            }});

        //---Foto 4
        DocumentReference docRefFoto4=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto4");
        docRefFoto4.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    try{
                        Glide.with(MainActivity_interface_principal.this)
                                .load(snapshot.getString("id"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto4);
                    }catch (Exception ex){}
                }

            }});

        //---Foto 5
        DocumentReference docRefFoto5=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document("foto5");
        docRefFoto5.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@NonNull DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(snapshot.exists()){
                    try{
                        Glide.with(MainActivity_interface_principal.this)
                                .load(snapshot.getString("id"))
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewFoto5);
                    }catch (Exception ex){}
                }

            }});

    }

    ///////////////////////////////// RESEÑAS //////////////////////////////////////////////////////
    public void LoadReseñas(){

        //Reference
        tNotReseña=(TextView) findViewById(R.id.textView_not_reseña);
        tNotReseña.setVisibility(View.VISIBLE);
        final RecyclerView recyclerViewReseñas;
        final List<adapter_reseña> adapter_reseñas;
        final adapter_recyclerView_Reseñas adapter_recyclerView_reseñas;

        ////////////////////////////////////Adaptador Reseñas //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewReseñas =(RecyclerView) findViewById(R.id.recyclerview_reseñas);
        recyclerViewReseñas.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_reseñas =new ArrayList<>();
        adapter_recyclerView_reseñas =new adapter_recyclerView_Reseñas(adapter_reseñas);
        recyclerViewReseñas.setAdapter(adapter_recyclerView_reseñas);

        //OnClick
        adapter_recyclerView_reseñas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Extrae la id de la reseña
                final String idReseña=adapter_reseñas.get(recyclerViewReseñas.getChildAdapterPosition(v)).getIdReseña();
                String id= adapter_reseñas.get(recyclerViewReseñas.getChildAdapterPosition(v)).getId();

                // obtener la fecha y salida por pantalla con formato:
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String datoFechaActual=dateFormat.format(date);
                String datoFechaReseña= dateFormat.format(adapter_reseñas.get(recyclerViewReseñas.getChildAdapterPosition(v)).getTimestamp());


                // AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
                builder.setTitle(  getString(R.string.DB_RESEÑAS)  );

                // si la id de la reseña es igual que la id del usuario (True)
                if (id.equals(ID_NEGOCIO)) {

                    // 2 Opciones
                    CharSequence opciones1[] = new CharSequence[]{"Editar", "Eliminar"};
                    CharSequence opciones2[] = new CharSequence[]{"Eliminar"};

                    if(datoFechaActual.equals(datoFechaReseña)){

                        builder.setItems(opciones1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if(item == 0){

                                    Toast.makeText(MainActivity_interface_principal.this,"Editar",Toast.LENGTH_SHORT).show();

                                }else if(item == 1){
                                    DocumentReference documentReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_RESEÑAS)  ).document(idReseña);
                                    documentReference.delete();
                                }
                            }
                        });
                        builder.show();

                    }else  if(datoFechaActual.equals(datoFechaReseña)){

                        builder.setItems(opciones2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if(item == 0){

                                    DocumentReference documentReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_RESEÑAS)  ).document(idReseña);
                                    documentReference.delete();

                                }
                            }
                        });
                        builder.show();
                    }

                }else {

                    //--------------------- Card View
                    // ID Cliente
                    ViewCardCliete( adapter_reseñas.get(recyclerViewReseñas.getChildAdapterPosition(v)).getId());

                }
            }
        });

        // ------------------- Carga lla lista de objetos en el recyclerView
        // Firestore
        CollectionReference AddReseñaNegocio=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_RESEÑAS)  );
        AddReseñaNegocio.orderBy(  getString(R.string.DB_timestamp)  , Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                adapter_reseñas.removeAll(adapter_reseñas);

                iEstrellasTotalReseñas=0;
                for (DocumentSnapshot doc : querySnapshot) {
                    tNotReseña.setVisibility(View.GONE);

                    //Adaptador objeto reseña
                    adapter_reseña adapterReseñ_a=doc.toObject(adapter_reseña.class);
                    if(adapterReseñ_a.getEstrellas() != null){
                        iEstrellasTotalReseñas+=adapterReseñ_a.getEstrellas();
                    }



                    adapterReseñ_a.setIdReseña(doc.getId());
                    adapter_reseñas.add(adapterReseñ_a);

                }

                // Create a new user with a first and last name
                Map<String, Object> Estrellas = new HashMap<>();
                Estrellas.put(getString(R.string.db_estrellastotal),iEstrellasTotalReseñas);

                // Firestore
                DocumentReference collectionReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);
                collectionReference.set(Estrellas, SetOptions.merge());

                adapter_recyclerView_reseñas.notifyDataSetChanged();
            }
        });

    }
    public void ButtonPublicarReseña(View view){
        EditText editTextReseña=(EditText) findViewById(R.id.editText_reseña);

        if(!editTextReseña.getText().toString().equals("")){

            // pone la primera inicial en mayuscula
            String ValueReseña=editTextReseña.getText().toString();
            ValueReseña=Character.toUpperCase(ValueReseña.charAt(0)) + ValueReseña.substring(1,ValueReseña.length());


            // informacion de la reseña
            Map<String , Object> reseñas=new HashMap<>();
            reseñas.put("nombre",adaptert_Profile.getNombre_negocio());
            reseñas.put("fotoPerfil",adaptert_Profile.getCategoria());
            reseñas.put("reseña",ValueReseña);
            reseñas.put("timestamp", FieldValue.serverTimestamp());
            reseñas.put("id",ID_NEGOCIO);

            // Firestore
            CollectionReference AddReseñaNegocio=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_RESEÑAS)  );
            AddReseñaNegocio.document().set(reseñas).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) { }
            });
            editTextReseña.setText("");
        }

    }
    public void ViewCardCliete(final String idCliente){

        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        final Dialog dialog=new Dialog(MainActivity_interface_principal.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.view_perfil_cliente);
        dialog.show();

        // Reference
        LinearLayout linearLayoutPhone=(LinearLayout) dialog.findViewById(R.id.lineallayout_phone);
        final Button buttonChat=(Button) dialog.findViewById(R.id.button_chat);
        final Button buttonCuenta=(Button) dialog.findViewById(R.id.button_cuenta);
        Button buttonDelete=(Button) dialog.findViewById(R.id.button15);
        buttonDelete.setVisibility(View.GONE);
        final CircleImageView imagePerfil =(CircleImageView) dialog.findViewById(R.id.imageView_perfilcliente);
        final TextView textView_Nombre=(TextView) dialog.findViewById(R.id.textView_nombre);
        final TextView textView_Telefono=(TextView) dialog.findViewById(R.id.textView_telefono);
        final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.progressBar2);


        DocumentReference docClient=db.collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
        docClient.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        final adapter_perfil_clientes perfil=doc.toObject(adapter_perfil_clientes.class);

                        //---- Carga imagen de perfil
                        if(!perfil.getUrlfotoPerfil().equals("default")){

                            Glide.with(MainActivity_interface_principal.this)
                                    .load(perfil.getUrlfotoPerfil())
                                    .fitCenter()
                                    .centerCrop()
                                    .into(imagePerfil);

                        }else{
                            progressBar.setVisibility(View.GONE);
                        }

                        //--- NOMBRE
                        // pone la iniciales en mayuscula
                        String name =perfil.getNombre();
                        char[] caracteres = name.toCharArray();
                        caracteres[0] = Character.toUpperCase(caracteres[0]);
                        // el -2 es para evitar una excepción al caernos del arreglo
                        for (int i = 0; i < name.length()- 2; i++){
                            // Es 'palabra'
                            if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',')
                                // Reemplazamos
                                caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);}
                        name=new String(caracteres);
                        textView_Nombre.setText(name);

                        //---- Telefono
                        if(!perfil.getTelefono().equals("")){
                            textView_Telefono.setText(perfil.getTelefono());
                        }else{ textView_Telefono.setText(  getString(R.string.sin_telefono)  );}



                        //---- Button

                        buttonChat.setOnClickListener(new View.OnClickListener() {

                            // Cliente
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                //--.lanzadador Activity
                                Intent intent2 = new Intent (MainActivity_interface_principal.this,Chat_view.class);
                                intent2.putExtra("parametroIdClient",idCliente);
                                startActivityForResult(intent2, 0);
                            }});

                        buttonCuenta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                //--.lanzadador Activity
                                Intent intent2 = new Intent (MainActivity_interface_principal.this,Cuenta_launchCliente.class);
                                intent2.putExtra("parametroIdCliente",idCliente);
                                startActivityForResult(intent2, 0);
                            }});

                    }
                }

            }
        });
    }
    //////////////////////////////// Control de Visibilidad de contenido ///////////////////////////
    public void ControlVisivilidadContenido(String parametro){
        if(parametro.equals("NoCliente")){
            scrollvie_contenido_Informacion.setVisibility(View.GONE);
            cardViewCountClient.setVisibility(View.GONE);
            LinealLayout_contenido_fondo_negocio.setVisibility(View.VISIBLE);
            textViewNo_Conexion_Internet.setVisibility(View.GONE);
            animation_view_no_internet.setVisibility(View.GONE);


        }else if(parametro.equals("NoInternet")){

            scrollvie_contenido_Informacion.setVisibility(View.GONE);
            cardViewCountClient.setVisibility(View.GONE);
            LinealLayout_contenido_fondo_negocio.setVisibility(View.GONE);
            textViewNo_Conexion_Internet.setVisibility(View.VISIBLE);
            animation_view_no_internet.setVisibility(View.VISIBLE);
            lottieAnimationView_load.setVisibility(View.GONE);
            notificacion_no_oferta_creada.setVisibility(View.GONE);
            notificacion_no_servicio_creada.setVisibility(View.GONE);

            include_ofertas.setVisibility(View.GONE);
            include_servicios.setVisibility(View.GONE);
            include_clientes.setVisibility(View.GONE);


        }else if(parametro.equals("PerfilIncompleto")){



            scrollvie_contenido_Informacion.setVisibility(View.GONE);
            cardViewCountClient.setVisibility(View.GONE);
            LinealLayout_contenido_fondo_negocio.setVisibility(View.VISIBLE);
            textViewNo_Conexion_Internet.setVisibility(View.GONE);
            animation_view_no_internet.setVisibility(View.GONE);
            lottieAnimationView_load.setVisibility(View.GONE);
            imageView_home.setVisibility(View.VISIBLE);

            Intent myIntent = new Intent(MainActivity_interface_principal.this, Add_info_profile.class);
            startActivity(myIntent);
            finish();



        }else{


            scrollvie_contenido_Informacion.setVisibility(View.VISIBLE);

            cardViewCountClient.setVisibility(View.VISIBLE);
            LinealLayout_contenido_fondo_negocio.setVisibility(View.GONE);
            textViewNo_Conexion_Internet.setVisibility(View.GONE);
            animation_view_no_internet.setVisibility(View.GONE);


        }
    }

/////////////////////////////////////  Button //////////////////////////////////////////
    public void ButtonProductos(View view){
        //---Lanzador de activity Cuentas
        Intent intent=new Intent(MainActivity_interface_principal.this,MainActivity_productos.class);
        startActivity(intent);
    }
    public void ButtonHorarios(View view){

        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.activity_panel_horarios, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
        builder.setView(dialoglayout);
        alertDialogHors=builder.show();

        ////////////////////////////////////Adaptador Navigation  Horarios //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewHorarios =(RecyclerView) dialoglayout.findViewById(R.id.recyclerView_Horarios);
        recyclerViewHorarios.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapterHorarios =new ArrayList<>();
        adapterRecyclerViewHorarios =new adapter_recyclerView_horario(adapterHorarios);
        adapterRecyclerViewHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //----------------------------------------Alert Dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity_interface_principal.this);
                builder1.setMessage(R.string.pregunta_eliminar_horario);
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //////////////////  Eliminada horario seleccionanda ////////////////////////////////////
                        String ID = adapterHorarios.get(recyclerViewHorarios.getChildAdapterPosition(view)).getId();

                        DocumentReference documentReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO)
                                .collection(  getString(R.string.DB_HORARIOS)  ).document(ID);
                        documentReference.delete();
                        ////////////////////////////////////////////////////////////////////////////////////////////
                        dialog.cancel();}});
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {dialog.cancel();}});
                AlertDialog alert11 = builder1.create();
                alert11.show();
                //----------------------------------------------Fin Alert Dialog
            }});
        recyclerViewHorarios.setAdapter(adapterRecyclerViewHorarios);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        CollectionReference collecHorario = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_HORARIOS)  );
        collecHorario.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                //---vacia la lista  recyclerView
                adapterHorarios.removeAll(adapterHorarios);

                for(DocumentSnapshot doc:querySnapshot){
                    if(doc.exists()){
                        //---Carga todos los datos en el adaptador de Servicios al adaptador
                        adapter_horario adapterServicios = doc.toObject(adapter_horario.class);
                        adapterServicios.setId(doc.getId());
                        adapterHorarios.add(adapterServicios);
                    }

                }
                adapterRecyclerViewHorarios.notifyDataSetChanged();


            }
        });


    }
    public void ButtonNewHors(View view){

        if(perfilCuenta.getTipocuenta().equals(getString(R.string.administrador))){
            //---Launch activity Config Hors
            Intent Lanzador1=new Intent(MainActivity_interface_principal.this,Configuracion_Horario.class);
            startActivity(Lanzador1);

            //finaliza AlertDialog Hors
            alertDialogHors.dismiss();
        }else{
            Toast.makeText(MainActivity_interface_principal.this,getString(R.string.no_tienes_permisos_necesarios),Toast.LENGTH_LONG).show();
        }


    }
    public void ButtonFinish(View view){
        //finaliza AlertDialog Hors
        alertDialogHors.dismiss();
    }
    public void ButtonCard(View view){
        Intent intent = new Intent(this,MainActivity_tarjeta_negocio.class);
        startActivity(intent);
    }
    public  void Button_Tips(View view){

    //////////////////////////////// Cuadro de Dialog //////////////////////////////////
    final Dialog dialog=new Dialog(MainActivity_interface_principal.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.setContentView(R.layout.view_tips_panel);
    dialog.show();

        // Referencias
        Button button_NEGOCIO=(Button) dialog.findViewById(R.id.button23);
        Button button_OFERTAS=(Button) dialog.findViewById(R.id.button24);
        Button button_SERVICIOS=(Button) dialog.findViewById(R.id.button25);
        Button button_CLIENTES=(Button) dialog.findViewById(R.id.button26);

        // Onclick
        button_NEGOCIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //---Lanzador de activity
                Intent intent = new Intent(MainActivity_interface_principal.this, nav_tips.class);
                intent.putExtra("parametro", getString(R.string.DB_NEGOCIOS));
                startActivity(intent);

            }
        });
        button_OFERTAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //---Lanzador de activity
                Intent intent = new Intent(MainActivity_interface_principal.this, nav_tips.class);
                intent.putExtra("parametro", getString(R.string.DB_OFERTAS));
                startActivity(intent);

            }
        });
        button_SERVICIOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //---Lanzador de activity
                Intent intent = new Intent(MainActivity_interface_principal.this, nav_tips.class);
                intent.putExtra("parametro", getString(R.string.DB_SERVICIOS));
                startActivity(intent);
            }
        });
        button_CLIENTES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //---Lanzador de activity
                Intent intent = new Intent(MainActivity_interface_principal.this, nav_tips.class);
                intent.putExtra("parametro", getString(R.string.DB_CLIENTES));
                startActivity(intent);
            }
        });



    }
    public void ButtonAddService(View view){

        // Valores para la base de datos
        String sPAIS=adaptert_Profile.getPais().toUpperCase(); // Convierte a mayuscula
        String sCATEGORIA=adaptert_Profile.getCategoria().toUpperCase(); // Convierte a mayuscula




        //Crea el ventanta flotante
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_servicios_add_servicios, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
        builder.setView(dialoglayout);
        alertDialogClient=builder.show();

        //Instancias
        final EditText editTextTitle;
        final EditText editTextDescription;


        //--Reference
        editTextTitle=(EditText) dialoglayout.findViewById(R.id.editText_title_serv);
        editTextDescription=(EditText) dialoglayout.findViewById(R.id.editText_descripciom_serv);
        LinearLayout buttonAcept=(LinearLayout) dialoglayout.findViewById(R.id.LinealLayout_button_acept_servicio);

        // Recycler
        //---Click en el item seleccionado
        view_recyclerview_Servicios =(RecyclerView) dialoglayout.findViewById(R.id.recyclerview_servicios);
        view_recyclerview_Servicios.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //--Adaptadores
        view_adapterServiciosNegocios =new ArrayList<>();
        view_adapter_recyclerView_Servicios_all =new adapter_recyclerView_Servicios_all(view_adapterServiciosNegocios);

        view_adapter_recyclerView_Servicios_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                view_Item_Negocio=view_adapterServiciosNegocios.get(view_recyclerview_Servicios.getChildAdapterPosition(view));

                editTextTitle.setText(view_Item_Negocio.getTitulo());
                editTextDescription.setText(view_Item_Negocio.getDescripcion());

            }});

        view_recyclerview_Servicios.setAdapter(view_adapter_recyclerView_Servicios_all);



        //------------------------------ OnClick Aceptar------------------------------------
        buttonAcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //--STring value
                String tittle=editTextTitle.getText().toString();
                String descripcion=editTextDescription.getText().toString();

                if(!tittle.equals("")  ){
                    if(tittle.length()<=30 ){
                        if(descripcion.length()<=80 ){

                            // Suma puntos
                            PuntosCuenta.SumaPuntos(MainActivity_interface_principal.this,5,"");

                            // Set
                            view_Item_Negocio.setTitulo(tittle);
                            view_Item_Negocio.setDescripcion(descripcion);

                            //Guarda los dato en la base de datos
                            db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_SERVICIOS)  ).add(view_Item_Negocio);

                            // Reset
                            view_Item_Negocio=null;
                            view_Item_Negocio=new adapter_servicios_negocio();

                            //---Finaliza ventana flotante
                            alertDialogClient.dismiss();

                        }else{Toast.makeText(MainActivity_interface_principal.this,R.string.la_descripcion_demasiado_largo,Toast.LENGTH_SHORT).show();}
                    }else{Toast.makeText(MainActivity_interface_principal.this,R.string.el_titulo_demasiado_largo,Toast.LENGTH_SHORT).show();}
                }else{Toast.makeText(MainActivity_interface_principal.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_SHORT).show();}
            }
        });


        // BASE DE DATOS FIRESTORE
        CollectionReference collectionReference=db.collection(  getString(R.string.DB_APP)  ).document(  sPAIS  ).collection(  getString(R.string.DB_SERVICIOS)  ).document( getString(R.string.DB_CATEGORIA) ).collection( sCATEGORIA );
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                view_adapterServiciosNegocios.removeAll(view_adapterServiciosNegocios);


                for (DocumentSnapshot doc : task.getResult()) {

                    //---Carga todos los datos en el adaptador de Servicios al adaptador
                    adapter_servicios_negocio adapterServicios = doc.toObject(adapter_servicios_negocio.class);


                    adapterServicios.setId(doc.getId());
                    view_adapterServiciosNegocios.add(adapterServicios);

                }
                view_adapter_recyclerView_Servicios_all.notifyDataSetChanged();
            }
        });
    }
    public void Button_ADD_Cliente(View view){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_add_cliente, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
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
        buttonAddClient=(Button) dialoglayout.findViewById(R.id.button13);
        buttonAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(url_play_store != null){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.info_compartir_mendaje_para_cliente)+url_play_store);
                    startActivity(Intent.createChooser(intent, "Comparta con un cliente para que se contacte contigo por medio de OPEN"));
                    alertDialogClient.dismiss();
                }

            }
        });
    }
    public void Button_ADD_ClienteLocal(View view){
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_add_client_local, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
        builder.setView(dialoglayout);
        alertDialogClient=builder.show();

        buttonAddClient=(Button) dialoglayout.findViewById(R.id.button14);
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
    public void Button_ADD_Ofertas(View view){
        //Crea el ventanta flotante
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_oferta_add_oferta, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_interface_principal.this);
        builder.setView(dialoglayout);
        alertDialogClient=builder.show();

        //----EditText
        final EditText editTextTitle;
        final EditText editTextDescription;
        final EditText editTextPrice;
        LinearLayout linearLayoutAceptar;
        final ProgressBar progressBar;
        //--Reference
        editTextTitle=(EditText) dialoglayout.findViewById(R.id.editText_title);
        editTextDescription=(EditText) dialoglayout.findViewById(R.id.editText_decription);
        editTextPrice=(EditText) dialoglayout.findViewById(R.id.editText_price);
        linearLayoutAceptar=(LinearLayout) dialoglayout.findViewById(R.id.LineallayoutOferta_aceptar);
        circleImageViewFoto=(ImageView) dialoglayout.findViewById(R.id.imageView_imageProducto2);
        progressBar=(ProgressBar) dialoglayout.findViewById(R.id.progressBar6);
        progressBar.setVisibility(View.GONE);
        circleImageViewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, idIntent_addImagenOferta);

            }
        });

        linearLayoutAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tittle=editTextTitle.getText().toString();
                String description=editTextDescription.getText().toString();
                String pricee=editTextPrice.getText().toString();
                //Referencia para acceso de DB)
                DatabaseReference firebaseDatabaseOferta= FirebaseDatabase.getInstance().getReference();

                if(!tittle.equals("") && !pricee.equals("") ){
                    if(tittle.length()<=20 ){
                        if(description.length()<=50 ){

                            // ID unico
                            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                            String ID_OFERTA = s.format(new Date());

                            // Adapter
                            final adapter_ofertas_negocio adapter_ofertas_negocio=new adapter_ofertas_negocio();
                            adapter_ofertas_negocio.setTitulo(tittle);
                            adapter_ofertas_negocio.setDescripcion(description);
                            adapter_ofertas_negocio.setPrecio(pricee);
                            adapter_ofertas_negocio.setId(ID_OFERTA);

                            // Condicion si agrego una foto
                            if(fotoOFerta != null){

                                progressBar.setVisibility(View.VISIBLE);

                                //Referencia a la direccion donde se va a guardar la foto
                                StorageReference filePath= storageReferenceGalery.child(  getString(R.string.DB_NEGOCIOS)  ).child(ID_NEGOCIO).child(  getString(R.string.DB_OFERTAS)  ).child(ID_OFERTA);
                                UploadTask uploadTask= filePath.putBytes(fotoOFerta);

                                final String finalID_OFERTA = ID_OFERTA;
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        lottieAnimationViewLoad_Galery.setVisibility(View.GONE);
                                        Lineallayout_galery.setVisibility(View.VISIBLE);

                                        //-Obtiene la url de la foto
                                        Uri descargarFoto=taskSnapshot.getDownloadUrl();
                                        adapter_ofertas_negocio.setUrlFoto(descargarFoto.toString());

                                        // Suma puntos
                                        PuntosCuenta.SumaPuntos(MainActivity_interface_principal.this,2,"");

                                        //Guarda los dato en la base de datos
                                        db.collection(  getString(R.string.DB_NEGOCIOS  )).document(ID_NEGOCIO).collection(  getString(R.string.DB_OFERTAS)  ).document(finalID_OFERTA).set(adapter_ofertas_negocio,SetOptions.merge());


                                        //---Finaliza ventana flotante
                                        progressBar.setVisibility(View.GONE);
                                        alertDialogClient.dismiss();
                                    }
                                });

                                // Clear
                                fotoOFerta=null;
                                ID_OFERTA=null;

                            }else {
                                // Suma puntos
                                PuntosCuenta.SumaPuntos(MainActivity_interface_principal.this,2,"");


                                //Guarda los dato en la base de datos
                                db.collection(  getString(R.string.DB_NEGOCIOS  )).document(ID_NEGOCIO).collection(  getString(R.string.DB_OFERTAS)  ).document(ID_OFERTA).set(adapter_ofertas_negocio,SetOptions.merge());

                                //---Finaliza ventana flotante
                                alertDialogClient.dismiss();
                            }



                        }else{Toast.makeText(MainActivity_interface_principal.this,R.string.la_descripcion_demasiado_largo,Toast.LENGTH_SHORT).show();}
                    }else{Toast.makeText(MainActivity_interface_principal.this,R.string.el_titulo_demasiado_largo,Toast.LENGTH_SHORT).show();}
                }else{Toast.makeText(MainActivity_interface_principal.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_SHORT).show();}
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
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
            StorageReference filePath= storageReferenceGalery.child(  getString(R.string.DB_NEGOCIOS)  ).child(ID_NEGOCIO).child(  getString(R.string.DBS_PERFIL)  ).child("perfil");
            UploadTask uploadTask= filePath.putBytes(foto);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //-Obtiene la url de la foto
                    Uri descargarFoto=taskSnapshot.getDownloadUrl();
                    Map<String, Object> url = new HashMap<>();
                    url.put("imagen_perfil",descargarFoto.toString());

                    //-Guardar el Uri de la foto en el Profile del Negocio en un String
                    db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).set(url,SetOptions.merge());

                }
            });

        }

        ///////////////////////////////  Intent Result Galery //////////////////////////////////////
        if(requestCode== ID_Intent_Result_addFoto && resultCode==RESULT_OK ){
            Uri uri=data.getData();

            lottieAnimationViewLoad_Galery.setVisibility(View.VISIBLE);
            Lineallayout_galery.setVisibility(View.GONE);

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

            //Referencia a la direccion donde se va a guardar la foto
            StorageReference filePath= storageReferenceGalery.child(  getString(R.string.DB_NEGOCIOS)  ).child(ID_NEGOCIO).child(  getString(R.string.DB_GALERIA_FOTOS)  ).child(PhotoValueDirectUri);
            UploadTask uploadTask= filePath.putBytes(foto);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    lottieAnimationViewLoad_Galery.setVisibility(View.GONE);
                    Lineallayout_galery.setVisibility(View.VISIBLE);

                    // Suma puntos
                    PuntosCuenta.SumaPuntos(MainActivity_interface_principal.this,1,getString(R.string.imagen_subida)+"!");

                    //-Obtiene la url de la foto
                    Uri descargarFoto=taskSnapshot.getDownloadUrl();
                    Map<String, Object> url = new HashMap<>();
                    url.put("id",descargarFoto.toString());

                    //-Guardar el Uri de la foto en el Profile del Negocio en un String
                    db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS) ).document(PhotoValueDirectUri)
                            .set(url,SetOptions.merge());

                }
            });
        }

        //////////////////////////  Intent Result Add Foto nueva Oferta ///////////////////////////////////

        if(requestCode== idIntent_addImagenOferta && resultCode==RESULT_OK ){
            Uri uri=data.getData();

            lottieAnimationViewLoad_Galery.setVisibility(View.VISIBLE);
            Lineallayout_galery.setVisibility(View.GONE);

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
            fotoOFerta=baos.toByteArray();

            // Carga la imagen de perfil
            Glide.with(MainActivity_interface_principal.this)
                    .load(fotoOFerta)
                    .fitCenter()
                    .centerCrop()
                    .into(circleImageViewFoto);



        }

    }

    /////////////////////////////////////// MAPS ///////////////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setAllGesturesEnabled(false); //Desactiva los gestos sobre el mapView

        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(20);

        // Aplica estilo a mapa
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_maps));


        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(18.0f);

        //LatLng sydney = new LatLng(Latitud, Longitud);
        // Add a marker in Sydney and move the camera
       //mMap.addMarker(new MarkerOptions().position(sydney));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LoadBusinessMakerMaps();
    }
    public void LoadBusinessMakerMaps(){


        DocumentReference documentReference =db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    adapter_profile_negocio ContructorItemRecycleview = documentSnapshot.toObject(adapter_profile_negocio.class);
                    if(mMap!=null){mMap.clear();}

                    if(ContructorItemRecycleview.getNombre_negocio()!=null){
                        ////////////////// Asigna el icono ssegun la categoria del negocio /////////////
                        String valueIdBusiness=ContructorItemRecycleview.getCategoria();

                        int id = getResources().getIdentifier("loc_"+valueIdBusiness, "mipmap", getPackageName());
                        BitmapDescriptor icon= BitmapDescriptorFactory.fromResource(id);

                       //---Crea los Makers
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(ContructorItemRecycleview.getGeopoint().getLatitude(),ContructorItemRecycleview.getGeopoint().getLongitude()))).setIcon(icon);
                        //Posiciona la camara en la ubicacion del negocio
                        LatLng sydney = new LatLng(ContructorItemRecycleview.getGeopoint().getLatitude(), ContructorItemRecycleview.getGeopoint().getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    }
                }


            }
        });
    }
}