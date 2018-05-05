package com.open.applic.open.create_form_profile;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.open.applic.open.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Configuracion_Horario extends AppCompatActivity {


    //---------- TimePickerDialog -------------
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;


    //--- Firebase
    // FIRESTORE DB
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    // AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    //--- Button closed/aperture
    public Button buttonClosed;
    public Button buttonAperture;
    public Button buttonClosed2;
    public Button buttonAperture2;

    //--- Initiate a Switch
    public Switch aSwitchLunes;
    public Switch aSwitchMartes;
    public Switch aSwitchMiercoles;
    public Switch aSwitchJueves;
    public Switch aSwitchViernes;
    public Switch aSwitchSabado;
    public Switch aSwitchDomingo;

    public Switch aSwitch24Horas;

    //--- LinearLayout
    public LinearLayout linearLayoutHorario;
    public LinearLayout linearLayoutHorario2;
    public TextView textViewSgundoHorarioOcional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_horarios_configuracion);
        setTitle(R.string.configurar_horario);

        //---introduce button de retroceso
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--- Initiate Reference
        //Button closed/aperture
        buttonAperture=(Button) findViewById(R.id.button_apertura);
        buttonClosed=(Button) findViewById(R.id.button_cierre);
        buttonAperture2=(Button) findViewById(R.id.button_apertura2);
        buttonClosed2=(Button) findViewById(R.id.button_cierre2);
        //Initiate a Switch
        aSwitchLunes=(Switch) findViewById(R.id.switch_lunes);
        aSwitchMartes=(Switch) findViewById(R.id.switch_martes);
        aSwitchMiercoles=(Switch) findViewById(R.id.switch_miercoles);
        aSwitchJueves=(Switch) findViewById(R.id.switch_jueves);
        aSwitchViernes=(Switch) findViewById(R.id.switch_viernes);
        aSwitchSabado=(Switch) findViewById(R.id.switch_sabado);
        aSwitchDomingo=(Switch) findViewById(R.id.switch_domingo);
        //LinearLayout hour
        linearLayoutHorario=(LinearLayout) findViewById(R.id.LinearLayout_Horario);
        linearLayoutHorario2=(LinearLayout) findViewById(R.id.LinearLayout_Horario2);
        textViewSgundoHorarioOcional=(TextView) findViewById(R.id.textView_segundo_horario_opcional);

        //----------------- Switch  onClick
        aSwitch24Horas=(Switch) findViewById(R.id.switch_24);
        aSwitch24Horas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    //Esconde la configuracion del horario
                    linearLayoutHorario.setVisibility(View.GONE);
                    linearLayoutHorario2.setVisibility(View.GONE);
                    textViewSgundoHorarioOcional.setVisibility(View.GONE);
                }else{
                    //Vuelve a mostrar la configuracion del horario
                    linearLayoutHorario.setVisibility(View.VISIBLE);
                    linearLayoutHorario2.setVisibility(View.VISIBLE);
                    textViewSgundoHorarioOcional.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        //--- Asignacion de horarios
        TimeDialog();
    }

    public void TimeDialog(){

        ///////////////////////////// BUTTON APERTURA 1 ////////////////////////////////////////////
        buttonAperture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(Configuracion_Horario.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String sHours = null;
                                String sMinutes = null;
                                if (hourOfDay <= 9) {sHours ="0"+ Integer.toString(hourOfDay);}
                                else{sHours = Integer.toString(hourOfDay);}
                                if (minute <= 9) {sMinutes = "0"+ Integer.toString(minute);}
                                else{sMinutes = Integer.toString(minute);}
                                buttonAperture.setText(sHours + ":" + sMinutes);
                            }}, CalendarHour, CalendarMinute, true);timepickerdialog.show();}});

        ///////////////////////////// BUTTON CLOSE 1 ////////////////////////////////////////////
        buttonClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(Configuracion_Horario.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String sHours = null;
                        String sMinutes = null;
                        if (hourOfDay <= 9) {sHours ="0"+ Integer.toString(hourOfDay);}
                        else{sHours = Integer.toString(hourOfDay);}
                        if (minute <= 9) {sMinutes = "0"+ Integer.toString(minute);}
                        else{sMinutes = Integer.toString(minute);}
                        buttonClosed.setText(sHours + ":" + sMinutes);
                    }}, CalendarHour, CalendarMinute, true);timepickerdialog.show();}});

        ///////////////////////////// BUTTON APERTURA 2 ////////////////////////////////////////////
        buttonAperture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(Configuracion_Horario.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String sHours = null;
                        String sMinutes = null;
                        if (hourOfDay <= 9) {sHours ="0"+ Integer.toString(hourOfDay);}
                        else{sHours = Integer.toString(hourOfDay);}
                        if (minute <= 9) {sMinutes = "0"+ Integer.toString(minute);}
                        else{sMinutes = Integer.toString(minute);}
                        buttonAperture2.setText(sHours + ":" + sMinutes);
                    }}, CalendarHour, CalendarMinute, true);timepickerdialog.show();}});

        ///////////////////////////// BUTTON CLOSE 2 ////////////////////////////////////////////
        buttonClosed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                timepickerdialog = new TimePickerDialog(Configuracion_Horario.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String sHours = null;
                        String sMinutes = null;
                        if (hourOfDay <= 9) {sHours ="0"+ Integer.toString(hourOfDay);}
                        else{sHours = Integer.toString(hourOfDay);}
                        if (minute <= 9) {sMinutes = "0"+ Integer.toString(minute);}
                        else{sMinutes = Integer.toString(minute);}
                        buttonClosed2.setText(sHours + ":" + sMinutes);
                    }}, CalendarHour, CalendarMinute, true);timepickerdialog.show();}});

    }

    public  void ButtonSave(View view){

        //---Values
        String sAperture=buttonAperture.getText().toString();
        String sClosed=buttonClosed.getText().toString();
        String sAperture2=buttonAperture2.getText().toString();
        String sClosed2=buttonClosed2.getText().toString();
        if(sAperture2.equals("00:00 ") && sClosed2.equals("00:00 ") ){
            sAperture2=" ";
            sClosed2=" ";
        }
        String s24Hors="24";

        //Testing values
        if(!buttonAperture.getText().toString().equals("00:00") || !buttonClosed.getText().toString().equals("00:00")
                || aSwitch24Horas.isChecked()){

            if(aSwitchLunes.isChecked() || aSwitchMartes.isChecked()   || aSwitchMiercoles.isChecked()
                    || aSwitchJueves.isChecked()  || aSwitchViernes.isChecked()  || aSwitchSabado.isChecked()  || aSwitchDomingo.isChecked()) {
                //Firestore
                CollectionReference collecDB=db.collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid()).collection( getString(R.string.DB_HORARIOS) );

                // check current state of a Switch (true or false).
                //lunes
                if(aSwitchLunes.isChecked()) {
                    if(aSwitch24Horas.isChecked()) {
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","lunes");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("1lunes").set(dataHorario);

                    }else{
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","lunes");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("1lunes").set(dataHorario);

                    }
                }
                //martes
                if(aSwitchMartes.isChecked()){
                    if(aSwitch24Horas.isChecked()) {

                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","martes");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("2martes").set(dataHorario);

                    }else{

                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","martes");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("2martes").set(dataHorario);
                    }
                }
                //miercoles
                if(aSwitchMiercoles.isChecked()){
                    if(aSwitch24Horas.isChecked()) {
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","miercoles");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("3miercoles").set(dataHorario);
                    }else{
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","miercoles");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("3miercoles").set(dataHorario);
                    }
                }
                //jueves
                if(aSwitchJueves.isChecked()){
                    if(aSwitch24Horas.isChecked()) {
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","jueves");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("4jueves").set(dataHorario);
                    }else{
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","jueves");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("4jueves").set(dataHorario);
                    }
                }
                //vienes
                if(aSwitchViernes.isChecked()){
                    if(aSwitch24Horas.isChecked()) {
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","viernes");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("5viernes").set(dataHorario);
                    }else{
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","viernes");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("5viernes").set(dataHorario);
                    }
                }
                //sabado
                if(aSwitchSabado.isChecked()){
                    if(aSwitch24Horas.isChecked()) {
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","sabado");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("6sabado").set(dataHorario);
                    }else{
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","sabado");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("6sabado").set(dataHorario);
                    }
                }
                //domingo
                if(aSwitchDomingo.isChecked()){
                    if(aSwitch24Horas.isChecked()) {
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","domingo");
                        dataHorario.put("apertura1",s24Hors);
                        dataHorario.put("cierre1"," ");
                        dataHorario.put("apertura2"," ");
                        dataHorario.put("cierre2"," ");

                        // Guarda los datos del horario
                        collecDB.document("7domingo").set(dataHorario);
                    }else{
                        // Mapa de horario
                        Map<String,Object> dataHorario= new HashMap<>();
                        dataHorario.put("dia","domingo");
                        dataHorario.put("apertura1",sAperture);
                        dataHorario.put("cierre1",sClosed);
                        dataHorario.put("apertura2",sAperture2);
                        dataHorario.put("cierre2",sClosed2);

                        // Guarda los datos del horario
                        collecDB.document("7domingo").set(dataHorario);
                    }
                }

                //Finaliza activity
                finish();
            }else{
                //messaje
                Toast.makeText(Configuracion_Horario.this,R.string.debes_elegir_una_dia, Toast.LENGTH_SHORT).show();
            }




        }else{
            //messaje
            Toast.makeText(Configuracion_Horario.this,R.string.Debe_definir_un_horario_apertura_y_cierre, Toast.LENGTH_SHORT).show();
        }


    }

    public void onBackPressed() {
        //---Finish Activity
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
