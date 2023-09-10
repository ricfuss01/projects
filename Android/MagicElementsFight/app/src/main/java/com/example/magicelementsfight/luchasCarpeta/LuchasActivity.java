package com.example.magicelementsfight.luchasCarpeta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.magicelementsfight.R;

public class LuchasActivity extends AppCompatActivity {

    public static String compartir = "";
    public static String compartirElemento = "";
    boolean dosjugadores = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_luchas);

        CheckBox mago = findViewById(R.id.checkMago);
        CheckBox guerrero = findViewById(R.id.checkGuerrero);
        CheckBox arquero = findViewById(R.id.checkArquero);

        ImageButton agua = findViewById(R.id.btnAgua);
        ImageButton fuego = findViewById(R.id.btnFuego);
        ImageButton tierra = findViewById(R.id.btnTierra);
        ImageButton aire = findViewById(R.id.btnAire);
        TextView infoElementos = findViewById(R.id.infoElementos);
        ImageButton botondosjug = findViewById(R.id.dosjugadores);

        ImageView siguiente = findViewById(R.id.siguiente);

        GradientDrawable marco = new GradientDrawable();
        marco.setCornerRadius(100);

        mago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mago.isChecked()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLuchas, new MagoFragment()).commit();
                    guerrero.setChecked(false);
                    arquero.setChecked(false);
                }
                else{
                    //si quitas el check se ponga en negro
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLuchas, new VacioFragment()).commit();
                }
            }
        });
        guerrero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guerrero.isChecked()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLuchas, new GuerreroFragment()).commit();
                    mago.setChecked(false);
                    arquero.setChecked(false);
                }
                else{
                    //si quitas el check se ponga en negro
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLuchas, new VacioFragment()).commit();
                }
            }
        });
        arquero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arquero.isChecked()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLuchas, new ArqueroFragment()).commit();
                    guerrero.setChecked(false);
                    mago.setChecked(false);
                }
                else{
                    //si quitas el check se ponga en negro
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLuchas, new VacioFragment()).commit();
                }
            }
        });

        agua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoElementos.setText("AGUA\n 20% probabilidad de ahogar\nAhoga al enemigo al atacar\n Fuerte contra: Fuego\n Debil contra: Aire");
                infoElementos.setTextColor(0xFF82C5FA);
                marco.setStroke(10, 0xFF82C5FA);
                agua.setBackground(marco);
                fuego.setBackgroundResource(R.drawable.esquinas_curvas);
                aire.setBackgroundResource(R.drawable.esquinas_curvas);
                tierra.setBackgroundResource(R.drawable.esquinas_curvas);
                compartirElemento = "agua";
            }
        });
        fuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoElementos.setText("FUEGO\n 20% probabilidad de quemar\n Quema con sus 3 proximos ataques\n Fuerte contra: Tierra\n Debil contra: Agua");
                infoElementos.setTextColor(0xFFF89088);
                marco.setStroke(10, 0xFFF89088);
                fuego.setBackground(marco);
                agua.setBackgroundResource(R.drawable.esquinas_curvas);
                tierra.setBackgroundResource(R.drawable.esquinas_curvas);
                aire.setBackgroundResource(R.drawable.esquinas_curvas);
                compartirElemento = "fuego";
            }
        });
        tierra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoElementos.setText("TIERRA\n 20% probabilidad de curarse\n Se cura al atacar\n Fuerte contra: Aire\n Debil contra: Fuego");
                infoElementos.setTextColor(0xFF95FF99);
                marco.setStroke(10, 0xFF95FF99);
                tierra.setBackground(marco);
                agua.setBackgroundResource(R.drawable.esquinas_curvas);
                fuego.setBackgroundResource(R.drawable.esquinas_curvas);
                aire.setBackgroundResource(R.drawable.esquinas_curvas);
                compartirElemento = "tierra";
            }
        });
        aire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoElementos.setText("AIRE\n 20% probabilidad de esquivar\n Esquiva el siguiente ataque recibido\n Fuerte contra: Agua\n Debil contra: Tierra");
                infoElementos.setTextColor(0xFFA1A0A0);
                marco.setStroke(10, 0xFFA1A0A0);
                aire.setBackground(marco);
                agua.setBackgroundResource(R.drawable.esquinas_curvas);
                tierra.setBackgroundResource(R.drawable.esquinas_curvas);
                fuego.setBackgroundResource(R.drawable.esquinas_curvas);
                compartirElemento = "aire";
            }
        });

        try {
            if (!getIntent().getStringExtra("clase1").isEmpty()) {
                botondosjug.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

        botondosjug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosjugadores = true;
                GradientDrawable marco = new GradientDrawable();
                marco.setCornerRadius(100);
                marco.setStroke(100, 0xFFFFEB3B);
                botondosjug.setBackground(marco);
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (!mago.isChecked() && !guerrero.isChecked() && !arquero.isChecked()) {
                        Toast.makeText(getApplicationContext(), "Selecciona clase", Toast.LENGTH_SHORT).show();
                    }
                    if (infoElementos.getText().equals("")) {
                        Toast.makeText(getApplicationContext(), "Selecciona elemento", Toast.LENGTH_SHORT).show();
                    }
                    if (compartir.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Selecciona subclase", Toast.LENGTH_SHORT).show();
                    }
                if((mago.isChecked() || guerrero.isChecked() || arquero.isChecked()) && (!infoElementos.getText().equals("")) && (!compartir.isEmpty())){
                    //finish();
                    //Intent j = new Intent(LuchasActivity.this, LuchasActivity2.class);
                    //startActivity(j);

                    // Obtener una instancia de SharedPreferences
                    SharedPreferences sharedPref = getSharedPreferences("mi_pref", Context.MODE_PRIVATE);
                    // Obtener el valor de primeraEjecucion desde las preferencias compartidas
                    boolean primeraEjecucion = sharedPref.getBoolean("primeraEjecucion", true);
                    if (primeraEjecucion) {
                        if(dosjugadores){
                        // Realizar acciones con el valor
                        Intent intent = new Intent(LuchasActivity.this, LuchasActivity.class);
                        intent.putExtra("clase1", compartir);
                        intent.putExtra("elemento1", compartirElemento);
                            startActivity(intent);
                            // Guardar el estado de primeraEjecucion en las preferencias compartidas
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("primeraEjecucion", false);
                            editor.apply();
                        }
                        else{
                            Intent intent = new Intent(LuchasActivity.this, LuchasActivity2.class);
                            intent.putExtra("clase1", compartir);
                            intent.putExtra("elemento1", compartirElemento);
                            intent.putExtra("clase2", "bot");
                            intent.putExtra("elemento2", "bot");
                            startActivity(intent);
                        }
                    }
                    else{
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("primeraEjecucion"); // Eliminar la clave "primeraEjecucion" de las preferencias compartidas
                        editor.apply();
                        String clase1 = getIntent().getStringExtra("clase1"); // Obtener el valor X de los extras del Intent
                        String elemento1 = getIntent().getStringExtra("elemento1"); // Obtener el valor X de los extras del Intent

                        // Abrir la nueva actividad y pasar los valores X e Y
                        Intent intent = new Intent(LuchasActivity.this, LuchasActivity2.class);
                        intent.putExtra("clase1", clase1);
                        intent.putExtra("elemento1", elemento1);
                        intent.putExtra("clase2", compartir);
                        intent.putExtra("elemento2", compartirElemento);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}