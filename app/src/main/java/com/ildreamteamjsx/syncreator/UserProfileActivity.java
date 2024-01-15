package com.ildreamteamjsx.syncreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView fullname = findViewById(R.id.fullname);
        TextView username = findViewById(R.id.username);
        Button logout = findViewById(R.id.logoutPage_btn);


        String nameandsurname = getIntent().getStringExtra("name")+" "+getIntent().getStringExtra("surname");
        String nickname = getIntent().getStringExtra("nickname");
        String pwd = getIntent().getStringExtra("password");

        fullname.setText(nameandsurname);
        username.setText(nickname);

        getSkills(nickname, pwd);


        logout.setOnClickListener(v -> {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.d("Listener", "Premuto conferma");
                        Intent intento = new Intent(UserProfileActivity.this, MainActivity.class);
                        startActivity(intento);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.d("Listener", "Premuto annulla");
                        break;
                }

            };

            new AlertDialog.Builder(UserProfileActivity.this)
                    .setTitle("Logging out")
                    .setMessage("Are you sure you want to exit your current account?")
                    .setPositiveButton("Yes.", listener)
                    .setNegativeButton("No.", listener)
                    .show();

        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Project")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<QueryDocumentSnapshot> currentFieldList = new ArrayList<>();
                        int count = 0;
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("by").equals(nickname)) {
                                    count++;
                                    currentFieldList.add(document);
                                }
                            }
                            if(count == 0) {

                            } else {
                                /*Log.d("Firestore", "Collection: "+currentFieldList.get(0).getString("title"));
                                Log.d("Firestore", "Collection: "+currentFieldList.get(1).getString("title"));
                                Log.d("Firestore", "Collection: "+currentFieldList.get(2).getString("title"));*/
                                RecyclerView recyclerView = findViewById(R.id.recycler_view_projects);
                                ProjectsOwnerAdapter projectAdapter = new ProjectsOwnerAdapter(nickname, count);
                                recyclerView.setAdapter(projectAdapter);

                                projectAdapter.OnItemClickListener((view, position) -> {
                                    //Log.d("Firestore", "Collection after click: "+currentFieldList.get(0).getString("title"));
                                    //Log.d("Firestore", "Collection after click: "+currentFieldList.get(1).getString("title"));
                                    //Log.d("Firestore", "Collection after click: "+currentFieldList.get(2).getString("title"));
                                    //Log.d("Firestore", "Collection SELECTED: "+currentFieldList.get(position).getString("title"));
                                    //Log.e("Firestore", "Invalid position index: " + position);

                                    Intent intento = new Intent(UserProfileActivity.this, ProjectDetailActivity.class);

                                    intento.putExtra("title", currentFieldList.get(position).getString("title"));
                                    intento.putExtra("desc", currentFieldList.get(position).getString("desc"));
                                    intento.putExtra("by", currentFieldList.get(position).getString("by"));
                                    intento.putExtra("category", currentFieldList.get(position).getString("category"));

                                    startActivity(intento);

                                });
                            }



                        }

                    }
                });



        Button deleteBtn = findViewById(R.id.deleteButton);

        deleteBtn.setOnClickListener(v -> {
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        db.collection("Utente")
                                .whereEqualTo("username", nickname)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                db.collection("Utente").document(document.getId()).delete();
                                            }
                                        }
                                    }
                                });
                        Log.d("Listener", "Premuto conferma");
                        Intent intento = new Intent(UserProfileActivity.this, MainActivity.class);
                        startActivity(intento);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.d("Listener", "Premuto annulla");
                        break;
                }

            };

            new AlertDialog.Builder(UserProfileActivity.this)
                    .setTitle("Deleting Account")
                    .setMessage("Are you sure you want to delete your current account? You can't undo this action.")
                    .setPositiveButton("Yes, delete.", listener)
                    .setNegativeButton("No.", listener)
                    .show();
        });



    }

    public void getSkills (String nickname, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Utente")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        TextView allskills = findViewById(R.id.skills);
                        String stringBuilder = "[";
                        if(task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(nickname.equals(document.getString("username")) && password.equals(document.getString("password"))) {
                                    ArrayList<String> skills = (ArrayList<String>) document.get("skills");

                                    for(int i=0; i<skills.size(); i++) {
                                        stringBuilder += skills.get(i)+", ";
                                    }

                                    if (stringBuilder.length() > 2) {
                                        stringBuilder = stringBuilder.substring(0, stringBuilder.length() - 2);
                                    }

                                    stringBuilder += "]";
                                }
                            }
                        }
                        allskills.setText(stringBuilder);
                    }
                });
    }
}