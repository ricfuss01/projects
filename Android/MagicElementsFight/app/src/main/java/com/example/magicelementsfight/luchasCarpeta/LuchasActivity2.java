package com.example.magicelementsfight.luchasCarpeta;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.magicelementsfight.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LuchasActivity2 extends AppCompatActivity {
    int[] datosHabilidades1 = new int[6]; //invocaciones y habilidades (daño,duracion,probFallo,usos,foto, activado)
    int[] datosHabilidades2 = new int[6]; //invocaciones y habilidades (daño,duracion,probFallo,usos,foto, activado)

    int[] datosFotos1 = new int[3]; //animacion atac, animacion norm, numero jugador
    int[] datosFotos2 = new int[3]; //animacion atac, animacion norm, numero jugador
    //historial
     ListView listView;
     AdapterHistorial adapter;
     List<ItemHistorial> itemList;
    ImageView jugador1;
    ImageView jugador2;
    LinearLayout laybarra1;
    LinearLayout laybarra2;
    final boolean[] aciertoHab1={false};
    final boolean[] aciertoHab2={false};

    private SeekBar seekBar;
    float interpolatedValue;


    public LuchasActivity2() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luchas2);
        Random aleatorio = new Random();
        String[] subclases = {"druida","nigromante","celestial","soldado","berserker","paladin","cazadordebestias","cazadordedemonios","cazadordedragones"};
        String[] elementos = {"agua","fuego","tierra","aire"};
        int[] prob ={1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // 1 de 16
        Button BotonAux = findViewById(R.id.buttonAux);

        //animacion barra ataque
        seekBar = findViewById(R.id.seekBar);
        // Configurar colores de la barra
        seekBar.getProgressDrawable().setColorFilter(Color.MAGENTA, android.graphics.PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.MAGENTA, android.graphics.PorterDuff.Mode.SRC_IN);
        // Configurar animación para el indicador
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(550); // Duración de la animación en milisegundos
        animator.setRepeatCount(ValueAnimator.INFINITE); // Repetir infinitamente la animación
        animator.setInterpolator(new CustomInterpolator()); // Interpolador personalizado
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                interpolatedValue = (float) animation.getAnimatedValue();
                seekBar.setProgress(Math.round(interpolatedValue));
                // Utiliza el valor interpolado para realizar la lógica deseada
            }
        });
        animator.start();


        //historial
        TextView historial = findViewById(R.id.historial);
        listView = findViewById(R.id.listviewhistorial);
        itemList = new ArrayList<>();
        adapter = new AdapterHistorial(this, itemList);
        listView.setAdapter(adapter);

        jugador1 = findViewById(R.id.imageViewJugador1);
        ImageView elemento1 = findViewById(R.id.elemento1);
        TextView vida1 = findViewById(R.id.vida1);//total 275
        TextView contador1 = findViewById(R.id.contador1);
        TextView estadoEscrito1 = findViewById(R.id.estado1);
        TextView usos1 = findViewById(R.id.usos1);
        ImageButton atacar1 = findViewById(R.id.atacar1);
        int[] daño1 ={0,0};
        ImageButton habilidad1 = findViewById(R.id.habilidad1);
        ImageView mascota1 = findViewById(R.id.imageViewInvocacion1);

        jugador2 = findViewById(R.id.imageViewJugador2);
        ImageView elemento2 = findViewById(R.id.elemento2);
        TextView vida2 = findViewById(R.id.vida2);//total 275
        TextView contador2 = findViewById(R.id.contador2);
        TextView estadoEscrito2 = findViewById(R.id.estado2);
        TextView usos2 = findViewById(R.id.usos2);
        ImageButton atacar2 = findViewById(R.id.atacar2);
        int[] daño2 ={0,0};
        ImageButton habilidad2 = findViewById(R.id.habilidad2);
        ImageView mascota2 = findViewById(R.id.imageViewInvocacion2);


        daño1[0] = buscarSubclase (getIntent().getStringExtra("clase1"), jugador1)[0];
        daño1[1] = buscarSubclase (getIntent().getStringExtra("clase1"), jugador1)[1];
        buscarElemento(getIntent().getStringExtra("elemento1"), elemento1);
        ImageView jugador1Copia = findViewById(R.id.aux); jugador1Copia.setImageDrawable(jugador1.getDrawable());
        jugador1.setImageResource(R.drawable.vacio);
        funcionAnimar(jugador1,datosFotos1[1],0);
        usos1.setText(String.valueOf(datosHabilidades1[3]));

        int ventaja = 0;
        String elemento2asignado = "";
        if(getIntent().getStringExtra("clase2").equals("bot")) {//es bot
            String jugador2Azar = subclases[aleatorio.nextInt(subclases.length)];
            String elemento2Azar = elementos[aleatorio.nextInt(elementos.length)];
            daño2[0] = buscarSubclase(jugador2Azar, jugador2)[0];
            daño2[1] = buscarSubclase(jugador2Azar, jugador2)[1];
            buscarElemento(elemento2Azar, elemento2);
            ventaja = ventajaTipo(getIntent().getStringExtra("elemento1"),elemento2Azar);///ventaja
            elemento2asignado=elemento2Azar;
        }
        else {//no es bot
            daño2[0] = buscarSubclase(getIntent().getStringExtra("clase2"), jugador2)[0];
            daño2[1] = buscarSubclase(getIntent().getStringExtra("clase2"), jugador2)[1];
            buscarElemento(getIntent().getStringExtra("elemento2"), elemento2);
            ventaja = ventajaTipo(getIntent().getStringExtra("elemento1"),getIntent().getStringExtra("elemento2"));//ventaja
            elemento2asignado=getIntent().getStringExtra("elemento2");
        }
        ImageView jugador2Copia = findViewById(R.id.aux2); jugador2Copia.setImageDrawable(jugador2.getDrawable());
        jugador2.setImageResource(R.drawable.vacio);
        funcionAnimar(jugador2,datosFotos2[1],1);
        //rotarFoto(0,jugador2,jugador2.getDrawable());
        usos2.setText(String.valueOf(datosHabilidades2[3]));

        int[] finalDaño1={0,0};
        finalDaño1[0]= daño1[0];
        finalDaño1[1]=daño1[1];
        int[]finalEstado1={0,0,0,0};//agua fuego tierra aire

        int[] finalDaño2={0,0};
        finalDaño2[0] = daño2[0];
        finalDaño2[1]=daño2[1];
        int[]finalEstado2={0,0,0,0};//agua fuego tierra aire

        int n1=0;int d1=0;int f1=0;
        if(ventaja==1){n1=6;f1=2;}
        else if(ventaja==2){d1=6;}
        switch (getIntent().getStringExtra("elemento1")){
            case "agua":finalEstado1[0]=30+n1-d1;break;
            case "fuego":finalEstado1[1]=10+f1;break;
            case "tierra":finalEstado1[2]=30+n1;break;
            case "aire":finalEstado1[3]=1 ;break;
        }

        int n2=0;int d2=0;int f2=0;
        if(ventaja==2){n2=6;f2=2;} //ventaja fuego y agua y tierra
        else if(ventaja==1){d2=6;}//desventaja agua
        switch (elemento2asignado){
            case "agua":finalEstado2[0]=30+n2-d2;break;
            case "fuego":finalEstado2[1]=10+f2;break;
            case "tierra":finalEstado2[2]=30+n2;break;
            case "aire":finalEstado2[3]=1 ;break;
        }


        final int[] cont = {-1}; //invocaciones y habilidades
        final boolean[] quemarCurarEsquivar1  = new boolean[]{false, false, false};
        final boolean[] quemarCurarEsquivar2  = new boolean[]{false, false, false};
        final int[] aux = {0};

         laybarra1 = findViewById(R.id.laybarra1);
         laybarra2 = findViewById(R.id.laybarra2);

       atacar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random aleatorio = new Random();

                if (Integer.valueOf(contador2.getText().toString()) <=0 ){ showAlert("Jugador 1");}
                else if(Integer.valueOf(contador1.getText().toString()) <= 0 ){showAlert("Jugador 2");}
                else {
                    animator.cancel();//parar barra de daño
                    funcionAnimar(jugador1, datosFotos1[0], 0);
                    funcionAtacar(daño1, vida2, vida1, contador2, contador1, "Tu:", finalDaño1, finalEstado1, estadoEscrito1, estadoEscrito2, aux, quemarCurarEsquivar1, quemarCurarEsquivar2);

                    if (habilidad1.getVisibility() != View.GONE && !getIntent().getStringExtra("clase2").equals("bot"))
                        habilidad1.setVisibility(View.INVISIBLE);
                    atacar1.setVisibility(View.INVISIBLE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (datosHabilidades1[1] != -1 && datosHabilidades1[5] == 1) {
                                habilidad1.performClick();
                            }
                            funcionAnimar(jugador1, datosFotos1[1], 0);
                            if (getIntent().getStringExtra("clase2").equals("bot")) {// comprobar si es vs bot o no
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        atacar2.performClick();
                                        atacar1.setVisibility(View.VISIBLE);
                                    }
                                }, aleatorio.nextInt(501) + 1000);
                            } else {
                                atacar2.setVisibility(View.VISIBLE);
                                if (habilidad2.getVisibility() != View.GONE)
                                    habilidad2.setVisibility(View.VISIBLE);
                            }
                            animator.start();//activar barra de daño
                            BotonAux.performClick();
                        }
                    }, 1000);

                }

            }
        });

        final boolean[] unavezaux = {true};
        atacar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(prob[aleatorio.nextInt(8)]==1 && unavezaux[0] && getIntent().getStringExtra("clase2").equals("bot") ){
                    habilidad2.performClick();
                    unavezaux[0] =false;
                }
                funcionAnimar(jugador2,datosFotos2[0],1);
                animator.cancel();//parar barra de daño
                funcionAtacar(daño2,vida1,vida2,contador1,contador2,"Bot:",finalDaño2,finalEstado2,estadoEscrito2,estadoEscrito1,aux,quemarCurarEsquivar2,quemarCurarEsquivar1);

                if(habilidad2.getVisibility()!=View.GONE)habilidad2.setVisibility(View.INVISIBLE);
                atacar2.setVisibility(View.INVISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (datosHabilidades2[1] != -1 && datosHabilidades2[5] == 1 ) {
                            habilidad2.performClick();
                        }
                        funcionAnimar(jugador2,datosFotos2[1],1);
                        if(!getIntent().getStringExtra("clase2").equals("bot")) {//visible si no es bot
                            atacar1.setVisibility(View.VISIBLE);
                            if(habilidad1.getVisibility()!=View.GONE)habilidad1.setVisibility(View.VISIBLE);
                        }
                        animator.start();//activar barra de daño
                        BotonAux.performClick();
                    }
                }, 1000);
                if (Integer.valueOf(contador2.getText().toString()) <= 0 ){ showAlert("Jugador 1");}
                else if(Integer.valueOf(contador1.getText().toString()) <= 0 ){showAlert("Jugador 2");}
            }
        });

        int aux_turnos1 = datosHabilidades1[1];
        int aux_turnos2 = datosHabilidades2[1];

        habilidad1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if(aciertoHab1[0]==false) {
                    showPopup(aciertoHab1, new OnPopupDismissedListener() {
                        @Override
                        public void onDismissed(boolean[] aciertos) {
                            if (aciertos[0]) {
                                funcionHabilidad(datosHabilidades1, vida2, vida1, contador2, contador1, mascota1, habilidad1, usos1, aux_turnos1, "Tu:", jugador1Copia, aciertoHab1);
                            }
                            else{
                                itemList.add(new ItemHistorial("Tu:" + " tu habilidad ha fallado", 0xFFFF9800, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                                adapter.notifyDataSetChanged();
                                datosHabilidades1[3]--;
                                usos1.setText(String.valueOf(datosHabilidades1[3]));
                                if(datosHabilidades1[3]==0)habilidad1.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    funcionHabilidad(datosHabilidades1,vida2,vida1,contador2,contador1,mascota1,habilidad1,usos1,aux_turnos1,"Tu:", jugador1Copia, aciertoHab1);
                System.out.println(aciertoHab1[0]);
                }
               }
        });

        habilidad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aciertoHab2[0]==false && !getIntent().getStringExtra("clase2").equals("bot")) {
                    showPopup(aciertoHab2, new OnPopupDismissedListener() {
                        @Override
                        public void onDismissed(boolean[] aciertos) {
                            if (aciertos[0]) {
                                funcionHabilidad(datosHabilidades2,vida1,vida2,contador1,contador2,mascota2,habilidad2,usos2,aux_turnos2,"Bot:", jugador2Copia, aciertoHab2);
                            }
                            else{
                                itemList.add(new ItemHistorial("Bot:" + " tu habilidad ha fallado", 0xFFFF9800, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                                adapter.notifyDataSetChanged();
                                datosHabilidades2[3]--;
                                usos2.setText(String.valueOf(datosHabilidades2[3]));
                                if(datosHabilidades2[3]==0)habilidad2.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else{
                    funcionHabilidad(datosHabilidades2,vida1,vida2,contador1,contador2,mascota2,habilidad2,usos2,aux_turnos2,"Bot:", jugador2Copia, aciertoHab2);
                    System.out.println(aciertoHab2[0]);
                }
            }
        });


        BotonAux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prob[aleatorio.nextInt(2)] == 1){
                    laybarra1.setVisibility(View.VISIBLE);
                    laybarra2.setVisibility(View.GONE);
                }
                else{
                    laybarra1.setVisibility(View.GONE);
                    laybarra2.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private int getValueFromSeekBar() {
//56 59 63 82// 44 41 37 18
        double progress = interpolatedValue;

        if (progress >= 44 && progress <= 56) {
            return 11; // Valor para el rango de color rojo
        } else if ((progress <= 37 && progress >= 18) || (progress >= 63 && progress <= 82)) {
            return 4; // Valor para el rango de color naranja
        } else if((progress < 41 && progress > 37) || (progress > 59 && progress < 63) || progress > 82 || progress < 18){
            return 1; // Valor para el rango de color verde
        } else{
            return 0; // Valor para el rango de color gris
        }
    }

private int getValueFromSeekBar2() {
// 8 23 26 36 39 60 68 85
    double progress = interpolatedValue;

    if (progress >= 26 && progress <= 36) {
        return 11; // Valor para el rango de color rojo
    } else if ((progress <= 85 && progress >= 68) ||  progress <= 8) {
        return 4; // Valor para el rango de color naranja
    } else if((progress < 60 && progress > 39) || (progress > 8 && progress < 23) || progress > 85){
        return 1; // Valor para el rango de color verde
    } else{
        return 0; // Valor para el rango de color gris
    }
}


    private void funcionAnimar(ImageView jugador,int drawable, int quien) {
        jugador.setBackgroundResource(drawable);
        AnimationDrawable animation = (AnimationDrawable) jugador.getBackground();
        if(quien==1) jugador.setRotationY(180f);
        animation.start();
    }

    public int[] buscarSubclase(String condicion, ImageView foto){
        int daño[]={0,0};//daño y prob de fallo
        switch (condicion){
            case "druida": foto.setImageResource(R.drawable.druida_luchas); daño[0] = 14;daño[1]=4;//20
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{6, 4, 0, 1, R.drawable.inv_druida, 0};datosFotos1 = new int[]{R.drawable.animacion_druid,R.drawable.rest_druid};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{6, 4, 0, 1, R.drawable.inv_druida, 0};datosFotos2 = new int[]{R.drawable.animacion_druid,R.drawable.rest_druid};}break;
            case "nigromante": foto.setImageResource(R.drawable.nigromante_luchas); daño[0] = 14;daño[1]=4;
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{9, 3, 4, 1, R.drawable.inv_nigro,0};datosFotos1 = new int[]{R.drawable.animacion_nigro,R.drawable.rest_nigro};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{9, 3, 4, 1, R.drawable.inv_nigro,0};datosFotos2 = new int[]{R.drawable.animacion_nigro,R.drawable.rest_nigro};}break;
            case "celestial": foto.setImageResource(R.drawable.celestial_luchas);daño[0] = 14;daño[1]=4;
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{24, 1, 0, 1, R.drawable.inv_celes,0};datosFotos1 = new int[]{R.drawable.animacion_celes,R.drawable.rest_celes};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{24, 1, 0, 1, R.drawable.inv_celes,0};datosFotos2 = new int[]{R.drawable.animacion_celes,R.drawable.rest_celes};}break;
            case "soldado": foto.setImageResource(R.drawable.soldado_luchas); daño[0] = 14;daño[1]=0;//15
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{20, 1, 0, 1, R.drawable.inv_espada,0};datosFotos1 = new int[]{R.drawable.animacion_sold,R.drawable.rest_sold};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{20, 1, 0, 1, R.drawable.inv_espada,0};datosFotos2 = new int[]{R.drawable.animacion_sold,R.drawable.rest_sold};}break;
            case "berserker": foto.setImageResource(R.drawable.berserker_luchas); daño[0] = 14;daño[1]=0;
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{8, 1, 0, 2, R.drawable.inv_hacha,0};datosFotos1 = new int[]{R.drawable.animacion_bers,R.drawable.rest_bers};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{8, 1, 0, 2, R.drawable.inv_hacha,0};datosFotos2 = new int[]{R.drawable.animacion_bers,R.drawable.rest_bers};}break;
            case "paladin": foto.setImageResource(R.drawable.paladin_luchas);daño[0] = 14;daño[1]=0;
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{-8, 1, 0, 3, R.drawable.inv_pocion,0};datosFotos1 = new int[]{R.drawable.animacion_pala,R.drawable.rest_pala};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{-8, 1, 0, 3, R.drawable.inv_pocion,0};datosFotos2 = new int[]{R.drawable.animacion_pala,R.drawable.rest_pala};}break;
            case "cazadordebestias":foto.setImageResource(R.drawable.cazadordebestias_luchas);daño[0] = 14;daño[1]=8;//17
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{12, 1, 0, 2, R.drawable.inv_bestias,0};datosFotos1 = new int[]{R.drawable.animacion_caz_best,R.drawable.rest_caz_best};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{12, 1, 0, 2, R.drawable.inv_bestias,0};datosFotos2 = new int[]{R.drawable.animacion_caz_best,R.drawable.rest_caz_best};}break;
            case "cazadordedemonios":foto.setImageResource(R.drawable.cazadordedemonios_luchas);daño[0] = 14;daño[1]=8;
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{6, 1, 0, 2, R.drawable.inv_demonio,0};datosFotos1 = new int[]{R.drawable.animacion_caz_demo,R.drawable.rest_caz_demo};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{6, 1, 0, 2, R.drawable.inv_demonio,0};datosFotos2 = new int[]{R.drawable.animacion_caz_demo,R.drawable.rest_caz_demo};}break;
            case "cazadordedragones":foto.setImageResource(R.drawable.cazadordedragones_luchas);daño[0] = 14;daño[1]=8;
                if(foto.equals(jugador1)){datosHabilidades1 = new int[]{6, 2, 0, 2, R.drawable.inv_dragon,0};datosFotos1 = new int[]{R.drawable.animacion_caz_drag,R.drawable.rest_caz_drag};}
                if(foto.equals(jugador2)){datosHabilidades2 = new int[]{6, 2, 0, 2, R.drawable.inv_dragon,0};datosFotos2 = new int[]{R.drawable.animacion_caz_drag,R.drawable.rest_caz_drag};}break;
        }
        return daño;
    }

    public void buscarElemento(String condicion, ImageView foto){
        switch (condicion){
            case "agua": foto.setImageResource(R.drawable.gota_luchas);break;
            case "fuego": foto.setImageResource(R.drawable.fuego_luchas);break;
            case "tierra": foto.setImageResource(R.drawable.tierra_luchas);break;
            case "aire": foto.setImageResource(R.drawable.aire_luchas);break;
        }
    }
    public int ventajaTipo (String tipo1, String tipo2){
        if((tipo1.equals("agua") && tipo2.equals("fuego")) || (tipo1.equals("fuego") && tipo2.equals("tierra")) || (tipo1.equals("tierra") && tipo2.equals("aire")) || (tipo1.equals("aire") && tipo2.equals("agua"))){
            return 1;//ventaja j1
        }
        else if((tipo2.equals("agua") && tipo1.equals("fuego")) || (tipo2.equals("fuego") && tipo1.equals("tierra")) || (tipo2.equals("tierra") && tipo1.equals("aire")) || (tipo2.equals("aire") && tipo1.equals("agua"))){
            return 2;//ventaja j2
        }
        else
            return 3;
    }

    private void showAlert(String jugador){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fin de partida");
        builder.setMessage(jugador+" ha ganado");
        builder.setNegativeButton("Aceptar",null);
        builder.setPositiveButton("Jugar de nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                Intent j = new Intent(LuchasActivity2.this, LuchasActivity.class);
                startActivity(j);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void funcionHabilidad(int[] datosHabilidad, TextView vidaContrario, TextView vida, TextView contadorContrario, TextView contador, ImageView imagen, ImageButton boton, TextView usos, int aux_turnos, String quien, ImageView personajeFoto, boolean[] aciertoHabilidadPopup){

        GradientDrawable marco = new GradientDrawable();
        marco.setCornerRadius(100);
        Random aleatorio = new Random();
        int[] prob ={1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // 1 de 16

        if(datosHabilidad[3]>0) {
            int aux = datosHabilidad[5];
            if (datosHabilidad[5] == 1) {// cuando este activada la habilidad
                if (datosHabilidad[1] > 0) {//habilidad tiene que durar varios turnos entonces
                    imagen.setImageResource(datosHabilidad[4]);//foto

                    if(quien.equals("Bot:"))rotarFoto(datosHabilidad[4],imagen, null);//rotar foto del bot

                    if ((datosHabilidad[2] == 0) || (prob[aleatorio.nextInt(datosHabilidad[2])] != 1)) { //prob acertar
                        if(datosHabilidad[0]>=0){ //si es mayor de 0 es daño
                            vidaContrario.setLayoutParams(new LinearLayout.LayoutParams(vidaContrario.getWidth() - datosHabilidad[0], 100)); //daño cada turno
                            contadorContrario.setText(String.valueOf(vidaContrario.getWidth() - datosHabilidad[0]));
                            itemList.add(new ItemHistorial(quien + " tu habilidad ha hecho " + datosHabilidad[0] + " de daño", 0xFFFFEB3B, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                            adapter.notifyDataSetChanged();
                            //berserker
                            if(personajeFoto.getDrawable().getConstantState().equals(ContextCompat.getDrawable(LuchasActivity2.this,R.drawable.berserker_luchas).getConstantState())){
                               if(prob[aleatorio.nextInt(3)] != 1) {
                                   vidaContrario.setLayoutParams(new LinearLayout.LayoutParams(vidaContrario.getWidth() - datosHabilidad[0] * 2, 100)); //daño cada turno
                                   contadorContrario.setText(String.valueOf(vidaContrario.getWidth() - datosHabilidad[0] * 2));
                                   itemList.add(new ItemHistorial(quien + " tu habilidad ha hecho " + datosHabilidad[0] + " de daño", 0xFFFFEB3B, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                                   adapter.notifyDataSetChanged();
                               }
                               else{
                                   itemList.add(new ItemHistorial(quien + " tu habilidad ha fallado el segundo golpe", 0xFFFF9800, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                               }
                            }
                            //soldado
                            if(personajeFoto.getDrawable().getConstantState().equals(ContextCompat.getDrawable(LuchasActivity2.this,R.drawable.soldado_luchas).getConstantState())
                                    && Integer.valueOf(contador.getText().toString())<=50){//solo soldado
                                vidaContrario.setLayoutParams(new LinearLayout.LayoutParams(vidaContrario.getWidth() - datosHabilidad[0]*3/2, 100)); //daño cada turno
                                contadorContrario.setText(String.valueOf(vidaContrario.getWidth() - datosHabilidad[0]*3/2));
                                itemList.add(new ItemHistorial(quien + " tu habilidad ha hecho " + datosHabilidad[0]*1/2 + " de daño adicional", 0xFFFFEB3B, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                                adapter.notifyDataSetChanged();
                            }
                            //cazador de demonios daño
                            if(personajeFoto.getDrawable().getConstantState().equals(ContextCompat.getDrawable(LuchasActivity2.this,R.drawable.cazadordedemonios_luchas).getConstantState()))datosHabilidad[0]*=-1;
                        }
                        //si el daño es negativo es una cura (cazador de demonios/paladin)
                        if(datosHabilidad[0]<0 || personajeFoto.getDrawable().getConstantState().equals(ContextCompat.getDrawable(LuchasActivity2.this,R.drawable.cazadordedemonios_luchas).getConstantState())){
                            vida.setLayoutParams(new LinearLayout.LayoutParams(vida.getWidth() + (-1*datosHabilidad[0]), 100)); //daño cada turno
                            contador.setText(String.valueOf(vida.getWidth() + (-1*datosHabilidad[0])));
                            itemList.add(new ItemHistorial(quien + " tu habilidad te ha curado " + -datosHabilidad[0], 0xFFC4FF3B, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                            adapter.notifyDataSetChanged();
                            //cazador de demonios cura
                            if(personajeFoto.getDrawable().getConstantState().equals(ContextCompat.getDrawable(LuchasActivity2.this,R.drawable.cazadordedemonios_luchas).getConstantState()))datosHabilidad[0]*=-1;
                        }
                    } else { //prob fallo/daño a ti mismo
                        //nigromante
                        if(personajeFoto.getDrawable().getConstantState().equals(ContextCompat.getDrawable(LuchasActivity2.this,R.drawable.nigromante_luchas).getConstantState())){
                            vida.setLayoutParams(new LinearLayout.LayoutParams(vida.getWidth() - 5, 100)); //5 de daño a ti mismo
                            contador.setText(String.valueOf(vida.getWidth() - 5));
                            itemList.add(new ItemHistorial(quien + " tu habilidad ha hecho " + 5 + " de daño A TI MISMO", 0xFFFF9800, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                        }
                        //fallo
                        else{
                            itemList.add(new ItemHistorial(quien + " tu habilidad ha fallado", 0xFFFF9800, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    datosHabilidad[1]--;//quitamos 1 a los turnos que quedan
                } else {
                    datosHabilidad[1]--;
                    imagen.setImageResource(R.drawable.vacio);//cuando acaban los turnos pone esta foto
                    marco.setStroke(100, 0xFF4E4D4D);//fondo gris
                    boton.setBackground(marco);
                    boton.setEnabled(true); //se puede volver a pulsar el boton
                    datosHabilidad[5]=0; //desactivamos habilidad
                    datosHabilidad[1]=aux_turnos; //restauramos usos
                    datosHabilidad[3]--; //quitamos un uso
                    usos.setText(String.valueOf(datosHabilidad[3]));//actualizar usos en la vista
                    aciertoHabilidadPopup[0]=false;
                    if(datosHabilidad[3]<=0)boton.setVisibility(View.GONE);
                }

            }
            if(datosHabilidad[5]==0 && aux == datosHabilidad[5]){
                boton.setEnabled(false);//desactivamos el click boton
                marco.setStroke(5, 0xFFFFEB3B);
                boton.setBackground(marco);//marco visual para indicar que esta activada
                datosHabilidad[5] = 1;//activar habilidad
            }

        }
        else{
            boton.setVisibility(View.GONE);
            usos.setVisibility(View.INVISIBLE);
            usos.setText("");
        }
    }

    private void funcionAtacar( int[] daño, TextView vidaContrario, TextView vida, TextView contadorContrario, TextView contador,  String quien, int[]dañofinal, int[] finalEstado, TextView estadoEscrito, TextView estadoEscritoContrario, int[]aux, boolean[] QCE, boolean[] QCEcontrario ){

        Random aleatorio = new Random();
        int[] prob ={1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // 1 de 16
        //int[] variarDaño={1,0,0,0,-1,1,0,0,0,-1,1,0,0,0,-1,2,-2,1,0,0,0,-1,1,0,0,0,-1,1,0,0,0,-1,2,-2,5};//17

        int dañoAdicional = 0;
        if(laybarra1.getVisibility() == View.VISIBLE){dañoAdicional = getValueFromSeekBar();}
        else if(laybarra2.getVisibility() == View.VISIBLE){dañoAdicional = getValueFromSeekBar2();}


            estadoEscritoContrario.setText("");
            //if ((daño[1] == 0 || prob[aleatorio.nextInt((Integer) daño[1])] != 1 || QCE[0]) && !QCEcontrario[2]) {//(prob ataque o quemar j1) y esquivar j2
            if ((dañoAdicional != 0 || (QCE[0]) && !QCEcontrario[2])) {//(prob ataque o quemar j1) y esquivar j2
                //int dañoAdicional = variarDaño[aleatorio.nextInt(35)];
                if (prob[aleatorio.nextInt(5)] == 1 || QCE[0]) {//prob 10% elemento o quemar j1
                    //quemar o ahogar j1
                    vidaContrario.setLayoutParams(new LinearLayout.LayoutParams(vidaContrario.getWidth() - (dañofinal[0] + finalEstado[0] + finalEstado[1] + dañoAdicional), 100));
                    contadorContrario.setText(String.valueOf(vidaContrario.getWidth() - (dañofinal[0] + finalEstado[0] + finalEstado[1] + dañoAdicional)));
                    if (finalEstado[0] != 0) {//agua
                        itemList.add(new ItemHistorial(quien + " Has hecho " + finalEstado[0] + " por ahogamiento", 0xFF82C5FA, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.gota_luchas)));
                        estadoEscritoContrario.setText("ahogado");
                        estadoEscritoContrario.setTextColor(0xFF82C5FA);
                    } else if (finalEstado[1] != 0) {//fuego
                        estadoEscritoContrario.setText("quemado");
                        estadoEscritoContrario.setTextColor(0xFFF89088);
                        //historial.setText(historial.getText() + "bot: quemado\n");
                        itemList.add(new ItemHistorial(quien + " Has hecho " + finalEstado[1] + " por quemadura", 0xFFF89088, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.fuego_luchas)));
                        if (aux[0] < 2) {
                            QCE[0] = true;
                            aux[0]++;
                        } else {
                            QCE[0] = false;
                            aux[0] = 0;
                        }
                    } else if (finalEstado[2] != 0) {//tierra
                        //historial.setText(historial.getText() + "tu: curado\n");
                        itemList.add(new ItemHistorial(quien + " curado " + finalEstado[2] + " por sanación", 0xFF95FF99, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.tierra_luchas)));
                        vida.setLayoutParams(new LinearLayout.LayoutParams(vida.getWidth() + finalEstado[2], 100));
                        contador.setText(String.valueOf(vida.getWidth() + finalEstado[2]));
                        estadoEscrito.setText("curado");
                        estadoEscrito.setTextColor(0xFF95FF99);
                        QCE[1] = false;
                    } else if (finalEstado[3] != 0) {//aire
                        //historial.setText(historial.getText() + "tu: esquivado\n");
                        itemList.add(new ItemHistorial(quien + " Esquivas el siguiente ataque", 0xFFDADADA, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.aire_luchas)));
                        estadoEscrito.setText("esquivado");
                        estadoEscrito.setTextColor(0xFFA1A0A0);
                        QCE[2] = true;
                    }
                    if(dañoAdicional==11)itemList.add(new ItemHistorial(quien + " CRÍTICO Has hecho " + (dañofinal[0] + dañoAdicional) + " de daño", 0xFF820ED5, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                    else itemList.add(new ItemHistorial(quien + " Has hecho " + (dañofinal[0] + dañoAdicional) + " de daño", Color.GRAY, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                    adapter.notifyDataSetChanged();
                } else {//sin nada
                    vidaContrario.setLayoutParams(new LinearLayout.LayoutParams(vidaContrario.getWidth() - dañofinal[0] - dañoAdicional, 100));
                    contadorContrario.setText(String.valueOf(vidaContrario.getWidth() - dañofinal[0] - dañoAdicional));
                    if(dañoAdicional==11)itemList.add(new ItemHistorial(quien + " CRÍTICO Has hecho " + (dañofinal[0] + dañoAdicional) + " de daño", 0xFF820ED5, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                    else itemList.add(new ItemHistorial(quien + " Has hecho " + (dañofinal[0] + dañoAdicional) + " de daño", Color.GRAY, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (!QCEcontrario[2]) {//esquivar j2
                    //historial.setText(historial.getText() + "tu: has fallado\n");
                    itemList.add(new ItemHistorial(quien + " Has fallado", Color.GRAY, ContextCompat.getDrawable(LuchasActivity2.this, R.drawable.vacio)));
                    adapter.notifyDataSetChanged();
                } else {
                    QCEcontrario[2] = false;
                }
            }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void rotarFoto (int num, ImageView imagen, Drawable drawable){
        Drawable originalDrawable;
        if(num != 0) {
             originalDrawable = getResources().getDrawable(num);
        }
        else{
             originalDrawable = drawable;
        }
        DrawableCompat.setTintMode(originalDrawable, PorterDuff.Mode.MULTIPLY);
        DrawableCompat.setTint(originalDrawable, Color.BLACK);
        Bitmap originalBitmap = drawableToBitmap(originalDrawable);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1); // Reflejar horizontalmente
        Bitmap reflectedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        BitmapDrawable mirroredBitmapDrawable = new BitmapDrawable(getResources(), reflectedBitmap);
        imagen.setImageDrawable(mirroredBitmapDrawable);
    }


    private void showPopup(final boolean[] acertar, final OnPopupDismissedListener onPopupDismissedListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_layout, null);

        // Configurar el layout y los botones en el popup
        RelativeLayout popupLayout = popupView.findViewById(R.id.relativeLayoutPopup);
        Button[] buttons = new Button[3];
        //int[] order = generateRandomOrder(buttons.length);
        int[] order = {0,1,2};
        final int[] currentIndex = {0};

        for (int i = 0; i < buttons.length; i++) {
            Button button = new Button(this);
            button.setId(i);
            button.setText(""+(i + 1));
            button.setBackgroundResource(R.drawable.esquinas_curvas);
            button.setVisibility(View.INVISIBLE);
            buttons[i] = button;
            popupLayout.addView(button);
        }
        builder.setTitle("Pulsa todo en orden");
        builder.setView(popupView);
        AlertDialog dialog = builder.create();
        final boolean[] correcto = {true};
        final int[] anterior = {-1};

        // Cuando se cierre el popup
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Llama a listener.onDismissed(aciertos) para enviar el resultado de los aciertos
                onPopupDismissedListener.onDismissed(acertar);
            }
        });
        showNextButton(buttons[order[currentIndex[0]]], order, currentIndex, buttons, dialog, popupView, anterior, correcto,acertar);


        dialog.show();
    }

    private void showNextButton(Button button, int[] order, final int[] currentIndex, Button[] buttons, AlertDialog dialog, View popup, final int[] anterior, final boolean[] correcto, final boolean[] acertar) {
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                GradientDrawable marco = new GradientDrawable();
                marco.setCornerRadius(100);
                marco.setStroke(100, 0xFF95FF99);//fondo gris
                button.setBackground(marco);

                int buttonId = v.getId();
                int expectedButtonId = order[currentIndex[0]];
                System.out.println(buttonId+"  * * *  "+expectedButtonId+"  * * *  "+(anterior[0]+1)+acertar[0]);
                if (buttonId == expectedButtonId) {
                    if(currentIndex[0] != (anterior[0]+1))correcto[0]=false;
                    //currentIndex[0]++;
                     else if(currentIndex[0]==2 && correcto[0]){
                        acertar[0]=true;
                    }
                    anterior[0]=currentIndex[0];
                } else {
                    System.out.println("El botón se pulsó en el orden incorrecto");
                    // El botón se pulsó en el orden incorrecto, se puede manejar el error aquí
                    //dialog.dismiss();
                }
            }
        });

        // Generar posiciones aleatorias dentro del popupLayout
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
        popup.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popup.getMeasuredWidth();
        int popupHeight = popup.getMeasuredHeight();
        int maxButtonX = popupWidth - (button.getWidth()+300);
        int maxButtonY = popupHeight - (button.getHeight()+300);
        int minButtonX = button.getWidth()+50;
        int minButtonY = button.getHeight()+50;
        Random random = new Random();
        int randomX = random.nextInt(maxButtonX-minButtonX+1)+minButtonX;
        int randomY = random.nextInt(maxButtonY-minButtonY+1)+minButtonY;
        layoutParams.leftMargin = randomX;
        layoutParams.topMargin = randomY;
        button.setLayoutParams(layoutParams);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (button.getVisibility() == View.VISIBLE) {
                    button.setVisibility(View.INVISIBLE);

                    if (currentIndex[0] < buttons.length - 1) {
                        currentIndex[0]++;
                        Button nextButton = buttons[order[currentIndex[0]]];
                        showNextButton(nextButton,  order, currentIndex, buttons, dialog, popup, anterior, correcto,acertar);
                    } else {
                        // El último botón no fue pulsado a tiempo, se puede manejar el error aquí
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {}
                        dialog.dismiss();
                    }
                }
            }
        }, 750);
    }

    interface OnPopupDismissedListener {
        void onDismissed(boolean[] aciertos);
    }


}
