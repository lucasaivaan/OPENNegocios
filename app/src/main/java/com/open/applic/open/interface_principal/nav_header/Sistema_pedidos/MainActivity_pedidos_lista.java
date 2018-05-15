package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adaptador_pedido;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adapter_recyclerView_lista_pedidos;
import com.open.applic.open.interface_principal.nav_header.chat.Chat_view;
import com.open.applic.open.interface_principal.nav_header.productos.MainActivity_productos;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_ProductosNegocio;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_pedidos_lista extends AppCompatActivity {

    // String Datos
    private String ID_NEGOCIO;

    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewPedidos;
    public List<adaptador_pedido> adapter_PedidosList;
    public adapter_recyclerView_lista_pedidos adapter_recyclerView_pedidos;

    // FIRESTORE
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pedidos);
        setTitle(getResources().getString(R.string.sitema_pedidos));

        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Datos APP
        ID_NEGOCIO= SharePreferencesAPP.getID_NEGOCIO(this);

        // Cargar DAtos
        CargarPedidos();
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


    public void CargarPedidos(){

        //---Click en el item seleccionado
        recyclerViewPedidos =(RecyclerView) findViewById(R.id.recyclerview_pedidosLista);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        //--Adaptadores
        adapter_PedidosList =new ArrayList<>();
        adapter_recyclerView_pedidos =new adapter_recyclerView_lista_pedidos(adapter_PedidosList,MainActivity_pedidos_lista.this);

        adapter_recyclerView_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Extrae la id de la reseña
                final adaptador_pedido adapterProductoOriginal=adapter_PedidosList.get(recyclerViewPedidos.getChildAdapterPosition(view));

                //---Lanzador de activity Cuentas
                Intent intent=new Intent(MainActivity_pedidos_lista.this,MainActivity_pedido.class);
                intent.putExtra("ID_PEDIDO",adapterProductoOriginal.getId());
                startActivity(intent);


            }
        });
        recyclerViewPedidos.setAdapter(adapter_recyclerView_pedidos);



        // Firesote
        CollectionReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS)  );
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot task, @Nullable FirebaseFirestoreException e) {

                adapter_PedidosList.removeAll(adapter_PedidosList);

                for (DocumentSnapshot doc : task) {
                    if (doc.exists()) {
                        // Adaptadores
                        adaptador_pedido adapterProducto=doc.toObject(adaptador_pedido.class);
                        adapter_PedidosList.add(adapterProducto);

                    }

                }
                adapter_recyclerView_pedidos.notifyDataSetChanged();

            }
        });


    }



}
