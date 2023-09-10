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

public class ArqueroFragment extends Fragment {


    public ArqueroFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_arquero, container, false);
        ImageView claseArquero = view.findViewById(R.id.imageViewArquero);
        TextView textoArquero = view.findViewById(R.id.textoarquero);
        CheckBox flecharapida = view.findViewById(R.id.checkBestias);
        CheckBox flechacertera = view.findViewById(R.id.checkDemonios);
        CheckBox dragon = view.findViewById(R.id.checkDragones);


        flecharapida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flecharapida.isChecked()) {
                    claseArquero.setImageResource(R.drawable.cazadordebestias_luchas);
                    textoArquero.setText("Capaz de poner un cepo delante suya que hace  daño al enemigo. Lo puedes usar 2 veces.");
                    flechacertera.setChecked(false);
                    dragon.setChecked(false);
                    LuchasActivity.compartir = "cazadordebestias";
                }
                else{
                    claseArquero.setImageResource(R.drawable.vacio);
                    textoArquero.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });
        flechacertera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flechacertera.isChecked()) {
                    claseArquero.setImageResource(R.drawable.cazadordedemonios_luchas);
                    textoArquero.setText("Capaz de robar un poco de vida al enemigo sin fallar. Lo puedes usar 2 veces.");
                    flecharapida.setChecked(false);
                    dragon.setChecked(false);
                    LuchasActivity.compartir = "cazadordedemonios";
                }
                else{
                    claseArquero.setImageResource(R.drawable.vacio);
                    textoArquero.setText("");
                    LuchasActivity.compartir = "";
                }
            }

        });
        dragon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dragon.isChecked()) {
                    claseArquero.setImageResource(R.drawable.cazadordedragones_luchas);
                    textoArquero.setText("Capaz de invocar un dragón que hace un poco de daño al enemigo sin fallar durante 2 turnos. Lo puedes usar 2 veces.");
                    flechacertera.setChecked(false);
                    flecharapida.setChecked(false);
                    LuchasActivity.compartir = "cazadordedragones";
                }
                else{
                    claseArquero.setImageResource(R.drawable.vacio);
                    textoArquero.setText("");
                    LuchasActivity.compartir = "";
                }
            }
        });

        return view;
    }
}