package com.open.applic.open.interface_principal.nav_header.galeria_fotos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;
import com.open.applic.open.interface_principal.metodos_funciones.PuntosCuenta;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.galeria_fotos.adaptadores.adaptador_foto;
import com.open.applic.open.interface_principal.nav_header.galeria_fotos.adaptadores.adapter_recyclerView_Fotos;
import com.open.applic.open.interface_principal.nav_header.productos.MainActivity_productos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class galeria_fotos extends AppCompatActivity {

    // Notificacion sin fotos
    private TextView textViewNoti_SinFotos;


    ////////////////////////////  Galeria
    public RecyclerView recyclerViewProducto;
    public List<adaptador_foto> adaptadorFotoList;
    public adapter_recyclerView_Fotos adapter_recyclerView_Galeria;
    private ImageView imageViewFondo;

    // Inten Result
    private static int ID_Intent_Result_ImagenPerfil =1;
    public byte[] bytesFoto;

    // Storage
    private StorageReference storageReferenceGalery;
    private String ID_NEGOCIO;

    // Firebase
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fotos);
        setTitle(getResources().getString(R.string.fotos).toUpperCase());

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // DATOS APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(galeria_fotos.this);

        //Reference
        textViewNoti_SinFotos=(TextView) findViewById(R.id.textViewNoti_SinFotos);


        // Cargar fotos
        cargar_fotos();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        //////////////////////////  Intent Result Add Foto nueva  ///////////////////////////////////

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
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos); //Calidad de la imagen
            bytesFoto=baos.toByteArray();

            if(bytesFoto != null){
                ADD_Foto(bytesFoto);
            }

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
                return true;}



        return super.onOptionsItemSelected(item);
    }

    public void Button_AgregarFoto(View view){

        final Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        //Selecciona la imagen desde la galeria del telefono
        ID_Intent_Result_ImagenPerfil =1;
        startActivityForResult(intent, ID_Intent_Result_ImagenPerfil);
    }


    public void ViewFoto(final String id, String sUrlFoto, String sComentario){

        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_foto, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(galeria_fotos.this);
        builder.setView(dialoglayout);
        final AlertDialog alertDialogHors;
        alertDialogHors=builder.show();

        // Reference
        // Reference
        ImageView imageView_Cerrar=(ImageButton) dialoglayout.findViewById(R.id.imageButton_close);
        ImageView imageView_Foto=(ImageView) dialoglayout.findViewById(R.id.imageView_foto);
        final EditText editText_Comentario=(EditText) dialoglayout.findViewById(R.id.editText_comentario);
        final ProgressBar progressBar=(ProgressBar) dialoglayout.findViewById(R.id.progressBar9);
        final Button button_Agregar=(Button) dialoglayout.findViewById(R.id.button_agregarFoto);
        button_Agregar.setVisibility(View.GONE);
        final Button button_Editar=(Button) dialoglayout.findViewById(R.id.button_aditarFoto);
        button_Editar.setVisibility(View.VISIBLE);
        final Button button_Actualizar=(Button) dialoglayout.findViewById(R.id.button_actualizarFoto);
        button_Actualizar.setVisibility(View.GONE);
        final Button button_Eliminar=(Button) dialoglayout.findViewById(R.id.button_EliminarFoto);
        button_Eliminar.setVisibility(View.GONE);

        // Condiciones
        if(sComentario.equals("")){
            editText_Comentario.setVisibility(View.GONE);
        }


        imageView_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cierra la vista
                alertDialogHors.dismiss();
            }
        });
        // Imagen
        Glide.with(galeria_fotos.this)
                .load(sUrlFoto)
                .fitCenter()
                .centerCrop()
                .into(imageView_Foto);

        // Comentario
        editText_Comentario.setText(sComentario);


        // OnClick
        button_Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Control de visibilidad
                button_Editar.setVisibility(View.GONE);
                button_Actualizar.setVisibility(View.VISIBLE);
                button_Eliminar.setVisibility(View.VISIBLE);
                editText_Comentario.setVisibility(View.VISIBLE);

            }
        });

        button_Actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // informacion de la reseña
                Map<String , Object> mFoto=new HashMap<>();
                mFoto.put("comentario",editText_Comentario.getText().toString());


                //Guarda los dato en la base de datos
                db.collection(  getString(R.string.DB_NEGOCIOS  )).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document(id).set(mFoto,SetOptions.merge());

                alertDialogHors.dismiss();
            }
        });
        button_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Eliminada la foto seleccionando

                // Firestore
                DocumentReference documentReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_GALERIA_FOTOS)).document(  id );
                documentReference.delete();

                // Storage
                storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                StorageReference  storageReference= storageReferenceGalery.child(getString(R.string.DB_NEGOCIOS)).child(ID_NEGOCIO).child(getString(R.string.DB_GALERIA_FOTOS)).child( id );
                storageReference.delete();

                // Resta punto
                //PuntosCuenta.RestaPuntos(1,galeria_fotos.this);


                alertDialogHors.dismiss();

            }
        });

    }
    public void ADD_Foto(final byte[] bytesFoto){

        //Crear AlertDialog Hors
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_foto, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(galeria_fotos.this);
        builder.setView(dialoglayout);
        final AlertDialog alertDialogHors;
        alertDialogHors=builder.show();

        // Reference
        ImageView imageView_Cerrar=(ImageButton) dialoglayout.findViewById(R.id.imageButton_close);
        ImageView imageView_Foto=(ImageView) dialoglayout.findViewById(R.id.imageView_foto);
        final EditText editText_Comentario=(EditText) dialoglayout.findViewById(R.id.editText_comentario);
        editText_Comentario.setHint(getResources().getString(R.string.haz_comentario_de_esta_foto));
        final ProgressBar progressBar=(ProgressBar) dialoglayout.findViewById(R.id.progressBar9);
        final Button button_Agregar=(Button) dialoglayout.findViewById(R.id.button_agregarFoto);
        button_Agregar.setVisibility(View.VISIBLE);
        final Button button_Editar=(Button) dialoglayout.findViewById(R.id.button_aditarFoto);
        button_Editar.setVisibility(View.GONE);
        final Button button_Actualizar=(Button) dialoglayout.findViewById(R.id.button_actualizarFoto);
        button_Actualizar.setVisibility(View.GONE);
        final Button button_Eliminar=(Button) dialoglayout.findViewById(R.id.button_EliminarFoto);
        button_Eliminar.setVisibility(View.GONE);

        //Carga imagen seleccionada
        try{
            Glide.with(galeria_fotos.this)
                    .load(bytesFoto)
                    .fitCenter()
                    .centerCrop()
                    .into(imageView_Foto);
        }catch (Exception ex){}

        imageView_Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Cierra la vista
                alertDialogHors.dismiss();
            }
        });

        button_Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bytesFoto != null){

                    progressBar.setVisibility(View.VISIBLE);
                    button_Agregar.setVisibility(View.GONE);

                    // ID unico
                    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                    final String ID_UNICO = s.format(new Date());

                    if(ID_UNICO != null){

                        //Referencia a la direccion donde se va a guardar la foto
                        storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                        StorageReference filePath= storageReferenceGalery.child(  getString(R.string.DB_NEGOCIOS)  ).child(ID_NEGOCIO).child(  getString(R.string.DB_GALERIA_FOTOS)  ).child(ID_UNICO);
                        UploadTask uploadTask= filePath.putBytes(bytesFoto);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                //-Obtiene la url de la foto
                                Uri descargarFoto=taskSnapshot.getDownloadUrl();

                                // informacion de la reseña
                                Map<String , Object> mFoto=new HashMap<>();
                                mFoto.put("id",ID_UNICO);
                                mFoto.put("urlfoto",descargarFoto.toString());
                                mFoto.put("comentario",editText_Comentario.getText().toString());
                                mFoto.put("timestamp", FieldValue.serverTimestamp());


                                //Guarda los dato en la base de datos
                                db.collection(  getString(R.string.DB_NEGOCIOS  )).document(ID_NEGOCIO).collection(  getString(R.string.DB_GALERIA_FOTOS)  ).document(ID_UNICO).set(mFoto,SetOptions.merge());

                                alertDialogHors.dismiss();

                            }
                        });
                    }else{

                        Toast.makeText(galeria_fotos.this,R.string.error_field_required,Toast.LENGTH_LONG).show();

                    }




                }

            }
        });



    }
    public void cargar_fotos(){

        //---Click en el item seleccionado
        recyclerViewProducto =(RecyclerView) findViewById(R.id.recyclerview_galeria);
        //recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this,4));
        //--Adaptadores
        adaptadorFotoList =new ArrayList<>();
        adapter_recyclerView_Galeria =new adapter_recyclerView_Fotos(adaptadorFotoList);
        adapter_recyclerView_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Adaptador
                adaptador_foto adapterNegocioPerfil= new adaptador_foto();
                adapterNegocioPerfil=adaptadorFotoList.get(recyclerViewProducto.getChildAdapterPosition(view));

                // funcion de viste de la imagen
                ViewFoto(adapterNegocioPerfil.getId(),adapterNegocioPerfil.getUrlfoto(),adapterNegocioPerfil.getComentario());
            }
        });

        recyclerViewProducto.setAdapter(adapter_recyclerView_Galeria);

        //  BASE DE DATOS
        CollectionReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_GALERIA_FOTOS));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adaptadorFotoList.removeAll(adaptadorFotoList);
                textViewNoti_SinFotos.setVisibility(View.VISIBLE);

                for (DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){
                        adaptador_foto adapterProducto=doc.toObject(adaptador_foto.class);

                        if(!adapterProducto.getId().equals("favorito")){
                            adaptadorFotoList.add(adapterProducto);

                            textViewNoti_SinFotos.setVisibility(View.GONE);
                        }


                    }

                }
                adapter_recyclerView_Galeria.notifyDataSetChanged();

            }
        });

    }
}
