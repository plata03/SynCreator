package com.ildreamteamjsx.syncreator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bottoneLogin = findViewById(R.id.login_btn);
        Button bottoneRegister = findViewById(R.id.register_btn);

        bottoneLogin.setOnClickListener(v -> {
            Intent intento = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intento);
        });

        bottoneRegister.setOnClickListener(v -> {
            Intent intento = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intento);
        });


    }
}