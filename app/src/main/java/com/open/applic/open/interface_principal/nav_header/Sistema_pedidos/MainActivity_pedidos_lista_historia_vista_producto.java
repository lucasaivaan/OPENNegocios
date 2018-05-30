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
import com.google.firebase.firestore.SetOptions;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adaptador_pedido;
import com.open.applic.open.interface_principal.nav_header.chat.Chat_view;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_producto;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_ProductosPedidos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_pedidos_lista_historia_vista_producto extends AppCompatActivity {

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
    private Button button31_Eliminar;

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
        setContentView(R.layout.activity_main_pedidos_lista_historia_vista_producto);

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

        textView_PrecioTotal=(TextView) findViewById(R.id.textView43_precioTotal);
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
        button31_Eliminar=(Button) findViewById(R.id.button31_Eliminar);


        // Control de visibilidad
        layout_ubicacion.setVisibility(View.GONE);


        // OnClick
        button31_Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapterProducto.getId() != null){

                    // Firesote
                    DocumentReference docRef=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS_HISTORIAL)  ).document( ID_PEDIDO );
                    docRef.delete();

                    // Finaliza la actividad
                    finish();

                }
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
        DocumentReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS_HISTORIAL)  ).document( ID_PEDIDO );
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
                            Intent intent2 = new Intent (MainActivity_pedidos_lista_historia_vista_producto.this,Chat_view.class);
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

                                    Glide.with( MainActivity_pedidos_lista_historia_vista_producto.this.getApplicationContext () ).load(adapterPerfilClientes.getUrlfotoPerfil()).fitCenter().centerCrop().into(imgFotoPerfil);
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
    public void CargarProductos(Map <String,Object> mapListProductos){

        // Obtener el Recycler
        recyclerViewProducto = (RecyclerView) findViewById(R.id.recyclerview_productos_pedidos);
        recyclerViewProducto.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(MainActivity_pedidos_lista_historia_vista_producto.this,3));

        // Crear un nuevo adaptador
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_ProductosPedidos(adapter_productoList,MainActivity_pedidos_lista_historia_vista_producto.this);

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
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity_pedidos_lista_historia_vista_producto.this);
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
        // Eventos de Recyclerviews     (problema de recursividad)
        adapter_recyclerView_productos.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                // Precio toltal
                dTotalPrecio=0.0;

                if(adapter_productoList.size() != 0){
                    // Recorre el ArrayList de los productos seleccionados
                    for(adapter_producto adapterProducto: adapter_productoList){

                        try {
                            // Cantidad de producto
                            Integer iCantidad= adapterProducto.getInfopedido().get("cantidad").hashCode();

                            if( adapterProducto.getPrecio() != null && iCantidad != null){
                                // Multiplica el precio del producto  por la cantidad
                                dTotalPrecio+=adapterProducto.getPrecio() * iCantidad ;
                            }
                            // set precio total
                            textView_PrecioTotal.setText( String.valueOf(Double.toString(dTotalPrecio)) );

                        }catch (Exception ex){  Toast.makeText(MainActivity_pedidos_lista_historia_vista_producto.this,"Error; "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                }else{ }

            }
        });


        // Recorre la lista de productos
        for (Map.Entry<String, Object> mapProductoInfoPedido : mapListProductos.entrySet()) {

            String key = mapProductoInfoPedido.getKey().toString();
            Object mapInfoPedido = mapProductoInfoPedido.getValue();


            // Adaptador
            adapter_producto adapterProducto=new adapter_producto();
            adapterProducto.setId(key);// CantidAd


            adapterProducto.setInfopedido((Map<String, Object>) mapInfoPedido);

            adapter_productoList.add(adapterProducto);


        }
        adapter_recyclerView_productos.notifyDataSetChanged();







    }

}
