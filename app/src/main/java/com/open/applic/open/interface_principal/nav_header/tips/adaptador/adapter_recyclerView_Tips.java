package com.open.applic.open.interface_principal.nav_header.tips.adaptador;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_ofertas_negocio;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_Tips extends RecyclerView.Adapter<adapter_recyclerView_Tips.homeViwHolder>
        implements View.OnClickListener{

    List<adapter_tips> adapterOfertasNegocios;
    private View.OnClickListener listener;

    public adapter_recyclerView_Tips(List<adapter_tips> business) {
        this.adapterOfertasNegocios = business;
    }

    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_tips,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {
        adapter_tips ADP= adapterOfertasNegocios.get(position);

        try{

            // Titulo
            holder.tTitulo.setText(ADP.getTitulo());

            // Descripcion
            if(ADP.getDescripcion().equals("")){ holder.tDescripcion.setVisibility(View.GONE);
            }else { holder.tDescripcion.setText(ADP.getDescripcion());}

            // url Foto
            if(!ADP.getUrlFoto().equals("")){

                Context context=holder.imageViewFoto.getContext();
                // Carga la imagen de perfil
                Glide.with(  context  )
                        .load(ADP.getUrlFoto())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.imageViewFoto);

            }else {holder.imageViewFoto.setVisibility(View.GONE);}

            ////////////////  INFACION DEL CREADOR /////////////////////////////////////////////////

            // Nombre
            holder.tNombreCreador.setText(ADP.getNombreCreador());

            //Reference ICon
            if(!ADP.getUrlFotoCreador().equals("")){
                Context context=holder.circleImageView_FotoCreador.getContext();
                int id = context.getResources().getIdentifier("logo_"+ADP.getUrlFotoCreador(), "mipmap", context.getPackageName());

                //icono del creador
                holder.circleImageView_FotoCreador.setImageResource(id);

            }


        }catch (Exception ex){}
    }




    @Override
    public int getItemCount() {
        return adapterOfertasNegocios.size();  //devuelve el numero de fila que tiene el recycleview
    }


    @Override
    public void onClick(View view) {
        if(listener!=null){ listener.onClick(view); }
        }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener=listener;
    }


    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        TextView tTitulo,tDescripcion,tNombreCreador;
        ImageView imageViewFoto;
        CircleImageView circleImageView_FotoCreador;

        public homeViwHolder(View itemView) {
            super(itemView);

            tTitulo=(TextView) itemView.findViewById(R.id.textView_tips_titulo);
            tDescripcion=(TextView) itemView.findViewById(R.id.textView_tips_descripcion);
            imageViewFoto=(ImageView) itemView.findViewById(R.id.imageView_tips_imagen);

            tNombreCreador=(TextView) itemView.findViewById(R.id.textview_creador);
            circleImageView_FotoCreador=(CircleImageView) itemView.findViewById(R.id.fotoPerfil_creador);




        }
    }}