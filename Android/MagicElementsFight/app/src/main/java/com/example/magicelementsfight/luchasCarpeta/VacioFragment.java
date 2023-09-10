package com.example.magicelementsfight.luchasCarpeta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.magicelementsfight.R;

public class VacioFragment extends Fragment {
    public VacioFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vacio, container, false);

        return view;
    }
}


/*
                if(Integer.valueOf(contador1.getText().toString()) <=0 || Integer.valueOf(contador2.getText().toString()) <=0 ){
                    showAlert();
                }
                else {
                    estadoEscrito2.setText("");
                    if ((prob[aleatorio.nextInt((Integer) daño1[1])] != 1 || quemarCurarEsquivar[0]) && !quemarCurarEsquivar[5]) {//(prob ataque o quemar j1) y esquivar j2
                        if (prob[aleatorio.nextInt(10)] == 1 || quemarCurarEsquivar[0]) {//prob 10% elemento o quemar j1
                            //quemar o ahogar j1
                            vida2.setLayoutParams(new LinearLayout.LayoutParams(vida2.getWidth() - (finalDaño1[0] + finalEstado1[0] + finalEstado1[1]), 100));
                            contador2.setText(String.valueOf(vida2.getWidth() - (finalDaño1[0] + finalEstado1[0] + finalEstado1[1])));
                            if (finalEstado1[0] != 0) {//agua
                                //historial.setText(historial.getText() + "bot: ahogado\n");
                                itemList.add(new ItemHistorial("Tu: Has hecho " + finalEstado1[0] + " por ahogamiento", 0xFF82C5FA, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.gota_luchas)));
                                estadoEscrito2.setText("ahogado");
                                estadoEscrito2.setTextColor(0xFF82C5FA);
                            } else if (finalEstado1[1] != 0) {//fuego
                                estadoEscrito2.setText("quemado");
                                estadoEscrito2.setTextColor(0xFFF89088);
                                //historial.setText(historial.getText() + "bot: quemado\n");
                                itemList.add(new ItemHistorial("Tu: Has hecho " + finalEstado1[1] + " por quemadura", 0xFFF89088, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.fuego_luchas)));
                                if (aux[0] < 2) {
                                    quemarCurarEsquivar[0] = true;
                                    aux[0]++;
                                } else {
                                    quemarCurarEsquivar[0] = false;
                                    aux[0] = 0;
                                }
                            } else if (finalEstado1[2] != 0) {//tierra
                                //historial.setText(historial.getText() + "tu: curado\n");
                                itemList.add(new ItemHistorial("Tu: te has curado " + finalEstado1[2] + " por sanación", 0xFF95FF99, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.tierra_luchas)));
                                vida1.setLayoutParams(new LinearLayout.LayoutParams(vida1.getWidth() + finalEstado1[2], 100));
                                contador1.setText(String.valueOf(vida1.getWidth() + finalEstado1[2]));
                                estadoEscrito1.setText("curado");
                                estadoEscrito1.setTextColor(0xFF95FF99);
                                quemarCurarEsquivar[1] = false;
                            } else if (finalEstado1[3] != 0) {//aire
                                //historial.setText(historial.getText() + "tu: esquivado\n");
                                itemList.add(new ItemHistorial("Tu: Esquivas el siguiente ataque", 0xFFDADADA, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.aire_luchas)));
                                estadoEscrito1.setText("esquivado");
                                estadoEscrito1.setTextColor(0xFFA1A0A0);
                                quemarCurarEsquivar[2] = true;
                            }
                            itemList.add(new ItemHistorial("Tu: Has hecho " + finalDaño1[0] + " de daño", Color.GRAY, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                            adapter.notifyDataSetChanged();
                        } else {//sin nada
                            vida2.setLayoutParams(new LinearLayout.LayoutParams(vida2.getWidth() - finalDaño1[0], 100));
                            contador2.setText(String.valueOf(vida2.getWidth() - finalDaño1[0]));
                            itemList.add(new ItemHistorial("Tu: Has hecho " + finalDaño1[0] + " de daño", Color.GRAY, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        if (!quemarCurarEsquivar[5]) {//esquivar j2
                            //historial.setText(historial.getText() + "tu: has fallado\n");
                            itemList.add(new ItemHistorial("Tu: Has fallado", Color.GRAY, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                            adapter.notifyDataSetChanged();
                        } else {
                            quemarCurarEsquivar[5] = false;
                        }
                    }
                    atacar1.setVisibility(View.INVISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (datosHabilidades1[1] != -1 && datosHabilidades1[5] == 1) {
                                habilidad1.performClick();
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    atacar2.performClick();
                                    atacar1.setVisibility(View.VISIBLE);
                                }
                            }, 1000);
                        }
                    }, 750);
                }*/