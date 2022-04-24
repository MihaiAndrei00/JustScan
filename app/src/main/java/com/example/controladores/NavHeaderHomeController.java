package com.example.controladores;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.just_scan.R;
import com.example.modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavHeaderHomeController extends AppCompatActivity {

    private TextView tvCorreo;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private String  fullName,email;
    private int permisos;
    private String telefono;
    TextView nombreUsuario;
    TextView mailUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nombreUsuario=findViewById(R.id.nombreUsuario);
        mailUsuario=findViewById(R.id.mailUuario);
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Usuarios");
        userId=user.getUid();


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        // Read from the database
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario userProfile= snapshot.getValue(Usuario.class);
                if(userProfile!=null){
                    fullName= userProfile.getNombreUsuario();
                    email=userProfile.getEmail();
                    permisos=userProfile.getEsAdmin();
                    telefono=userProfile.getTelefono();
                    nombreUsuario.setText(fullName);
                    mailUsuario.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
