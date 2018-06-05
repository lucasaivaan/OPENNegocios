package com.open.applic.open.interface_principal.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.metodos_funciones.icono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ivan on 15/3/2018.
 */

public class adapter_recyclerView_Reseñas extends RecyclerView.Adapter<adapter_recyclerView_Reseñas.homeViwHolder>
        implements View.OnClickListener{


    List<adapter_reseña> adapter_reseñas;
    protected adapter_reseña adapterReseña;
    private  Context context;
    // Firesbase
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();


    private View.OnClickListener listener;

    public adapter_recyclerView_Reseñas(List<adapter_reseña> List_business, Context context) {
        this.adapter_reseñas = List_business;
        this.context = context;
    }



    @Override
    public adapter_recyclerView_Reseñas.homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_reviews,parent,false);
        view.setOnClickListener(this);

        adapter_recyclerView_Reseñas.homeViwHolder holder=new adapter_recyclerView_Reseñas.homeViwHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final adapter_recyclerView_Reseñas.homeViwHolder holder, int position) {
        adapterReseña= adapter_reseñas.get(position);

        try{

            Glide.clear(holder.circleImageView);

            holder.dato2.setText(adapterReseña.getReseña());

            //fecha
            Date adapterReseñaTimestamp = adapterReseña.getTimestamp();
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

            // hora
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");//a pm o am
            holder.dato3.setText(sdf.format(adapterReseñaTimestamp.getTime())+" "+format1.format(adapterReseñaTimestamp.getTime()));

            // foto perfil
            if(adapterReseña.getFotoPerfil() != null) {

                //Asignacion de icono de la categoria
                Context context = holder.circleImageView.getContext();
                int id = icono.getIconLogoCategoria(adapterReseña.getFotoPerfil(), context);

                holder.circleImageView.setBackgroundResource(id);

            }

            // Firebase
            firestore.collection( context.getResources().getString(R.string.DB_CLIENTES)).document( adapterReseña.getId())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc_PerfilCliente=task.getResult();
                        if(doc_PerfilCliente.exists()){
                            adapter_perfil_clientes adapterPerfilClientes=doc_PerfilCliente.toObject(adapter_perfil_clientes.class);


                            holder.dato1.setText(adapterPerfilClientes.getNombre());

                            Context context = holder.circleImageView.getContext();
                            Glide.with(context).load(adapterPerfilClientes.getUrlfotoPerfil())
                                    .fitCenter()
                                    .centerCrop()
                                    .into( holder.circleImageView);
                        }
                    }

                }
            });


            // Reseña positiva o negativa
            if(adapterReseña.getEstrellas() != null){

                // Reseña positiva
                holder.ratingBarStat.setRating(adapterReseña.getEstrellas());

            }else{ holder.ratingBarStat.setVisibility(View.GONE); }

        }catch (Exception ex){}
    }

    @Override
    public int getItemCount() {
        return adapter_reseñas.size();  //devuelve el numero de fila que tiene el recycleview
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
        CircleImageView circleImageView;
        RatingBar ratingBarStat;

        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textview_nombre);
            dato2=(TextView) itemView.findViewById(R.id.textview_reseña);
            dato3=(TextView) itemView.findViewById(R.id.textview_hora);
            circleImageView=(CircleImageView) itemView.findViewById(R.id.fotoPerfilReseña);
            ratingBarStat=(RatingBar) itemView.findViewById(R.id.rationbar_star);

        }
    }}