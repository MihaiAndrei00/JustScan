package com.example.modelo;

public class Calle {
    private String uId;
    private String nombre;
    private String historia;
    private String foto;
    private int valoracion;

    public Calle() {
    }

    public Calle(String uId, String nombre, String historia, String foto, int valoracion) {
        this.uId = uId;
        this.nombre = nombre;
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

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getImagen() {
        return foto;
    }

    public void setImagen(String imagen) {
        this.foto = imagen;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}
