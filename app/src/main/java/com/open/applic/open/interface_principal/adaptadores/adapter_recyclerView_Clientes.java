package com.open.applic.open.interface_principal.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.open.applic.open.interface_principal.nav_header.chat.adaptador.HolderMensaje;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_Clientes extends RecyclerView.Adapter<adapter_recyclerView_Clientes.homeViwHolder> implements View.OnClickListener{

    private List<adapter_perfil_clientes> List_Client;
    private View.OnClickListener listener;
   private Context context;


   private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public adapter_recyclerView_Clientes(List<adapter_perfil_clientes> business,Context context) {
        this.List_Client = business;
        this.context=context;
    }


    @Override
    public adapter_recyclerView_Clientes.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_clientes,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Clientes.homeViwHolder holder=new adapter_recyclerView_Clientes.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, final int position) {
        final adapter_perfil_clientes ADP= List_Client.get(position);


        final TextView dato1=holder.dato1;
        final TextView dato2=holder.dato2;
        final CircleImageView dato3=holder.dato3;
        final ImageView dato4=holder.dato4;
        final DocumentReference documentReference;


        documentReference=firestore.collection(context.getResources().getString(R.string.DB_CLIENTES)).document(ADP.getId());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                        if(documentSnapshot.exists()){
                            adapter_perfil_clientes perfilClientes=documentSnapshot.toObject(adapter_perfil_clientes.class);



                            if(perfilClientes.getLocal() == false){

                                ////// CLIENTE ONLINE

                                List_Client.get(position).setNombre(perfilClientes.getNombre());

                                // Nombre
                                dato1.setText(perfilClientes.getNombre());

                                // TELEFONO
                                if(  perfilClientes.getTelefono() == null ){ dato2.setVisibility(View.GONE);}
                                else {
                                    if(perfilClientes.getTelefono().equals("")){ dato2.setVisibility(View.GONE); }
                                    else{ dato2.setText(perfilClientes.getTelefono());}}

                                // Imagen
                                if(!perfilClientes.getUrlfotoPerfil().equals("default")){
                                    Context context=dato3.getContext();
                                    Glide.with(context)
                                            .load(perfilClientes.getUrlfotoPerfil())
                                            .fitCenter()
                                            .centerCrop()
                                            .into(dato3);
                                }else{
                                    Glide.clear(dato3);

                                    dato3.setImageDrawable(null);
                                    dato3.setBackgroundResource(R.mipmap.ic_user2);
                                }


                                //icono mensaje de notificacion de mensaje nuevo
                                if(perfilClientes.getMensaje_nuevo().equals(true)){
                                    dato4.setVisibility(View.VISIBLE);
                                }else if(perfilClientes.getMensaje_nuevo().equals(false)){
                                    dato4.setVisibility(View.GONE);
                                }





                            }else{

                                //////// CLIENTE LOCAL

                                // Nombre
                                dato1.setText(ADP.getNombre());

                                // TELEFONO
                                if( ADP.getTelefono() == null){ dato2.setVisibility(View.GONE);}
                                else {dato2.setText(ADP.getTelefono());}

                                Glide.clear(dato3);

                                dato3.setImageDrawable(null);
                                dato3.setBackgroundResource(R.mipmap.ic_user2);

                                //icono mensaje de notificacion de mensaje nuevo
                                if(ADP.getMensaje_nuevo().equals(true)){  dato4.setVisibility(View.VISIBLE); }
                                else if(ADP.getMensaje_nuevo().equals(false)){  dato4.setVisibility(View.GONE); }

                            }

                        }else{
                            // Si la referencia de la ID no existe
                        }

                    }
                });


    }

    @Override
    public int getItemCount() {
        return List_Client.size();  //devuelve el numero de fila que tiene el recycleview
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){ listener.onClick(view); }
    }

    public void setOnClickListener(View.OnClickListener listener) { this.listener=listener; }

    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        private TextView dato1,dato2;
        private CircleImageView dato3;
        private ImageView dato4;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textView_info);
            dato3=(CircleImageView) itemView.findViewById(R.id.imageView_perfilcliente);
            dato4=(ImageView) itemView.findViewById(R.id.imageView_noti_nuevo_mensaje2);



        }
    }



}