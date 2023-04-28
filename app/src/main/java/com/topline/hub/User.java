package com.topline.hub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class User extends AppCompatActivity {

    LinearLayout lnOrder, ln2;
    TextView user, phone;
    ImageView edit;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final String userId = SharedPrefManager.getInstance(User.this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(User.this).getUsername().trim();
        final String my_phone = SharedPrefManager.getInstance(User.this).getUserTelephone().trim();

        user = (TextView) findViewById(R.id.my_name);
        phone = (TextView) findViewById(R.id.phone);
        lnOrder = (LinearLayout) findViewById(R.id.lnOrder);
        ln2 = (LinearLayout) findViewById(R.id.ln2);
        edit = (ImageView) findViewById(R.id.edit);

        user.setText(userName);
        phone.setText(my_phone);

        lnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this, MyOrders.class);
                startActivity(intent);
            }
        });

        ln2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this, Favourites.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(User.this, UserEdit.class);

                //PACK DATA
                i.putExtra("ID_KEY", userId);
                i.putExtra("USER_NAME", userName);
                i.putExtra("PHONE_KEY", my_phone);


                startActivity(i);
            }
        });


    }

    public void onBackPressed(){
        //super.onBackPressed();

        startActivity(new Intent(User.this, MainActivity.class));
    }


}