package com.example.magicelementsfight.luchasCarpeta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.magicelementsfight.R;


public class MagoFragment extends Fragment {

    public MagoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mago, container, false);
        ImageView claseMago = view.findViewById(R.id.imageViewMago);
        TextView textomago = view.findViewById(R.id.textomago);
        CheckBox druida = view.findViewById(R.id.checkDruida);
        CheckBox nigromante = view.findViewById(R.id.checkNigromante);
        CheckBox celestial = view.findViewById(R.id.checkCelestial);


        druida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(druida.isChecked()) {
                    claseMago.setImageResource(R.drawable.druida_luchas);
                    textomago.setText("Capaz de invocar a un animal el cual le ayudara a hacer un poco de daño al enemigo durante 4 turnos. Invocar requiere mucho esfuerzo asi que solo lo puede hacer 1 vez.");
                    nigromante.setChecked(false);
                    celestial.setChecked(false);
                    LuchasActivity.compartir = "druida";
                }
                else{
                    claseMago.setImageResource(R.drawable.vacio);
                    textomago.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });
        nigromante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nigromante.isChecked()) {
                    claseMago.setImageResource(R.drawable.nigromante_luchas);
                    textomago.setText("Capaz de invocar a un zombie el cual le ayudara a hacer daño al enemigo durante 3 turnos pero... sabe el zombie a quien tiene que atacar? Invocar requiere mucho esfuerzo asi que solo lo puede hacer 1 vez.");
                    druida.setChecked(false);
                    celestial.setChecked(false);
                    LuchasActivity.compartir = "nigromante";
                }
                else{
                    claseMago.setImageResource(R.drawable.vacio);
                    textomago.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });
        celestial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(celestial.isChecked()) {
                    claseMago.setImageResource(R.drawable.celestial_luchas);
                    textomago.setText("Capaz de lanzar una bola de luz radiante que daña mucho al enemigo. Requiere mucha energia no creo que pueda hacerlo mas de una vez.");
                    druida.setChecked(false);
                    nigromante.setChecked(false);
                    LuchasActivity.compartir = "celestial";
                }
                else{
                    claseMago.setImageResource(R.drawable.vacio);
                    textomago.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });

        return view;
    }
}