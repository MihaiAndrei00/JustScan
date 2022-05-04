package com.example.just_scan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.just_scan.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


public class Home extends AppCompatActivity {
    //firenbase
    private StorageTask uploadTask;
    private String myUri="";
    private Uri imageUri;
    private StorageReference storageProfileReference;
    private FirebaseUser usuario;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    //vistas
    private ImageView fotoUser;
    private TextView nombreUsuario;
    private TextView mailUsuario;
    private NavigationView navigationView;
    private View headerView;
    private String userId;
    private String fullName, email;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    //intent
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ini
        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        userId = usuario.getUid();
        storageProfileReference= FirebaseStorage.getInstance().getReference().child("Profile Pic");
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        //mAppBarConfiguration contendra las rutas a las diferentes pestañas
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        upDateNavHeader();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void upDateNavHeader() {
        //mailUsuario.setText(usuario.getEmail());

        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        nombreUsuario = headerView.findViewById(R.id.nombreUsuario);
        mailUsuario = headerView.findViewById(R.id.mailUuario);
        fotoUser=headerView.findViewById(R.id.fotoUsuario);
        reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        userId = usuario.getUid();
        mailUsuario.setText(usuario.getEmail());
        nombreUsuario.setText(usuario.getDisplayName());

        fotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }



    /*
    public void anadirFotoPerfilPorDefecto() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LUsuario> listaUsuarios = new ArrayList<>();
                for (DataSnapshot childDataSnapShot : snapshot.getChildren()) {
                    usuarioClass = childDataSnapShot.getValue(Usuario.class);
                    LUsuario lUsuario=new LUsuario(childDataSnapShot.getKey(),usuarioClass);
                    listaUsuarios.add(lUsuario);
                }

                for (LUsuario lUsuario : listaUsuarios){
                    if (lUsuario.getUsuario().getFotoPerfil() == null || lUsuario.getUsuario().getFotoPerfil() == "") {
                        reference.child(lUsuario.getKey()).child("fotoPerfil").setValue(Constantes.URL_FOTO_DEFECTO);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

     */
    }
