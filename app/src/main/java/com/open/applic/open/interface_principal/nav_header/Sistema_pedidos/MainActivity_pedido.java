package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adaptador_pedido;
import com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores.adapter_recyclerView_lista_pedidos;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_producto;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_ProductosNegocio;
import com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores.adapter_recyclerView_ProductosPedidos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_pedido extends AppCompatActivity {

    // String Datos
    private String ID_NEGOCIO;
    private String ID_PEDIDO;

    // ToolBar
    private CircleImageView imgFotoPerfil;
    private TextView tvNombre;

    // Button
    private Button button_Confirmar;
    private Button button_Eliminar;



    ////////////////////////////  PRODUCTO
    public RecyclerView recyclerViewProducto;
    public List<adapter_producto> adapter_productoList;
    public adapter_recyclerView_ProductosPedidos adapter_recyclerView_productos;

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
        button_Confirmar=(Button) findViewById(R.id.button_confirmar_pedido);
        button_Eliminar=(Button) findViewById(R.id.button_cancelar_pedido);


        // CARGAR DATOS
        CargarDatosCliente();

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

    public void CargarDatosCliente(){


        // Firesote
        DocumentReference collectionReference=firestore.collection(  getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  getString(R.string.DB_PEDIDOS)  ).document( ID_PEDIDO );
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();

                    if(documentSnapshot.exists()){
                        // Adaptador del pedido
                        adaptador_pedido adapterProducto=documentSnapshot.toObject(adaptador_pedido.class);

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

                                    // Imagen
                                    if(!adapterPerfilClientes.getUrlfotoPerfil().equals("default")) {
                                        Glide.with(MainActivity_pedido.this).load(adapterPerfilClientes.getUrlfotoPerfil()).fitCenter().centerCrop().into(imgFotoPerfil);
                                    }

                                    // Nombre
                                    tvNombre.setText(adapterPerfilClientes.getNombre());

                                }
                            }
                        });
                    }
                }
            }
        });
    }
    public void CargarProductos(Map <String,Object> mapListProductos){

        //---Click en el item seleccionado
        recyclerViewProducto =(RecyclerView) findViewById(R.id.recyclerview_productos_pedidos);
        //recyclerViewProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProducto.setLayoutManager(new GridLayoutManager(this,4));
        //--Adaptadores
        adapter_productoList =new ArrayList<>();
        adapter_recyclerView_productos =new adapter_recyclerView_ProductosPedidos(adapter_productoList,this);
        adapter_recyclerView_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Recorre la lista de productos
        for (Map.Entry<String, Object> entry : mapListProductos.entrySet()) {

            String key = entry.getKey();
            Object value = entry.getValue();


            // Adaptador
            adapter_producto adapterProducto=new adapter_producto();
            adapterProducto.setId(key);

            adapter_productoList.add(adapterProducto);


        }
        adapter_recyclerView_productos.notifyDataSetChanged();




    }
}
