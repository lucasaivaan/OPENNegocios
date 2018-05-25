package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.icono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_lista_pedidos extends RecyclerView.Adapter<adapter_recyclerView_lista_pedidos.homeViwHolder>
        implements View.OnClickListener{

    // FIREBASE
    FirebaseFirestore firestore= FirebaseFirestore.getInstance();

    List<adaptador_pedido> ListPedidos;
    private Context context;

    private View.OnClickListener listener;

    public adapter_recyclerView_lista_pedidos(List<adaptador_pedido> ListDirecciones, Context context) {
        this.ListPedidos = ListDirecciones;
        this.context = context;
    }


    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_pedidos,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, final int position) {
        final adaptador_pedido ADP= ListPedidos.get(position);


        ListPedidos.get(position).setEstado(ADP.getEstado());

        // Nombre
        holder.tNombre.setText( ADP.getContacto() );


        // Tipo de entrega  1=retira_en_el_local   2=delivery
        if( ADP.getTipo_entrega() == 1){

            // set Tipo de pedido
            holder.tTipoEntrega.setText(  context.getResources().getString(R.string.retira_en_el_local) );
            holder.tDatoUbicacionRetira.setText( context.getResources().getString(R.string.retiro) );

            // set
            holder.tUbicacionHora.setText( ADP.getHora()  );
            
        }else if( ADP.getTipo_entrega() == 2 ){

            // set Tipo de pedido
            holder.tTipoEntrega.setText(  context.getResources().getString(R.string.delivery)  );
            holder.tDatoUbicacionRetira.setText( context.getResources().getString(R.string.ubicacion) );

            // Direccion
            String sCalle=ADP.getDireccion().get("calle");
            String sNumero=ADP.getDireccion().get("numero");
            String sLocaclidad=ADP.getDireccion().get("localidad");
            String sCiudad=ADP.getDireccion().get("ciudad");
            // set
            holder.tUbicacionHora.setText( sCalle+" "+sNumero+", "+sLocaclidad+", "+sCiudad  );

        }


        // Estado del pedido
        if(ADP.getEstado() != null ){

            switch (ADP.getEstado()){
                case 0:
                    holder.tEstado.setText("Pendiente");
                    holder.tEstado.setBackgroundResource(R.color.md_deep_orange_400);
                    break;
                case 1:
                    holder.tEstado.setText("En proceso");
                    holder.tEstado.setBackgroundResource(R.color.md_teal_300);
                    break;
                case 2:
                    holder.tEstado.setText("Pedido Enviado");
                    holder.tEstado.setBackgroundResource(R.color.md_light_green_400);
                    break;
                case 3:
                    holder.tEstado.setText("Listo para retirar");
                    holder.tEstado.setBackgroundResource(R.color.md_light_green_400);
                    break;

                case 4:
                    holder.tEstado.setText("Cancelado");
                    holder.tEstado.setBackgroundResource(R.color.md_red_400);
                    break;
                case 5:
                    holder.tEstado.setText("Recibido");
                    holder.tEstado.setBackgroundResource(R.color.md_light_green_500);
                    break;

            }

        }

        // Cantidad del producto
        Map<String, Object> map = ADP.getLista_productos();
        holder.tCantidadProducto.setText( String.valueOf(map.size()) );


        // hora
        Date codigoHora = ADP.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");//a pm o am
        holder.tHora.setText(sdf.format(codigoHora.getTime()));



        DocumentReference documentReference=firestore.collection( context.getString(R.string.DB_CLIENTES) ).document(ADP.getId_cliente());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){

                        // Adaptador perfil del negocip
                        adapter_perfil_clientes adapterProfileClientes=documentSnapshot.toObject(adapter_perfil_clientes.class);

                        // Imagen de perfil del negocio
                        if(adapterProfileClientes.getUrlfotoPerfil().equals("default")){
                            // Default
                        }else{
                            Glide.clear(holder.profile_image);
                            Context context=holder.profile_image.getContext();
                            //-Carga la imagen de perfil
                            Glide.with(context).load( adapterProfileClientes.getUrlfotoPerfil() ).into( holder.profile_image );

                        }


                    }
                }

            }
        });



    }




    @Override
    public int getItemCount() {
        return ListPedidos.size();  //devuelve el numero de fila que tiene el recycleview
    }




    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);

        }



    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }


    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        CircleImageView profile_image;
        TextView tTipoEntrega,tCantidadProducto,tHora,tEstado,tUbicacionHora,tNombre,tDatoUbicacionRetira;

        public homeViwHolder(View itemView) {
            super(itemView);

            tTipoEntrega=(TextView) itemView.findViewById(R.id.textView39);
            tCantidadProducto=(TextView) itemView.findViewById(R.id.textView42);
            tHora=(TextView) itemView.findViewById(R.id.textView41);
            tEstado=(TextView) itemView.findViewById(R.id.textView_estado);
            tUbicacionHora=(TextView) itemView.findViewById(R.id.textView43_ubicacion_Hora);
            tNombre=(TextView) itemView.findViewById(R.id.textView52_nombre);
            tDatoUbicacionRetira=(TextView) itemView.findViewById(R.id.textView_dato);


            profile_image=(CircleImageView) itemView.findViewById(R.id.profile_image);



        }
    }}