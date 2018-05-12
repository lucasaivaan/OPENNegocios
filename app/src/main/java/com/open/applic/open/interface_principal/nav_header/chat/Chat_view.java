 package com.open.applic.open.interface_principal.nav_header.chat;

 import android.support.annotation.NonNull;
 import android.support.annotation.WorkerThread;
 import android.support.v7.app.AppCompatActivity;
 import android.os.Bundle;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.support.v7.widget.Toolbar;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;

 import com.bumptech.glide.Glide;
 import com.google.android.gms.tasks.OnCompleteListener;
 import com.google.android.gms.tasks.Task;
 import com.google.android.gms.tasks.Tasks;
 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.auth.FirebaseUser;
 import com.google.firebase.firestore.CollectionReference;
 import com.google.firebase.firestore.DocumentChange;
 import com.google.firebase.firestore.DocumentReference;
 import com.google.firebase.firestore.DocumentSnapshot;
 import com.google.firebase.firestore.EventListener;
 import com.google.firebase.firestore.FieldPath;
 import com.google.firebase.firestore.FieldValue;
 import com.google.firebase.firestore.FirebaseFirestore;
 import com.google.firebase.firestore.FirebaseFirestoreException;
 import com.google.firebase.firestore.Query;
 import com.google.firebase.firestore.QuerySnapshot;
 import com.google.firebase.firestore.SetOptions;
 import com.google.firebase.firestore.WriteBatch;
 import com.open.applic.open.R;
 import com.open.applic.open.interface_principal.adaptadores.adapter_perfil_clientes;
 import com.open.applic.open.interface_principal.adaptadores.adapter_profile_negocio;
 import com.open.applic.open.interface_principal.metodos_funciones.SharePreferencesAPP;
 import com.open.applic.open.interface_principal.nav_header.chat.adaptador.AdapterMensajes;
 import com.open.applic.open.interface_principal.nav_header.chat.adaptador.MensajeRecibir;

 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.Callable;
 import java.util.concurrent.Executor;
 import java.util.concurrent.LinkedBlockingQueue;
 import java.util.concurrent.ThreadPoolExecutor;
 import java.util.concurrent.TimeUnit;

 import de.hdodenhof.circleimageview.CircleImageView;

 public class Chat_view extends AppCompatActivity {


     private String ID_NEGOCIO ;
     private adapter_perfil_clientes adapter_PERFIL_CLIENTE;

     //---------------------------------- Firestore -------------------------------------------------
     FirebaseFirestore db=FirebaseFirestore.getInstance();
     private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

     //Auth
     private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //datos usuaio actual

     //---------------------------------- Elementos -------------------------------------------------
     private adapter_profile_negocio profile_negocio;
     private CircleImageView fotoPerfil;
     private TextView nombre;
     private RecyclerView rvMensajes;
     private EditText txtMensaje;
     private Button btnEnviar;
     private AdapterMensajes adapter;
     private String fotoPerfilCadena;
     private String idClient ="null";




     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

         //-------------------------------- Toolbar -------------------------------------------------
         // (Barra de herramientas)
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarChat);
         setSupportActionBar(toolbar);

         //---introduce button de retroceso
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         //Datos APP
         ID_NEGOCIO = SharePreferencesAPP.getID_NEGOCIO(this);

         //- Extraxion de dato pasado por parametro
         idClient =getIntent().getStringExtra("parametroIdClient");


         //- Referencias
         fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
         nombre = (TextView) findViewById(R.id.nombre);
         rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
         txtMensaje = (EditText) findViewById(R.id.txtMensaje);
         btnEnviar = (Button) findViewById(R.id.btnEnviar);
         fotoPerfilCadena = "";

         //---Carga datos del cliente
         CargaPerfilCliente();

     }


     public void CargaPerfilCliente(){
         ///////////////////////////////// Carga el nombre del Cliente //////////////////////////////
         DocumentReference docRefCliente = db.collection( getString(R.string.DB_CLIENTES) ).document(idClient);
         docRefCliente .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();
                     if (document.exists()) {

                         //Set Adapter
                         adapter_PERFIL_CLIENTE=document.toObject(adapter_perfil_clientes.class);


                         //--Carga el nombre en el TextView
                         nombre.setText( adapter_PERFIL_CLIENTE.getNombre() );

                         //-Uri foto perfil
                         fotoPerfilCadena=adapter_PERFIL_CLIENTE.getUrlfotoPerfil();

                         if(fotoPerfilCadena.equals("default")){
                             fotoPerfil.setImageResource(R.mipmap.ic_user2);
                         }else {
                             LoadImagePerfil(fotoPerfilCadena);
                         }

                         // Carga el chat
                         Carga_DbChat();
                     }

                 }
             }});
     }
     //- Metodo que carga el chat
     protected void Carga_DbChat() {



         ///////////////////////////////// Carga el nombre del Negocio //////////////////////////////
         DocumentReference docRefNegocio= db.collection( getString(R.string.DB_NEGOCIOS) ).document(ID_NEGOCIO);
         docRefNegocio.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot document = task.getResult();

                     if( document != null){
                         // adaptador perfil de negocio
                         profile_negocio =document.toObject(adapter_profile_negocio.class);
                         }
                 }
             }});

         //Mensaje leido
         final DocumentReference AddMensajeChatNegocio=db.collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid()).collection( getString(R.string.DB_CLIENTES) ).document(idClient);
         Map<String, Object> objectMap = new HashMap<>();
         objectMap.put( getString(R.string.DB_mensaje_nuevo) ,false);
         AddMensajeChatNegocio.set(objectMap, SetOptions.merge());

         //-Database Negocio
         final DocumentReference AddMensajeChatClientes=db.collection( getString(R.string.DB_CLIENTES) ).document(idClient).collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid());




         //-Adapter Chat
         adapter = new AdapterMensajes(this);
         LinearLayoutManager l = new LinearLayoutManager(this);
         rvMensajes.setLayoutManager(l);
         rvMensajes.setAdapter(adapter);

         //-Button Enviar
         btnEnviar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if(!txtMensaje.getText().toString().equals("")){
                     //Guarda informacion del mensaje
                     Map<String , Object> dataClient=new HashMap<>();
                     dataClient.put( getString(R.string.DB_mensaje) ,txtMensaje.getText().toString());
                     dataClient.put( getString(R.string.DB_timestamp) , FieldValue.serverTimestamp());
                     dataClient.put( getString(R.string.DB_type_mensaje) ,"1");
                     dataClient.put( getString(R.string.DB_nombre) ,profile_negocio.getNombre_negocio());
                     dataClient.put( getString(R.string.DB_urlfotoPerfil) ,profile_negocio.getImagen_perfil());
                     dataClient.put( getString(R.string.DB_categoria) ,profile_negocio.getCategoria());




                     AddMensajeChatNegocio.collection( getString(R.string.DB_CHAT) ).document().set(dataClient);
                     AddMensajeChatClientes.collection( getString(R.string.DB_CHAT) ).document().set(dataClient);

                     //Notifica de un mensaje nuevo y el ultimo mensaje enviado
                     Map<String, Object> objectMap = new HashMap<>();

                     // DB Negocio
                     objectMap.put( getString(R.string.DB_ultimo_mensaje) ,txtMensaje.getText().toString());
                     AddMensajeChatNegocio.set(objectMap,SetOptions.merge());

                     // DB Cliente
                     objectMap.put( getString(R.string.DB_mensaje_nuevo) ,true);
                     AddMensajeChatClientes.set(objectMap,SetOptions.merge());

                     // Resetea valores de datos de entrada
                     txtMensaje.setText("");
                 }}});


         //-Escucha Evento RecyclerView
         adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
             @Override
             public void onItemRangeInserted(int positionStart, int itemCount) {
                 super.onItemRangeInserted(positionStart, itemCount);
                 setScrollbar();}});



         //-Escucha Eventos en la Database
         CollectionReference collectionReference=db.collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid()).collection( getString(R.string.DB_CLIENTES) ).document(idClient).collection( getString(R.string.DB_CHAT) );
         collectionReference.orderBy( getString(R.string.DB_timestamp) , Query.Direction.ASCENDING)
                 .addSnapshotListener(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                 for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                     switch (dc.getType()) {
                         case ADDED:
                             //-Si el cliente no tiene foto se le asigna una foto por ddefecto
                             if(fotoPerfilCadena == null || fotoPerfilCadena.equals("")){
                                 fotoPerfilCadena="default";
                             }

                             MensajeRecibir m = dc.getDocument().toObject(MensajeRecibir.class);

                             if(m.getTimestamp() != null){
                                 // Añade datos del cliente al adapadordel los mensajes
                                 if(m.getType_mensaje().equals("2")){
                                     m.setUrlfotoPerfil(adapter_PERFIL_CLIENTE.getUrlfotoPerfil());
                                     m.setNombre(adapter_PERFIL_CLIENTE.getNombre());
                                 }

                                 // Agrega el mensaje nuevo
                                 adapter.addMensaje(m);
                             }
                             break;
                         case MODIFIED:
                             //-Si el cliente no tiene foto se le asigna una foto por ddefecto
                             if(fotoPerfilCadena == null || fotoPerfilCadena.equals("")){
                                 fotoPerfilCadena="default";
                             }

                             MensajeRecibir m2 = dc.getDocument().toObject(MensajeRecibir.class);

                             if(m2.getTimestamp() != null){

                                 // Añade datos del cliente al adapadordel los mensajes
                                 if(m2.getType_mensaje().equals("2")){
                                     m2.setUrlfotoPerfil(adapter_PERFIL_CLIENTE.getUrlfotoPerfil());
                                     m2.setNombre(adapter_PERFIL_CLIENTE.getNombre());
                                 }

                                 // Agrega el mensaje nuevo
                                 adapter.addMensaje(m2);
                             }
                            break;
                         case REMOVED:

                             break;
                     }
                 }
             }
         });


     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu_chat_mensajes, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();

         //-Button retroceso
         switch (item.getItemId()) {
             case android.R.id.home:
                 finish();
                 return true;}

         //noinspection SimplifiableIfStatement
         if (id == R.id.action_vaciar_chat) {

             DocumentReference documentReferenceChat=db.collection( getString(R.string.DB_NEGOCIOS) ).document(firebaseUser.getUid()).collection( getString(R.string.DB_CLIENTES) ).document(idClient);
             deleteCollection(documentReferenceChat.collection( getString(R.string.DB_CHAT) ), 50, EXECUTOR);

             //Elimina el dato del ultimo mensaje de referencia
             Map<String,Object> update=new HashMap<>();
             update.put( getString(R.string.DB_ultimo_mensaje) ,FieldValue.delete());
             documentReferenceChat.update(update);

             finish();
             return true;
         }

         return super.onOptionsItemSelected(item);
     }

     //-Metodo para cargar la foto de perfil
     public void LoadImagePerfil(String uri){
         if(uri!=null){
             try{
                 Glide.with(Chat_view.this)
                         .load(uri)
                         .fitCenter()
                         .centerCrop()
                         .into(fotoPerfil);
             }catch (Exception ex){}
         }else{
             fotoPerfil.setImageResource(R.mipmap.ic_user2);
         }

     }

     //- Posiciona la vista de recyclerView al ultimo mensaje recibido
     private void setScrollbar(){
         rvMensajes.scrollToPosition(adapter.getItemCount()-1);
     }


     /**
      * Delete all documents in a collection. Uses an Executor to perform work on a background
      * thread. This does *not* automatically discover and delete subcollections.
      */
     private Task<Void> deleteCollection(final CollectionReference collection, final int batchSize, Executor executor) {

         // Perform the delete operation on the provided Executor, which allows us to use
         // simpler synchronous logic without blocking the main thread.
         return Tasks.call(executor, new Callable<Void>() {
             @Override
             public Void call() throws Exception {
                 // Get the first batch of documents in the collection
                 Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                 // Get a list of deleted documents
                 List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                 // While the deleted documents in the last batch indicate that there
                 // may still be more documents in the collection, page down to the
                 // next batch and delete again
                 while (deleted.size() >= batchSize) {
                     // Move the query cursor to start after the last doc in the batch
                     DocumentSnapshot last = deleted.get(deleted.size() - 1);
                     query = collection.orderBy(FieldPath.documentId())
                             .startAfter(last.getId())
                             .limit(batchSize);

                     deleted = deleteQueryBatch(query);
                 }

                 return null;
             }
         });

     }

     /**
      * Delete all results from a query in a single WriteBatch. Must be run on a worker thread
      * to avoid blocking/crashing the main thread.
      */
     @WorkerThread
     private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
         QuerySnapshot querySnapshot = Tasks.await(query.get());

         WriteBatch batch = query.getFirestore().batch();
         for (DocumentSnapshot snapshot : querySnapshot) {
             batch.delete(snapshot.getReference());
         }
         Tasks.await(batch.commit());

         return querySnapshot.getDocuments();
     }




 }