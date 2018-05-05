package com.open.applic.open;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.create_form_profile.Add_info_profile;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_cuenta;


public class Splash_Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private ProgressBar progressBar4;
    private LinearLayout layout_nInternet;

    // STATIC VARIABLE GLOBAL
    public static String ID_NEGOCIO;
    public static String ID_USUARIO;
    public static String ID_PAIS;


    public  SharedPreferences misPreferencias ;
    private String PREFS_KEY = "MisPreferencias";


    private GoogleApiClient googleApiClient;
    private Boolean estado=false;

    //Declaracion de instancia FierebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual


    // Database
    private FirebaseFirestore db =FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screaan);



        //Ocultar ActionBar
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();


        // References
        misPreferencias= getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        progressBar4=(ProgressBar) findViewById(R.id.progressBar4);
        layout_nInternet=(LinearLayout) findViewById(R.id.lineallayout_no_internet);

        // Verification
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Comprobación de conexion a internet
                    if( VerificaConectividad()  ){
                        Authentication();
                    }else{
                        progressBar4.setVisibility(View.GONE);
                        layout_nInternet.setVisibility(View.VISIBLE);
                    }
                }
            }, 1500);// Tiempo de espera
        } catch (Exception e) { }


    }


    @Override
    protected void onStart() {
        super.onStart();
        }
    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    // Button
    public  void Button_Reintentar(View view){

        //Visibility
        progressBar4.setVisibility(View.VISIBLE);
        layout_nInternet.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Comprobación de conexion a internet
                if( VerificaConectividad()  ){

                    //Visibility
                    progressBar4.setVisibility(View.VISIBLE);
                    layout_nInternet.setVisibility(View.GONE);

                    Authentication();
                }else{
                    //Visibility
                    progressBar4.setVisibility(View.GONE);
                    layout_nInternet.setVisibility(View.VISIBLE);
                }
            }
        }, 1500);// Tiempo de espera
    }
    // Authentication
    private void Authentication(){

        //----Authentication Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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
                if (user != null) {

                    ComprobacionUsuario();

                }else{
                    RevokeAuth();
                }
            }
        };

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }
    private void ComprobacionUsuario() {

        String PrefCorreo = misPreferencias.getString("email", null);
        final String PrefId_negocio = misPreferencias.getString("ID_NEGOCIO", null);

        if(PrefCorreo != null && PrefId_negocio != null && PrefCorreo.equals(firebaseUser.getEmail())){

            // Inicio mas rapido mediante mis preferencias
            // Asigna la Id del negocio a la variable global
            ID_NEGOCIO=PrefId_negocio;
            ID_USUARIO=PrefCorreo;

            goMainScreen();


        }else {

            // Consulta a la Base de Datos
            CollectionReference collectionReferenceDB=db.collection( getString(R.string.DB_NEGOCIOS) );
            collectionReferenceDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(final DocumentSnapshot docRef:task.getResult()){

                            //Base de Datos de cuentas del negocio
                            CollectionReference collectionReferenceCuentas=db.collection( getString(R.string.DB_NEGOCIOS) ).document(docRef.getId()).collection( getString(R.string.DB_CUENTAS) );
                            collectionReferenceCuentas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (DocumentSnapshot docCuenta:task.getResult()){
                                            if (docCuenta.exists()) {

                                                adapter_perfil_cuenta perfilCuenta =docCuenta.toObject(adapter_perfil_cuenta.class);

                                                // Comprobar existencia de la cuenta en el negocio
                                                if(perfilCuenta.getEmail().equals(firebaseUser.getEmail())){

                                                   // Asigna la Id del negocio a la variable global
                                                    ID_NEGOCIO=docRef.getId();
                                                    ID_USUARIO=firebaseUser.getEmail();
                                                    estado=true;

                                                    Toast.makeText(Splash_Login.this,"registe!!!",Toast.LENGTH_LONG).show();
                                                    SharedPreferences.Editor editor = misPreferencias.edit();
                                                    editor.putString("email",firebaseUser.getEmail());
                                                    editor.putString("ID_NEGOCIO", docRef.getId());
                                                    editor.commit();

                                                    goMainScreen();

                                                    break;
                                                }


                                            }else{ }

                                        }
                                    }
                                }
                            });

                        }

                        if(estado==false){
                            // SI EL USUARIO NO TIENE RELACION CON NINGUN NEGOCIO

                            ID_NEGOCIO=firebaseUser.getUid();
                            if(ID_NEGOCIO != null) {


                                //---Lanzador de activity Auth
                                Intent Lanzador1=new Intent(Splash_Login.this,Add_info_profile.class);
                                startActivity(Lanzador1);
                                finish();

                            }

                        }


                    }

                }
            });

        }





    }
    private void goMainScreen() {
        //---Lanzador de activity Auth
        Intent Lanzador1=new Intent(this,MainActivity_interface_principal.class);
        startActivity(Lanzador1);
        finish();

    }
    private void RevokeAuth() {
        Intent intent = new Intent(this, MainActivity_Auth.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle options = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.anim_trans2,R.anim.anim_trans1).toBundle();
        ActivityCompat.startActivity(this, intent, options);
        finish();

    }
    //------------------------ comprobacion de conexion a internet ---------------------------------
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


}
