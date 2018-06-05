package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adaptador_pedido;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adapter_recyclerView_lista_pedidos;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_pedidos_lista_historia extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    // String Datos
    private String ID_NEGOCIO;

    // estado Delivery
    private TextView textView_cantidadPeidos_delivery;
    private ImageView imageView_delivery;

    // estado Pedido en el negocio
    private TextView textView_cantidadPeidos_Local;
    private ImageView imageView_ReitorPorNegocio;



    ////////////////////////////  Delivery
    public RecyclerView recyclerViewPedidos;
    public List<adaptador_pedido> adapter_PedidosList;
    public adapter_recyclerView_lista_pedidos adapter_recyclerView_pedidos;
    private Integer iCantidad_PedidosDelivery=0;


    ////////////////////////////  Retiro en el domicilio
    public RecyclerView recyclerViewPedidos_RetiroDomicilio;
    public List<adaptador_pedido> adapter_PedidosList_RetiroDomicilio;
    public adapter_recyclerView_lista_pedidos adapter_recyclerView_pedidos_RetiroDomicilio;
    private Integer iCantidad_PedidosEnRetiroLocal=0;


    // FIRESTORE
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedidos);
        setTitle(getResources().getString(R.string.historial));

        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Datos APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);

        // REference
        textView_cantidadPeidos_delivery =(TextView) findViewById(R.id.textView_cantidadPeidos_delivery);
        textView_cantidadPeidos_Local =(TextView) findViewById(R.id.textView_cantidadPeidos_Local);
        imageView_delivery=(ImageView) findViewById(R.id.imageView19);
        imageView_ReitorPorNegocio=(ImageView) findViewById(R.id.imageView21);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_sistema_pedidos);

        // color
        final int newColor = getResources().getColor(R.color.colorPrimary);
        // Set Color
        textView_cantidadPeidos_delivery.setTextColor( newColor );
        imageView_delivery.setColorFilter( newColor , PorterDuff.Mode.SRC_ATOP);
        // Set Color
        textView_cantidadPeidos_delivery.setTextColor( newColor );
        imageView_delivery.setColorFilter( newColor , PorterDuff.Mode.SRC_ATOP);


        //Bottom bar navigation OnClick
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.navigation_delivery:
                        //Vista Informacion del Negocio
                        recyclerViewPedidos.setVisibility(View.VISIBLE);
                        recyclerViewPedidos_RetiroDomicilio.setVisibility(View.GONE);

                        break;
                    case R.id.navigation_pedidosLocal:
                        //Vista de ofertas
                        recyclerViewPedidos.setVisibility(View.GONE);
                        recyclerViewPedidos_RetiroDomicilio.setVisibility(View.VISIBLE);

                        break;
                }
                return true;
            }});

        // Cargar DAtos
        CargarPedidos_Delivery();
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


    public void CargarPedidos_Delivery(){

        //---Click en el item seleccionado
        recyclerViewPedidos_RetiroDomicilio =(RecyclerView) findViewById(R.id.recyclerView2_retiro_local);
        recyclerViewPedidos_RetiroDomicilio.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_PedidosList_RetiroDomicilio =new ArrayList<>();
        adapter_recyclerView_pedidos_RetiroDomicilio =new adapter_recyclerView_lista_pedidos(adapter_PedidosList_RetiroDomicilio,MainActivity_pedidos_lista_historia.this);

        adapter_recyclerView_pedidos_RetiroDomicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adaptador_pedido adapterProductoOriginal=adapter_PedidosList_RetiroDomicilio.get(recyclerViewPedidos_RetiroDomicilio.getChildAdapterPosition(view));

                //---Lanzador de activity Cuentas
                Intent intent=new Intent(MainActivity_pedidos_lista_historia.this,MainActivity_pedidos_lista_historia_vista_producto.class);
                intent.putExtra("ID_PEDIDO",adapterProductoOriginal.getId());
                startActivity(intent);


            }
        });
        recyclerViewPedidos_RetiroDomicilio.setAdapter(adapter_recyclerView_pedidos_RetiroDomicilio);


        //---Click en el item seleccionado
        recyclerViewPedidos =(RecyclerView) findViewById(R.id.recyclerview_pedidosLista);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_PedidosList =new ArrayList<>();
        adapter_recyclerView_pedidos =new adapter_recyclerView_lista_pedidos(adapter_PedidosList,MainActivity_pedidos_lista_historia.this);

        adapter_recyclerView_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adaptador_pedido adapterProductoOriginal=adapter_PedidosList.get(recyclerViewPedidos.getChildAdapterPosition(view));

                //---Lanzador de activity Cuentas
                Intent intent=new Intent(MainActivity_pedidos_lista_historia.this,MainActivity_pedidos_lista_historia_vista_producto.class);
                intent.putExtra("ID_PEDIDO",adapterProductoOriginal.getId());
                startActivity(intent);


            }
        });
        recyclerViewPedidos.setAdapter(adapter_recyclerView_pedidos);



        // Firesote
        CollectionReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS_HISTORIAL)  );
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot task, @Nullable FirebaseFirestoreException e) {

                adapter_PedidosList.removeAll(adapter_PedidosList);
                adapter_PedidosList_RetiroDomicilio.removeAll(adapter_PedidosList_RetiroDomicilio);

                for (DocumentSnapshot doc : task) {
                    if (doc.exists()) {

                        // Adaptadores
                        adaptador_pedido adapterProducto=doc.toObject(adaptador_pedido.class);
                        adapterProducto.setId(doc.getId());


                        if( adapterProducto.getTipo_entrega() == 1 ){

                            // CArga en la lista de pedidos en el local
                            adapter_PedidosList_RetiroDomicilio.add(adapterProducto);

                        }else if( adapterProducto.getTipo_entrega() == 2 ){

                            // Carga en la lista de delivery
                            adapter_PedidosList.add(adapterProducto);

                        }

                    }

                    // Cantidad de pedidos de Retiros en el local
                    iCantidad_PedidosEnRetiroLocal=adapter_PedidosList_RetiroDomicilio.size();

                    // Set Cantidad de productos
                    textView_cantidadPeidos_Local.setText( String.valueOf( iCantidad_PedidosEnRetiroLocal ) );

                    // Cantidad de pedidos de Delivery
                    iCantidad_PedidosDelivery=adapter_PedidosList.size();

                    // Set Cantidad de productos
                    textView_cantidadPeidos_delivery.setText( String.valueOf( iCantidad_PedidosDelivery ) );

                }
                adapter_recyclerView_pedidos.notifyDataSetChanged();
                recyclerViewPedidos_RetiroDomicilio.setAdapter(adapter_recyclerView_pedidos_RetiroDomicilio);


            }
        });


    }




}
