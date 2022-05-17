package com.example.modelo;

public class Edificio {
    private String uId;
    private String nombre;
    private String calle;
    private String historia;
    private String foto;
    private int valoracion;

    public Edificio() {
    }

    public Edificio(String uId, String nombre, String calle, String historia, String foto, int valoracion) {
        this.uId = uId;
        this.nombre = nombre;
        this.calle = calle;
        this.historia = historia;
        this.foto = foto;
        this.valoracion = valoracion;
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

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}
