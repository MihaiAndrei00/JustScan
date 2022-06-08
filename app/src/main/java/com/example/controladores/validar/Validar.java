package com.example.controladores.validar;

import android.widget.EditText;

public class Validar {
    public static boolean validarUsuario(EditText usuario){
        String usuarioValido=usuario.getText().toString();
        String regex="^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,29}$";
        return usuarioValido.matches(regex);
    }
    public static boolean validarEmail(EditText email){
        String emailValido=email.getText().toString();
        String regex="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        return emailValido.matches(regex);
    }
    public static boolean validarPassword(EditText pass){
        String passValido=pass.getText().toString();
        String regex="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        return passValido.matches(regex);
    }
    public static boolean validarTelefono(EditText tel){
        String telValido=tel.getText().toString();
        String regex="(\\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8}";
        return telValido.matches(regex);
    }
    public static boolean validarLetrasNumYSpace(EditText calle){
        String calleValido=calle.getText().toString();
        String regex="/^[A-Za-z0-9\\s]+$/g";
        return calleValido.matches(regex);
    }
    public static boolean validarNumDouble(EditText num){
        String numValido=num.getText().toString();
        String regex= "(^[-]?\\d*[\\.]?\\d+)$";
        return numValido.matches(regex);
    }

    public static boolean validarLetras(EditText let){

        String letValida=let.getText().toString();
        String regex= "[a-zA-ZñÑ ]$";
        return letValida.matches(regex);
    }
}
