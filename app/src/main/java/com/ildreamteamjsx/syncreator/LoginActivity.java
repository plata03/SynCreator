package com.ildreamteamjsx.syncreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText user = findViewById(R.id.username);
        EditText pwd = findViewById(R.id.password);
        Button loginBtn = findViewById(R.id.loginPage_btn);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(v -> {

            String username = user.getText().toString();
            String password = pwd.getText().toString();


            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username and password are required.", Toast.LENGTH_LONG).show();
            } else {
                db.collection("Utente")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                boolean hasFound = false;
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(username.equals(document.getString("username")) && password.equals(document.getString("password"))) {
                                            hasFound = true;
                                            Intent intento = new Intent(LoginActivity.this, HomeActivity.class);
                                            intento.putExtra("username", username);
                                            intento.putExtra("password", password);
                                            intento.putExtra("name", document.getString("name"));
                                            intento.putExtra("surname", document.getString("surname"));
                                            //intento.putExtra("skills", document.getString("skills"));
                                            startActivity(intento);
                                        }
                                    }
                                    if(!hasFound) {
                                        Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
}