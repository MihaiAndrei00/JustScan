package com.example.modelo;

import android.net.Uri;

public class Ruta {
    private String uId;
    private String titulo;
    private String descripcion;
    private String duración;
    private String foto;

    public Ruta() {

    }

    public Ruta(String uId, String titulo, String descripcion, String duración, String foto) {
        this.uId = uId;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duración = duración;
        this.foto = foto;

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuración() {
        return duración;
    }

    public void setDuración(String duración) {
        this.duración = duración;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}
