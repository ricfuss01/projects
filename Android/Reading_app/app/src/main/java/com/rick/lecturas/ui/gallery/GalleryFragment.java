package com.rick.lecturas.ui.gallery;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.slice.Slice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Values;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rick.lecturas.AuthActivity;
import com.rick.lecturas.HomeActivity;
import com.rick.lecturas.R;
import com.rick.lecturas.databinding.FragmentGalleryBinding;
import com.rick.lecturas.ui.slideshow.AdapterComent;
import com.rick.lecturas.ui.slideshow.AdapterUser;
import com.rick.lecturas.ui.slideshow.ComentCard;
import com.rick.lecturas.ui.slideshow.UserCard;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class GalleryFragment extends DialogFragment {

    private FragmentGalleryBinding binding;
    private FirebaseFirestore bd;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private static String userCorreo;
    private static String userCorreoAntiguo;
    boolean clickGraf = true, clickAmis = true, clickComs = true, clickRetos = true;
    ImageButton botonGrafico, botonAmis, botonComs, botonRetos, botonFoto;
    ImageView fotito;
    LinearLayout linearamigos, lineargraf, linearcoms, linearretos;
    RecyclerView recyclerAmis;
    RecyclerView recyclerComs;
    RecyclerView recyclerRetos;

    TextView t1, t2, t3, t4, t5, t6, t1pr, t2pr, t3pr, t4pr, t5pr, t6pr;

    static int contComents = 0, contAmis = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        t1 = binding.t1;
        t2 = binding.t2;
        t3 = binding.t3;
        t4 = binding.t4;
        t5 = binding.t5;
        t6 = binding.t6;
        t1pr = binding.t1pr;
        t2pr = binding.t2pr;
        t3pr = binding.t3pr;
        t4pr = binding.t4pr;
        t5pr = binding.t5pr;
        t6pr = binding.t6pr;


        linearamigos = binding.lo2;
        lineargraf = binding.lo;
        linearcoms = binding.lo3;
        linearretos = binding.lo4;

        fotito = binding.imageView2;

        bd = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        StorageReference str = storage.getReferenceFromUrl("gs://lecturas-fe182.appspot.com/profiles/" + userCorreo);
        str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(fotito);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //error
            }
        });

        //boton foto
        botonFoto = (ImageButton) binding.fotoo;
        //boton amigos
        botonAmis = (ImageButton) binding.botonAmis;
        //boton de logout
        final FloatingActionButton exit = binding.aAdirLibro;
        //color e icono de logout
        if(userCorreo.equals(userCorreoAntiguo)){
            exit.setBackgroundColor(R.drawable.boton_green);
            exit.setImageResource(R.drawable.baseline_logout_24);
            botonAmis.setVisibility(View.VISIBLE);
            linearamigos.setVisibility(View.VISIBLE);
            botonFoto.setVisibility(View.VISIBLE);
        }
        else{
            exit.setBackgroundColor(R.drawable.round_button_red);
            exit.setImageResource(R.drawable.baseline_arrow_back_24);
            botonAmis.setVisibility(View.GONE);
            linearamigos.setVisibility(View.GONE);
            botonFoto.setVisibility(View.GONE);
        }
        Intent authIntent = new Intent(this.getContext(), AuthActivity.class);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userCorreo.equals(userCorreoAntiguo)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Cerrar sesión");
                    builder.setMessage("¿Quieres cerrar sesión?");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userCorreo=null;
                            FirebaseAuth.getInstance().signOut();
                            startActivity(authIntent);
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    userCorreo=userCorreoAntiguo;
                    FragmentManager m = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                    FragmentTransaction t = m.beginTransaction();
                    GalleryFragment g = new GalleryFragment();
                    //g.get
                    t.replace(R.id.nav_host_fragment_content_homee, g);
                    t.addToBackStack(null);
                    //ocultar barra de arriba
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    activity.getSupportActionBar().show();

                    t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    t.commit();
                }
            }
        });

        TextView correo = (TextView) binding.usuarioCorreo;
        correo.setText(userCorreo.split("@")[0]);
        TextView correoLargo = (TextView) binding.correolargo;
        correoLargo.setText(userCorreo);

        botonGrafico = (ImageButton) binding.botonGraficos;
        ScrollView scrollGraf = (ScrollView) binding.scrollGraf;
        lineargraf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clickGraf){
                    scrollGraf.setVisibility(View.GONE);
                    botonGrafico.setImageResource(R.drawable.baseline_keyboard_arrow_right_24);
                    clickGraf = true;
                }
                else{
                    scrollGraf.setVisibility(View.VISIBLE);
                    botonGrafico.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                    clickGraf = false;
                    if(recyclerComs.getVisibility() != View.GONE){
                        linearcoms.performClick();
                    }
                    if(recyclerAmis.getVisibility() != View.GONE){
                        linearamigos.performClick();
                    }
                    if(recyclerRetos.getVisibility() != View.GONE){
                        linearretos.performClick();
                    }
                }
            }
        });

        System.out.println(userCorreo);
        System.out.println(userCorreoAntiguo);

        //grafico autores leidos
        ArrayList<Bar> values = new ArrayList<Bar>();
        values = crearGrafica("autor","favs", "estado", "leido");
        BarGraph grafica = binding.autorGrafica;
        grafica.setBars(values);

        //grafico generos leidos
        ArrayList<Bar> values1 = new ArrayList<Bar>();
        values1 = crearGrafica("genero","favs", "estado", "leido");
        BarGraph grafica1 = binding.generoGrafica;
        grafica1.setBars(values1);

        //paginas leidos
        ArrayList<PieSlice> slices = new ArrayList<PieSlice>();
        slices = crearGraficaCirculo("paginas", "favs", "estado", "leido");
        PieGraph pie = binding.paginasGrafica;
        pie.setSlices(slices);

        //grafico autores por leer
        ArrayList<Bar> values3 = new ArrayList<Bar>();
        values3 = crearGrafica("autor","favs", "estado", "quieroleer");
        BarGraph grafica3 = binding.autorGrafica2;
        grafica3.setBars(values3);

        //grafico generos por leer
        ArrayList<Bar> values4 = new ArrayList<Bar>();
        values4 = crearGrafica("genero","favs", "estado", "quieroleer");
        BarGraph grafica4 = binding.generoGrafica2;
        grafica4.setBars(values4);

        //paginas por leer
        ArrayList<PieSlice> slices2 = new ArrayList<PieSlice>();
        slices2 = crearGraficaCirculo("paginas", "favs", "estado", "quieroleer");
        PieGraph pie2 = binding.paginasGrafica2;
        pie2.setSlices(slices2);

        //grafico valoraciones
        ArrayList<Bar> values5 = new ArrayList<Bar>();
        values5 = crearGrafica("valorar","coments", "user", userCorreo);
        BarGraph grafica5 = binding.valorarGrafica;
        grafica5.setBars(values5);


        recyclerAmis = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerAmis);
        recyclerAmis.setLayoutManager(new LinearLayoutManager(root.getContext()));
        AdapterUser adapterAmis = new AdapterUser(amigosbbdd(), getContext());
        recyclerAmis.setAdapter(adapterAmis);
        linearamigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clickAmis){
                    recyclerAmis.setVisibility(View.GONE);
                    botonAmis.setImageResource(R.drawable.baseline_keyboard_arrow_right_24);
                    clickAmis = true;
                }
                else{
                    recyclerAmis.setVisibility(View.VISIBLE);
                    botonAmis.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                    clickAmis = false;
                    if(recyclerComs.getVisibility() != View.GONE){
                        linearcoms.performClick();
                    }
                    if(scrollGraf.getVisibility() != View.GONE){
                        lineargraf.performClick();
                    }
                    if(recyclerRetos.getVisibility() != View.GONE){
                        linearretos.performClick();
                    }
                }
            }
        });


        botonComs = (ImageButton) binding.botonComs;
        recyclerComs = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerComs);
        recyclerComs.setLayoutManager(new LinearLayoutManager(root.getContext()));
        AdapterComent adapterComs = new AdapterComent(comentariosbbdd());
        recyclerComs.setAdapter(adapterComs);
        linearcoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clickComs){
                    recyclerComs.setVisibility(View.GONE);
                    botonComs.setImageResource(R.drawable.baseline_keyboard_arrow_right_24);
                    clickComs = true;
                }
                else{
                    recyclerComs.setVisibility(View.VISIBLE);
                    botonComs.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                    clickComs = false;
                    if(scrollGraf.getVisibility() != View.GONE){
                        lineargraf.performClick();
                    }
                    if(recyclerAmis.getVisibility() != View.GONE){
                        linearamigos.performClick();
                    }
                    if(recyclerRetos.getVisibility() != View.GONE){
                        linearretos.performClick();
                    }
                }
            }
        });



        botonRetos = (ImageButton) binding.botonRetos;
        recyclerRetos = (RecyclerView) binding.getRoot().findViewById(R.id.recyclerRetos);
        recyclerRetos.setLayoutManager(new LinearLayoutManager(root.getContext()));
        linearretos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clickRetos){
                    recyclerRetos.setVisibility(View.GONE);
                    botonRetos.setImageResource(R.drawable.baseline_keyboard_arrow_right_24);
                    clickRetos = true;
                }
                else{
                    AdapterReto adapterRetos = new AdapterReto(retos());
                    recyclerRetos.setAdapter(adapterRetos);
                    recyclerRetos.setVisibility(View.VISIBLE);
                    botonRetos.setImageResource(R.drawable.baseline_keyboard_arrow_down_24);
                    clickRetos = false;
                    if(recyclerComs.getVisibility() != View.GONE){
                        linearcoms.performClick();
                    }
                    if(recyclerAmis.getVisibility() != View.GONE){
                        linearamigos.performClick();
                    }
                    if(scrollGraf.getVisibility() != View.GONE){
                        lineargraf.performClick();
                    }
                }
            }
        });

        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Si no tienes permiso, solicítalo al usuario
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA }, 1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //botonFoto.performClick();
                } else {
                    // Si ya tienes permiso, abre la cámara
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
            }
        });

        Button volver = (Button) binding.buttonvolver;
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return root;
        //GRAFICOS
        //autor V, genero V, ficcion/no ficcion, paginas V, mejor valorados por ti V, leidos por mes
        //RETOS

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotito.setImageBitmap(imageBitmap);
            //subir foto
            StorageReference fotosRef = storageRef.child("profiles/"+userCorreo);
            fotito.setDrawingCacheEnabled(true);
            fotito.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) fotito.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = baos.toByteArray();

            UploadTask uploadTask = fotosRef.putBytes(datas);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getContext(), "No se ha podidido guardar la imagen", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
    }







    private ArrayList<RetoCard> retos() {
        ArrayList<RetoCard> lista = new ArrayList<RetoCard>();
        lista.add(new RetoCard("Haz tu primer comentario", R.drawable.round_buttons));
        lista.add(new RetoCard("Haz 5 comentarios", R.drawable.round_buttons));
        lista.add(new RetoCard("Haz 1 amigo", R.drawable.round_buttons));
        lista.add(new RetoCard("Haz 5 amigos", R.drawable.round_buttons));
        lista.add(new RetoCard("guarda tu primer libro en \"mis libros\"", R.drawable.round_buttons));
        lista.add(new RetoCard("guarda 10 libros en \"mis libros\"", R.drawable.round_buttons));
        if(contComents>=1) {
            lista.get(0).setFoto(R.drawable.boton_green);
        }
        if(contComents>=5){
            lista.get(1).setFoto(R.drawable.boton_green);
        }
        if(contAmis >= 1){
            lista.get(2).setFoto(R.drawable.boton_green);
        }
        if(contAmis >= 5){
            lista.get(3).setFoto(R.drawable.boton_green);
        }
        return lista;
    }

    private ArrayList<ComentCard> comentariosbbdd() {
        ArrayList<ComentCard> lista = new ArrayList<ComentCard>();
        contComents = 0;
        bd.collection("coments").whereEqualTo("user", userCorreo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().getDocuments().isEmpty()) {
                        for (QueryDocumentSnapshot document2 : task.getResult()) {
                            lista.add(new ComentCard(document2.get("titulo").toString(), document2.get("autor").toString(), document2.get("comentario").toString(), document2.get("valorar").toString(), document2.get("user").toString(), R.drawable.j));
                            contComents+=1;
                        }
                    }
                }
            }
        });
        return lista;
    }

    private ArrayList<UserCard> amigosbbdd() {
        ArrayList<UserCard> lista = new ArrayList<UserCard>();
        contAmis = 0;
        bd.collection("amigos").whereEqualTo("user1", userCorreo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        bd.collection("users").whereEqualTo("correo", document.get("amigo2").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                        if(!document1.get("correo").toString().equals(userCorreo)) {
                                            lista.add(new UserCard(document1.get("correo").toString(), R.drawable.j));
                                            contAmis += 1;
                                        }
                                    }
                                } else {
                                }
                            }
                        });
                    }
                }
            }
        });
        return lista;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String colorAleatorio(){
        String[] letras = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String color = "#";
        for (int i=0; i<=5; i++){
            color += letras[(int) Math.round(Math.random()*15)];
        }
        return color;
    }

    public ArrayList<Bar> crearGrafica(String tipo, String coleccion, String que, String buscas){
        ArrayList<Bar> values = new ArrayList<Bar>();
        bd.collection(coleccion).whereEqualTo("user",userCorreo).whereEqualTo(que, buscas).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        boolean a = true;
                        for(int i=0; i<values.size(); i++) {

                            if (values.get(i).getName().toString().equals(document.get(tipo).toString()) && a){
                                values.get(i).setValue(values.get(i).getValue()+1);
                                a = false;
                            }
                            else if(a && i==values.size()-1){
                                Bar barra = new Bar();
                                String color = colorAleatorio();
                                barra.setColor(Color.parseColor(color));
                                barra.setName(document.get(tipo).toString());
                                barra.setValue(1);
                                values.add(barra);
                                a = false;
                            }
                        }
                        if(values.size()==0){
                            Bar barra = new Bar();
                            String color = colorAleatorio();
                            barra.setColor(Color.parseColor(color));
                            barra.setName("");
                            barra.setValue(1);
                            values.add(barra);
                        }
                    }
                }
            }
        });
        return values;
    }

    public ArrayList<PieSlice> crearGraficaCirculo(String tipo, String coleccion, String que, String buscas){
        ArrayList<PieSlice> values = new ArrayList<PieSlice>();
        PieSlice p = new PieSlice();
        p.setTitle("menos de 100");
        p.setValue(0);
        values.add(p);
        PieSlice p1 = new PieSlice();
        p1.setValue(0);
        p1.setTitle("100 a 299");
        values.add(p1);
        PieSlice p2 = new PieSlice();
        p2.setValue(0);
        p2.setTitle("300 a 499");
        values.add(p2);
        PieSlice p3 = new PieSlice();
        p3.setValue(0);
        p3.setTitle("500 a 699");
        values.add(p3);
        PieSlice p4 = new PieSlice();
        p4.setValue(0);
        p4.setTitle("700 a 999");
        values.add(p4);
        PieSlice p5 = new PieSlice();
        p5.setValue(0);
        p5.setTitle("más de 1000");
        values.add(p5);
        bd.collection(coleccion).whereEqualTo("user",userCorreo).whereEqualTo(que, buscas).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        bd.collection("libros").whereEqualTo("titulo", document.get("titulo").toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("SuspiciousIndentation")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                        PieSlice slice = new PieSlice();
                                        String color = colorAleatorio();
                                        if(Integer.parseInt(document1.getData().get("paginas").toString()) <100){
                                            values.get(0).setValue(values.get(0).getValue()+1);
                                            values.get(0).setColor(Color.parseColor(color));
                                            if(buscas.equals("leido"))
                                                t1.setTextColor(values.get(0).getColor());
                                            else
                                                t1pr.setTextColor(values.get(0).getColor());
                                        }
                                        else if(Integer.parseInt(document1.getData().get("paginas").toString()) >= 100 &&
                                                Integer.parseInt(document1.getData().get("paginas").toString()) < 300){
                                            values.get(1).setValue(values.get(1).getValue()+1);
                                            values.get(1).setColor(Color.parseColor(color));
                                            if(buscas.equals("leido"))
                                                t2.setTextColor(values.get(1).getColor());
                                            else
                                                t2pr.setTextColor(values.get(1).getColor());
                                        }
                                        else if(Integer.parseInt(document1.getData().get("paginas").toString()) >= 300 &&
                                                Integer.parseInt(document1.getData().get("paginas").toString()) < 500){
                                            values.get(2).setValue(values.get(2).getValue()+1);
                                            values.get(2).setColor(Color.parseColor(color));
                                            if(buscas.equals("leido"))
                                                t3.setTextColor(values.get(2).getColor());
                                            else
                                                t3pr.setTextColor(values.get(2).getColor());
                                        }
                                        else if(Integer.parseInt(document1.getData().get("paginas").toString()) >= 500 &&
                                                Integer.parseInt(document1.getData().get("paginas").toString()) < 700){
                                            values.get(3).setValue(values.get(3).getValue()+1);
                                            values.get(3).setColor(Color.parseColor(color));
                                            if(buscas.equals("leido"))
                                                t4.setTextColor(values.get(3).getColor());
                                            else
                                                t4pr.setTextColor(values.get(3).getColor());
                                        }
                                        else if(Integer.parseInt(document1.getData().get("paginas").toString()) >= 700 &&
                                                Integer.parseInt(document1.getData().get("paginas").toString()) < 1000){
                                            values.get(4).setValue(values.get(4).getValue()+1);
                                            values.get(4).setColor(Color.parseColor(color));
                                            if(buscas.equals("leido"))
                                                t5.setTextColor(values.get(4).getColor());
                                            else
                                                t5pr.setTextColor(values.get(4).getColor());
                                        }
                                        else{
                                            values.get(5).setValue(values.get(5).getValue()+1);
                                            values.get(5).setColor(Color.parseColor(color));
                                            if(buscas.equals("leido"))
                                                t6.setTextColor(values.get(5).getColor());
                                            else
                                                t6pr.setTextColor(values.get(5).getColor());
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        return values;
    }

    public static void enviarFragmentoPerfil(String correo) {
        if(userCorreo!=null){
            userCorreoAntiguo = userCorreo;
            userCorreo = correo;
        }
        else{
            userCorreo = correo;
            userCorreoAntiguo = userCorreo;
        }
    }

}

