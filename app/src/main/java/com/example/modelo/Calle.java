package com.example.modelo;

public class Calle {
    private String nombre;
    private String historia;
    private String imagen;
    private int valoracion;

    public Calle() {
    }

    public Calle(String nombre, String historia, String imagen, int valoracion) {
        this.nombre = nombre;
        this.historia = historia;
        this.imagen = imagen;
        this.valoracion = valoracion;
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
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}
