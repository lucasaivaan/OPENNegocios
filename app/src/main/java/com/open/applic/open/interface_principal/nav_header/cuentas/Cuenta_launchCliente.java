package com.open.applic.open.interface_principal.nav_header.cuentas;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.cuentas.adaptadores.adapter_productos;
import com.open.applic.open.interface_principal.nav_header.cuentas.adaptadores.adapter_recyclerView_producos;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_producto;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_ProductosNegocio;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cuenta_launchCliente extends AppCompatActivity {

    // FIRESTORE
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());


    ////////////////////////// Firebase ////////////////////////////////////////////////////////////
    //Auth
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    ///////////////////////// LAYOUT ADD PRODUCT ///////////////////////////////////////////////////
    //Button
    private Button ButtonAddProduct;
    private LinearLayout ButtonAddProductAlertDialog;
    //EditTextView
    private EditText editTextProducto;
    private EditText editTextPrecio;
    private AlertDialog alertDialogAddProduct;

    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_ProductosNegocio adapter_recyclerView_productos;


    //////////////////////////////// RecyclerView productos ////////////////////////////////////////
    //variables
     private DecimalFormat formato = new DecimalFormat("#.00");
    private double priceTotal=0.0;
    private double priceParcial=0.0;
    private TextView textViewPriceTotal;
    private LinearLayout linearLayoutPagoParcial;
    private TextView textViewPagoParcial;

    public RecyclerView recyclerViewProduts;
    public List<adapter_productos> adapter_productsList;
    public adapter_recyclerView_producos adapter_recyclerView_products;
    //Firebase
    private DatabaseReference databaseReferenceRecyclrViewProducts= FirebaseDatabase.getInstance().getReference();
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //-String
    private String idCliente;
    private String ID_NEGOCIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_launch_cliente);
        setTitle(R.string.cuentas);
        getSupportActionBar().setElevation(0);

        //---habilita button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Datos APP SharePreferences commit
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);

        //ID del cliente
        idCliente= getIntent().getStringExtra("parametroIdCliente");


        //Reference
        textViewPriceTotal=(TextView) findViewById(R.id.textView_total);
        linearLayoutPagoParcial=(LinearLayout) findViewById(R.id.linealloyout_pago_parcial);
        textViewPagoParcial=(TextView) findViewById(R.id.textView_value_pago_parcial);



        /////////////////////////////// AlertDialog Add product ////////////////////////////////////
        ButtonAddProduct = (Button) findViewById(R.id.buttonAddProducotoLauch);
        ButtonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.view_cuenta_launch_add_producto, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Cuenta_launchCliente.this);
                builder.setView(dialoglayout);
                alertDialogAddProduct = builder.show();



                ////////////////////////////////////////////////////////////////////////////////////
                //Reference
                editTextProducto=(EditText)  dialoglayout.findViewById(R.id.editText_producto);
                editTextPrecio=(EditText)  dialoglayout.findViewById(R.id.editText_precio);
                recyclerViewProducto =(RecyclerView) dialoglayout.findViewById(R.id.recyclerview_AddProductos);
                editTextPrecio.addTextChangedListener(onTextChangedListener());

                // Escucha del EditText
                editTextProducto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override
                    public void afterTextChanged(Editable s) {

                        if(!editTextProducto.getText().toString().equals("")){

                            // Buscador
                            seach_product(editTextProducto.getText().toString());
                        }else {

                            }

                    }
                });

            }
        });

    }


    //////////////////////////////////// carga los datos en el RecyclerView ////////////////////////
    public void LoadRecyclerViewProducos(){

        //Title action bar
        DocumentReference documentReference=db.collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        if(doc.getString(  getString(R.string.DB_nombre)  ) != null || doc.getString(  getString(R.string.DB_nombrecliente)  ) != null){
                            setTitle(doc.getString(  getString(R.string.DB_nombre)  ));
                        }else{setTitle(R.string.cuenta);}
                    }
                }
            }
        });





        ////////////////////////////////// RecyclerView Product ////////////////////////////////////
        recyclerViewProduts =(RecyclerView) findViewById(R.id.recyclerview_cuenta_productos);
        recyclerViewProduts.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_productsList =new ArrayList<>();
        adapter_recyclerView_products =new adapter_recyclerView_producos(adapter_productsList);
        //---Click en el item seleccionado
        adapter_recyclerView_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //----------------------------------------Alert Dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Cuenta_launchCliente.this);
                builder1.setMessage(R.string.pregunta_elliminar_producto);
                builder1.setCancelable(true);
                builder1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference databaseReferenceFelete= FirebaseDatabase.getInstance().getReference();

                        DocumentReference docRefProduct=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid())
                                .collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente).collection(  getString(R.string.DB_CUENTA)  ).document(adapter_productsList.get(recyclerViewProduts.getChildAdapterPosition(view)).getId());

                        // Elimina el producto
                        docRefProduct.delete();


                        dialog.cancel();
                    }
                });
                builder1.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {dialog.cancel();}});
                AlertDialog alert11 = builder1.create();
                alert11.show();
                //----------------------------------------------Fin Alert Dialog

            }});
        recyclerViewProduts.setAdapter(adapter_recyclerView_products);


        // FIRESTORE
        CollectionReference collectionReferenceDB=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente).collection(  getString(R.string.DB_CUENTA)  );
        collectionReferenceDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                priceTotal=0;
                //Vacia la lista del recyclerView
                adapter_productsList.removeAll(adapter_productsList);
                for(DocumentSnapshot snapshot:querySnapshot) {

                    //Carga los datos en el adaptador
                    adapter_productos adapterProductos = snapshot.toObject(adapter_productos.class);

                    if (adapterProductos.getPrecio() != null) {
                        adapterProductos.setId(snapshot.getId());
                        //Agrega el adaptador con los datos en la lista de recyclerView
                        adapter_productsList.add(adapterProductos);
                        //-Suma el precio
                        priceTotal += adapterProductos.getPrecio();


                    }
                }
                // Actualiza el recyclerView
                adapter_recyclerView_products.notifyDataSetChanged();


                if (priceTotal >= 1) {


                    final DocumentReference collectionReferenceDB2 = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
                    collectionReferenceDB2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {



                                DocumentSnapshot docPerfil = task.getResult();
                                if (docPerfil.exists()) {

                                    if (docPerfil.getDouble("pagoparcial") != null) {
                                        if (docPerfil.getDouble("pagoparcial") >= 1) {

                                            //Muwstra un mensaje que el cliente tiene un pago parcial
                                            linearLayoutPagoParcial.setVisibility(View.VISIBLE);

                                            priceParcial = docPerfil.getDouble("pagoparcial");
                                            priceTotal -= priceParcial;
                                            textViewPagoParcial.setText("$" + String.valueOf(priceParcial));
                                            textViewPriceTotal.setText("$" + formato.format(priceTotal));


                                        }
                                    } else {
                                        textViewPriceTotal.setText("$" + formato.format(priceTotal));


                                    }
                                }
                            }
                        }
                    });

                } else {
                    textViewPriceTotal.setText("$0");
                    linearLayoutPagoParcial.setVisibility(View.GONE);

                    //--------------------- Firestore -------------------------------------------
                    DocumentReference docRef = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("pagoparcial", FieldValue.delete());
                    docRef.update(updates);
                }


            }
        });


    }
    public void seach_product(final String sClaveSeach){

        //---Click en el item seleccionado
        //recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //--Adaptadores
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_ProductosNegocio(adapter_productoList,this);
        adapter_recyclerView_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Extrae la id de la reseña
                final adapter_producto adapterProductoOriginal=adapter_productoList.get(recyclerViewProducto.getChildAdapterPosition(v));

                editTextProducto.setText(adapterProductoOriginal.getInfo1()+" "+adapterProductoOriginal.getInfo2());
                editTextPrecio.setText(Double.toString(adapterProductoOriginal.getPrecio()));

            }
        });

        recyclerViewProducto.setAdapter(adapter_recyclerView_productos);


        //  BASE DE DATOS
        CollectionReference collectionReference=db.collection(getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(getString(R.string.DB_PRODUCTOS));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adapter_productoList.removeAll(adapter_productoList);

                for (DocumentSnapshot doc:documentSnapshots){
                    if(doc.exists()){
                        adapter_producto adapterProducto=doc.toObject(adapter_producto.class);
                        adapterProducto.setId(doc.getId());

                        // Agrega el producto



                        if(sClaveSeach.equals("")){

                        }else if(!sClaveSeach.equals("")){
                            if(seach(adapterProducto.getSubcategoria(),sClaveSeach)
                                    || seach(adapterProducto.getSubcategoria(),sClaveSeach)
                                    || seach(adapterProducto.getSubcategoria_2(),sClaveSeach)
                                    || seach(adapterProducto.getInfo1(),sClaveSeach)
                                    || seach(adapterProducto.getInfo2(),sClaveSeach)
                                    || seach(adapterProducto.getCodigo(),sClaveSeach)){


                                adapter_productoList.add(adapterProducto);

                            }else { }
                        }

                    }
                }

                adapter_recyclerView_productos.notifyDataSetChanged();

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


    //________________________________ Button ______________________________________________________
    public void ButtonLiquidatePayment(View view){

        /////////////////////////////// AlertDialog Add product ////////////////////////////////////
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.layout_cuenta_launch_liquidar_cuenta, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Cuenta_launchCliente.this);
        builder.setView(dialoglayout);
        final AlertDialog alertDialogAddProduct = builder.show();



        ////////////////////////////////////////////////////////////////////////////////////
        //Reference
        Button Button_PagoFinal=(Button)  dialoglayout.findViewById(R.id.button_pago_final);
        Button Button_PagoParcial=(Button)  dialoglayout.findViewById(R.id.button_pago_parcial);
        editTextProducto=(EditText)  dialoglayout.findViewById(R.id.editText_producto);
        editTextPrecio=(EditText)  dialoglayout.findViewById(R.id.editText_precio);

        //____________________________ Button ______________________________________________________
        Button_PagoFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Cuenta_launchCliente.this);
                builder.setCancelable(true);
                builder.setTitle(R.string.liquidar_cuenta);
                DecimalFormat formato = new DecimalFormat("#.00");
                builder.setMessage("$"+formato.format(priceTotal));
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DocumentReference docRef=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
                                // Remove cuenta
                                Map<String,Object> updates = new HashMap<>();
                                updates.put("cuenta", FieldValue.delete());
                                docRef.update(updates);

                                CollectionReference collectionReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid())
                                        .collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente).collection(  getString(R.string.DB_CUENTA)  );

                                // Elimina todos los productos de la base de datos del cliente
                                deleteCollection(collectionReference, 50, EXECUTOR);

                                //-Finaliza el actyvity
                                finish();
                            }});builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}});
                AlertDialog dialog = builder.create();
                dialog.show();}});

        //____________________________________ Button _______________________________________________
        Button_PagoParcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // otherwise, show a dialog to ask for their name
                AlertDialog.Builder alert = new AlertDialog.Builder(Cuenta_launchCliente.this);
                alert.setTitle(R.string.liquidar_un_pago_parcial);
                //alert.setMessage("What is your name?");

                // Create EditText for entry
                final EditText editTextValue = new EditText(Cuenta_launchCliente.this);
                editTextValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextValue.setHint(R.string.escriba_la_cantidad);
                alert.setView(editTextValue);

                // Make an "OK" button to save the name
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {


                        // Grab the EditText's input
                        Double value = Double.valueOf(editTextValue.getText().toString());
                        priceTotal -= value;
                        if(value!=0 || value !=null){
                            value+=priceParcial;
                                //--------------------- FIRESTORE -------------------------------------------
                            DocumentReference documentReference=db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
                            Map<String,Object> valueMap=new HashMap<>();
                            valueMap.put("pagoparcial",value);
                            documentReference.set(valueMap, SetOptions.merge());


                            textViewPagoParcial.setText("$" + String.valueOf(value));
                            textViewPagoParcial.setVisibility(View.VISIBLE);
                            textViewPriceTotal.setText("$" + formato.format(priceTotal));

                            //Control de visibilidad
                            linearLayoutPagoParcial.setVisibility(View.VISIBLE);

                            alertDialogAddProduct.dismiss();
                            dialog.dismiss();
                        }else{Toast.makeText(Cuenta_launchCliente.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_SHORT).show();}



                    }
                });

                // Make a "Cancel" button
                // that simply dismisses the alert
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                });

                alert.show();

            }
        });

                ////////////////////////////////////////////////////////////////////////////////////

    }
    public void ButtonAddProduct(View view){
        if(!editTextProducto.getText().toString().equals("") || !editTextPrecio.getText().toString().equals("")) {

            // Cuenta Estado
            Map<String, Boolean> mapValue = new HashMap<>();
            mapValue.put("cuenta", true);
            DocumentReference docRef = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente);
            docRef.set(mapValue, SetOptions.merge());

            // guarda el objeto producto
            DocumentReference documentReference = db.collection(  getString(R.string.DB_NEGOCIOS)  ).document(firebaseUser.getUid()).collection(  getString(R.string.DB_CLIENTES)  ).document(idCliente).collection(  getString(R.string.DB_CUENTA)  ).document();
            Map<String, Object> producto = new HashMap<>();
            producto.put("producto", editTextProducto.getText().toString());
            String priceValue=editTextPrecio.getText().toString().replace(",",".").replace("$","");
            producto.put("precio",Double.parseDouble(priceValue));


            //Guarda el producto en la base de datos
            documentReference.set(producto);


            alertDialogAddProduct.dismiss();
        }else{Toast.makeText(Cuenta_launchCliente.this,R.string.uno_o_mas_campos_estan_vacios,Toast.LENGTH_SHORT).show();}

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Carga los datos en el recyclerView de producots
        LoadRecyclerViewProducos();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        TextView dato1,dato2;
        CircleImageView dato3;
        ImageView dato4;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textView_info);
            dato3=(CircleImageView) itemView.findViewById(R.id.imageView_perfilcliente);
            dato4=(ImageView) itemView.findViewById(R.id.imageView_noti_nuevo_mensaje2);



        }
    }

    /**
     * Delete all documents in a collection. Uses an Executor to perform work on a background
     * thread. This does *not* automatically discover and delete subcollections.
     */
    private Task<Void> deleteCollection(final CollectionReference collection, final int batchSize, Executor executor) {

        // Perform the delete operation on the provided Executor, which allows us to use
        // simpler synchronous logic without blocking the main thread.
        return Tasks.call(executor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Get the first batch of documents in the collection
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    // Move the query cursor to start after the last doc in the batch
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);

                    deleted = deleteQueryBatch(query);
                }

                return null;
            }
        });

    }

    /**
     * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
     * to avoid blocking/crashing the main thread.
     */
    @WorkerThread
    private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (DocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextPrecio.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }

                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,##");
                    String formattedString = formatter.format(longval);




                    //setting text after format to EditText
                    editTextPrecio.setText(formattedString);
                    editTextPrecio.setSelection(editTextPrecio.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editTextPrecio.addTextChangedListener(this);
            }
        };
    }
}
