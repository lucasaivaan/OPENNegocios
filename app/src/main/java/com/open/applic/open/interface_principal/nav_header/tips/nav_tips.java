package com.open.applic.open.interface_principal.nav_header.tips;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.PuntosCuenta;
import com.open.applic.open.interface_principal.nav_header.tips.adaptador.adapter_recyclerView_Tips;
import com.open.applic.open.interface_principal.nav_header.tips.adaptador.adapter_tips;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.open.applic.open.Splash_Login.ID_NEGOCIO;

public class nav_tips extends AppCompatActivity {

    //////////////////////////////////// IMAGEN DE  FONDO ///////////////////////////////////////////////////
    private ImageView imageViewFondo;
    //////////////////////////////////  Perfil negocio  ////////////////////////////////////////////
    private adapter_profile_negocio adapterProfileNegocion;

    ////////////////////////////////////// Views TIPS ///////////////////////////////////////////////
    public RecyclerView recyclerViewTips;
    public List<adapter_tips> adapteraRRAY_Tips;
    public adapter_recyclerView_Tips adapterRecyclerViewTips;

    private String sCatgoria_Tips;
    public ImageView imageViewTips_foto;


    //////////////////////////////////// FIREBASE //////////////////////////////////////////////////
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    // Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private static final int idIntent_addImagenTips=1;
    public byte[] bFotoTips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_tips);


        //-------------------------------- Toolbar -------------------------------------------------
        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sCatgoria_Tips=getIntent().getExtras().getString("parametro");

        // Titulo del activity
        setTitle(sCatgoria_Tips);


        //Referencias
        imageViewFondo=(ImageView) findViewById(R.id.imageView18_fondo);



        // DAtos del negocio
        DocumentReference firebaseFirestore=db.collection( getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO);
        firebaseFirestore.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){
                        adapterProfileNegocion=documentSnapshot.toObject(adapter_profile_negocio.class);

                        // cargador de Tips
                        Cargador_Tips(adapterProfileNegocion.getPais(),adapterProfileNegocion.getCategoria(),sCatgoria_Tips);
                    }
                }
            }
        });


    }




    public void Cargador_Tips(final String Parametro0_Pais, final String Parametro1_catNegocio, final String Parametro2_ItemTips){

        //////////////////////////////////// Adapter Navigation Oferta //////////////////////////////////////////////////////
        //---Click en el item seleccionado
        recyclerViewTips =(RecyclerView) findViewById(R.id.recyclerview_tips);
        recyclerViewTips.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapteraRRAY_Tips =new ArrayList<>();
        adapterRecyclerViewTips =new adapter_recyclerView_Tips(adapteraRRAY_Tips);
        adapterRecyclerViewTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //---extrae la id de la Oferta
                final adapter_tips adapterValues_Tips=adapteraRRAY_Tips.get(recyclerViewTips.getChildAdapterPosition(view));

                // Condicion si el Tips es del negocio puede editarlo
                if(adapterValues_Tips.getIdCreador().equals(ID_NEGOCIO)){

                    // Obteniendo los datos

                    final Dialog dialog=new Dialog(nav_tips.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.view_crear_tips);
                    dialog.show();

                    // REFERENCIAS
                    imageViewTips_foto=(ImageView) dialog.findViewById(R.id.imageView_tips_imagen);
                    final EditText eTitulo=(EditText) dialog.findViewById(R.id.editText_tips_titulo);
                    final EditText eDescripcion=(EditText) dialog.findViewById(R.id.editText_tips_descripcion);
                    Button buttonEliminar=(Button) dialog.findViewById(R.id.button29);
                    buttonEliminar.setVisibility(View.VISIBLE);
                    Button buttonGuardar=(Button) dialog.findViewById(R.id.button28);
                    buttonGuardar.setText(R.string.actualizar);
                    final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.progressBar5);
                    progressBar.setVisibility(View.GONE);

                    // SET
                    if(!adapterValues_Tips.getUrlFoto().equals("")){ Glide.with(nav_tips.this).load(adapterValues_Tips.getUrlFoto()).fitCenter().centerCrop().into(imageViewTips_foto); }
                    eTitulo.setText(adapterValues_Tips.getTitulo());
                    eDescripcion.setText(adapterValues_Tips.getDescripcion());


                    // OnClick
                    imageViewTips_foto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Intent intent=new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            //Selecciona la imagen desde la galeria del telefono
                            startActivityForResult(intent, idIntent_addImagenTips);
                        }
                    });

                    buttonGuardar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            adapterValues_Tips.setTitulo(eTitulo.getText().toString());
                            adapterValues_Tips.setDescripcion(eDescripcion.getText().toString());
                            adapterValues_Tips.setNombreCreador(adapterProfileNegocion.getNombre_negocio());
                            adapterValues_Tips.setUrlFotoCreador(adapterProfileNegocion.getCategoria());
                            adapterValues_Tips.setIdCreador(ID_NEGOCIO);

                            // Referencia a la base de datos
                            final DocumentReference collectionReference=db.collection(  getString(R.string.DB_APP)  ).document(  adapterProfileNegocion.getPais().toUpperCase() ).collection(    getString(R.string.DB_TIPS)    ).document( adapterProfileNegocion.getCategoria().toUpperCase() ).collection(sCatgoria_Tips).document(adapterValues_Tips.getId());
                            // Referencia al Storage
                            StorageReference desertRef = storageRef.child(getString(R.string.DB_APP)).child(   adapterProfileNegocion.getPais().toUpperCase()  ).child(getString(R.string.DB_TIPS)).child(  adapterProfileNegocion.getCategoria().toUpperCase()  ).child(  sCatgoria_Tips  ).child(adapterValues_Tips.getId());

                            // Condiciones
                            if(!adapterValues_Tips.getTitulo().equals("") && !adapterValues_Tips.getDescripcion().equals("")){
                                if(bFotoTips != null){

                                    progressBar.setVisibility(View.VISIBLE);

                                    // Sube la Foto al Storage
                                    UploadTask uploadTask= desertRef.putBytes(bFotoTips);
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            //-Obtiene la url de la foto
                                            Uri descargarFoto=taskSnapshot.getDownloadUrl();
                                            adapterValues_Tips.setUrlFoto(descargarFoto.toString());

                                            // Guarda los datos en la Base de Datos
                                            collectionReference.set(adapterValues_Tips, SetOptions.merge());


                                            bFotoTips=null;
                                            // Finaliza
                                            dialog.dismiss();

                                        }
                                    });


                                }else{

                                    // guarda los datos en la base de datos
                                    collectionReference.set(adapterValues_Tips, SetOptions.merge());

                                    // Finaliza
                                    dialog.dismiss();
                                }
                            }else { Toast.makeText(nav_tips.this,getString(R.string.uno_o_mas_campos_estan_vacios),Toast.LENGTH_LONG).show(); }
                            }
                    });

                    buttonEliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //////////////////  Eliminada el Tips seleccionanda ////////////////////////////////////
                            db.collection(  getString(R.string.DB_APP)  ).document(  Parametro0_Pais.toUpperCase() ).collection(    getString(R.string.DB_TIPS)    ).document( Parametro1_catNegocio.toUpperCase() ).collection(Parametro2_ItemTips).document(adapterValues_Tips.getId()).delete();

                            // Storage
                            StorageReference desertRef = storageRef.child(getString(R.string.DB_APP)).child(  Parametro0_Pais.toUpperCase()  ).child(getString(R.string.DB_TIPS)).child(  Parametro1_catNegocio.toUpperCase()  ).child(  Parametro2_ItemTips  ).child(adapterValues_Tips.getId());
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    PuntosCuenta.RestaPuntos(5);

                                }
                            });

                            dialog.cancel();
                        }
                    });


                }else{}


                //----------------------------------------------Fin Alert Dialog
            }});
        recyclerViewTips.setAdapter(adapterRecyclerViewTips);



        //////////////////////////////////// DATABASE TIPS /////////////////////////////////////////
        CollectionReference collectionReference=db.collection(  getString(R.string.DB_APP)  ).document(  Parametro0_Pais.toUpperCase() ).collection(    getString(R.string.DB_TIPS)    ).document( Parametro1_catNegocio.toUpperCase() ).collection(Parametro2_ItemTips);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        adapteraRRAY_Tips.removeAll(adapteraRRAY_Tips);

                        imageViewFondo.setVisibility(View.VISIBLE);
                        for(DocumentSnapshot doc:documentSnapshots){

                            // Constructor
                            adapter_tips ContructorItemRecycleviewOferta=doc.toObject(adapter_tips.class);
                            ContructorItemRecycleviewOferta.setId(doc.getId());

                            // agrega objeto al arratAdapter
                            adapteraRRAY_Tips.add(ContructorItemRecycleviewOferta);

                            imageViewFondo.setVisibility(View.GONE);
                        }
                        adapterRecyclerViewTips.notifyDataSetChanged();
                    }
                });

    }

    public void Button_CrearTips(View view){

//////////////////////////////// Cuadro de Dialog //////////////////////////////////
        final Dialog dialog=new Dialog(nav_tips.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.view_crear_tips);
        dialog.show();

        // REFERENCIAS
        imageViewTips_foto=(ImageView) dialog.findViewById(R.id.imageView_tips_imagen);
        final EditText eTitulo=(EditText) dialog.findViewById(R.id.editText_tips_titulo);
        final EditText eDescripcion=(EditText) dialog.findViewById(R.id.editText_tips_descripcion);
        Button buttonCrear=(Button) dialog.findViewById(R.id.button28);
        final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.GONE);

        // OnClick
        imageViewTips_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //Selecciona la imagen desde la galeria del telefono
                startActivityForResult(intent, idIntent_addImagenTips);
            }
        });

        buttonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ID unico
                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                final String ID_Tips = s.format(new Date());

                // Adapter Tips
                final adapter_tips adapterTips=new adapter_tips();
                adapterTips.setTitulo(eTitulo.getText().toString());
                adapterTips.setDescripcion(eDescripcion.getText().toString());
                adapterTips.setId(ID_Tips);
                adapterTips.setNombreCreador(adapterProfileNegocion.getNombre_negocio());
                adapterTips.setUrlFotoCreador(adapterProfileNegocion.getCategoria());
                adapterTips.setIdCreador(ID_NEGOCIO);

                // Referencia a la base de datos
                final DocumentReference collectionReference=db.collection(  getString(R.string.DB_APP)  ).document(  adapterProfileNegocion.getPais().toUpperCase() ).collection(    getString(R.string.DB_TIPS)    ).document( adapterProfileNegocion.getCategoria().toUpperCase() ).collection(sCatgoria_Tips).document(ID_Tips);
                // Referencia al Storage
                StorageReference desertRef = storageRef.child(getString(R.string.DB_APP)).child(   adapterProfileNegocion.getPais().toUpperCase()  ).child(getString(R.string.DB_TIPS)).child(  adapterProfileNegocion.getCategoria().toUpperCase()  ).child(  sCatgoria_Tips  ).child(ID_Tips);

                // Condiciones
                if(!adapterTips.getTitulo().equals("") && !adapterTips.getDescripcion().equals("")){
                    if(bFotoTips != null){

                        progressBar.setVisibility(View.VISIBLE);

                        // Sube la Foto al Storage
                        UploadTask uploadTask= desertRef.putBytes(bFotoTips);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                //-Obtiene la url de la foto
                                Uri descargarFoto=taskSnapshot.getDownloadUrl();
                                adapterTips.setUrlFoto(descargarFoto.toString());

                                // Guarda los datos en la Base de Datos
                                collectionReference.set(adapterTips, SetOptions.merge());

                                // Suma puntos
                                PuntosCuenta.SumaPuntos(nav_tips.this,5,getString(R.string.DB_TIPS)+"!");



                                bFotoTips=null;
                                // Finaliza
                                dialog.dismiss();

                            }
                        });


                    }else{

                        // guarda los datos en la base de datos
                        collectionReference.set(adapterTips, SetOptions.merge());

                        // Finaliza
                        dialog.dismiss();
                    }
                }else { Toast.makeText(nav_tips.this,getString(R.string.uno_o_mas_campos_estan_vacios),Toast.LENGTH_LONG).show(); }




            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //////////////////////////  Intent Result Add Foto nueva Oferta ///////////////////////////////////

        if(requestCode== idIntent_addImagenTips && resultCode==RESULT_OK ){
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
            bFotoTips =baos.toByteArray();

            // Carga la imagen de perfil
            Glide.with(nav_tips.this)
                    .load(bFotoTips)
                    .fitCenter()
                    .centerCrop()
                    .into(imageViewTips_foto);



        }

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
