package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.open.applic.open.R;
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
    private Context c;

    private View viewMensaje;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        viewMensaje = LayoutInflater.from(c).inflate(R.layout.recycler_item_mensaje_chat,parent,false);
        return new HolderMensaje(viewMensaje);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {

        // 1-Receptor  2-Emisor
        if(listMensaje.get(position).getType_mensaje().equals("1")){

            // Receptor
            holder.getNombre_1().setText(listMensaje.get(position).getNombre());
            holder.getMensaje_1().setText(listMensaje.get(position).getMensaje());

            holder.getLinearLayoutEmisor_2().setVisibility(View.GONE);

            //Imagen del negocio
            if(listMensaje.get(position).getUrlfotoPerfil().equals("default")){

                // icono
                Context context =  holder.getFotoMensajePerfil1_1().getContext();
                int id= icono.getIconLogoCategoria(listMensaje.get(position).getCategoria(),context);
                holder.getFotoMensajePerfil1_1().setImageResource(id);


            }else{
                //-Carga la imagen de perfil
                Glide.with(c).load(listMensaje.get(position).getUrlfotoPerfil()).into(holder.getFotoMensajePerfil1_1());

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
                Glide.with(c).load(listMensaje.get(position).getUrlfotoPerfil()).into(holder.getFotoMensajePerfil2_2());

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