package com.open.applic.open.interface_principal.adaptadores;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.open.applic.open.R;

import java.util.List;

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


        try{
            holder.dato1.setText(ADP.getTitulo());
            holder.dato2.setText(ADP.getDescripcion());
            holder.ic_service.setImageDrawable(ADP.getIc_servico());


            //holder.dato3.setImageDrawable(ADP.getImg());
        }catch (Exception ex){
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
        ImageView ic_service;


        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textView_info);
            ic_service=(ImageView) itemView.findViewById(R.id.ic_service);
            //dato3=(ImageView) itemView.findViewById(R.id.imageView2);



        }
    }}