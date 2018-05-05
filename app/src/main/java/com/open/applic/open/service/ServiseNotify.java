package com.open.applic.open.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.open.applic.open.R;
import com.open.applic.open.interface_principal.nav_header.chat.Chat_principal;

import static com.open.applic.open.Splash_Login.ID_NEGOCIO;

/**
 * Created by ivan on 24/1/2018.
 */

public class ServiseNotify extends Service {

    // FIRESTORE
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    //Firebase AUTH
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

    public Notification.Builder mBuilder;

    @Override
    public void onCreate(){}

    @Override
    public int onStartCommand(Intent intent,int flag,int idProcess){

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("RSSPullService");

        Intent myIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        @SuppressLint("WrongConstant") final PendingIntent pendingIntent= PendingIntent.getActivity(getBaseContext(),0,myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
        final Context context=getApplicationContext();


        //////////////////////////////// Carga lista de Clientes /////////////////////////////////////////
        if(ID_NEGOCIO != null){

            // Firestore
            CollectionReference collectionReference=db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO).collection( getString(R.string.DB_CLIENTES) );
            collectionReference.whereEqualTo(  getString(R.string.DB_mensaje_nuevo)  ,true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                    for (DocumentSnapshot snapshot4 : querySnapshot) {
                        if(snapshot4.getString(  getString(R.string.DB_nombrecliente)  ) != null){

                        }else{
                            ///// Funcion para cargar los datos del cliente//////
                            Boolean value=snapshot4.getBoolean(  getString(R.string.DB_mensaje_nuevo)  );

                            if(value==null){value=false;}
                            if(value){

                                //-- Notify
                                mBuilder= new Notification.Builder(getBaseContext())
                                        .setContentTitle(getResources().getString(R.string.chat_clientes))
                                        .setContentText(getResources().getString(R.string.tienes_nuevos_mensaje))
                                        .setColor(R.color.colorPrimaryDark)
                                        .setContentIntent(pendingIntent)
                                        .setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true)
                                        .setSmallIcon((R.mipmap.ic_launcher))
                                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_message));
                                Intent notificacionIntent =  new Intent(getBaseContext().getApplicationContext(), Chat_principal.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 1, notificacionIntent,0);
                                mBuilder.setContentIntent(pendingIntent);
                                mBuilder.setAutoCancel(true);


                                Notification notification= mBuilder.build();
                                NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(1,notification);

                                break;
                            }
                        }
                    }
                }
            });

        }


        return START_STICKY;
    }

    @Override
    public  void onDestroy(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
