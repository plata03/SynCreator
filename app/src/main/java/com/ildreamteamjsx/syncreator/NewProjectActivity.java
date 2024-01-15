package com.ildreamteamjsx.syncreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        Button newProjBtn = findViewById(R.id.newProject_btn);
        EditText newTitleTxt = findViewById(R.id.new_title);
        EditText newDescTxt = findViewById(R.id.new_desc);
        RadioButton rdGames = findViewById(R.id.radio_games);
        RadioButton rdArt = findViewById(R.id.radio_art);
        RadioButton rdVideo = findViewById(R.id.radio_video);
        RadioButton rdAudio = findViewById(R.id.radio_audio);

        String owner = getIntent().getStringExtra("nickname");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        newProjBtn.setOnClickListener(v -> {
            String newTitle = newTitleTxt.getText().toString();
            String newDesc = newDescTxt.getText().toString();
            if(newTitle.isEmpty()) {
                Toast.makeText(this, "Title of project is required.", Toast.LENGTH_LONG).show();
            } else {
                if(!rdGames.isChecked() && !rdAudio.isChecked() && !rdArt.isChecked() && !rdVideo.isChecked()) {
                    Toast.makeText(this, "Category of project is required.", Toast.LENGTH_LONG).show();
                } else {
                    // Create a new user with a first, middle, and last name
                    Map<String, Object> project = new HashMap<>();
                    project.put("title", newTitle);
                    project.put("desc", newDesc);
                    project.put("by", owner);
                    if(rdGames.isChecked()) {
                        project.put("category", "Games");
                    }
                    if(rdArt.isChecked()) {
                        project.put("category", "Art");
                    }
                    if(rdVideo.isChecked()) {
                        project.put("category", "Video");
                    }
                    if(rdAudio.isChecked()) {
                        project.put("category", "Audio");
                    }

// Add a new document with a generated ID
                    db.collection("Project")
                            .add(project)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                    newTitleTxt.setText("");
                                    newDescTxt.setText("");
                                    if(rdGames.isChecked()) {
                                        rdGames.setActivated(false);
                                    }
                                    if(rdArt.isChecked()) {
                                        rdArt.setActivated(false);
                                    }
                                    if(rdVideo.isChecked()) {
                                        rdVideo.setActivated(false);
                                    }
                                    if(rdAudio.isChecked()) {
                                        rdAudio.setActivated(false);
                                    }

                                    DialogInterface.OnClickListener listener = (dialog, which) -> {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                Log.d("Listener", "Premuto conferma");
                                                break;
                                        }

                                    };

                                    new AlertDialog.Builder(NewProjectActivity.this)
                                            .setTitle("Good job!")
                                            .setMessage("Your new project was successfully uploaded")
                                            .setPositiveButton("Okay, thanks!", listener)
                                            .show();


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
        });
    }
}