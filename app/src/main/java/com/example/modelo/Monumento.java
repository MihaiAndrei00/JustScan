package com.example.modelo;

public class Monumento {
    private String uId;
    private String nombre;
    private String calle;
    private String historia;
    private String foto;
    private double latitud;
    private double longitud;

    public Monumento() {
    }

    public Monumento(String uId, String nombre, String calle, String historia, String foto, double latitud, double longitud) {
        this.uId = uId;
        this.nombre = nombre;
        this.calle = calle;
        this.historia = historia;
        this.foto = foto;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
