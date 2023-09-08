package com.rick.lecturas.ui.slideshow;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rick.lecturas.R;
import com.rick.lecturas.ui.gallery.GalleryFragment;
import com.rick.lecturas.ui.home.AdapterLibro;
import com.rick.lecturas.ui.home.LibroCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolderUser> {
    ArrayList<UserCard> listaUsers;
    private FirebaseFirestore bd = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private String user;
    private Context context;


    public AdapterUser(ArrayList<UserCard> listaUsers, Context context) {
        this.listaUsers = listaUsers;
        this.context = context;
    }
    @NonNull
    @Override
    public AdapterUser.ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, null, false);
        return new AdapterUser.ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUser.ViewHolderUser holder, int position) {
        holder.correoEt.setText(listaUsers.get(position).getCorreo());
        holder.fotouserEt.setImageResource(listaUsers.get(position).getFoto());

        StorageReference str = storage.getReferenceFromUrl("gs://lecturas-fe182.appspot.com/profiles/" + holder.correoEt.getText().toString());
        str.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.fotouserEt);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //error
            }
        });

        bd.collection("amigos").whereEqualTo("user1",user).whereEqualTo("amigo2",holder.correoEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    System.out.println("-------------------------------1");
                    if (!task.getResult().getDocuments().isEmpty()) {
                        System.out.println("-------------------------------------2");
                        holder.addEt.setBackgroundResource(R.drawable.boton_green);
                    }
                    else{
                        holder.addEt.setBackgroundResource(R.drawable.round_buttons);
                    }
                }
            }
        });

        holder.addEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bd.collection("amigos").whereEqualTo("user1",user).whereEqualTo("amigo2",holder.correoEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()) {
                                Toast.makeText(view.getContext(), "\"" + holder.correoEt.getText().toString() + "\" añadido a amigos", Toast.LENGTH_SHORT).show();
                                //base de datos para almacenar que usuario ha guardado a quien en amigos y cambiar color del boton añadir
                                HashMap mapUserUser = new HashMap();
                                mapUserUser.put("user1", user);
                                mapUserUser.put("amigo2", holder.correoEt.getText().toString());
                                bd.collection("amigos").document(user + holder.correoEt.getText().toString()).set(mapUserUser);
                                bd.collection("amigos").whereEqualTo("user1", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        System.out.println("***********************************1");
                                        bd.collection("amigos").whereEqualTo("amigo2", holder.correoEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                System.out.println("***********************************2");
                                                holder.addEt.setBackgroundResource(R.drawable.boton_green);
                                            }
                                        });
                                    }
                                });
                            } else {
                                bd.collection("amigos").document(user + holder.correoEt.getText().toString()).delete();
                                bd.collection("amigos").whereEqualTo("user1", user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        bd.collection("amigos").whereEqualTo("amigo2", holder.correoEt.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                holder.addEt.setBackgroundResource(R.drawable.round_buttons);
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        holder.infoEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager m = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                FragmentTransaction t = m.beginTransaction();
                GalleryFragment g = new GalleryFragment();
                //g.get
                t.replace(R.id.nav_host_fragment_content_homee, g);
                t.addToBackStack(null);
                GalleryFragment.enviarFragmentoPerfil(holder.correoEt.getText().toString());
                //ocultar barra de arriba
                AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
                if (activity.getSupportActionBar() != null) {
                    activity.getSupportActionBar().hide();
                }
                t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                t.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaUsers.size();
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {
        TextView correoEt;
        ImageView fotouserEt;
        ImageButton addEt, infoEt;
        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            correoEt = (TextView) itemView.findViewById(R.id.correocard);
            fotouserEt = (ImageView) itemView.findViewById(R.id.fotousercard);
            addEt = (ImageButton) itemView.findViewById(R.id.addUserButton);
            infoEt = (ImageButton) itemView.findViewById(R.id.infoButtonUser);

        }
    }

    public void enviarAdapterUSer(String correo){
        this.user = correo;
    }
}


