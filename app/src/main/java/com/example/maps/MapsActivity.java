package com.example.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.just_scan.R;
import com.example.just_scan.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private FloatingActionButton btnBuscar;
    private String textoBuscadorCiudad;
    private TextView tvBuscar;
    private Geocoder geocoder;
    private EditText etBuscar;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double lat, longi;
    private UiSettings uiSettings;
    private  List<Address>adressList=null;
    private Address address=null;
    private LatLng latLng=null;
    private CameraPosition cameraPosition;
    public static final int REQUEST_CODE=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnBuscar=findViewById(R.id.btnBuscadorCiudad);
        tvBuscar=findViewById(R.id.tvBuscar);
        etBuscar=findViewById(R.id.etBuscar);

        lat = getIntent().getDoubleExtra("latitud", lat);
        longi = getIntent().getDoubleExtra("longitud", longi);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        habilitarMiLocacion();
        //buscarCiudad();


        // Add a marker in Sydney and move the camera
        latLng = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Toro Burger"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }



    private void habilitarMiLocacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE );
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                habilitarMiLocacion();
            }else{
                Toast.makeText(this,"Debes tener aceptados los permisos necesarios", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void buscarCiudad(){
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textoBuscadorCiudad=etBuscar.getText().toString();
                if(!Geocoder.isPresent()){
                    Toast.makeText(MapsActivity.this, "Su dispositivo no soporta el Geocoder", Toast.LENGTH_SHORT).show();
                }
                geocoder=new Geocoder(MapsActivity.this);
                try {

                    adressList = geocoder.getFromLocationName( textoBuscadorCiudad,1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(adressList!=null){
                    address=adressList.get(0);
                }
                if (address!=null){
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                }
                if(latLng!=null){
                    mMap.addMarker(new MarkerOptions().position(latLng).title(textoBuscadorCiudad+ " en "+address.getCountryCode()+ " : " + address.getCountryName()));
                }

                cameraPosition=new CameraPosition.Builder()
                        .target(latLng).zoom(15.5f).bearing(300).tilt(50).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        });
    }

}