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


public class GuerreroFragment extends Fragment {

    public GuerreroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guerrero, container, false);
        ImageView claseGuerrero = view.findViewById(R.id.imageViewGuerrero);
        TextView textoguerrero = view.findViewById(R.id.textoguerrero);
        CheckBox soldado = view.findViewById(R.id.checkSoldado);
        CheckBox berserker = view.findViewById(R.id.checkBerserker);
        CheckBox paladin = view.findViewById(R.id.checkPaladin);


        soldado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(soldado.isChecked()) {
                    claseGuerrero.setImageResource(R.drawable.soldado_luchas);
                    textoguerrero.setText("Capaz de asestar un golpe de espada que hace mucho daño al enemigo, el cual aumenta si tienes poca vida. Solo tienes una oportunidad de usarlo.");
                    berserker.setChecked(false);
                    paladin.setChecked(false);
                    LuchasActivity.compartir = "soldado";
                }
                else{
                    claseGuerrero.setImageResource(R.drawable.vacio);
                    textoguerrero.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });
        berserker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(berserker.isChecked()) {
                    claseGuerrero.setImageResource(R.drawable.berserker_luchas);
                    textoguerrero.setText("Capaz de lanzar sus dos hachas y dañar al enemigo, pero... al lanzar un objeto no siempre das en el blanco. Lo puedes usar 2 veces.");
                    soldado.setChecked(false);
                    paladin.setChecked(false);
                    LuchasActivity.compartir = "berserker";
                }
                else{
                    claseGuerrero.setImageResource(R.drawable.vacio);
                    textoguerrero.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });
        paladin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paladin.isChecked()) {
                    claseGuerrero.setImageResource(R.drawable.paladin_luchas);
                    textoguerrero.setText("Lleva pociones en su bolillo que le haran restaurar parte de su vida. LLeva 3 consigo usalas sabiamente.");
                    soldado.setChecked(false);
                    berserker.setChecked(false);
                    LuchasActivity.compartir = "paladin";
                }
                else{
                    claseGuerrero.setImageResource(R.drawable.vacio);
                    textoguerrero.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });

        return view;
    }
}