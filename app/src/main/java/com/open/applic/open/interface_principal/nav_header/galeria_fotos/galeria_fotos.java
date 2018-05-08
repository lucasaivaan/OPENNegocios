package com.open.applic.open.interface_principal.nav_header.galeria_fotos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.open.applic.open.R;

public class galeria_fotos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_fotos);
        setTitle(getResources().getString(R.string.fotos).toUpperCase());
        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void Button_AgregarFoto(View view){

    }
}
