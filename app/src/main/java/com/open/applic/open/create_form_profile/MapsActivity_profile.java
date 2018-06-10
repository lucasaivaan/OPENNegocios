package com.open.applic.open.create_form_profile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.open.applic.open.R;
import com.open.applic.open.create_form_profile.adaptadores.adapter_categoriaNegocio;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.icono;
import com.open.applic.open.interface_principal.nav_header.perfil_negocio.Nav_header_perfil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapsActivity_profile extends FragmentActivity implements OnMapReadyCallback  {

    //--Button
    Button buttonCambiarUbicacion;

    //Referencias
    public  SharedPreferences misPreferencias ;
    private String PREFS_KEY = "MisPreferencias";
    private String id_Negocio;

    //------------------------------------ MAPS ----------------------------------------------------
    private LocationManager locationManage;
    private LocationListener listener;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    //---Double Latitud y Longitud
    public double Latitud = 0;
    public double Longitud = 0;
    //Bitmap iconMaker
    private BitmapDescriptor iconKiosco;
    private BitmapDescriptor iconAlmacen;
    private BitmapDescriptor iconPizzeria;

    //___________________________________- GPS _____________________________________________________
    private LocationManager locationManager;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;



    //------------------------------- Firebase AUTH ------------------------------------------------
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual


    //-- Acceso a una instancia de Cloud Firestore desde la actividad
    private FirebaseFirestore dbClodFirestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_profile);

        int permissOnCheck=ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissOnCheck==PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }}


        //---REference
        buttonCambiarUbicacion = (Button) findViewById(R.id.button6);
        // References
        misPreferencias= getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);


        //----------------------------------Maps----------------------------------------------------
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity_profile.this);


        //--------------------------------------- GPS ----------------------------------------------
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //---Extraxion de datos pasados por parametros
        Latitud = getIntent().getExtras().getDouble("parametroLatitud");
        Longitud = getIntent().getExtras().getDouble("parametroLongitud");

        id_Negocio = misPreferencias.getString("ID_NEGOCIO", null);


    }


    public void saveValuePreference(Context context, Boolean mostrar) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = settings.edit();
        editor.putBoolean("license", mostrar);
        editor.commit();
    }

    public void Button_Save(View view) {

        if (Latitud != 0 && Longitud != 0) {

            //--Check profile completado
            saveValuePreference(getApplicationContext(), false);

            // Create a new user with a first and last name
            Map<String, Object> mapsLocation = new HashMap<>();
            mapsLocation.put("geopoint", new GeoPoint(Latitud,Longitud));

            dbClodFirestore.collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid())
                    .set(mapsLocation, SetOptions.merge());


            //--- Lanza el activity para completar el perfil del negocio
            Intent myIntent = new Intent(MapsActivity_profile.this, MainActivity_interface_principal.class);
            startActivity(myIntent);
            //---finaliza activity
            finish();
        }


    }

    public void Button_Edit_info(View view) {

        //--.lanzadador Activity
        Intent intent2 = new Intent(MapsActivity_profile.this, Nav_header_perfil.class);
        startActivityForResult(intent2, 0);

        //---finaliza activity
        finish();

    }

    public void ButtonLocationGps(View view) {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!gps){showAlert();}

        ActivityCompat.requestPermissions(MapsActivity_profile.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);


        GpsTracker gt = new GpsTracker(getApplicationContext());
        Location l = gt.getLocation();
        if( l == null){
            Toast.makeText(getApplicationContext(),R.string.datos_gps_no_disponible,Toast.LENGTH_SHORT).show();
        }else {
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            LoadBusinessMakerMaps(l.getLatitude(), l.getLongitude());

        }

    }
    /////////////////////////////////////// MAPS ///////////////////////////////////////////////////////

    public void LoadBusinessMakerMaps(final Double Lat, final Double Long) {
        Latitud=Lat;
        Longitud=Long;

        if(id_Negocio != null){

            //  FIRESTORE
            DocumentReference docRef = dbClodFirestore.collection( getString(R.string.DB_NEGOCIOS) ).document(id_Negocio);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (mMap != null) { mMap.clear(); }

                    adapter_profile_negocio ContructorItemRecycleview = documentSnapshot.toObject(adapter_profile_negocio.class);

                    if(ContructorItemRecycleview.getCategoria() != null){

                        ////////////////// Asigna el icono ssegun la categoria del negocio /////////////

                        // Firebase DB categorias
                        FirebaseFirestore firestore_categoria=FirebaseFirestore.getInstance();
                        firestore_categoria.collection( getString(R.string.DB_APP) ).document( ContructorItemRecycleview.getPais().toUpperCase() ).collection( getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( ContructorItemRecycleview.getCategoria() )
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot=task.getResult();
                                    if( documentSnapshot.exists() ){

                                        // adapter
                                        adapter_categoriaNegocio categoriaNegocio=documentSnapshot.toObject(adapter_categoriaNegocio.class);

                                        // Glide Descarga de imagen
                                        Glide.with(getBaseContext())
                                                .load(categoriaNegocio.getIcon_location())
                                                .asBitmap()
                                                .fitCenter()
                                                .override(70,70)
                                                .into(new SimpleTarget<Bitmap>(70,70) {
                                                    @Override
                                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                                                        //---Crea los Makers
                                                        mMap.addMarker(new MarkerOptions().position(new LatLng( Lat,Long ))).setIcon(BitmapDescriptorFactory.fromBitmap( resource ));

                                                        //Posiciona la camara en la ubicacion del negocio
                                                        LatLng sydney = new LatLng(Lat, Long);
                                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                                    }

                                                    @Override
                                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                        super.onLoadFailed(e, errorDrawable);
                                                        Toast.makeText(MapsActivity_profile.this,"Error al carga imagen",Toast.LENGTH_SHORT).show();
                                                    }}
                                                );


                                    }
                                }
                            }
                        });

                    }
                }
            });
        }

    }
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity_profile.this);
        dialog.setTitle(R.string.ubicacion_desactivada)
                .setMessage(R.string.ubicacion_no_disponible_info)
                .setPositiveButton(R.string.config_ubicacion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(20);

        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(18.0f);

        LoadBusinessMakerMaps(Latitud, Longitud);

        //LatLng sydney = new LatLng(Latitud, Longitud);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
