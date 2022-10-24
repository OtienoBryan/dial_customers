package com.topline.hub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class WineCategory extends AppCompatActivity {

    LinearLayout ln11, ln2, ln3, ln4, ln5, ln7, ln8, ln9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine_category);

        ln11 = (LinearLayout) findViewById(R.id.ln11);
        ln2 = (LinearLayout) findViewById(R.id.ln2);
        ln3 = (LinearLayout) findViewById(R.id.ln3);
        ln4 = (LinearLayout) findViewById(R.id.ln4);
        ln5 = (LinearLayout) findViewById(R.id.ln5);
        ln7 = (LinearLayout) findViewById(R.id.ln7);
        ln8 = (LinearLayout) findViewById(R.id.ln8);
        ln9 = (LinearLayout) findViewById(R.id.ln9);

        ln11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "1";
                final String cat_id = "1";
                final String name = "White Wines";

                openCategory(id, cat_id, name);
            }
        });

        ln2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "2";
                final String cat_id = "2";
                final String name = "Red Wines";

                openCategory(id, cat_id, name);
            }
        });

        ln3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "3";
                final String cat_id = "3";
                final String name = "Rose' Wines";

                openCategory(id, cat_id, name);
            }
        });
        ln4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "4";
                final String cat_id = "4";
                final String name = "Sparkling Wines";

                openCategory(id, cat_id, name);
            }
        });
        ln5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "5";
                final String cat_id = "5";
                final String name = "Red Sweet Wines";

                openTaste(id, cat_id, name);
            }
        });

        ln7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "7";
                final String cat_id = "7";
                final String name = "Red Dry Wines";

                openTaste(id, cat_id, name);
            }
        });

        ln8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "8";
                final String cat_id = "8";
                final String name = "White Sweet Wines";

                openTaste(id, cat_id, name);
            }
        });

        ln9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = "9";
                final String cat_id = "9";
                final String name = "White Dry Wines";

                openTaste(id, cat_id, name);
            }
        });


    }

    private void openCategory(String id, String cat_id, String name){

        Intent i = new Intent(WineCategory.this, Wines.class);

        //PACK DATA
        i.putExtra("ID_KEY", id);
        i.putExtra("CAT_ID", cat_id);
        i.putExtra("CAT_NAME", name);

        startActivity(i);

    }

    private void openTaste(String id, String cat_id, String name){

        Intent i = new Intent(WineCategory.this, WineTaste.class);

        //PACK DATA
        i.putExtra("ID_KEY", id);
        i.putExtra("CAT_ID", cat_id);
        i.putExtra("CAT_NAME", name);

        startActivity(i);

    }
}