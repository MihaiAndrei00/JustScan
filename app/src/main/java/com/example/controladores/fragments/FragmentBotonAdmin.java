package com.example.controladores.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.controladores.interfaces.BotonAddInterfaz;
import com.example.just_scan.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBotonAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBotonAdmin extends Fragment {

    private int idBoton=R.id.botonAnadir;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentBotonAdmin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBotonAdmin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBotonAdmin newInstance(String param1, String param2) {
        FragmentBotonAdmin fragment = new FragmentBotonAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View miBoton= inflater.inflate(R.layout.fragment_boton_admin, container, false);
        ImageButton botonParaAnadir;
        botonParaAnadir=(ImageButton) miBoton.findViewById(idBoton);

        botonParaAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity estaActividad = getActivity();
                ((BotonAddInterfaz)estaActividad).pulsarBotonAnadir(idBoton);
            }
        });
        return miBoton;
    }
}