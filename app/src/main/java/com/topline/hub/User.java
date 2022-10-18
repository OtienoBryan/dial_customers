package com.topline.hub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class User extends AppCompatActivity {

    LinearLayout lnOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        lnOrder = (LinearLayout) findViewById(R.id.lnOrder);

        lnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this, MyOrders.class);
                startActivity(intent);
            }
        });
    }
}