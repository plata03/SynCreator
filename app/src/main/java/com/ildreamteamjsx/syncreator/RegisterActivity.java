package com.ildreamteamjsx.syncreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText user = findViewById(R.id.username);
        EditText pwd = findViewById(R.id.password);
        EditText nametxt = findViewById(R.id.name);
        EditText surnametxt = findViewById(R.id.surname);
        Button registerBtn = findViewById(R.id.registerPage_btn);
        CheckBox gamesCheck = findViewById(R.id.checkGames);
        CheckBox artCheck = findViewById(R.id.checkArt);
        CheckBox videoCheck = findViewById(R.id.checkVideo);
        CheckBox audioCheck = findViewById(R.id.checkAudio);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        registerBtn.setOnClickListener(v -> {
            String username = user.getText().toString();
            String password = pwd.getText().toString();
            String name = nametxt.getText().toString();
            String surname = surnametxt.getText().toString();
            ArrayList<String> skills = new ArrayList<>();

            if(username.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                Toast.makeText(this, "Full name, username and password are required.", Toast.LENGTH_LONG).show();
            } else {
                db.collection("Utente")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    boolean hasFound = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(username.equals(document.getString("username"))) {
                                            hasFound = true;
                                        }
                                    }

                                    if(hasFound) {
                                        Toast.makeText(RegisterActivity.this, "Username already exists.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Map<String, Object> newUser = new HashMap<>();
                                        newUser.put("name", name);
                                        newUser.put("surname", surname);
                                        newUser.put("username", username);
                                        newUser.put("password", password);
                                        if(gamesCheck.isChecked()) {
                                            skills.add("Games");
                                        }
                                        if(artCheck.isChecked()) {
                                            skills.add("Art");
                                        }
                                        if(videoCheck.isChecked()) {
                                            skills.add("Video");
                                        }
                                        if(audioCheck.isChecked()) {
                                            skills.add("Audio");
                                        }
                                        newUser.put("skills", skills);

                                        db.collection("Utente")
                                                .add(newUser)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        Intent intento = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intento);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                    }

                                }
                            }
                        });









            }
        });

    }
}