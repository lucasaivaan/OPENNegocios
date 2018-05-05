package com.open.applic.open.interface_principal.nav_header.cuentas.adaptadores;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.applic.open.R;
import com.open.applic.open.interface_principal.adaptadores.adapter_ofertas_negocio;

import java.util.List;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_producos extends RecyclerView.Adapter<adapter_recyclerView_producos.homeViwHolder>
        implements View.OnClickListener{

    List<adapter_productos> pases;

    private View.OnClickListener listener;

    public adapter_recyclerView_producos(List<adapter_productos> business) {
        this.pases = business;
    }


    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_producto,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {
        adapter_productos ADP= pases.get(position);

        try{
            holder.dato1.setText(ADP.getProducto());
            holder.dato3.setText(Double.toString(ADP.getPrecio()));
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

        TextView dato1,dato3;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_titulo);
            dato3=(TextView) itemView.findViewById(R.id.textView_precio);




        }
    }}