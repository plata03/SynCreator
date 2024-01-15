package com.ildreamteamjsx.syncreator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class ProjectDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        TextView projTitle = findViewById(R.id.proj_title);
        TextView projOwner = findViewById(R.id.proj_owner);
        TextView projCategory = findViewById(R.id.proj_category);
        TextInputEditText projDesc = findViewById(R.id.proj_desc);

        String whichCategory = getIntent().getStringExtra("category");
        colorText(whichCategory, projCategory);


        projTitle.setText(getIntent().getStringExtra("title"));
        projOwner.setText("by "+getIntent().getStringExtra("by"));
        projCategory.setText(whichCategory);
        projDesc.setText(getIntent().getStringExtra("desc"));
    }

    public void colorText (String category, TextView projCategory) {

        if(category.equals("Games")) {
            projCategory.setTextColor(getResources().getColor(R.color.c_games));
        }
        if(category.equals("Art")) {
            projCategory.setTextColor(getResources().getColor(R.color.c_art));
        }
        if(category.equals("Video")) {
            projCategory.setTextColor(getResources().getColor(R.color.c_video));
        }
        if(category.equals("Audio")) {
            projCategory.setTextColor(getResources().getColor(R.color.c_audio));
        }
    }
}