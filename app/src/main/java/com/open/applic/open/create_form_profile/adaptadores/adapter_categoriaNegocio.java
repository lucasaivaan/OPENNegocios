package com.open.applic.open.create_form_profile.adaptadores;

import android.graphics.Bitmap;

public class adapter_categoriaNegocio {

    private String id;
    private String nombre;
    private String logo;
    private String icon_location;
    private Boolean productos_precargados=false;
    private Bitmap bitmap_logo;
    private Bitmap bitmap_icon_location;


    public Bitmap getBitmap_logo() { return bitmap_logo; }
    public Bitmap getBitmap_icon_location() { return bitmap_icon_location; }
    public void setBitmap_logo(Bitmap bitmap_logo) { this.bitmap_logo = bitmap_logo; }
    public void setBitmap_icon_location(Bitmap bitmap_icon_location) { this.bitmap_icon_location = bitmap_icon_location; }

    public Boolean getProductos_precargados() { return productos_precargados; }
    public void setProductos_precargados(Boolean productos_precargados) { this.productos_precargados = productos_precargados; }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getLogo() { return logo; }
    public String getIcon_location() { return icon_location; }
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setLogo(String logo) { this.logo = logo; }
    public void setIcon_location(String icon_location) { this.icon_location = icon_location; }
}
