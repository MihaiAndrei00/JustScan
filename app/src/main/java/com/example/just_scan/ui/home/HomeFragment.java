package com.example.just_scan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controladores.AdapterRutas.MainAdapter;
import com.example.just_scan.databinding.FragmentHomeBinding;
import com.example.modelo.Ruta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private DatabaseReference referenceRutas;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    //bd
    private DatabaseReference referenfciaRutas;
    private RecyclerView rv;
    private MainAdapter adpt;
    private ArrayList<Ruta> listaRutas;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                referenceRutas= FirebaseDatabase.getInstance().getReference("Rutas");
                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                listaRutas=new ArrayList<>();
                //adpt= new MainAdapter(getContext(),listaRutas);
                rv.setAdapter(adpt);
                referenceRutas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Ruta ruta= dataSnapshot.getValue(Ruta.class);
                            listaRutas.add(ruta);
                        }
                        adpt.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}