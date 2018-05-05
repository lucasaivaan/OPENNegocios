package com.open.applic.open.interface_principal.adaptadores;

import android.content.Context;
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
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_Cuentas extends RecyclerView.Adapter<adapter_recyclerView_Cuentas.homeViwHolder>
        implements View.OnClickListener{


    List<adapter_perfil_cuenta> pases;


    private View.OnClickListener listener;

    public adapter_recyclerView_Cuentas(List<adapter_perfil_cuenta> business) {

        this.pases = business;
    }


    @Override
    public adapter_recyclerView_Cuentas.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_clientes,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Cuentas.homeViwHolder holder=new adapter_recyclerView_Cuentas.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {
        adapter_perfil_cuenta ADP= pases.get(position);

        try{
            holder.dato1.setText(ADP.getNombre());
            holder.dato2.setText(ADP.getCuenta());
            if(!ADP.getUrlfotoPerfil().equals("clientelocal") && !ADP.getUrlfotoPerfil().equals("default") && ADP.getUrlfotoPerfil() != null ){
                //carga la imagen del cliente

                Glide.with(holder.dato1.getContext())
                        .load(ADP.getUrlfotoPerfil())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.dato3);

            }else{

                //Carga la imagen por defecto
                holder.dato3.setImageResource(R.mipmap.ic_user2);

            }

        }catch (Exception ex){}
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
            dato3=(CircleImageView) itemView.findViewById(R.id.imageView_perfilcliente);
            dato4=(ImageView) itemView.findViewById(R.id.imageView_noti_nuevo_mensaje2);



        }
    }



}