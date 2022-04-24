package com.example.modelo;

public class Usuario {

    private String email;
    private String nombreUsuario;
    private String telefono;
    private String contraseña;
    private int esAdmin;

    public Usuario() {

    }

    public Usuario(String email, String nombreUsuario, String telefono, String contraseña, int esAdmin) {
        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.esAdmin = esAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public int getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(int esAdmin) {
        this.esAdmin = esAdmin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
