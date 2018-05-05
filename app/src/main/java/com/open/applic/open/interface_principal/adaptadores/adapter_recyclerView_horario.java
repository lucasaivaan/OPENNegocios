package com.open.applic.open.interface_principal.adaptadores;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.open.applic.open.R;

import java.util.List;

/**
 * Created by lucas on 23/10/2017.
 */

public class adapter_recyclerView_horario extends RecyclerView.Adapter<adapter_recyclerView_horario.homeViwHolder>
        implements View.OnClickListener{

    List<adapter_horario> pases;

    private View.OnClickListener listener;

    public adapter_recyclerView_horario(List<adapter_horario> business) {
        this.pases = business;
    }



    @Override
    public homeViwHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_horarios,parent,false);
        view.setOnClickListener(this);

        homeViwHolder holder=new homeViwHolder(view);
        return holder;



    }

    @Override
    public void onBindViewHolder(homeViwHolder holder, int position) {

        try{

            adapter_horario ADP= pases.get(position);

            if(ADP.getApertura1().equals("24")){
                holder.como1.setVisibility(View.GONE);
                holder.coma2.setVisibility(View.GONE);
                ADP.setApertura1(ADP.getApertura1()+" Horas");

            }if(ADP.getApertura2().equals(" ") || ADP.getCierre2().equals(" ")){
                holder.coma2.setVisibility(View.GONE);
            }



            holder.dato1.setText(ADP.getDia());
            holder.dato2.setText(ADP.getApertura1());
            holder.dato3.setText(ADP.getCierre1());
            holder.dato4.setText(ADP.getApertura2());
            holder.dato5.setText(ADP.getCierre2());

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

        TextView dato1,dato2,dato3,dato4,dato5,como1,coma2;


        public homeViwHolder(View itemView) {
            super(itemView);

            dato1=(TextView) itemView.findViewById(R.id.textView_dia);
            dato2=(TextView) itemView.findViewById(R.id.textView_apertura1);
            dato3=(TextView) itemView.findViewById(R.id.textView_cierre1);
            dato4=(TextView) itemView.findViewById(R.id.textView_apertura2);
            dato5=(TextView) itemView.findViewById(R.id.textView_cierre2);

            como1=(TextView) itemView.findViewById(R.id.textView_divisor1);
            coma2=(TextView) itemView.findViewById(R.id.textView_divisor2);




        }
    }}