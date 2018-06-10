package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_Chat extends RecyclerView.Adapter<adapter_recyclerView_Chat.homeViwHolder>
        implements View.OnClickListener{

    private final Context context;
    private List<adapter_perfil_clientes> pases;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    private View.OnClickListener listener;

    public adapter_recyclerView_Chat(List<adapter_perfil_clientes> business, Context context) {
        this.context=context;
        this.pases = business;
    }


    @Override
    public adapter_recyclerView_Chat.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_chat_principal,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Chat.homeViwHolder holder=new adapter_recyclerView_Chat.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final homeViwHolder holder, int position) {
        final adapter_perfil_clientes ADP= pases.get(position);

        // Firebase DB
        firestore.collection( context.getResources().getString(R.string.DB_CLIENTES)).document(ADP.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc_perfilCiente=task.getResult();
                    if(doc_perfilCiente.exists()){
                        adapter_perfil_clientes adapterPerfilClientes=doc_perfilCiente.toObject(adapter_perfil_clientes.class);

                        // Nombre
                        holder.dato1.setText(adapterPerfilClientes.getNombre());


                        // Imagen de perfil
                        if(adapterPerfilClientes.getUrlfotoPerfil() !=null && !adapterPerfilClientes.getUrlfotoPerfil().equals("default")){
                            Glide.with(context)
                                    .load(adapterPerfilClientes.getUrlfotoPerfil())
                                    .fitCenter()
                                    .centerCrop()
                                    .into(holder.dato3);
                        }else{
                            holder.dato3.setImageResource(R.mipmap.ic_user2);}

                    }
                }
            }
        });

        // ultimo mensaje
        holder.dato2.setText(ADP.getTelefono());


        //icono mensaje de notificacion de mensaje nuevo
        if(ADP.getMensaje_nuevo()==true){
            holder.dato4.setVisibility(View.VISIBLE);
        }else if(ADP.getMensaje_nuevo()==false){
            holder.dato4.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return pases.size();  //devuelve el numero de fila que tiene el recycleview
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

        TextView dato1,dato2;
        CircleImageView dato3;
        ImageView dato4;
        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textView_info);
            dato3=(CircleImageView) itemView.findViewById(R.id.imageView_perfil_chat_principal);
            dato4=(ImageView) itemView.findViewById(R.id.imageView_noti_nuevo_mensaje2);



        }
    }}