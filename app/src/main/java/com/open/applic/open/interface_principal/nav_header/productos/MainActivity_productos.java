package com.open.applic.open.interface_principal.nav_header.productos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.PuntosCuenta;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_producto;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_ProductosNegocio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity_productos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //TOllbar
    private EditText editText_Toolbar_Seach;


    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_ProductosNegocio adapter_recyclerView_productos;
    private ImageView imageViewFondo;

    // Spinner Categoria
    private Spinner spinnerCategoria;// Elementos en Spinner
    List<String> listCatSpinner = new ArrayList<String>();


    // View Flotante
    private CircleImageView circleImageViewProducto;
    private EditText editTextMarca;
    public EditText editTextCodigo;
    private EditText editTextInfo;
    private EditText editTextPrecio;
    private Button buttonGuardarProduct;
    private  Button buttonEliminar;

    // Recyclerview categorias
    private RecyclerView recyclerProductos;

    ////////////////////////////  FIRESTORE
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private adapter_profile_negocio adapterProfileNegocio;

    //-Storage
    // Create a storage reference from our SharePreferencesAPP
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    // Create a reference to the file to delete
    StorageReference desertRef;

    private StorageReference mStorage;
    private static final int GALLLERY_INTENT = 1;
    private String urlDescargarFoto;
    private byte[] bytesMapImagen;
    String idUnicoProducto;

    private ProgressBar progressBar_foto;

    private String ID_NEGOCIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos);
        setTitle(R.string.mis_productos);

        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Datos APP SharePreferences commit
        ID_NEGOCIO=SharePreferencesAPP.getID_NEGOCIO(this);


        // Reference

        spinnerCategoria=(Spinner) findViewById(R.id.spinner_categorias);
        imageViewFondo=(ImageView) findViewById(R.id.imageView15_fondo2);
        editText_Toolbar_Seach=(EditText) findViewById(R.id.editText2_seach);





        editText_Toolbar_Seach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(!editText_Toolbar_Seach.getText().toString().equals("")){

                    // Buscador
                    Recycler_Producto(editText_Toolbar_Seach.getText().toString());
                }else {

                    if(spinnerCategoria.getSelectedItem() != null){
                        if(!spinnerCategoria.getSelectedItem().toString().equals(getString(R.string.elije_una_categoaria))){
                            Recycler_Producto(spinnerCategoria.getSelectedItem().toString());
                        }else { Recycler_Producto("");}
                    } }

            }
        });


        //  BASE DE DATOS
        CollectionReference collectionReference2=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS));
        collectionReference2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                listCatSpinner.removeAll(listCatSpinner);

                for (DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()) {
                        adapter_producto adapterProducto = doc.toObject(adapter_producto.class);

                        // inflar el Spinner de categorias
                        Boolean estadoRepeticion=false;

                        // Recoore el arrayList
                        if(listCatSpinner.size()==0){listCatSpinner.add(getString(R.string.elije_una_categoaria));}
                        for(int x=0;x<listCatSpinner.size();x++) {
                            if(listCatSpinner.get(x).equals(adapterProducto.getSubcategoria()) || listCatSpinner.get(x).equals(adapterProducto.getSubcategoria_2()) )
                            { estadoRepeticion=true; }
                        }
                        // agrega la categoria al arrayList
                        if(estadoRepeticion == false){
                            if( adapterProducto.getSubcategoria_2() == null){

                                listCatSpinner.add(adapterProducto.getSubcategoria());
                                spinnerCategoria.setVisibility(View.VISIBLE);

                            }else {

                                if(adapterProducto.getSubcategoria_2().equals("") ){
                                    listCatSpinner.add(adapterProducto.getSubcategoria());
                                    spinnerCategoria.setVisibility(View.VISIBLE);
                                }else {
                                    listCatSpinner.add(adapterProducto.getSubcategoria_2());
                                    spinnerCategoria.setVisibility(View.VISIBLE);
                                }
                            }

                            //Visibility
                            imageViewFondo.setVisibility(View.GONE);
                        }
                    }
                }
                if(listCatSpinner.size()==0){spinnerCategoria.setVisibility(View.GONE);}

                spinnerCategoria.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,listCatSpinner));
                spinnerCategoria.setOnItemSelectedListener(MainActivity_productos.this);

            }

        });



        // Carga los productos
        Recycler_Producto("");


    }
    //---Algoritmo de busqueda
    private boolean seach(String value,String valueSeach){
        boolean resultado = false;

        if(value == null){
            resultado=  false;
        }else{
            //Convierte los valores String en minuscula para facilitar la busqueda
            value=value.toLowerCase();


            String [] stringsArrayValueBuscador;
            int rdsultado2;

            //------------------   Algoritbo de busqueda --------------------------------------
            stringsArrayValueBuscador=valueSeach.split(" ");
            if(!valueSeach.equals("")){
                for (int i = 0; i < stringsArrayValueBuscador.length; i++){
                    // aqui se puede referir al objeto con arreglo[i];
                    rdsultado2 = value.indexOf(stringsArrayValueBuscador[i].toLowerCase());
                    if(rdsultado2 != -1) {
                        resultado=true;
                    }else{
                        resultado=false;
                        break;
                    }}}
        }

        return resultado;
    }

    //////////////////////////  CARGA PRODUCTOS ///////////////////////////////////////////////////
    public  void Recycler_Producto(final String categoria){



        //---Click en el item seleccionado
        recyclerViewProducto =(RecyclerView) findViewById(R.id.recyclerView_productos);
        //recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this,4));
        //--Adaptadores
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_ProductosNegocio(adapter_productoList,this);
        adapter_recyclerView_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Extrae la id de la reseña
                final adapter_producto adapterProductoOriginal=adapter_productoList.get(recyclerViewProducto.getChildAdapterPosition(view));



                //////////////////////////////// Cuadro de Dialog //////////////////////////////////
                final Dialog dialog=new Dialog(MainActivity_productos.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.view_crear_producto);
                dialog.show();

                // Reference
                circleImageViewProducto=(CircleImageView) dialog.findViewById(R.id.imageView_producto);

                editTextMarca=(EditText) dialog.findViewById(R.id.editText_marca);
                editTextCodigo=(EditText) dialog.findViewById(R.id.editText_codigo);
                editTextInfo=(EditText) dialog.findViewById(R.id.editText_info);
                editTextPrecio=(EditText) dialog.findViewById(R.id.editText_Precio);
                buttonGuardarProduct=(Button) dialog.findViewById(R.id.button18);
                buttonEliminar=(Button) dialog.findViewById(R.id.button19_eliminar);
                progressBar_foto =(ProgressBar) dialog.findViewById(R.id.progressBar2);
                progressBar_foto.setVisibility(View.GONE);
                final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.progressBar7);
                progressBar.setVisibility(View.GONE);

                //Set
                // Carga la imagen de perfil
                if(!adapterProductoOriginal.getUrlimagen().equals("default")){
                    Glide.with(MainActivity_productos.this)
                            .load(adapterProductoOriginal.getUrlimagen())
                            .fitCenter()
                            .centerCrop()
                            .into(circleImageViewProducto);
                }

                idUnicoProducto=adapterProductoOriginal.getId();
                urlDescargarFoto=adapterProductoOriginal.getUrlimagen();

                editTextCodigo.setText(adapterProductoOriginal.getCodigo());
                editTextMarca.setText(adapterProductoOriginal.getInfo1());
                editTextInfo.setText(adapterProductoOriginal.getInfo2());
                editTextPrecio.setText(Double.toString(adapterProductoOriginal.getPrecio()));


                buttonEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Firestore
                        DocumentReference documentReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS)).document(adapterProductoOriginal.getId());
                        documentReference.delete();

                        // Storage
                        desertRef = storageRef.child(getString(R.string.DB_NEGOCIOS)).child(ID_NEGOCIO).child(getString(R.string.DB_PRODUCTOS)).child(adapterProductoOriginal.getId());
                        desertRef.delete();

                        // Resta punto
                        PuntosCuenta.RestaPuntos(1,MainActivity_productos.this);

                        // Reset
                        idUnicoProducto=null;
                        urlDescargarFoto=null;
                        dialog.dismiss();



                    }
                });

                buttonGuardarProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String urlImagen=urlDescargarFoto;
                        if(urlImagen==null){urlImagen="default";}String finalUrlImagen = urlImagen;

                        String ValorInfo_1=editTextMarca.getText().toString();
                        String ValorInfo_2=editTextInfo.getText().toString();

                        String Codigo=editTextCodigo.getText().toString();
                        double ValorPrecio=Double.parseDouble(editTextPrecio.getText().toString());



                        if(!ValorInfo_1.equals("") && !ValorInfo_2.equals("") ){
                            if(ValorPrecio != 0 ){

                                // Referencia de la db
                                DocumentReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS)).document(adapterProductoOriginal.getId());
                                adapter_producto adapterProducto = new adapter_producto();

                                // por defecto
                                adapterProducto.setCategoria(adapterProductoOriginal.getCategoria());
                                adapterProducto.setSubcategoria(adapterProductoOriginal.getSubcategoria());
                                adapterProducto.setSubcategoria_2(adapterProductoOriginal.getSubcategoria_2());
                                adapterProducto.setId(adapterProductoOriginal.getId());

                                // Set value
                                adapterProducto.setCodigo(Codigo);
                                adapterProducto.setUrlimagen(finalUrlImagen);
                                adapterProducto.setPrecio(ValorPrecio);
                                adapterProducto.setInfo1(ValorInfo_1);
                                adapterProducto.setInfo2(ValorInfo_2);
                                // Set
                                collectionReference.set(adapterProducto, SetOptions.merge());

                                // Reset
                                idUnicoProducto=null;
                                urlDescargarFoto=null;
                                dialog.dismiss();

                            }else { Toast.makeText(MainActivity_productos.this,R.string.escriba_un_precio,Toast.LENGTH_LONG).show(); }
                        }else { Toast.makeText(MainActivity_productos.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_LONG).show(); }
                    }
                });


            }});

        recyclerViewProducto.setAdapter(adapter_recyclerView_productos);



        //  BASE DE DATOS
        CollectionReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adapter_productoList.removeAll(adapter_productoList);

                //Visibility
                imageViewFondo.setVisibility(View.VISIBLE);

                for (DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){
                        adapter_producto adapterProducto=doc.toObject(adapter_producto.class);

                        adapterProducto.setId(doc.getId());

                        // Agrega el producto
                        if(categoria.equals("")){
                            adapter_productoList.add(adapterProducto);
                        }else if(!categoria.equals("")){
                            if(seach(adapterProducto.getSubcategoria(),categoria)
                                    || seach(adapterProducto.getSubcategoria(),categoria)
                                    || seach(adapterProducto.getSubcategoria_2(),categoria)
                                    || seach(adapterProducto.getInfo1(),categoria)
                                    || seach(adapterProducto.getInfo2(),categoria)
                                    || seach(adapterProducto.getCodigo(),categoria)){

                                // ADD
                                adapter_productoList.add(adapterProducto);

                                //Visibility
                                imageViewFondo.setVisibility(View.GONE);
                            }else { }
                        }

                    }
                }

                if(adapter_productoList.size()==0){ imageViewFondo.setVisibility(View.VISIBLE);}

                adapter_recyclerView_productos.notifyDataSetChanged();

            }
        });
    }

    //////////////////////////// Accion Retroceso //////////////////////////////////////////////////
    @Override
    public void onBackPressed() { finish(); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //-Button retroceso
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    //---------------------------   BUTTON
    public void button_AgregarProducto(View view){
        //---Lanzador de activity Cuentas
        Intent intent=new Intent(MainActivity_productos.this,MainActivity_productos_add.class);
        startActivity(intent);
    }
    public  void ButtonADDFotho(View view) {

        //-metodo para seleccion una imagen en la galeria
        Intent intent = new Intent(Intent.ACTION_PICK);

        //-Tipo de elementos a seleccionar
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, GALLLERY_INTENT);

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, GALLLERY_INTENT);
    }
    public void button_CrarProducto(View view){

        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        final Dialog dialog=new Dialog(MainActivity_productos.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.view_crear_producto);
        dialog.show();

        // Reference
        circleImageViewProducto=(CircleImageView) dialog.findViewById(R.id.imageView_producto);
        editTextMarca=(EditText) dialog.findViewById(R.id.editText_marca);
        editTextInfo=(EditText) dialog.findViewById(R.id.editText_info);
        editTextPrecio=(EditText) dialog.findViewById(R.id.editText_Precio);
        buttonGuardarProduct=(Button) dialog.findViewById(R.id.button18);
        buttonEliminar=(Button) dialog.findViewById(R.id.button19_eliminar);
        buttonEliminar.setVisibility(View.GONE);
        progressBar_foto =(ProgressBar) dialog.findViewById(R.id.progressBar2);
        progressBar_foto.setVisibility(View.GONE);





        buttonGuardarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlImagen=urlDescargarFoto;

                String ValorMarcaNombre=editTextMarca.getText().toString();
                String ValorInfo=editTextInfo.getText().toString();
                double ValorPrecio=Double.parseDouble(editTextPrecio.getText().toString());
                if(urlImagen==null){urlImagen="default";}
                String finalUrlImagen = urlImagen;


                if(!ValorMarcaNombre.equals("") && !ValorInfo.equals("") ){
                    if(ValorPrecio != 0 ){

                        // ID producto
                        if(idUnicoProducto == null){
                            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                            idUnicoProducto = s.format(new Date());
                        }

                        // Referencia de la db
                        DocumentReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS)).document(idUnicoProducto);
                        adapter_producto adapterProducto = new adapter_producto();
                        adapterProducto.setUrlimagen(finalUrlImagen);
                        adapterProducto.setInfo1(ValorInfo);
                        adapterProducto.setInfo2(ValorMarcaNombre);
                        adapterProducto.setPrecio(ValorPrecio);
                        adapterProducto.setId(idUnicoProducto);
                        // Set
                        collectionReference.set(adapterProducto, SetOptions.merge());

                        // Reset
                        idUnicoProducto=null;
                        urlDescargarFoto=null;
                        dialog.dismiss();

                    }else { Toast.makeText(MainActivity_productos.this,R.string.escriba_un_precio,Toast.LENGTH_LONG).show(); }
                }else { Toast.makeText(MainActivity_productos.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_LONG).show(); }

            }
        });

    }
    public  void Button_Scanner(View view){

        IntentIntegrator integrator = new IntentIntegrator(MainActivity_productos.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Scanner Codigo
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                editTextCodigo.setText(result.getContents().toString());

            }
        }



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
                bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);
                bytesMapImagen=baos.toByteArray();

                if(idUnicoProducto == null){
                    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                    idUnicoProducto = s.format(new Date());
                }


                //-Crea la carpeta dentro del Storage
                mStorage= FirebaseStorage.getInstance().getReference();
                StorageReference filePath=mStorage.child(getString(R.string.DB_NEGOCIOS)).child(ID_NEGOCIO).child(getString(R.string.DB_PRODUCTOS)).child(idUnicoProducto);

                // Subida de imagen al Storage
                UploadTask uploadTask= filePath.putBytes(bytesMapImagen);

                //Proceso de subida del archivo
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(MainActivity_productos.this,R.string.foto_actualizada,Toast.LENGTH_SHORT).show();
                        progressBar_foto.setVisibility(View.GONE);


                        //-Obtiene la url de la foto
                        urlDescargarFoto= String.valueOf(taskSnapshot.getDownloadUrl());


                        // Carga la imagen de perfil
                        Glide.with(MainActivity_productos.this)
                                .load(bytesMapImagen)
                                .fitCenter()
                                .centerCrop()
                                .into(circleImageViewProducto);


                    }
                });




            }else{ Toast.makeText(MainActivity_productos.this,"error",Toast.LENGTH_SHORT).show(); }


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinner_categorias:
                if(  spinnerCategoria.getSelectedItemPosition() !=0  ){
                    Recycler_Producto(spinnerCategoria.getSelectedItem().toString());
                }else{Recycler_Producto("");}
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }
}
