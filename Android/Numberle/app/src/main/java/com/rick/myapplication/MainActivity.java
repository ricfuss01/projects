package com.rick.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView correct;
    int[] numeros = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    char[] operadores = {'+', '-', '*'};
    char[] solucion ={' ',' ',' ',' ',' '};
    final boolean[] si ={false,false,false,false,false};
    final boolean[] esta ={false,false,false,false,false};
    Random alea = new Random();
    int cont = 0;
    int contFallos = 0;
    int porQueCaracterVa=0;
    boolean suma = false;
    boolean resta = false;
    boolean multi = false;
    Button bx,b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bmas,bmenos,bpor;
    public static String compartido="";
    int []aciertosXintentos = {0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView[] edi1 ={null,null,null,null,null};
        edi1[0] = findViewById(R.id.editTextNumberSigned1);
        edi1[1] = findViewById(R.id.editTextNumberSigned2);
        edi1[2] = findViewById(R.id.editTextNumberSigned3);
        edi1[3] = findViewById(R.id.editTextNumberSigned4);
        edi1[4] = findViewById(R.id.editTextNumberSigned5);
        Button boton = findViewById(R.id.button);
        TextView resultado = findViewById(R.id.textView2);
        int a =aleatorio();
        if(a==1000000)
            resultado.setText("?????");
        else
            resultado.setText(String.valueOf(a));


        correct = findViewById(R.id.textView);
        final int[] correcto = {0};
        correct.setText(String.valueOf(correcto[0]));
        SharedPreferences sp = getSharedPreferences("marcador", Context.MODE_PRIVATE);
        correct.setText(sp.getString("corr", ""));
        if(!sp.getString("fallos","").toString().equals(""))
        contFallos=Integer.valueOf(sp.getString("fallos",""));
        if(!sp.getString("c1","").toString().equals(""))
        aciertosXintentos[0]=Integer.valueOf(sp.getString("c1",""));
        if(!sp.getString("c2","").toString().equals(""))
        aciertosXintentos[1]=Integer.valueOf(sp.getString("c2",""));
        if(!sp.getString("c3","").toString().equals(""))
        aciertosXintentos[2]=Integer.valueOf(sp.getString("c3",""));
        if(!sp.getString("c4","").toString().equals(""))
        aciertosXintentos[3]=Integer.valueOf(sp.getString("c4",""));

        System.out.println(R.drawable.curvas_amarillo);
        System.out.println(R.drawable.curvas_verde);
        System.out.println(R.drawable.curvas_noesta);

        FloatingActionButton recargar = findViewById(R.id.floatingActionButton);
        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                recuerda(view);
            }
        });
        ImageButton imageBoton = findViewById(R.id.imageButton);
        imageBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correcto[0]=0;
                aciertosXintentos[0]=0;
                aciertosXintentos[1]=0;
                aciertosXintentos[2]=0;
                aciertosXintentos[3]=0;
                contFallos=0;
                correct.setText(String.valueOf(correcto[0]));
            }
        });
        ImageButton info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!correct.getText().toString().equals("")) {
                    if (Integer.valueOf(correct.getText().toString()) == 0) {
                        correct.setText("1");
                        compartido = "1 intento:   " + aciertosXintentos[0] + " " + Math.round(aciertosXintentos[0] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "2 intentos: " + aciertosXintentos[1] + " " + Math.round(aciertosXintentos[1] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "3 intentos: " + aciertosXintentos[2] + " " + Math.round(aciertosXintentos[2] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "4 intentos: " + aciertosXintentos[3] + " " + Math.round(aciertosXintentos[3] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n"+
                                "\nfallos: " +contFallos;
                        correct.setText("0");
                    } else {
                        compartido = "1 intento:   " + aciertosXintentos[0] + " " + Math.round(aciertosXintentos[0] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "2 intentos: " + aciertosXintentos[1] + " " + Math.round(aciertosXintentos[1] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "3 intentos: " + aciertosXintentos[2] + " " + Math.round(aciertosXintentos[2] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "4 intentos: " + aciertosXintentos[3] + " " + Math.round(aciertosXintentos[3] * 100 / Integer.valueOf(correct.getText().toString())) + "%" + "\n" +
                                "\nfallos: " +contFallos;

                    }
                }
                Intent i = new Intent(getApplicationContext(),PopUp.class);
                startActivity(i);
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                porQueCaracterVa=0;
                if (!edi1[0].getText().toString().equals("") && !edi1[1].getText().toString().equals("") && !edi1[2].getText().toString().equals("") && !edi1[3].getText().toString().equals("") && !edi1[4].getText().toString().equals("") && cont <= 3) {
                    Toast.makeText(getApplicationContext(), "comprobado <3", Toast.LENGTH_SHORT).show();
                    si[0]=false;si[1]=false;si[2]=false;si[3]=false;si[4]=false;
                    System.out.println((int)solucion[0]+(int)solucion[1]+(int)solucion[2]+(int)solucion[3]+(int)solucion[4]);
                    //1
                    if(String.valueOf((int)solucion[0]).equals(edi1[0].getText().toString())){
                        teclado(edi1[0], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[0].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[0]=true;
                        esta[0]=true;
                    }
                    //2
                    if(String.valueOf(solucion[1]).equals(edi1[1].getText().toString())){
                        teclado(edi1[1], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[1].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[1]=true;
                        esta[1]=true;
                    }
                    else if(String.valueOf((int)solucion[1]).equals(edi1[1].getText().toString())){
                        teclado(edi1[1], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[1].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[1]=true;
                        esta[1]=true;
                    }
                    //3
                    if(String.valueOf(solucion[2]).equals(edi1[2].getText().toString())){
                        teclado(edi1[2], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[2].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[2]=true;
                        esta[2]=true;
                    }
                    else if(String.valueOf((int)solucion[2]).equals(edi1[2].getText().toString())){
                        teclado(edi1[2], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[2].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[2]=true;
                        esta[2]=true;
                    }
                    //4
                    if(String.valueOf(solucion[3]).equals(edi1[3].getText().toString())){
                        teclado(edi1[3], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[3].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[3]=true;
                        esta[3]=true;
                    }
                    else if(String.valueOf((int)solucion[3]).equals(edi1[3].getText().toString())){
                        teclado(edi1[3], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[3].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[3]=true;
                        esta[3]=true;
                    }
                    //5
                    if(String.valueOf((int)solucion[4]).equals(edi1[4].getText().toString())){
                        teclado(edi1[4], ContextCompat.getColor(MainActivity.this,R.color.verde));
                        edi1[4].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_verde));
                        si[4]=true;
                        esta[4]=true;
                    }
                    //1


                    if(!si[0]) {
                        if ((String.valueOf((int) solucion[1]).equals(edi1[0].getText().toString()) || String.valueOf(solucion[1]).equals(edi1[0].getText().toString())) && !esta[1] && !si[1]) {
                            teclado(edi1[0], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[0].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[1] = true;
                        } else if ((String.valueOf((int) solucion[2]).equals(edi1[0].getText().toString()) || String.valueOf(solucion[2]).equals(edi1[0].getText().toString())) && !esta[2] && !si[2]) {
                            teclado(edi1[0], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[0].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[2] = true;
                        } else if ((String.valueOf((int) solucion[3]).equals(edi1[0].getText().toString()) || String.valueOf(solucion[3]).equals(edi1[0].getText().toString())) && !esta[3] && !si[3]) {
                            teclado(edi1[0], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[0].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[3] = true;
                        } else if ((String.valueOf((int) solucion[4]).equals(edi1[0].getText().toString()) || String.valueOf(solucion[4]).equals(edi1[0].getText().toString())) && !esta[4] && !si[4]) {
                            teclado(edi1[0], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[0].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[4] = true;
                        }
                        else{
                            teclado(edi1[0], ContextCompat.getColor(MainActivity.this, R.color.noesta));
                        }
                    }
                    //2
                    if(!si[1]) {
                        if ((String.valueOf((int) solucion[0]).equals(edi1[1].getText().toString()) || String.valueOf(solucion[0]).equals(edi1[1].getText().toString())) && !esta[0] && !si[0]) {
                            teclado(edi1[1], ContextCompat.getColor(MainActivity.this, R.color.amarillo));
                            edi1[1].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[0] = true;
                        } else if ((String.valueOf((int) solucion[2]).equals(edi1[1].getText().toString()) || String.valueOf(solucion[2]).equals(edi1[1].getText().toString())) && !esta[2] && !si[2]) {
                            teclado(edi1[1], ContextCompat.getColor(MainActivity.this, R.color.amarillo));
                            edi1[1].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[2] = true;
                        } else if ((String.valueOf((int) solucion[3]).equals(edi1[1].getText().toString()) || String.valueOf(solucion[3]).equals(edi1[1].getText().toString())) && !esta[3] && !si[3]) {
                            teclado(edi1[1], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[1].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[3] = true;
                        } else if ((String.valueOf((int) solucion[4]).equals(edi1[1].getText().toString()) || String.valueOf(solucion[4]).equals(edi1[1].getText().toString())) && !esta[4] && !si[4]) {
                            teclado(edi1[1], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[1].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[4] = true;
                        }
                        else{
                            teclado(edi1[1], ContextCompat.getColor(MainActivity.this, R.color.noesta));
                        }
                    }
                    //3
                    if(!si[2]) {
                        if ((String.valueOf((int) solucion[0]).equals(edi1[2].getText().toString()) || String.valueOf(solucion[0]).equals(edi1[2].getText().toString())) && !esta[0] && !si[0]) {
                            teclado(edi1[2], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[2].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[0] = true;
                        } else if ((String.valueOf((int) solucion[1]).equals(edi1[2].getText().toString()) || String.valueOf(solucion[1]).equals(edi1[2].getText().toString())) && !esta[1] && !si[1]) {
                            teclado(edi1[2], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[2].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[1] = true;
                        } else if ((String.valueOf((int) solucion[3]).equals(edi1[2].getText().toString()) || String.valueOf(solucion[3]).equals(edi1[2].getText().toString())) && !esta[3] && !si[3]) {
                            teclado(edi1[2], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[2].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[3] = true;
                        } else if ((String.valueOf((int) solucion[4]).equals(edi1[2].getText().toString()) || String.valueOf(solucion[4]).equals(edi1[2].getText().toString())) && !esta[4] && !si[4]) {
                            teclado(edi1[2], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[2].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[4] = true;
                        }
                        else{
                            teclado(edi1[2], ContextCompat.getColor(MainActivity.this, R.color.noesta));
                        }
                    }
                    //4
                    if(!si[3]) {
                        if ((String.valueOf((int) solucion[0]).equals(edi1[3].getText().toString()) || String.valueOf(solucion[0]).equals(edi1[3].getText().toString())) && !esta[0] && !si[0]) {
                            teclado(edi1[3], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[3].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[0] = true;
                        } else if ((String.valueOf((int) solucion[1]).equals(edi1[3].getText().toString()) || String.valueOf(solucion[1]).equals(edi1[3].getText().toString())) && !esta[1] && !si[1]) {
                            teclado(edi1[3], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[3].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[1] = true;
                        } else if ((String.valueOf((int) solucion[2]).equals(edi1[3].getText().toString()) || String.valueOf(solucion[2]).equals(edi1[3].getText().toString())) && !esta[2] && !si[2]) {
                            teclado(edi1[3], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[3].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[2] = true;
                        } else if ((String.valueOf((int) solucion[4]).equals(edi1[3].getText().toString()) || String.valueOf(solucion[4]).equals(edi1[3].getText().toString())) && !esta[4] && !si[4]) {
                            teclado(edi1[3], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[3].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[4] = true;
                        }
                        else{
                            teclado(edi1[3], ContextCompat.getColor(MainActivity.this, R.color.noesta));
                        }
                    }
                    //5
                    if(!si[4]) {
                        if ((String.valueOf((int) solucion[0]).equals(edi1[4].getText().toString()) || String.valueOf(solucion[0]).equals(edi1[4].getText().toString())) && !esta[0] && !si[0]) {
                            teclado(edi1[4], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[4].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[0] = true;
                        } else if ((String.valueOf((int) solucion[1]).equals(edi1[4].getText().toString()) || String.valueOf(solucion[1]).equals(edi1[4].getText().toString())) && !esta[1] && !si[1]) {
                            teclado(edi1[4], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[4].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[1] = true;
                        } else if ((String.valueOf((int) solucion[2]).equals(edi1[4].getText().toString()) || String.valueOf(solucion[2]).equals(edi1[4].getText().toString())) && !esta[2] && !si[2]) {
                            teclado(edi1[4], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[4].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[2] = true;
                        } else if ((String.valueOf((int) solucion[3]).equals(edi1[4].getText().toString()) || String.valueOf(solucion[3]).equals(edi1[4].getText().toString())) && !esta[3] && !si[3]) {
                            teclado(edi1[4], ContextCompat.getColor(MainActivity.this,R.color.amarillo));
                            edi1[4].setBackground(ContextCompat.getDrawable(MainActivity.this,R.drawable.curvas_amarillo));
                            esta[3] = true;
                        }
                        else{
                            teclado(edi1[4], ContextCompat.getColor(MainActivity.this, R.color.noesta));
                        }
                    }
                    if(si[0] && si[1] && si[2] && si[3] && si[4]){
                        Toast.makeText(getApplicationContext(), "correcto!!!!  ^.^  ily<3", Toast.LENGTH_LONG).show();
                        System.out.println(aciertosXintentos[cont]);
                        aciertosXintentos[cont]=aciertosXintentos[cont]+1;
                        if(correct.getText().toString().equals(""))
                            correcto[0]=1;
                        else
                            correcto[0]=Integer.valueOf(correct.getText().toString())+1;
                        correct.setText(String.valueOf(correcto[0]));

                    }
                    if(cont == 0) {
                        edi1[0] = findViewById(R.id.editTextNumberSigned6);
                        edi1[1] = findViewById(R.id.editTextNumberSigned7);
                        edi1[2] = findViewById(R.id.editTextNumberSigned8);
                        edi1[3] = findViewById(R.id.editTextNumberSigned9);
                        edi1[4] = findViewById(R.id.editTextNumberSigned10);
                    }
                    if (cont==1){
                        edi1[0] = findViewById(R.id.editTextNumberSigned11);
                        edi1[1] = findViewById(R.id.editTextNumberSigned12);
                        edi1[2] = findViewById(R.id.editTextNumberSigned13);
                        edi1[3] = findViewById(R.id.editTextNumberSigned14);
                        edi1[4] = findViewById(R.id.editTextNumberSigned15);
                    }
                    if (cont==2){
                        edi1[0] = findViewById(R.id.editTextNumberSigned16);
                        edi1[1] = findViewById(R.id.editTextNumberSigned17);
                        edi1[2] = findViewById(R.id.editTextNumberSigned18);
                        edi1[3] = findViewById(R.id.editTextNumberSigned19);
                        edi1[4] = findViewById(R.id.editTextNumberSigned20);
                    }
                    cont++;
                    esta[0]=false;esta[1]=false;esta[2]=false;esta[3]=false;esta[4]=false;
                } else {
                        Toast.makeText(getApplicationContext(), "no valido", Toast.LENGTH_SHORT).show();
                    }
                if(cont > 3 && !(si[0] && si[1] && si[2] && si[3] && si[4]) ){
                    String[] resultadoImprimir={"","","","",""};
                    for(int i=0; i<5; i++) {
                        if (((int) solucion[i]) == 43) resultadoImprimir[i]="+";
                        if (((int) solucion[i]) == 45) resultadoImprimir[i]="-";
                        if (((int) solucion[i]) == 42) resultadoImprimir[i]="*";
                        if (((int) solucion[i]) < 10) resultadoImprimir[i]=String.valueOf((int) solucion[i]);
                    }
                    Toast.makeText(getApplicationContext(), Arrays.toString(resultadoImprimir), Toast.LENGTH_SHORT).show();
                contFallos++;
                }
            }
        });

        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(porQueCaracterVa<=4) {
                    edi1[porQueCaracterVa].setText("1");
                    porQueCaracterVa++;
                }
            }
        });
        b2 = findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(porQueCaracterVa<=4) {
                    edi1[porQueCaracterVa].setText("2");
                    porQueCaracterVa++;
                }
            }
        });
        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4){
                    edi1[porQueCaracterVa].setText("3");
                    porQueCaracterVa++;
            }
            }
        });
        b4 = findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("4");
                    porQueCaracterVa++;
                }
            }
        });
        b5 = findViewById(R.id.b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("5");
                    porQueCaracterVa++;
                }
            }
        });
        b6 = findViewById(R.id.b6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("6");
                    porQueCaracterVa++;
                }
            }
        });
        b7 = findViewById(R.id.b7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("7");
                    porQueCaracterVa++;
                }
            }
        });
        b8 = findViewById(R.id.b8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("8");
                    porQueCaracterVa++;
                }
            }
        });
        b9 = findViewById(R.id.b9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("9");
                    porQueCaracterVa++;
                }
            }
        });
        b0 = findViewById(R.id.b0);
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("0");
                    porQueCaracterVa++;
                }
            }
        });
        bmas = findViewById(R.id.bmas);
        bmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("+");
                    porQueCaracterVa++;
                }
            }
        });
        bmenos = findViewById(R.id.bmenos);
        bmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("-");
                    porQueCaracterVa++;
                }
            }
        });
        bpor = findViewById(R.id.bpor);
        bpor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (porQueCaracterVa <= 4) {
                    edi1[porQueCaracterVa].setText("*");
                    porQueCaracterVa++;
                }
            }
        });
        bx = findViewById(R.id.bx);
        bx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(porQueCaracterVa>0)
                    porQueCaracterVa--;
                edi1[porQueCaracterVa].setText(" ");
            }

        });
    }
        public int aleatorio(){
            boolean esNum = false;
            int sol = 0;
            int numAct=0;
            int numAct2=0;
            //1
            solucion[0] = (char) numeros[alea.nextInt(10)];
            //2
            if(alea.nextInt(2) == 0){
                solucion[1] = (char) numeros[alea.nextInt(10)];
                esNum = true;
            }
            else{
                solucion[1] = operadores[alea.nextInt(3)];
            }
            //3
            if(esNum != true){
                solucion[2] = (char) numeros[alea.nextInt(10)];
                esNum = true;
            }
            else{
                esNum = false;
                if(alea.nextInt(2) == 0){
                    solucion[2] = (char) numeros[alea.nextInt(10)];
                    esNum = true;
                }
                else{
                    solucion[2] = operadores[alea.nextInt(3)];
                }
            }
            //4
            if(esNum != true){
                solucion[3] = (char) numeros[alea.nextInt(10)];
                esNum = true;
            }
            else{
                esNum = false;
                if(alea.nextInt(3) != 0){
                    solucion[3] = operadores[alea.nextInt(2)];
                }
                else{
                    solucion[3] = (char) numeros[alea.nextInt(10)];
                    esNum = true;

                }
            }
            //5
                solucion[4] = (char) numeros[alea.nextInt(10)];

            esNum = true;
            for(int i=0; i<5; i++) {
                if ((int) solucion[i] < 10 && (int) solucion[i] >= 0) {
                    if(esNum){
                        numAct = numAct * 10 + (int) solucion[i];
                    System.out.println(numAct);}
                    else {
                        numAct2 = numAct2 * 10 + (int) solucion[i];
                        System.out.println(numAct2);
                    }
                } if((int) solucion[i] >= 10 || (int) solucion[i] < 0 || i==4){
                    esNum = !esNum;
                    if(!esNum){
                        if(suma) {numAct=numAct+numAct2;}
                        if(resta) {numAct=numAct2-numAct;}
                        if(multi) {numAct=numAct2*numAct;}
                        sol=numAct;
                        if(multi || resta || suma) {numAct=0;}
                        suma=false;resta=false;multi=false;


                    }
                    else{
                        if(suma) {numAct2=numAct+numAct2;}
                        if(resta) {numAct2=numAct-numAct2;}
                        if(multi) {numAct2=numAct2*numAct;}
                        sol=numAct2;
                        if(multi || resta || suma) {numAct=0;}
                        suma=false;resta=false;multi=false;

                    }
                    if(((int)solucion[i])==43) suma=true;
                    if(((int)solucion[i])==45) resta = true;
                    if(((int)solucion[i])==42) multi=true;
                    //numAct=0;
                }
            }
            System.out.println(sol);
            if(solucion[0]<10 && solucion[1]<10 && solucion[2]<10 && solucion[3]<10 && solucion[4]<10)
                return 1000000;
            else
                return sol;
        }
    public void recuerda(View view) {
        SharedPreferences spb = getSharedPreferences("marcador", Context.MODE_PRIVATE);
        SharedPreferences.Editor edi = spb.edit();
        edi.putString("corr", correct.getText().toString());
        edi.putString("c1", String.valueOf(aciertosXintentos[0]));
        edi.putString("c2", String.valueOf(aciertosXintentos[1]));
        edi.putString("c3", String.valueOf(aciertosXintentos[2]));
        edi.putString("c4", String.valueOf(aciertosXintentos[3]));
        edi.putString("fallos", String.valueOf(contFallos));
        edi.commit();
    }
    int colorAnterior[]={0,0,0,0,0,0,0,0,0,0,0,0,0};
    public void teclado(TextView tv , int color){
        @SuppressLint("ResourceType") int verde = ContextCompat.getColor(MainActivity.this,R.color.verde);
        @SuppressLint("ResourceType") int noesta = ContextCompat.getColor(MainActivity.this,R.color.noesta);
        if(tv.getText().toString().equals("1")){
            if(colorAnterior[0]!=verde && color!=noesta)
                b1.setBackgroundColor(color);
            else if(colorAnterior[0]==0 && color==noesta)
                b1.setBackgroundColor(noesta);
            colorAnterior[0]=color;
        }if(tv.getText().toString().equals("2")){
            if(colorAnterior[1]!=verde && color!=noesta)
                b2.setBackgroundColor(color);
            else if(colorAnterior[1]==0 && color==noesta)
                b2.setBackgroundColor(noesta);
            colorAnterior[1]=color;
        }if(tv.getText().toString().equals("3")){
            if(colorAnterior[2]!=verde && color!=noesta)
            b3.setBackgroundColor(color);
            else if(colorAnterior[2]==0 && color==noesta)
                b3.setBackgroundColor(noesta);
            colorAnterior[2]=color;
        }if(tv.getText().toString().equals("4")){
            if(colorAnterior[3]!=verde && color!=noesta)
            b4.setBackgroundColor(color);
            else if(colorAnterior[3]==0 && color==noesta)
                b4.setBackgroundColor(noesta);
            colorAnterior[3]=color;
        }if(tv.getText().toString().equals("5")){
            if(colorAnterior[4]!=verde && color!=noesta)
            b5.setBackgroundColor(color);
            else if(colorAnterior[4]==0 && color==noesta)
                b5.setBackgroundColor(noesta);
            colorAnterior[4]=color;
        }if(tv.getText().toString().equals("6")){
            if(colorAnterior[5]!=verde && color!=noesta)
            b6.setBackgroundColor(color);
            else if(colorAnterior[5]==0 && color==noesta)
                b6.setBackgroundColor(noesta);
            colorAnterior[5]=color;
        }if(tv.getText().toString().equals("7")){
            if(colorAnterior[6]!=verde && color!= noesta)
            b7.setBackgroundColor(color);
            else if(colorAnterior[6]==0 && color== noesta)
                b7.setBackgroundColor(noesta);
            colorAnterior[6]=color;
        }if(tv.getText().toString().equals("8")){
            if(colorAnterior[7]!=verde && color!= noesta)
            b8.setBackgroundColor(color);
            else if(colorAnterior[7]==0 && color== noesta)
                b8.setBackgroundColor(noesta);
            colorAnterior[7]=color;
        }if(tv.getText().toString().equals("9")){
            if(colorAnterior[8]!=verde && color!= noesta)
            b9.setBackgroundColor(color);
            else if(colorAnterior[8]==0 && color== noesta)
                b9.setBackgroundColor(noesta);
            colorAnterior[8]=color;
        }if(tv.getText().toString().equals("0")){
            if(colorAnterior[9]!=verde && color!= noesta)
            b0.setBackgroundColor(color);
            else if(colorAnterior[9]==0 && color== noesta)
                b0.setBackgroundColor(noesta);
            colorAnterior[9]=color;
        }if(tv.getText().toString().equals("+")){
            if(colorAnterior[10]!=verde && color!= noesta)
            bmas.setBackgroundColor(color);
            else if(colorAnterior[10]==0 && color== noesta)
                bmas.setBackgroundColor(noesta);
            colorAnterior[10]=color;
        }if(tv.getText().toString().equals("-")){
            if(colorAnterior[11]!=verde && color!= noesta)
            bmenos.setBackgroundColor(color);
            else if(colorAnterior[11]==0 && color== noesta)
                bmenos.setBackgroundColor(noesta);
            colorAnterior[11]=color;
        }if(tv.getText().toString().equals("*")){
            if(colorAnterior[12]!=verde && color!= noesta)
            bpor.setBackgroundColor(color);
            else if(colorAnterior[12]==0 && color== noesta)
                bpor.setBackgroundColor(noesta);
            colorAnterior[12]=color;
        }
    }

}