package com.example.controladores.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controladores.logs.LogUsuario;
import com.example.controladores.validar.Validar;
import com.example.just_scan.R;
import com.example.modelo.Usuario;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AnadirAdmin extends AppCompatActivity {
    //AdMob
    private String tag ="Principal";
    private AdView mAdView;
    private AdRequest adRequest;
    //vistas
    private EditText txtEmail;
    private EditText txtNombreUsuario;
    private EditText txtTelefono;
    private EditText txtContra;
    private Button btnRegistrar;
    //intent
    private Intent intent;
    //atributo para que por defecto sean no admin
    private int esAdmin;
    //llamamos a la base de datos
    private FirebaseDatabase database=FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_admin);
        MobileAds.initialize(this);
        mAdView = findViewById(R.id.adView);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //EditText
        txtEmail=findViewById(R.id.txtEmailAdmin);
        txtNombreUsuario=findViewById(R.id.txtNombreUsuarioAdmin);
        txtTelefono=findViewById(R.id.txtTelefonoAdmin);
        txtContra=findViewById(R.id.txtContraAdmin);
        //botón
        btnRegistrar=findViewById(R.id.btnRegistrarAdmin);
        //creamos la instancia de la base de datos con la referencia de nuestro modelo, en este caso Usuario
        mAuth = FirebaseAuth.getInstance();
        //llamo al metodo para registrar
        registrar();
        //muestro el anuncio
        anuncio();
    }
    private void anuncio() {
        //AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    private void registrar(){
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= txtEmail.getText().toString();
                String nombreUsuario= txtNombreUsuario.getText().toString();
                String telefono= txtTelefono.getText().toString();
                String password= txtContra.getText().toString();
                esAdmin=1;
                if(email.isEmpty()){
                    Toast.makeText(AnadirAdmin.this,"El email no puede estar vacío", Toast.LENGTH_SHORT).show();

                }else{
                    if(nombreUsuario.isEmpty()){
                        Toast.makeText(AnadirAdmin.this,"El nombre usuario no puede estar vacío", Toast.LENGTH_SHORT).show();

                    }else{
                        if(telefono.isEmpty()){
                            Toast.makeText(AnadirAdmin.this,"El teléfono no puede estar vacío", Toast.LENGTH_SHORT).show();

                        }else{
                            if(password.isEmpty()){
                                Toast.makeText(AnadirAdmin.this,"La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                            }else{
                                if(!Validar.validarEmail(txtEmail)){
                                    Toast.makeText(AnadirAdmin.this,"Formato del email no válido", Toast.LENGTH_SHORT).show();
                                }else{
                                    if (!Validar.validarUsuario(txtNombreUsuario)){
                                        Toast.makeText(AnadirAdmin.this,"Formato del usuario no válido", Toast.LENGTH_SHORT).show();

                                    }else{
                                        if(!Validar.validarTelefono(txtTelefono)){
                                            Toast.makeText(AnadirAdmin.this,"Formato del telefono no válido", Toast.LENGTH_SHORT).show();

                                        }else{
                                            if(!Validar.validarPassword(txtContra)){
                                                Toast.makeText(AnadirAdmin.this,"Formato de la contraseña no válido", Toast.LENGTH_SHORT).show();

                                            }else{
                                                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()){
                                                            Usuario usuario= new Usuario(email,nombreUsuario,telefono,password, "https://w7.pngwing.com/pngs/527/663/png-transparent-logo-person-user-person-icon-rectangle-photography-computer-wallpaper.png", esAdmin);
                                                            FirebaseDatabase.getInstance().getReference("Usuarios").child(FirebaseAuth.
                                                                    getInstance().getCurrentUser().getUid()).
                                                                    setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(AnadirAdmin.this,"Usuario Registrado",Toast.LENGTH_LONG).show();
                                                                        intent=new Intent(AnadirAdmin.this, LogUsuario.class);
                                                                        startActivity(intent);
                                                                    }else{
                                                                        Toast.makeText(AnadirAdmin.this,"Registro Fallido,volver a intentar",Toast.LENGTH_LONG).show();

                                                                    }
                                                                }
                                                            });
                                                        }else{
                                                            Toast.makeText(AnadirAdmin.this,"Fallido",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}