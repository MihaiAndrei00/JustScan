package com.example.modelo;

public class Usuario {

    private String email;
    private String nombreUsuario;
    private String telefono;
    private String contraseña;
    private String fotoPerfil;
    private int esAdmin;

    public Usuario() {

    }

    public Usuario(String email, String nombreUsuario, String telefono, String contraseña, String fotoPerfil, int esAdmin) {
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.fotoPerfil = fotoPerfil;
        this.esAdmin = esAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public int getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(int esAdmin) {
        this.esAdmin = esAdmin;
    }
}
