package com.example.modelo;

public class Usuario {

    private String email;
    private String nombreUsuario;
    private String telefono;
    private String contraseña;

    public Usuario() {
    }

    public Usuario( String email, String nombreUsuario, String telefono, String contraseña) {

        this.email = email;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
        this.contraseña = contraseña;
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
