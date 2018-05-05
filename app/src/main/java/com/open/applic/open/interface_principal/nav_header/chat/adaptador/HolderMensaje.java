package com.open.applic.open.interface_principal.nav_header.chat.adaptador;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.open.applic.open.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lucas on 30/10/2017.
 */

public class HolderMensaje extends RecyclerView.ViewHolder{

    //-- Receptor (1)
    private TextView nombre_1;
    private TextView mensaje_1;
    private TextView hora_1;
    private ImageView fotoMensaje_1;
    private CircleImageView fotoMensajePerfil1_1;
    private RelativeLayout RelativeLayout_Receptor;


    //-- Emisor (2)
    private TextView nombre_2;
    private TextView mensaje_2;
    private TextView hora_2;
    private ImageView fotoMensaje_2;
    private RelativeLayout linearLayoutEmisor_2;
    private CircleImageView fotoMensajePerfil2_2;



    public HolderMensaje(View itemView) {
        super(itemView);
        // Receptor
        nombre_1 = (TextView) itemView.findViewById(R.id.nombreMensaje_1);
        mensaje_1 = (TextView) itemView.findViewById(R.id.mensajeMensaje_1);
        hora_1 = (TextView) itemView.findViewById(R.id.horaMensaje_1);
        fotoMensajePerfil1_1 = (CircleImageView) itemView.findViewById(R.id.fotoPerfilMensaje_receptor);
        RelativeLayout_Receptor =(RelativeLayout) itemView.findViewById(R.id.linealLayout_receptor);

        // Emisor
        nombre_2 = (TextView) itemView.findViewById(R.id.nombreMensaje_2);
        mensaje_2 = (TextView) itemView.findViewById(R.id.mensajeMensaje_2);
        hora_2 = (TextView) itemView.findViewById(R.id.horaMensaje_2);
        fotoMensajePerfil2_2 = (CircleImageView) itemView.findViewById(R.id.fotoPerfilMensaje_emisor);
        linearLayoutEmisor_2=(RelativeLayout) itemView.findViewById(R.id.linealLayout_emisor);

    }


    // Set
    public void setNombre_1(TextView nombre_1) {this.nombre_1 = nombre_1;}
    public void setMensaje_1(TextView mensaje_1) {this.mensaje_1 = mensaje_1;}
    public void setHora_1(TextView hora_1) {this.hora_1 = hora_1;}
    public void setFotoMensaje_1(ImageView fotoMensaje_1) {this.fotoMensaje_1 = fotoMensaje_1;}
    public void setFotoMensajePerfil1_1(CircleImageView fotoMensajePerfil1_1) {this.fotoMensajePerfil1_1 = fotoMensajePerfil1_1;}
    public void setRelativeLayout_Receptor(RelativeLayout relativeLayout_Receptor) {this.RelativeLayout_Receptor = relativeLayout_Receptor;}
    public void setNombre_2(TextView nombre_2) {this.nombre_2 = nombre_2;}
    public void setMensaje_2(TextView mensaje_2) {this.mensaje_2 = mensaje_2;}
    public void setHora_2(TextView hora_2) {this.hora_2 = hora_2;}
    public void setFotoMensaje_2(ImageView fotoMensaje_2) {this.fotoMensaje_2 = fotoMensaje_2;}
    public void setLinearLayoutEmisor_2(RelativeLayout linearLayoutEmisor_2) {this.linearLayoutEmisor_2 = linearLayoutEmisor_2;}
    public void setFotoMensajePerfil2_2(CircleImageView fotoMensajePerfil2_2) {this.fotoMensajePerfil2_2 = fotoMensajePerfil2_2;}

    // Get
    public TextView getNombre_1() {return nombre_1;}
    public TextView getMensaje_1() {return mensaje_1;}
    public TextView getHora_1() {return hora_1;}
    public ImageView getFotoMensaje_1() {return fotoMensaje_1;}
    public CircleImageView getFotoMensajePerfil1_1() {return fotoMensajePerfil1_1;}
    public RelativeLayout getRelativeLayout_Receptor() {return RelativeLayout_Receptor;}
    public TextView getNombre_2() {return nombre_2;}
    public TextView getMensaje_2() {return mensaje_2;}
    public TextView getHora_2() {return hora_2;}
    public ImageView getFotoMensaje_2() {return fotoMensaje_2;}
    public RelativeLayout getLinearLayoutEmisor_2() {return linearLayoutEmisor_2;}
    public CircleImageView getFotoMensajePerfil2_2() {return fotoMensajePerfil2_2;}

}