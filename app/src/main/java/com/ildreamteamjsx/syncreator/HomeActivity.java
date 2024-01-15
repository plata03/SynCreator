package com.ildreamteamjsx.syncreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView userTxt = findViewById(R.id.userTop);


        String nicknameGot = getIntent().getStringExtra("username");
        String nameGot = getIntent().getStringExtra("name");
        String surnameGot = getIntent().getStringExtra("surname");
        String passwordGot = getIntent().getStringExtra("password");
        //ArrayList<String> skills = getIntent().getStringArrayListExtra("skills");


        userTxt.setText(nicknameGot);


        applicaFragment("Search");

        BottomNavigationView nav = findViewById(R.id.nav_bottom);
        nav.setSelectedItemId(R.id.nav_search);

        ImageButton pfp = findViewById(R.id.profile);
        ImageButton addProj = findViewById(R.id.newproj);

        nav.setOnItemSelectedListener(item -> {
            applicaFragment(item.getTitle().toString());
            return true;
        });


        pfp.setOnClickListener(v -> {

            Intent intento = new Intent(HomeActivity.this, UserProfileActivity.class);
            intento.putExtra("nickname", nicknameGot);
            intento.putExtra("name", nameGot);
            intento.putExtra("surname", surnameGot);
            intento.putExtra("password", passwordGot);
            //intento.putExtra("skills", skills);
            startActivity(intento);

        });

        addProj.setOnClickListener(v -> {
            Intent intento = new Intent(HomeActivity.this, NewProjectActivity.class);
            intento.putExtra("nickname", nicknameGot);
            startActivity(intento);
        });

    }

    protected void applicaFragment(String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmento,
                        name.equals("Games") ? GameActivity.class : name.equals("Art") ? ArtActivity.class : name.equals("Video") ? VideoActivity.class : name.equals("Audio") ? AudioActivity.class : SearchActivity.class,
                        null
                )
                .setReorderingAllowed(true)
                //.addToBackStack("name")
                .commit();
    }
}