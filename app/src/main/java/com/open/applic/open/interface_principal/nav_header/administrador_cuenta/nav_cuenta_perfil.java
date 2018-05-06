package com.open.applic.open.interface_principal.nav_header.administrador_cuenta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_cuenta;
import com.open.applic.open.interface_principal.adaptadores.adapter_recyclerView_Cuentas;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class nav_cuenta_perfil extends AppCompatActivity {

    // Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Lista Cuentas
    public RecyclerView recyclerViewCuentas;
    public List<adapter_perfil_cuenta> adapterListaCuentas;
    public adapter_recyclerView_Cuentas adapterRecyclerViewCuentas;
    private String urlDescargarFoto;
    private adapter_perfil_cuenta Peril_cuenta;

    //-Storage
    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private CircleImageView circleImageViewProfile;

    private ProgressBar progressBar_foto;

    // Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    private String ID_NEGOCIO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_cuenta_perfil);

        setTitle(R.string.perfil);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Datos APP SharePreferences commit
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);

        // Read Data
        LoadData();

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
                StorageReference filePath=mStorage.child( getString(R.string.DB_NEGOCIOS) ).child(ID_NEGOCIO).child( getString(R.string.DB_CUENTAS) ).child(firebaseUser.getUid()).child( getString(R.string.DBS_PERFIL) ).child("fotoperfil");

                // Subida de imagen al Storage
                UploadTask uploadTask= filePath.putBytes(foto);

                //Proceso de subida del archivo
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(nav_cuenta_perfil.this,R.string.foto_actualizada,Toast.LENGTH_SHORT).show();
                        progressBar_foto.setVisibility(View.GONE);


                        //-Obtiene la url de la foto
                        urlDescargarFoto= String.valueOf(taskSnapshot.getDownloadUrl());

                        // Carga la imagen de perfil
                        Glide.with(nav_cuenta_perfil.this)
                                .load(urlDescargarFoto.toString())
                                .fitCenter()
                                .centerCrop()
                                .into(circleImageViewProfile);

                    }
                });
            }else{
                Toast.makeText(nav_cuenta_perfil.this,"Ocurrio un error",Toast.LENGTH_SHORT).show();

            }


        }
    }


    public  void ButtonADDFotho(View view) {

        //-metodo para seleccion una imagen en la galeria
        Intent intent = new Intent(Intent.ACTION_PICK);

        //-Tipo de elementos a seleccionar
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, GALLLERY_INTENT);
    }



    public void LoadData(){



        // Reference
        circleImageViewProfile = (CircleImageView) findViewById(R.id.nav_foto_perfil);
        final EditText editTextNombre = (EditText)findViewById(R.id.EditText_nombre);
        final EditText editTextEmail = (EditText) findViewById(R.id.EditText_correo);
        final Spinner SpinnerProfile_cuenta = (Spinner) findViewById(R.id.spinner_cuentas);
        final Spinner SpinnerProfile_cuenta_permisos = (Spinner)findViewById(R.id.spinner_permisos);
        progressBar_foto =(ProgressBar) findViewById(R.id.progressBar3);
        progressBar_foto.setVisibility(View.GONE);

        Button buttonUpdate=(Button)findViewById(R.id.button_crear);
        buttonUpdate.setText(R.string.actualizar);


        final String[] aTipoCuenta = getResources().getStringArray(R.array.cuentas);
        final String[] aTipoPermiso = getResources().getStringArray(R.array.permisos);


       // Firestore Data Base
        db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(ID_NEGOCIO).collection(  getString(R.string.DB_CUENTAS)  ).document(firebaseUser.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        Peril_cuenta=doc.toObject(adapter_perfil_cuenta.class);

                        // Imagen de perfil
                        urlDescargarFoto=Peril_cuenta.getUrlfotoPerfil();
                        if(!Peril_cuenta.getUrlfotoPerfil().equals("default")){ Glide.with(nav_cuenta_perfil.this).load(Peril_cuenta.getUrlfotoPerfil()).fitCenter().centerCrop().into(circleImageViewProfile); }

                        //Spinner
                        for(int x=0;x < aTipoCuenta.length; x++){ if(Peril_cuenta.getCuenta().equals(aTipoCuenta[x])){ SpinnerProfile_cuenta.setSelection(x); } }
                        for(int x=0;x < aTipoPermiso.length; x++){ if(Peril_cuenta.getTipocuenta().equals(aTipoPermiso[x])){ SpinnerProfile_cuenta_permisos.setSelection(x); } }

                        if(Peril_cuenta.getTipocuenta().equals(getString(R.string.estandar))){
                            SpinnerProfile_cuenta.setEnabled(false);
                            SpinnerProfile_cuenta_permisos.setEnabled(false);
                        }
                        // Nombre y email
                        editTextNombre.setText(Peril_cuenta.getNombre());
                        editTextEmail.setText(Peril_cuenta.getEmail());
                    }
                }
            }
        });



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
                            DocumentReference docREfCuenta=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_CUENTAS)).document(email);
                            docREfCuenta.set(adapterPerfilCuenta, SetOptions.merge());

                            finish();


                        }else { Toast.makeText(getApplicationContext(),R.string.debe_elejir_tipo_permiso, Toast.LENGTH_SHORT).show();}

                    }else{ Toast.makeText(getApplicationContext(),R.string.selecciona_tipo_cuenta, Toast.LENGTH_SHORT).show();}

                }else{ Toast.makeText(getApplicationContext(),R.string.uno_o_mas_campos_estan_vacios, Toast.LENGTH_SHORT).show();}

            }
        });



    }


}
