package com.open.applic.open.interface_principal.nav_header.productos.metodos_adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_ProductosPedidos extends RecyclerView.Adapter<adapter_recyclerView_ProductosPedidos.homeViwHolder>
        implements View.OnClickListener{



    private  adapter_producto adapterProducto;
    private String ID_NEGOCIO;
    private Context context;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private List<adapter_producto> adapter_productoList;


    private View.OnClickListener listener;

    public adapter_recyclerView_ProductosPedidos(List<adapter_producto> productos, Context context) {
        this.context=context;
        this.adapter_productoList = productos;
    }


    @Override
    public adapter_recyclerView_ProductosPedidos.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_productos_pedido,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_ProductosPedidos.homeViwHolder holder=new adapter_recyclerView_ProductosPedidos.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, final int position) {
        adapterProducto= adapter_productoList.get(position);

        //Datos
        ID_NEGOCIO = SharePreferencesAPP.getID_NEGOCIO(context);

        // Firestore
        DocumentReference documentReference=db.collection(  context.getString(R.string.DB_NEGOCIOS)  ).document( ID_NEGOCIO ).collection(  context.getString(R.string.DB_PRODUCTOS)  ).document(adapterProducto.getId());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if(task.isSuccessful()){

                    DocumentSnapshot documentProducto=task.getResult();
                    if(documentProducto.exists()){

                        // Adaptador
                        adapterProducto=documentProducto.toObject(adapter_producto.class);

                        // SET
                        adapter_productoList.get(position).setId( adapterProducto.getId() );
                        adapter_productoList.get(position).setUrlimagen( adapterProducto.getUrlimagen() );
                        adapter_productoList.get(position).setCategoria( adapterProducto.getCategoria() );
                        adapter_productoList.get(position).setSubcategoria( adapterProducto.getSubcategoria() );
                        adapter_productoList.get(position).setSubcategoria_2( adapterProducto.getSubcategoria_2() );
                        adapter_productoList.get(position).setInfo1( adapterProducto.getInfo1() );
                        adapter_productoList.get(position).setInfo2( adapterProducto.getInfo2() );
                        adapter_productoList.get(position).setCodigo( adapterProducto.getCodigo() );
                        adapter_productoList.get(position).setPrecio( adapterProducto.getPrecio() );
                        adapter_productoList.get(position).setTipo(adapterProducto.getTipo());

                        // info Pedido
                        adapter_productoList.get(position).setInfopedido(adapter_productoList.get(position).getInfopedido());


                        // SET
                        if(adapter_productoList.get(position).getInfo1() != null){
                            // Marca
                            holder.datoMarca.setText(adapter_productoList.get(position).getInfo1());
                            // Nombre del producto
                            holder.datoInfo.setText(adapter_productoList.get(position).getInfo2());

                            // Cantidad de producto
                            holder.datoCantidad.setText( String.valueOf( adapter_productoList.get(position).getInfopedido().get("cantidad").hashCode() ) );

                            // Precio
                            if(adapter_productoList.get(position).getPrecio() ==null){
                                holder.datoPrecio.setVisibility(View.GONE);
                            }else {
                                holder.datoPrecio.setText(String.valueOf(  adapter_productoList.get(position).getPrecio() * adapter_productoList.get(position).getInfopedido().get("cantidad").hashCode()  ));
                            }

                            // Imagen del producto
                            if(!adapter_productoList.get(position).getUrlimagen().equals("default")){

                                Glide.with(context.getApplicationContext ())
                                        .load(adapter_productoList.get(position).getUrlimagen())
                                        .fitCenter()
                                        .centerCrop()
                                        .into(holder.ImageProducto);
                            }else { holder.progressBar.setVisibility(View.GONE); }

                            // Actualiza Precio total
                            notifyDataSetChanged();
                        }

                    }
                }
            }
        });





    }


    @Override
    public int getItemCount() {
        return adapter_productoList.size();  //devuelve el numero de fila que tiene el recycleview
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){ listener.onClick(view); }
        }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }

    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        // Campos respectivos de un item
        private TextView datoMarca,datoInfo,datoPrecio,datoCantidad;
        private CircleImageView ImageProducto;
        private ImageView imageViewTilde;
        private CardView cardView;
        private ProgressBar progressBar;

        public homeViwHolder(View itemView) {
            super(itemView);

            datoMarca=(TextView) itemView.findViewById(R.id.textView_marca);
            datoInfo=(TextView) itemView.findViewById(R.id.textView_info);
            datoCantidad=(TextView) itemView.findViewById(R.id.textViewProducto_cantidad);
            datoPrecio=(TextView) itemView.findViewById(R.id.textView_precio);
            ImageProducto=(CircleImageView) itemView.findViewById(R.id.imageView_productoPedidos);
            imageViewTilde=(ImageView) itemView.findViewById(R.id.imageView15_tilde);
            cardView=(CardView) itemView.findViewById(R.id.carview_producto);

            progressBar=(ProgressBar) itemView.findViewById(R.id.progressBar2);

        }
    }



}