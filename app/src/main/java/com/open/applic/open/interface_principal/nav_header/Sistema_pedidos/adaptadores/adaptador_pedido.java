package com.open.applic.open.interface_principal.nav_header.Sistema_pedidos.adaptadores;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class adaptador_pedido {
    public adaptador_pedido() { }

    private String id_cliente;
    private String id_negocio;
    private String id;
    private String contacto;
    private String telefono;
    private Integer tipo_entrega;  // Tipo de entrega  1=retira_en_el_local   2=delivery
    private String forma_pago;
    private String nota;
    private Map<String,String> direccion= new HashMap<String, String>();
    private Date timestamp;
    private Integer estado;  // 0= PENDIENTE  1=EN PROCESO  2=PEDIDO ENVIADO  3=LISTO PARA RETIRAR  4= CANCELADO  5=RECIBIDO
    private Map<String,Object> lista_productos= new HashMap<String, Object>();
    private String cantidad_pago="0";
    private String hora;





    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getCantidad_pago() { return cantidad_pago; }
    public void setCantidad_pago(String cantidad_pago) { this.cantidad_pago = cantidad_pago; }
    public String getId_negocio() { return id_negocio; }
    public void setId_negocio(String id_negocio) { this.id_negocio = id_negocio; }
    public String getId() { return id; }
    public String getContacto() { return contacto; }
    public Integer getTipo_entrega() { return tipo_entrega; }
    public String getForma_pago() { return forma_pago; }
    public String getNota() { return nota; }
    public Map<String,String> getDireccion() { return direccion; }
    public Integer getEstado() { return estado; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Map<String,Object> getLista_productos() { return lista_productos; }
    public void setLista_productos(Map<String,Object> lista_productos) { this.lista_productos = lista_productos; }

    public String getId_cliente() { return id_cliente; }
    public void setId_cliente(String id_cliente) { this.id_cliente = id_cliente; }

    public void setId(String id) { this.id = id; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public void setTipo_entrega(Integer tipo_entrega) { this.tipo_entrega = tipo_entrega; }
    public void setForma_pago(String forma_pago) { this.forma_pago = forma_pago; }
    public void setNota(String nota) { this.nota = nota; }
    public void setDireccion(Map<String,String> direccion) { this.direccion = direccion; }
    public void setEstado(Integer estado) { this.estado = estado; }

    @ServerTimestamp
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }


}
