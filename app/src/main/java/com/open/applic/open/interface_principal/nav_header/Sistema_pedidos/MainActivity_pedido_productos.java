package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adaptador_pedido;
import com.open.applic.open.interface_principal.nav_header.chat.Chat_view;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_producto;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adapter_recyclerView_ProductosPedidos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_pedido_productos extends AppCompatActivity {

    // String Datos
    private String ID_NEGOCIO;
    private String ID_PEDIDO;
    private String ID_CLIENTE;
    private Double dTotalPrecio;

    // ToolBar
    private CircleImageView imgFotoPerfil;
    private TextView tvNombre;
    private ImageButton button_Telefono;
    private ImageButton button_Chat;

    // Button
    private Button button_ConfirmarPedido;
    private Button button_CancelarPedido;
    private Button button_pedidoEnviado;
    private Button button_pedidoListo;
    private Button button31_hecho;

    // Informacion
    private TextView textView_PrecioTotal;
    private TextView textView_Nombre;
    private TextView textView_telefono;
    private TextView textView_calle;
    private TextView textView_numero;
    private TextView textView_entreCalles;
    private TextView textView_ubicacion;
    private TextView textView_piso_depto;
    private TextView textView_abono;
    private TextView textView_nota;
    private TextView textView_TipoPedido;
    private TextView textView_horario_de_retiro;

    // LAYOUT
    private LinearLayout layout_ubicacion;
    private LinearLayout layout_nota;




    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_ProductosPedidos adapter_recyclerView_productos;
    private adaptador_pedido adapterProducto;

    /*
    Declarar instancias globales
    */
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    // FIRESTORE
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedido);
        //setTitle(getResources().getString(R.string.sitema_pedidos));

        //-------------------------------- Toolbar -------------------------------------------------
        // (Barra de herramientas)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_perfil);
        setSupportActionBar(toolbar);

        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Datos APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);
        ID_PEDIDO =getIntent().getStringExtra("ID_PEDIDO");

        // Reference
        imgFotoPerfil=(CircleImageView) findViewById(R.id.fotoPerfil);
        tvNombre=(TextView) findViewById(R.id.tv_nombre);
        button_Telefono=(ImageButton) findViewById(R.id.imageButton4);
        button_Chat=(ImageButton) findViewById(R.id.imageButton5);

        button_ConfirmarPedido=(Button) findViewById(R.id.button_confirmar_pedido);
        button_ConfirmarPedido.setVisibility(View.GONE);
        button_CancelarPedido=(Button) findViewById(R.id.button_cancelar_pedido);
        textView_PrecioTotal=(TextView) findViewById(R.id.textView43_precioTotal);
        button_pedidoEnviado =(Button) findViewById(R.id.button31_pedidoEnviado);
        button_pedidoListo =(Button) findViewById(R.id.textview_pedidoListo);
        button31_hecho=(Button) findViewById(R.id.button31_hecho);
        button31_hecho.setVisibility(View.GONE);
        button_pedidoEnviado.setVisibility(View.GONE);
        button_pedidoListo.setVisibility(View.GONE);

        textView_Nombre=(TextView) findViewById(R.id.textView50_nombre);
        textView_telefono=(TextView) findViewById(R.id.textView62_telefono);
        textView_calle=(TextView) findViewById(R.id.textView52_calle);
        textView_numero=(TextView) findViewById(R.id.textView56_numero);
        textView_entreCalles=(TextView) findViewById(R.id.textView58_EntreCalles);
        textView_ubicacion=(TextView) findViewById(R.id.textView60_ubicacion);
        textView_piso_depto=(TextView) findViewById(R.id.textview_depto_piso);
        layout_ubicacion=(LinearLayout) findViewById(R.id.layout_ubicacion);
        textView_abono=(TextView) findViewById(R.id.textView75_abono);
        textView_nota=(TextView) findViewById(R.id.textView78_nota);
        textView_TipoPedido =(TextView) findViewById(R.id.textView83_tipo_de_pedido);
        textView_horario_de_retiro =(TextView) findViewById(R.id.textView_horario_de_retiro);
        layout_nota=(LinearLayout) findViewById(R.id.layout_nota);



        // Control de visibilidad
        layout_ubicacion.setVisibility(View.GONE);

        // OnClick
        button_ConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firesote
                DocumentReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS)  ).document( ID_PEDIDO );

                // MAP DATOS ESTADO
                Map <String,Integer> mapESTADO =new HashMap<String, Integer>();
                mapESTADO.put("estado",1);  //0=(pendiente) 1= (confirmado) 2 =(eliminado)
                collectionReference.set(mapESTADO, SetOptions.merge());

                // FIRESTORE CLIENTE
                DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(  ID_CLIENTE  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                documentReferenceCliente.set(mapESTADO, SetOptions.merge());

            }
        });
        button_pedidoListo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firesote
                DocumentReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS)  ).document( ID_PEDIDO );

                // MAP DATOS ESTADO
                Map <String,Integer> mapESTADO =new HashMap<String, Integer>();
                mapESTADO.put("estado",3); //0=(pendiente) 1= (En proceso) 2 =(Enviado) 3=( Listo) 4= (cancelado) 5 =(recibido)
                collectionReference.set(mapESTADO, SetOptions.merge());

                // FIRESTORE CLIENTE
                DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(  ID_CLIENTE  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                documentReferenceCliente.set(mapESTADO, SetOptions.merge());

            }
        });
        button_pedidoEnviado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Firesote
                DocumentReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS)  ).document( ID_PEDIDO );

                // MAP DATOS ESTADO
                Map <String,Integer> mapESTADO =new HashMap<String, Integer>();
                mapESTADO.put("estado",2); //0=(pendiente) 1= (En proceso) 2 =(Enviado) 3=( Listo) 4= (cancelado) 5 =(recibido)
                collectionReference.set(mapESTADO, SetOptions.merge());

                // FIRESTORE CLIENTE
                DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(  ID_CLIENTE  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                documentReferenceCliente.set(mapESTADO, SetOptions.merge());

                button_pedidoEnviado.setVisibility(View.GONE);

            }
        });
        button_CancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ID_CLIENTE != null){
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity_pedido_productos.this);
                    dialogo1.setMessage(R.string.pregunta_cancelar_pedido);
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                            AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity_pedido_productos.this);

                            LinearLayout ll_alert_layout = new LinearLayout(MainActivity_pedido_productos.this);
                            ll_alert_layout.setOrientation(LinearLayout.VERTICAL);
                            final EditText ed_input = new EditText(MainActivity_pedido_productos.this);
                            ll_alert_layout.addView(ed_input);

                            alertbox.setTitle("Mensaje (Motivo de la cancelación)");
                            alertbox.setMessage("");

                            //setting linear layout to alert dialog

                            alertbox.setView(ll_alert_layout);

                            alertbox.setNegativeButton("Omitir",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            // Envia el pedido al historial
                                            CollectionReference docRef_HistorialPedidos=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS_HISTORIAL)  );
                                            adapterProducto.setEstado(4);
                                            docRef_HistorialPedidos.document( adapterProducto.getId() ).set( adapterProducto );

                                            // MAP DATOS ESTADO
                                            Map <String,Object> mapESTADO =new HashMap<String, Object>();
                                            mapESTADO.put("estado",4);   //0=(pendiente) 1= (En proceso) 2 =(Enviado) 3=( Listo) 4= (cancelado) 5 =(recibido)
                                            mapESTADO.put("mensaje","");
                                            // FIRESTORE CLIENTE
                                            DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(  ID_CLIENTE  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                                            documentReferenceCliente.set(mapESTADO, SetOptions.merge());

                                            // FIRESTORE NEGOCIO
                                            DocumentReference documentReferenceNegocio=firestore.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                                            documentReferenceNegocio.delete();

                                            // Finaliza la activity
                                            finish();
                                        }
                                    });


                            alertbox.setPositiveButton("Enviar mensaje",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            String input_text = ed_input.getText().toString();

                                            // MAP DATOS ESTADO
                                            Map <String,Object> mapESTADO =new HashMap<String, Object>();
                                            mapESTADO.put("estado",4);  //0=(pendiente) 1= (En proceso) 2 =(Enviado) 3=( Listo) 4= (cancelado) 5 =(recibido)
                                            mapESTADO.put("mensaje",input_text);

                                            // FIRESTORE CLIENTE
                                            DocumentReference documentReferenceCliente=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document(  ID_CLIENTE  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                                            documentReferenceCliente.set(mapESTADO, SetOptions.merge());

                                            // FIRESTORE NEGOCIO
                                            DocumentReference documentReferenceNegocio=firestore.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                                            documentReferenceNegocio.delete();

                                            // Finaliza la activity
                                            finish();

                                        }
                                    });
                            alertbox.show();




                        }
                    });
                    dialogo1.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            // User cancelled the dialog
                        }
                    });
                    dialogo1.show();
                }else{
                    Toast.makeText(MainActivity_pedido_productos.this,R.string.error_field_required,Toast.LENGTH_LONG).show();
                }




            }
        });

        button31_hecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Envia el pedido al historial
                CollectionReference docRef_HistorialPedidos=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS_HISTORIAL)  );
                docRef_HistorialPedidos.document( adapterProducto.getId() ).set( adapterProducto );


                // FIRESTORE NEGOCIO
                DocumentReference documentReferenceNegocio=firestore.collection( getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(  ID_PEDIDO  );
                documentReferenceNegocio.delete();

                // Finaliza la activity
                finish();



            }
        });



        // CARGAR DATOS
        CargarDatosPedido();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void CargarDatosPedido(){


        // Firesote
        DocumentReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS)  ).document( ID_PEDIDO );
        collectionReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    // Adaptador del pedido
                    adapterProducto=documentSnapshot.toObject(adaptador_pedido.class);


                    // Set datos pedido
                    textView_Nombre.setText(  adapterProducto.getContacto()  );

                    // Telefono
                    if( !adapterProducto.getTelefono().equals("")){
                        textView_telefono.setText(  adapterProducto.getTelefono()  );
                    }else { textView_telefono.setText(  getResources().getString(R.string.sin_especificar)  ); }


                    // Abono
                    if(!adapterProducto.getCantidad_pago().equals("")){
                        textView_abono.setText(  adapterProducto.getCantidad_pago()  );
                    }else{ textView_abono.setText( getResources().getString(R.string.sin_especificar) );}

                    // nota
                    if(!adapterProducto.getNota().equals("")){
                        textView_nota.setText(  adapterProducto.getNota()  );
                    }else {
                        textView_nota.setVisibility(View.GONE);
                        layout_nota.setVisibility(View.GONE);
                    }


                    // Tipo de pedido
                    if( adapterProducto.getTipo_entrega() == 1){
                        // Titulo : Retira en el local
                        textView_TipoPedido.setText( getResources().getString(R.string.retira_en_el_local) );

                        // Horario de retiro
                        String sTexto = getResources().getString(R.string.retiro);
                        textView_horario_de_retiro.setText( sTexto +": "+ adapterProducto.getHora() );

                        // Control de visibilidad
                        layout_ubicacion.setVisibility(View.GONE);

                        switch (adapterProducto.getEstado()){
                            case 0:
                                // Control de visibilidad
                                button_ConfirmarPedido.setVisibility(View.VISIBLE);

                                break;
                            case 1:
                                // Control de visibilidad
                                button_pedidoEnviado.setVisibility(View.GONE);
                                button_pedidoListo.setVisibility(View.VISIBLE);
                                button_ConfirmarPedido.setVisibility(View.GONE);

                                break;

                            case 5:
                                // Control de visibilidad
                                button31_hecho.setVisibility(View.VISIBLE);
                                break;
                        }

                    }else if( adapterProducto.getTipo_entrega() == 2){

                        // Delivery
                        textView_TipoPedido.setText( getResources().getString(R.string.delivery)  );

                        // Direccion
                        if(adapterProducto.getDireccion() != null){
                            Map <String,String> mapDireccion =adapterProducto.getDireccion();
                            // SET
                            textView_calle.setText(  mapDireccion.get("calle")  );
                            textView_numero.setText( mapDireccion.get("numero") );

                            // Entre calles
                            if( !mapDireccion.get("entre_calles").equals("")){
                                textView_entreCalles.setText(  mapDireccion.get("entre_calles")  );
                            }else { textView_entreCalles.setText(  getResources().getString(R.string.sin_especificar)  ); }

                            // Localidad
                            textView_ubicacion.setText(  mapDireccion.get("localidad")+","+mapDireccion.get("ciudad")  );

                            // Piso/Depto
                            if( !mapDireccion.get("piso_depto").equals("")){
                                textView_piso_depto.setText(  mapDireccion.get("piso_depto")  );
                            }else { textView_piso_depto.setText(  getResources().getString(R.string.sin_especificar)  ); }


                            // Control de visibilidad
                            layout_ubicacion.setVisibility(View.VISIBLE);

                            switch (adapterProducto.getEstado()){
                                case 0:
                                    // Control de visibilidad
                                    button_ConfirmarPedido.setVisibility(View.VISIBLE);

                                    break;
                                case 1:
                                    // Control de visibilidad
                                    button_pedidoEnviado.setVisibility(View.VISIBLE);
                                    button_pedidoListo.setVisibility(View.GONE);
                                    button_ConfirmarPedido.setVisibility(View.GONE);

                                    break;
                                case 5:
                                    // Control de visibilidad
                                    button31_hecho.setVisibility(View.VISIBLE);
                                    break;
                            }


                            //  Control de visivilidad
                            textView_horario_de_retiro.setVisibility(View.GONE);
                        }

                    }

                    //---- Button
                    button_Telefono.setOnClickListener(new View.OnClickListener() {

                        // Cliente
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_DIAL);
                            i.setData(Uri.parse("tel:" + adapterProducto.getTelefono()));
                            startActivity(i);
                        }});

                    button_Chat.setOnClickListener(new View.OnClickListener() {

                        // Cliente
                        @Override
                        public void onClick(View view) {
                            //--.lanzadador Activity
                            Intent intent2 = new Intent (MainActivity_pedido_productos.this,Chat_view.class);
                            intent2.putExtra("parametroIdClient",adapterProducto.getId_cliente());
                            startActivityForResult(intent2, 0);
                        }});



                    // Carga los productos
                    CargarProductos(adapterProducto.getLista_productos());

                    //Base de datos delcliente
                    DocumentReference documentCliente=firestore.collection(  getString(R.string.DB_CLIENTES)  ).document(  adapterProducto.getId_cliente()  );
                    documentCliente.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot docClient=task.getResult();


                                // Adaptador
                                adapter_perfil_clientes adapterPerfilClientes=docClient.toObject(adapter_perfil_clientes.class);

                                ID_CLIENTE=adapterPerfilClientes.getId();

                                // Imagen
                                if(!adapterPerfilClientes.getUrlfotoPerfil().equals("default")) {

                                    Glide.with( MainActivity_pedido_productos.this.getApplicationContext () ).load(adapterPerfilClientes.getUrlfotoPerfil()).fitCenter().centerCrop().into(imgFotoPerfil);
                                }

                                // Nombre
                                tvNombre.setText(adapterPerfilClientes.getNombre());

                            }
                        }
                    });
                }

            }
        });
    }
    public void CargarProductos(final Map <String,Object> mapListProductos){

        // Obtener el Recycler
        recyclerViewProducto = (RecyclerView) findViewById(R.id.recyclerview_productos_pedidos);
        recyclerViewProducto.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(MainActivity_pedido_productos.this,3));

        // Crear un nuevo adaptador
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_ProductosPedidos(adapter_productoList,MainActivity_pedido_productos.this);

        //--Adaptadores
        recyclerViewProducto.setAdapter(adapter_recyclerView_productos);

        // OnClick
        adapter_recyclerView_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adapter_producto adapterProductoOriginal=adapter_productoList.get(recyclerViewProducto.getChildAdapterPosition(view));


                if(adapterProductoOriginal.getTipo() == 1){

                    List<String> ListaSabores = new ArrayList<String>();

                    Map <String, Object> mapGustos = (Map <String, Object>) adapterProductoOriginal.getInfopedido().get("gustos");
                    for (Map.Entry<String, Object> mapProductoInfoPedido : mapGustos.entrySet()) {
                        ListaSabores.add(mapProductoInfoPedido.getValue().toString());
                    }

                    //Create sequence of items
                    final CharSequence[] Animals = ListaSabores.toArray(new String[ListaSabores.size()]);
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity_pedido_productos.this);
                    dialogBuilder.setTitle( getResources().getString(R.string.sabores));
                    dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                        }
                    });
                    //Create alert dialog object via builder
                    AlertDialog alertDialogObject = dialogBuilder.create();
                    //Show the dialog
                    alertDialogObject.show();

                }




            }
        });




        // Firebase
        CollectionReference referenceProductos=firestore.collection( getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection( getString(R.string.DB_PRODUCTOS));
        referenceProductos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    // Precio toltal
                    dTotalPrecio=0.0;

                    for(DocumentSnapshot docProducto : task.getResult()){
                        // Adaptador
                        adapter_producto adapterProducto=docProducto.toObject(adapter_producto.class);

                        // Recorre la lista de productos
                        for (Map.Entry<String, Object> mapProductoInfoPedido : mapListProductos.entrySet()) {
                            final String key = mapProductoInfoPedido.getKey().toString();
                            final Object mapInfoPedido = mapProductoInfoPedido.getValue();

                            if(adapterProducto.getId().equals( key)){

                                adapterProducto.setInfopedido((Map<String, Object>) mapInfoPedido);
                                adapter_productoList.add(adapterProducto);  // agrega el producto al recyclerview

                                //-- Cantidad de producto
                                Integer iCantidad= adapterProducto.getInfopedido().get("cantidad").hashCode();
                                if( adapterProducto.getPrecio() != null && iCantidad != null){
                                    // Multiplica el precio del producto  por la cantidad
                                    dTotalPrecio+=adapterProducto.getPrecio() * iCantidad ;
                                }
                                textView_PrecioTotal.setText( String.valueOf(Double.toString(dTotalPrecio)) );   // set precio total

                                adapter_recyclerView_productos.notifyDataSetChanged(); // Actualiza la lista del  recyclerview
                            }
                            }
                        }
                }
            }
        });









    }

}
