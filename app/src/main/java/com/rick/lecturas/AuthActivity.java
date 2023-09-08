package com.rick.lecturas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rick.lecturas.ui.home.HomeFragment;
import com.rick.lecturas.ui.slideshow.AdapterUser;
import com.rick.lecturas.ui.slideshow.SlideshowFragment;

import java.util.HashMap;

public class AuthActivity extends AppCompatActivity {

    Button registrar;
    Button acceder;
    ImageButton verBoton;
    EditText correo;
    EditText password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_auth);

         registrar = findViewById(R.id.register);
         acceder = findViewById(R.id.acceder);
         correo = findViewById(R.id.editTextEmailAddress);
         password = findViewById(R.id.editTextPassword);
         verBoton = findViewById(R.id.verboton);

        //setup
        setup();
    }

    private void setup(){
        setTitle("Autenticación");
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(!correo.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                showHome(task.getResult().getUser().getEmail());

                                HashMap mapCorreo = new HashMap();
                                mapCorreo.put("correo", correo.getText().toString());
                                FirebaseFirestore.getInstance().collection("users").document(correo.getText().toString()).set(mapCorreo);
                                HashMap mapUserUser = new HashMap();
                                mapUserUser.put("user1", correo.getText().toString());
                                mapUserUser.put("amigo2",correo.getText().toString() );
                                FirebaseFirestore.getInstance().collection("amigos").document(correo.getText().toString()).set(mapUserUser);

                            }
                            else{
                                showAlert();
                            }
                        }
                    });
                }*/
                                Intent intent = new Intent(AuthActivity.this, RegisterActivity.class);
                                startActivity(intent);
            }
        });

        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!correo.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(correo.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                showHome(task.getResult().getUser().getEmail());
                            }
                            else{
                                showAlert();
                            }
                        }
                    });
                }
            }
        });

        verBoton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(String correo){
        HomeFragment.enviarFragmentoLibros(correo);
        SlideshowFragment.enviarFragmentoUsers(correo);
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("correo",correo);
        startActivity(homeIntent);
    }
}

 //FirebaseAuth.getInstance().signOut();
        // onBackPressed();