package com.open.applic.open.interface_principal.adaptadores;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.open.applic.open.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_Servicios extends RecyclerView.Adapter<adapter_recyclerView_Servicios.homeViwHolder>
        implements View.OnClickListener{

    List<adapter_servicios_negocio> pases;

    private View.OnClickListener listener;

    public adapter_recyclerView_Servicios(List<adapter_servicios_negocio> business) {
        this.pases = business;
    }



    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_servicios,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {
        adapter_servicios_negocio ADP= pases.get(position);

        // Titulo
        holder.dato1.setText(ADP.getTitulo());

        // Descripcion
        if(!ADP.getDescripcion().equals("")){
            holder.dato2.setText(ADP.getDescripcion());
        }else{  holder.dato2.setVisibility(View.GONE); }

        // Imagen
        if(!ADP.getUrl_imagen().equals("default")){
            // Carga la imagen de perfil
            Glide.clear(holder.circleImageView);
            Context context=holder.circleImageView.getContext();
            Glide.with(context)
                    .load(ADP.getUrl_imagen())
                    .fitCenter()
                    .centerCrop()
                    .into(holder.circleImageView);
        }else{
            Glide.clear(holder.circleImageView);
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
        CircleImageView circleImageView;


        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textView_info);
            circleImageView=(CircleImageView) itemView.findViewById(R.id.service_image);
            //dato3=(ImageView) itemView.findViewById(R.id.imageView2);



        }
    }}