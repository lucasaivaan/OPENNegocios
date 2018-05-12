package com.open.applic.open.interface_principal.metodos_funciones;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.open.applic.open.R;

public class EliminarDatosCuenta {
    public EliminarDatosCuenta() { }

    public static void Galeriafotos(final String ID_NEGOCIO, final Context context){

        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_GALERIA_FOTOS));


        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        // Storage
                        StorageReference storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                        StorageReference  storageReference= storageReferenceGalery.child( context.getString(R.string.DB_NEGOCIOS)).child( ID_NEGOCIO ).child( context.getString(R.string.DB_GALERIA_FOTOS)).child( documentSnapshot.getId() );
                        storageReference.delete();

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_GALERIA_FOTOS)).document(documentSnapshot.getId());
                        documentReference.delete();

                    }
                }

            }
        });
    }
    public static void Cuentas( final String ID_NEGOCIO, final Context context){

        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_CUENTAS));


        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_CUENTAS)).document(documentSnapshot.getId());
                        documentReference.delete();

                    }
                }

            }
        });
    }

    public static void Clientes(final String ID_NEGOCIO, final Context context){


        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        final CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_CLIENTES));

        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        final CollectionReference collectionReference=collectionReference1.document(  documentSnapshot.getId()  ).collection(  context.getString(R.string.DB_CHAT)  );
                        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot documentSnapshot1:task.getResult()) {
                                        //Firestore
                                        DocumentReference documentReference=collectionReference.document(documentSnapshot1.getId());
                                        documentReference.delete();
                                    }
                                }
                            }
                        });

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(  context.getString(R.string.DB_CLIENTES)  ).document(documentSnapshot.getId());
                        documentReference.delete();

                    }
                }

            }
        });
    }

    public static void Horarios(final String ID_NEGOCIO, final Context context){


        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_HORARIOS));

        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_HORARIOS)).document(documentSnapshot.getId());
                        documentReference.delete();

                    }
                }

            }
        });
    }

    public static void Reseñas(final String ID_NEGOCIO, final Context context){


        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_RESEÑAS));

        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_RESEÑAS)).document(documentSnapshot.getId());
                        documentReference.delete();

                    }
                }

            }
        });
    }

    public static void Servicios(final String ID_NEGOCIO, final Context context){


        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_SERVICIOS));

        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_SERVICIOS)).document(documentSnapshot.getId());
                        documentReference.delete();

                        // Storage
                        try {
                            StorageReference storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                            StorageReference  storageReference= storageReferenceGalery.child( context.getString(R.string.DB_NEGOCIOS)).child( ID_NEGOCIO ).child( context.getString(R.string.DB_SERVICIOS)).child( documentSnapshot.getId() );
                            storageReference.delete();
                        }catch (Exception ex){}


                    }
                }

            }
        });


    }

    public static void Ofertas(final String ID_NEGOCIO, final Context context){


        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_OFERTAS));

        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_OFERTAS)).document(documentSnapshot.getId());
                        documentReference.delete();

                        // Storage
                        try {
                            StorageReference storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                            StorageReference  storageReference= storageReferenceGalery.child( context.getString(R.string.DB_NEGOCIOS)).child( ID_NEGOCIO ).child( context.getString(R.string.DB_OFERTAS)).child( documentSnapshot.getId() );
                            storageReference.delete();
                        }catch (Exception ex){}


                    }
                }

            }
        });


    }

    public static void Productos(final String ID_NEGOCIO, final Context context){


        //  Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference1=firebaseFirestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS) ).document( ID_NEGOCIO ).collection(context.getResources().getString(R.string.DB_PRODUCTOS));

        collectionReference1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){

                        //Firestore
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO).collection(context.getString(R.string.DB_PRODUCTOS)).document(documentSnapshot.getId());
                        documentReference.delete();

                        // Storage
                        try {
                            StorageReference storageReferenceGalery= FirebaseStorage.getInstance().getReference();
                            StorageReference  storageReference= storageReferenceGalery.child( context.getString(R.string.DB_NEGOCIOS)).child( ID_NEGOCIO ).child( context.getString(R.string.DB_PRODUCTOS)).child( documentSnapshot.getId() );
                            storageReference.delete();
                        }catch (Exception ex){}


                    }
                }

            }
        });


    }

    public static void Cuenta(final String ID_NEGOCIO, final Context context){
        //Firestore
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        DocumentReference documentReference=firestore.collection( context.getResources().getString(R.string.DB_NEGOCIOS)).document(ID_NEGOCIO);
        documentReference.delete();

        // Storage
        try {
            StorageReference storageReferenceGalery= FirebaseStorage.getInstance().getReference();
            StorageReference  storageReference= storageReferenceGalery.child( context.getString(R.string.DB_NEGOCIOS)).child( ID_NEGOCIO ).child( context.getString(R.string.DBS_PERFIL)).child( context.getString(R.string.DBS_imagen_perfil) );
            storageReference.delete();
        }catch (Exception ex){}

    }






}
