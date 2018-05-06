package com.open.applic.open.interface_principal.nav_header.productos;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.metodos_funciones.PuntosCuenta;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_producto;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_Productos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class MainActivity_productos_add extends AppCompatActivity   implements AdapterView.OnItemSelectedListener{

    // DAtos SharePreferencesAPP
    public String ID_NEGOCIO;



    //TOllbar
    public EditText editText_Toolbar_Seach;
    private CardView cardViewSeachToolbar;
    private  Boolean ValueSeachCode=false;

    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_Productos adapter_recyclerView_productos;
    private ImageView imageViewFondo;

    //Variables para cargar Spinner desde un Array
    String ItemSpinner_1;
    String ItemSpinner_2;
    String ItemSpinner_3;

    String sSpinnerCategoria_1="";
    String sSpinnerCategoria_2="";
    String sSpinnerCategoria_3="";

    private LinearLayout layout_cat_1;
    private LinearLayout layout_cat_2;
    private LinearLayout layout_cat_3;

    private Button buttonCrearProduct;

    // View Flotante
    private CircleImageView circleImageViewProducto;
    private EditText editTextMarca;
    private EditText editTextInfo;
    private EditText editTextPrecio;
    public  EditText editTextCodigo;
    private Button buttonGuardarProduct;
    private  Button buttonEliminar;

    private Spinner spinnerCategoria_1;
    private Spinner spinnerCategoria_2;
    private Spinner spinnerCategoria_3;

    ////////////////////////////  FIRESTORE
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_productos_add);
        setTitle(R.string.productos);

        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Datos APP SharePreferences commit
        ID_NEGOCIO=SharePreferencesAPP.getID_NEGOCIO(this);


        // Rederece
        spinnerCategoria_1=(Spinner) findViewById(R.id.spinner_1);
        buttonCrearProduct=(Button) findViewById(R.id.button19_crear_producto);
        buttonCrearProduct.setVisibility(View.GONE);
        imageViewFondo=(ImageView) findViewById(R.id.imageView15_fondo);
        editText_Toolbar_Seach=(EditText) findViewById(R.id.editText2_seach);
        cardViewSeachToolbar=(CardView) findViewById(R.id.cardview_seach);
        cardViewSeachToolbar.setVisibility(View.GONE);

        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
        spinnerCategoria_1.setOnItemSelectedListener(this);
        spinnerCategoria_2=(Spinner) findViewById(R.id.spinner_2);
        spinnerCategoria_3=(Spinner) findViewById(R.id.spinner_3);

        layout_cat_1=(LinearLayout) findViewById(R.id.layout_cat_1);
        layout_cat_2=(LinearLayout) findViewById(R.id.layout_cat_2);
        layout_cat_3=(LinearLayout) findViewById(R.id.layout_cat_3);

        layout_cat_2.setVisibility(View.GONE);
        layout_cat_3.setVisibility(View.GONE);




        //---
        editText_Toolbar_Seach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {

                if(!editText_Toolbar_Seach.getText().toString().equals("")){
                    if(!spinnerCategoria_1.getSelectedItem().toString().equals(getString(R.string.elije_una_categoaria))){

                        // Buscador
                        BuscarProducto(spinnerCategoria_1.getSelectedItem().toString(),editText_Toolbar_Seach.getText().toString());
                    }
                }else {

                    if(!spinnerCategoria_1.getSelectedItem().toString().equals(getString(R.string.elije_una_categoaria))){

                        // Si ahi una categoria seleccionada muestra los elementos
                        Recycler_Producto(spinnerCategoria_1.getSelectedItem().toString(),null,null);
                    }else {

                        //Si no ahi ninguna categoria seleccionada muesta la lista vacia
                        adapter_productoList.removeAll(adapter_productoList);
                        imageViewFondo.setVisibility(View.VISIBLE);
                        cardViewSeachToolbar.setVisibility(View.GONE);
                        adapter_recyclerView_productos.notifyDataSetChanged();
                    }
                }
            }
        });


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



    ///////////////////////////  CARGA PRODUCTOS ///////////////////////////////////////////////////
    public  void Recycler_Producto(String Categoria_1, final String Categoria_2, final String Categoria_3){



        //---Click en el item seleccionado
        recyclerViewProducto =(RecyclerView) findViewById(R.id.recyclerView_productos);
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this,4));
        //--Adaptadores
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_Productos(adapter_productoList,this);
        adapter_recyclerView_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adapter_producto adapterProductoOriginal=adapter_productoList.get(recyclerViewProducto.getChildAdapterPosition(view));



                //////////////////////////////// Cuadro de Dialog //////////////////////////////////
                final Dialog dialog=new Dialog(MainActivity_productos_add.this);
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
                editTextCodigo=(EditText) dialog.findViewById(R.id.editText_codigo);
                buttonGuardarProduct=(Button) dialog.findViewById(R.id.button18);
                buttonGuardarProduct.setText(R.string.guardar_producto);
                buttonEliminar=(Button) dialog.findViewById(R.id.button19_eliminar);
                buttonEliminar.setVisibility(View.GONE);
                progressBar_foto =(ProgressBar) dialog.findViewById(R.id.progressBar2);
                progressBar_foto.setVisibility(View.GONE);
                progressBar_foto =(ProgressBar) dialog.findViewById(R.id.progressBar7);
                progressBar_foto.setVisibility(View.GONE);

                //Set
                // Carga la imagen de perfil
                Glide.with(MainActivity_productos_add.this)
                        .load(adapterProductoOriginal.getUrlimagen())
                        .fitCenter()
                        .centerCrop()
                        .into(circleImageViewProducto);
                idUnicoProducto=adapterProductoOriginal.getId();
                urlDescargarFoto=adapterProductoOriginal.getUrlimagen();
                editTextMarca.setText(adapterProductoOriginal.getInfo1());
                editTextInfo.setText(adapterProductoOriginal.getInfo2());
                editTextCodigo.setText(adapterProductoOriginal.getCodigo());


                buttonGuardarProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar_foto.setVisibility(View.VISIBLE);


                        String urlImagen=urlDescargarFoto;
                        String ValorMarcaNombre=editTextMarca.getText().toString();
                        String ValorInfo=editTextInfo.getText().toString();
                        String ValueCode=editTextCodigo.getText().toString();
                        double ValorPrecio;
                        if(!editTextPrecio.getText().toString().equals("")){
                            ValorPrecio = Double.parseDouble(editTextPrecio.getText().toString());
                        }else {ValorPrecio=0;}

                        if(urlImagen==null){urlImagen="default";}
                        String finalUrlImagen = urlImagen;


                        if(ValorPrecio != 0 && !ValorMarcaNombre.equals("") && !ValorInfo.equals("") ){

                            // Referencia de la db
                            DocumentReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS)).document(adapterProductoOriginal.getId());
                            adapter_producto adapterProducto = new adapter_producto();

                            // por defecto
                            adapterProducto.setCategoria(adapterProductoOriginal.getCategoria());
                            adapterProducto.setSubcategoria(adapterProductoOriginal.getSubcategoria());
                            if(spinnerCategoria_3 != null){ adapterProducto.setSubcategoria_2(adapterProductoOriginal.getSubcategoria_2());}
                            adapterProducto.setId(adapterProductoOriginal.getId());
                            // set
                            adapterProducto.setCodigo(ValueCode);
                            adapterProducto.setUrlimagen(finalUrlImagen);
                            adapterProducto.setInfo2(ValorInfo);
                            adapterProducto.setInfo1(ValorMarcaNombre);
                            adapterProducto.setPrecio(ValorPrecio);

                            // Set
                            collectionReference.set(adapterProducto, SetOptions.merge());

                            // Suma puntos
                            PuntosCuenta.SumaPuntos(MainActivity_productos_add.this,1,getString(R.string.producto_nuevo)+"!");



                            Recycler_Producto(ItemSpinner_1,ItemSpinner_2,ItemSpinner_3);
                            // Reset
                            idUnicoProducto=null;
                            urlDescargarFoto=null;
                            dialog.dismiss();

                        }else { Toast.makeText(MainActivity_productos_add.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_LONG).show(); }

                    }
                });


            }});
        recyclerViewProducto.setAdapter(adapter_recyclerView_productos);


        //   BASE DE DATOS
        CollectionReference collectionReference=db.collection(getString(R.string.DB_APP)).document( SharePreferencesAPP.getPAIS(this) ).collection( getString(R.string.DB_PRODUCTOS) ).document(getString(R.string.DB_CATEGORIA)).collection(Categoria_1);

        // .whereEqualTo( getString(R.string.db_producto_categoria ),sProvincia)
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                adapter_productoList.removeAll(adapter_productoList);
                imageViewFondo.setVisibility(View.VISIBLE);

                if(task.isSuccessful()){
                    for (DocumentSnapshot doc:task.getResult()){
                        if(doc.exists()){
                            adapter_producto adapterProducto=doc.toObject(adapter_producto.class);

                            if( Categoria_2!=null && Categoria_3==null ){

                                if(adapterProducto.getSubcategoria().equals(Categoria_2)){
                                    adapterProducto.setId(doc.getId());
                                    adapter_productoList.add(adapterProducto);
                                    adapter_recyclerView_productos.notifyDataSetChanged();

                                    //Visibility
                                    imageViewFondo.setVisibility(View.GONE);
                                }
                            }else if( Categoria_2!=null && Categoria_3!=null ){
                                if(adapterProducto.getSubcategoria().equals(Categoria_2) && adapterProducto.getSubcategoria_2().equals(Categoria_3 ) ){
                                    adapterProducto.setId(doc.getId());
                                    adapter_productoList.add(adapterProducto);

                                    adapter_recyclerView_productos.notifyDataSetChanged();

                                    //Visibility
                                    imageViewFondo.setVisibility(View.GONE);
                                }

                            }else{
                                adapterProducto.setId(doc.getId());
                                adapter_productoList.add(adapterProducto);
                                adapter_recyclerView_productos.notifyDataSetChanged();

                                //Visibility
                                imageViewFondo.setVisibility(View.GONE);
                            }


                        }
                    }
                }

            }
        });
    }

    public void BuscarProducto(String Categoria,final String producto){
        CollectionReference collectionReference1=db.collection(getString(R.string.DB_APP)).document( SharePreferencesAPP.getPAIS(this) ).collection( getString(R.string.DB_PRODUCTOS) ).document(getString(R.string.DB_CATEGORIA)).collection(Categoria);

        // .whereEqualTo( getString(R.string.db_producto_categoria ),sProvincia)
        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                adapter_productoList.removeAll(adapter_productoList);
                imageViewFondo.setVisibility(View.VISIBLE);

                if(task.isSuccessful()){



                    for (DocumentSnapshot doc:task.getResult()){
                        if(doc.exists()){
                            adapter_producto adapterProducto=doc.toObject(adapter_producto.class);

                            if(seach(adapterProducto.getSubcategoria_2(),producto)
                                    || seach(adapterProducto.getSubcategoria_2(),producto)
                                    || seach(adapterProducto.getInfo1(),producto)
                                    || seach(adapterProducto.getInfo2(),producto)
                                    || seach(adapterProducto.getCodigo(),producto)){
                                adapterProducto.setId(doc.getId());
                                adapter_productoList.add(adapterProducto);
                                adapter_recyclerView_productos.notifyDataSetChanged();

                                //Visibility
                                imageViewFondo.setVisibility(View.GONE);

                            }

                        }
                    }
                    adapter_recyclerView_productos.notifyDataSetChanged();
                    if(adapter_productoList.size()==0){ imageViewFondo.setVisibility(View.VISIBLE); }
                }

            }
        });
    }


    ///////////////////////////////////////////  BUTTON ///////////////////////////////////////////////
    public  void ButtonADDFotho(View view) {

    //-metodo para seleccion una imagen en la galeria
    Intent intent = new Intent(Intent.ACTION_PICK);

    //-Tipo de elementos a seleccionar
    intent.setType("image/jpeg");
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
    startActivityForResult(intent, GALLLERY_INTENT);

}

    public void Button_CrearPRoducto(View view){

        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        final Dialog dialog=new Dialog(MainActivity_productos_add.this);
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
        editTextCodigo=(EditText) dialog.findViewById(R.id.editText_codigo);
        buttonGuardarProduct=(Button) dialog.findViewById(R.id.button18);
        buttonGuardarProduct.setText(R.string.guardar_producto);
        buttonEliminar=(Button) dialog.findViewById(R.id.button19_eliminar);
        buttonEliminar.setVisibility(View.GONE);
        progressBar_foto =(ProgressBar) dialog.findViewById(R.id.progressBar2);
        progressBar_foto.setVisibility(View.GONE);
        final ProgressBar progressBar=(ProgressBar) dialog.findViewById(R.id.progressBar7);
        progressBar.setVisibility(View.GONE);

        buttonGuardarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String urlImagen=urlDescargarFoto;
                String cat_1=ItemSpinner_1;
                String cat_2=ItemSpinner_2;
                String cat_3=ItemSpinner_3;
                String ValorMarcaNombre=editTextMarca.getText().toString();
                String ValorInfo=editTextInfo.getText().toString();
                String ValueCodigo=editTextCodigo.getText().toString();

                double ValorPrecio;
                if(!editTextPrecio.getText().toString().equals("")){ ValorPrecio = Double.parseDouble(editTextPrecio.getText().toString()); }else {ValorPrecio=0;}
                if(urlImagen==null){urlImagen="default";}
                final String finalUrlImagen = urlImagen;

                // ID Unico
                if(idUnicoProducto == null){
                    SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                    idUnicoProducto = s.format(new Date());
                }


                if(ValorPrecio != 0 && !ValorMarcaNombre.equals("") && !ValorInfo.equals("") ){

                    // Genera ID unico para el item
                    if(idUnicoProducto == null){
                        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                        idUnicoProducto = s.format(new Date());
                    }

                    // Referencia de la db
                    final DocumentReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS)).document(idUnicoProducto);
                    final adapter_producto adapterProducto = new adapter_producto();



                    if(bytesMapImagen != null){
                        progressBar.setVisibility(View.VISIBLE);

                        // por defecto
                        adapterProducto.setCategoria(cat_1);
                        adapterProducto.setSubcategoria(cat_2);
                        adapterProducto.setSubcategoria_2(cat_3);
                        adapterProducto.setId( idUnicoProducto );
                        // set
                        adapterProducto.setCodigo(ValueCodigo);
                        adapterProducto.setInfo2(ValorInfo);
                        adapterProducto.setInfo1(ValorMarcaNombre);
                        adapterProducto.setPrecio(ValorPrecio);

                        //-Crea la carpeta dentro del Storage
                        mStorage= FirebaseStorage.getInstance().getReference();
                        StorageReference filePath=mStorage.child(getString(R.string.DB_APP)).child(ID_NEGOCIO).child(getString(R.string.DB_PRODUCTOS)).child(idUnicoProducto);

                        // Subida de imagen al Storage
                        UploadTask uploadTask= filePath.putBytes(bytesMapImagen);

                        //Proceso de subida del archivo
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(MainActivity_productos_add.this,R.string.foto_actualizada,Toast.LENGTH_SHORT).show();
                                progressBar_foto.setVisibility(View.GONE);

                                //-Obtiene la url de la foto
                                urlDescargarFoto= String.valueOf(taskSnapshot.getDownloadUrl());

                                adapterProducto.setUrlimagen(urlDescargarFoto);

                                // Set
                                collectionReference.set(adapterProducto, SetOptions.merge());

                                // Suma puntos
                                PuntosCuenta.SumaPuntos(MainActivity_productos_add.this,1,getString(R.string.producto_nuevo)+"!");

                                // Reset
                                idUnicoProducto=null;
                                urlDescargarFoto=null;
                                bytesMapImagen=null;
                                dialog.dismiss();
                            }
                        });



                    }else{

                        // por defecto
                        adapterProducto.setCategoria(cat_1);
                        adapterProducto.setSubcategoria(cat_2);
                        adapterProducto.setSubcategoria_2(cat_3);
                        adapterProducto.setId( idUnicoProducto );
                        // set
                        adapterProducto.setCodigo(ValueCodigo);
                        adapterProducto.setUrlimagen("default");
                        adapterProducto.setInfo2(ValorInfo);
                        adapterProducto.setInfo1(ValorMarcaNombre);
                        adapterProducto.setPrecio(ValorPrecio);

                        // Set
                        collectionReference.set(adapterProducto, SetOptions.merge());

                        // Suma puntos
                        PuntosCuenta.SumaPuntos(MainActivity_productos_add.this,1,getString(R.string.producto_nuevo)+"!");

                        // Reset
                        idUnicoProducto=null;
                        urlDescargarFoto=null;
                        bytesMapImagen=null;
                        dialog.dismiss();
                    }





                }else { Toast.makeText(MainActivity_productos_add.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_LONG).show(); }

            }
        });

    }

    public  void Button_Scanner(View view){

        IntentIntegrator integrator = new IntentIntegrator(MainActivity_productos_add.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    public  void Button_Scanner_tollbar(View view){

        ValueSeachCode=true;

        IntentIntegrator integrator = new IntentIntegrator(MainActivity_productos_add.this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_1:


                if(position!=0){
                    //Almaceno el nombre  seleccionado
                    ItemSpinner_1 = spinnerCategoria_1.getSelectedItem().toString();
                    ItemSpinner_2=null;
                    ItemSpinner_3=null;
                    sSpinnerCategoria_1 =String.valueOf(position);


                    // Carga RecyclerView
                    Recycler_Producto(ItemSpinner_1,ItemSpinner_2,ItemSpinner_3);


                    int Id_Array = this.getResources().getIdentifier("spinner_"+ String.valueOf(position), "array", this.getPackageName());
                    ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), Id_Array,android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria_2.setAdapter(adapter);
                    //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
                    spinnerCategoria_2.setOnItemSelectedListener(this);

                    // Visibility
                    layout_cat_2.setVisibility(View.VISIBLE);
                    layout_cat_3.setVisibility(View.GONE);
                    buttonCrearProduct.setVisibility(View.GONE);
                    cardViewSeachToolbar.setVisibility(View.VISIBLE);

                }

                break;
            case R.id.spinner_2:


                if(position!=0){
                    //Almaceno el nombre  seleccionado
                    ItemSpinner_2 = spinnerCategoria_2.getSelectedItem().toString();
                    ItemSpinner_3=null;
                    sSpinnerCategoria_2 =String.valueOf(position);

                    // Carga RecyclerView
                    Recycler_Producto(ItemSpinner_1,ItemSpinner_2,null);

                    try {


                        int Id_Array = this.getResources().getIdentifier("spinner_"+ sSpinnerCategoria_1 + sSpinnerCategoria_2, "array", this.getPackageName());
                        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), Id_Array,android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoria_3.setAdapter(adapter);
                        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
                        spinnerCategoria_3.setOnItemSelectedListener(this);

                        // Visibility
                        layout_cat_2.setVisibility(View.VISIBLE);
                        layout_cat_3.setVisibility(View.VISIBLE);
                        buttonCrearProduct.setVisibility(View.GONE);

                    }catch (Exception ec){

                        // Visibility
                        layout_cat_2.setVisibility(View.VISIBLE);
                        layout_cat_3.setVisibility(View.GONE);
                        buttonCrearProduct.setVisibility(View.VISIBLE);
                        spinnerCategoria_3.setAdapter(null);
                    }

                }

                break;
            case R.id.spinner_3:

                if(position!=0){

                    //Almaceno el nombre  seleccionado
                    ItemSpinner_3 = spinnerCategoria_3.getSelectedItem().toString();
                    sSpinnerCategoria_3 =String.valueOf(position);

                    // Carga RecyclerView
                    Recycler_Producto(ItemSpinner_1,ItemSpinner_2,ItemSpinner_3);

                    // Control Visibilidad
                    buttonCrearProduct.setVisibility(View.VISIBLE);

                }

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Scanner Codigo
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {

            if(ValueSeachCode == true){
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                    ValueSeachCode=false;
                } else {
                    editText_Toolbar_Seach.setText(result.getContents().toString());
                    ValueSeachCode=false;
                }
            }else{
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    editTextCodigo.setText(result.getContents().toString());

                }
            }

        }

        ////////////////// Comprobacion que se alla seleccionado una foto //////////////////////////
        if(requestCode==GALLLERY_INTENT && resultCode==RESULT_OK){

            progressBar_foto.setVisibility(View.GONE); //progressBar cargando foto de perfil
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


                // Carga la imagen de perfil
                Glide.with(MainActivity_productos_add.this)
                        .load(bytesMapImagen)
                        .fitCenter()
                        .centerCrop()
                        .into(circleImageViewProducto);



            }else{ Toast.makeText(MainActivity_productos_add.this,"error",Toast.LENGTH_SHORT).show(); }


        }
    }
}
