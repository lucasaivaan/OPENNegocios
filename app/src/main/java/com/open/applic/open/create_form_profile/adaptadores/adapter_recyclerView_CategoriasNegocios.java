package com.open.applic.open.create_form_profile.adaptadores;

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

/**
 * Created by lucas on 26/10/2017.
 */

public class adapter_recyclerView_CategoriasNegocios extends RecyclerView.Adapter<adapter_recyclerView_CategoriasNegocios.homeViwHolder> implements View.OnClickListener{

    private List<adapter_categoriaNegocio> List_Negocios;
    private View.OnClickListener listener;
   private Context context;

    public adapter_recyclerView_CategoriasNegocios(List<adapter_categoriaNegocio> business, Context context) {
        this.List_Negocios = business;
        this.context=context;
    }


    @Override
    public adapter_recyclerView_CategoriasNegocios.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_categorias,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_CategoriasNegocios.homeViwHolder holder=new adapter_recyclerView_CategoriasNegocios.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, final int position) {
        final adapter_categoriaNegocio ADP= List_Negocios.get(position);

        final TextView dato1=holder.dato1;
        final ImageView dato3=holder.dato3;

        if(ADP.getNombre() != null){

            // Nombre
            dato1.setText(ADP.getNombre());


            // Imagen
            if( ADP.getLogo() != null ){
                Context context=dato3.getContext();
                Glide.with(context)
                        .load(ADP.getLogo())
                        .fitCenter()
                        .centerCrop()
                        .into(dato3);
            }else{ }



        }

    }

    @Override
    public int getItemCount() {
        return List_Negocios.size();  //devuelve el numero de fila que tiene el recycleview
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){ listener.onClick(view); }
    }

    public void setOnClickListener(View.OnClickListener listener) { this.listener=listener; }

    public  static  class homeViwHolder extends RecyclerView.ViewHolder{

        private TextView dato1;
        private ImageView dato3;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_nombre_categoria);
            dato3=(ImageView) itemView.findViewById(R.id.imageView_imagen_categoroia);


        }
    }



}