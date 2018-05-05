package com.open.applic.open.interface_principal.adaptadores;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.MainActivity_interface_principal;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_Ofertas extends RecyclerView.Adapter<adapter_recyclerView_Ofertas.homeViwHolder>
        implements View.OnClickListener{

    List<adapter_ofertas_negocio> adapterOfertasNegocios;
    private View.OnClickListener listener;

    public adapter_recyclerView_Ofertas(List<adapter_ofertas_negocio> business) {
        this.adapterOfertasNegocios = business;
    }

    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_ofertas,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {
        adapter_ofertas_negocio ADP= adapterOfertasNegocios.get(position);

        try{

            // Titulo
            holder.dato1.setText(ADP.getTitulo());

            // Descripcion
            if(ADP.getDescripcion().equals("")){ holder.dato2.setVisibility(View.GONE);
            }else { holder.dato2.setText(ADP.getDescripcion());}

            // Precio
            holder.dato3.setText(ADP.getPrecio());

            // url Foto
            if(ADP.getUrlFoto() != null && !ADP.getUrlFoto().equals("default")){

                Context context=holder.circleImageViewFoto.getContext();
                // Carga la imagen de perfil
                Glide.with(  context  )
                        .load(ADP.getUrlFoto())
                        .fitCenter()
                        .centerCrop()
                        .into(holder.circleImageViewFoto);

            }else {
                Glide.clear( holder.circleImageViewFoto);

                holder.circleImageViewFoto.setImageDrawable(null);
                holder.circleImageViewFoto.setBackgroundResource(R.mipmap.ic_user2);
                holder.circleImageViewFoto.setVisibility(View.GONE);  }


        }catch (Exception ex){}
    }

    @Override
    public int getItemCount() {
        return adapterOfertasNegocios.size();  //devuelve el numero de fila que tiene el recycleview
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

        TextView dato1,dato2,dato3;
        CircleImageView circleImageViewFoto;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_titulo);
            dato2=(TextView) itemView.findViewById(R.id.textView_decripcion);
            dato3=(TextView) itemView.findViewById(R.id.textView_precio);
            circleImageViewFoto=(CircleImageView) itemView.findViewById(R.id.imageView_imageProducto);




        }
    }}