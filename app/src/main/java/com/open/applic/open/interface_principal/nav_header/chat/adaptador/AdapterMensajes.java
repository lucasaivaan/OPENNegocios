package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.open.applic.open.R;
import com.open.applic.open.create_form_profile.adaptadores.adapter_categoriaNegocio;
import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
import com.open.applic.open.interface_principal.metodos_funciones.icono;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 04/09/2017. 04
 */

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context context;
    private View viewMensaje;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public AdapterMensajes(Context context) {
        this.context = context;
    }

    public void addMensaje(MensajeRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        viewMensaje = LayoutInflater.from(context).inflate(R.layout.recycler_item_mensaje_chat,parent,false);
        return new HolderMensaje(viewMensaje);
    }

    @Override
    public void onBindViewHolder(final HolderMensaje holder, int position) {

        // 1-Receptor  2-Emisor
        if(listMensaje.get(position).getType_mensaje().equals("1")){

            // Receptor
            holder.getNombre_1().setText( listMensaje.get(position).getNombre());
            holder.getMensaje_1().setText(listMensaje.get(position).getMensaje());

            holder.getLinearLayoutEmisor_2().setVisibility(View.GONE);

            //Imagen del negocio
            if(listMensaje.get(position).getUrlfotoPerfil().equals("default")){

                // Firebase Negocio
                final DocumentReference documentReference=firestore.collection( context.getString(R.string.DB_NEGOCIOS) ).document( listMensaje.get(position).getId() );
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if(documentSnapshot.exists()){
                                adapter_profile_negocio adapterProfileNegocio=documentSnapshot.toObject(adapter_profile_negocio.class);

                                // firebase DB  Categgorias
                                DocumentReference documentReference1=firestore.collection( context.getString(R.string.DB_APP) ).document( adapterProfileNegocio.getPais().toUpperCase() ).collection( context.getString(R.string.DB_CATEGORIAS_NEGOCIOS) ).document( adapterProfileNegocio.getCategoria() );
                                documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot documentSnapshot1=task.getResult();
                                            if(documentSnapshot1.exists()){
                                                adapter_categoriaNegocio categoriaNegocio=documentSnapshot1.toObject(adapter_categoriaNegocio.class);


                                                // Glide Descarga de imagen
                                                Glide.with(context.getApplicationContext())
                                                        .load(categoriaNegocio.getLogo())
                                                        .fitCenter()
                                                        .centerCrop()
                                                        .into(holder.getFotoMensajePerfil1_1());

                                            }
                                        }
                                    }
                                });

                            }
                        }
                    }
                });


            }else{
                //-Carga la imagen de perfil
                Glide.with(context).load(listMensaje.get(position).getUrlfotoPerfil()).into(holder.getFotoMensajePerfil1_1());

            }


            // hora
            Date codigoHora = listMensaje.get(position).getTimestamp();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");//a pm o am
            holder.getHora_1().setText(sdf.format(codigoHora.getTime()));

        }else if(listMensaje.get(position).getType_mensaje().equals("2")){

            // Emisor
            if(!listMensaje.get(position).getUrlfotoPerfil().equals("default")){

                // Nombre y mensaje
                holder.getNombre_2().setText(listMensaje.get(position).getNombre());
                holder.getMensaje_2().setText(listMensaje.get(position).getMensaje());

                holder.getRelativeLayout_Receptor().setVisibility(View.GONE);

                //-Carga la imagen de perfil
                Glide.with(context).load(listMensaje.get(position).getUrlfotoPerfil()).into(holder.getFotoMensajePerfil2_2());

                // hora
                Date codigoHora = listMensaje.get(position).getTimestamp();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");//a pm o am
                holder.getHora_2().setText(sdf.format(codigoHora.getTime()));

            }else{
                // Nombre y mensaje
                holder.getNombre_2().setText(listMensaje.get(position).getNombre());
                holder.getMensaje_2().setText(listMensaje.get(position).getMensaje());

                holder.getRelativeLayout_Receptor().setVisibility(View.VISIBLE);


                //-Imagen por defecto
                holder.getFotoMensajePerfil2_2().setBackgroundResource(R.mipmap.ic_user2);

                // hora
                Date codigoHora = listMensaje.get(position).getTimestamp();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");//a pm o am
                holder.getHora_1().setText(sdf.format(codigoHora.getTime()));

            }

            //holder.getMensaje_1().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

}