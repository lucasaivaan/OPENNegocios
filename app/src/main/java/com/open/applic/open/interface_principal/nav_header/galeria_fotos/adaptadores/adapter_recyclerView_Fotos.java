package com.open.applic.open.interface_principal.nav_header.galeria_fotos.adaptadores;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_servicios_negocio;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_Fotos extends RecyclerView.Adapter<adapter_recyclerView_Fotos.homeViwHolder>
        implements View.OnClickListener{

    List<adaptador_foto> ListFotos;

    private View.OnClickListener listener;

    public adapter_recyclerView_Fotos(List<adaptador_foto> business) {
        this.ListFotos = business;
    }



    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_foto,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {
        adaptador_foto ADP= ListFotos.get(position);

        // Imagen
        if(!ADP.getUrlfoto().equals("default")){
            Glide.clear(holder.imageView);
            // Carga la imagen de perfil
            Glide.clear(holder.imageView);
            Context context=holder.imageView.getContext();
            Glide.with(context)
                    .load(ADP.getUrlfoto())
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imageView);
        }else{ Glide.clear(holder.imageView); }


    }



    @Override
    public int getItemCount() {
        return ListFotos.size();  //devuelve el numero de fila que tiene el recycleview
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


        ImageView imageView;


        public homeViwHolder(View itemView) {
            super(itemView);

            imageView=(ImageView) itemView.findViewById(R.id.imageViewRecycler_foto);
        }
    }}