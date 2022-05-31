package com.example.controladores.logs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.controladores.Principal;
import com.example.controladores.validar.Validar;
import com.example.just_scan.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogUsuario extends AppCompatActivity {
    //AdMob
    private String tag ="Principal";
    private AdView mAdView;
    private AdRequest adRequest;
    //vistas
    private EditText txtEmail;
    private EditText txtContra;
    private Button btnInicioSesion;
    private TextView tvContra;
    private TextView tvRegistro;
    //animacion
    private ConstraintLayout constraintLayout;
    private AnimationDrawable animationDrawable;
    //intent
    private Intent intent;
    //bd
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String llave="sesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_usuario);

        //AdMob
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //variables para la animacion del main
        constraintLayout = findViewById(R.id.mainLayout);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        //vistas
        txtEmail=findViewById(R.id.txtEmail);
        txtContra=findViewById(R.id.txtContra);
        btnInicioSesion=findViewById(R.id.btnIniciarSesion);
        tvContra=findViewById(R.id.olvidarContra);
        tvRegistro=findViewById(R.id.tvRegistro);
        //bd
        mAuth=FirebaseAuth.getInstance();

        //llamada al método de inicio de sesión
        iniciarSesion();
        //llamada al metodo de recuperar la contraseña
        linkArecuperarContrasena();
        //llamada al metodo de registro
        linkARegistro();
        //mostrarAnuncio
        anuncio();

    }

    private void anuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void iniciarSesion(){
        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= txtEmail.getText().toString();
                String contrasena=txtContra.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(LogUsuario.this, "El email no puede estar vacio", Toast.LENGTH_SHORT).show();
                }else{
                    if (contrasena.isEmpty()){
                        Toast.makeText(LogUsuario.this, "La contraseña no puede estar vacia", Toast.LENGTH_SHORT).show();
                    }else{
                        if(!Validar.validarEmail(txtEmail)){
                            Toast.makeText(LogUsuario.this, "El formato del email es incorrecto", Toast.LENGTH_SHORT).show();
                        }else{
                            if(!Validar.validarPassword(txtContra)){
                                Toast.makeText(LogUsuario.this, "El formato de la contraseña es incorrecto", Toast.LENGTH_SHORT).show();
                            }else{
                                mAuth.signInWithEmailAndPassword(email,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            user=mAuth.getCurrentUser();
                                            if (!user.isEmailVerified()){
                                                user.sendEmailVerification();
                                                Toast.makeText(LogUsuario.this,"Verifica tu correo electrónico",Toast.LENGTH_LONG).show();
                                            } else{
                                                intent= new Intent(LogUsuario.this, Principal.class);
                                                startActivity(intent);
                                            }
                                        }else{
                                            Toast.makeText(LogUsuario.this,"Email o contraseña incorrectos",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }
    private void linkArecuperarContrasena(){
        tvContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(LogUsuario.this, LogCambiarContra.class);
                startActivity(intent);
            }
        });
    }

    private void linkARegistro(){
        tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent= new Intent(LogUsuario.this, LogRegistro.class);
                startActivity(intent);
            }
        });
    }

}

