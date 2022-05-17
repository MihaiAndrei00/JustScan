package com.example.modelo;

public class Restaurante {
    private String uId;
    private String nombre;
    private String calle;
    private String tipoDeComida;
    private String descripcion;
    private String foto;
    private int valoracion;

    public Restaurante() {
    }

    public Restaurante(String uId, String nombre, String calle, String tipoDeComida, String descripcion, String foto, int valoracion) {
        this.uId = uId;
        this.nombre = nombre;
        this.calle = calle;
        this.tipoDeComida = tipoDeComida;
        this.descripcion = descripcion;
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

    public String getTipoDeComida() {
        return tipoDeComida;
    }

    public void setTipoDeComida(String tipoDeComida) {
        this.tipoDeComida = tipoDeComida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
