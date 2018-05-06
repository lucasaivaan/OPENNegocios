package com.open.applic.open;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity_Auth extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String url_play_store;
    private String PREFS_KEY = "mispreferencias";
    public boolean aBoolean;
    private Button signInButton;
    private ProgressBar progressBar;

    //LinealLayaut
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    //Animation
    private Animation animationUptodown;
    private Animation animationDowntoup1;
    private Animation animationDowntoup2;

    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777;

    //Declaracion de instancia FierebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    // FIRESTORE
    FirebaseFirestore db=FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__auth);

        //Ocultar ActionBar
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();





        //----Authentication Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);}
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };


        // Reference
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        linearLayout1=(LinearLayout) findViewById(R.id.LinealLayout_1);
        linearLayout2=(LinearLayout) findViewById(R.id.LinealLayout_2);
        linearLayout2.setVisibility(View.GONE);
        linearLayout3=(LinearLayout) findViewById(R.id.LinealLayout_3);
        //animacion
        animationUptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);//aparece de arriba a su posision
        animationDowntoup1= AnimationUtils.loadAnimation(this,R.anim.downtotop);//aparece de abajo a su posision
        animationDowntoup2= AnimationUtils.loadAnimation(this,R.anim.downtotop);//aparece de abajo a su posision

        //SET
        linearLayout1.setAnimation(animationUptodown);
        linearLayout3.setAnimation(animationDowntoup2);

        animationDowntoup1.setDuration(1000);
        linearLayout2.setVisibility(View.VISIBLE);
        linearLayout2.setAnimation(animationDowntoup1);



        // Url Play Store
        DocumentReference docUrl=db.collection( getString(R.string.DB_APP) ).document( getString(R.string.DB_INFORMACION) );
        docUrl.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        url_play_store=doc.getString("url_open_cliente");
                    }
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);


    }
    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        progressBar.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.not_firebase_auth, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void goMainScreen() {
        Intent intent = new Intent(this, Splash_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle options = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.anim_trans2,R.anim.anim_trans1).toBundle();
        ActivityCompat.startActivity(this, intent, options);
    }


    public void ButtonRedireccionamientoOpenNegocios(View view){

        //Redireccionamient a Play Store para la descarga del SharePreferencesAPP
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=com.mypass.applic.mypass&hl"));

        try{ startActivity(intent); }
        catch(Exception e){ intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mypass.applic.mypass&hl")); }

    }
    public void ButtonRedireccionamientoOpenClient(View view){

        if(url_play_store != null && !url_play_store.equals("")){

            // Redireccionamiento a Play Store para descargar Open Cliente
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.mypass.applic.mypass&hl"));

            try{ startActivity(intent); }
            catch(Exception e){ intent.setData(Uri.parse(url_play_store)); }
        }else{Toast.makeText(MainActivity_Auth.this,R.string.error_obtener_link_playstore,Toast.LENGTH_SHORT).show();}



    }


}