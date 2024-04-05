package com.topline.hub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class OrderTracking extends AppCompatActivity {

    ImageView emc1, emc2, emc3, check1, check2, check3, shop;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        setTitle("Order Tracking");

        emc1 = (ImageView) findViewById(R.id.emc1);
        emc2 = (ImageView) findViewById(R.id.emc2);
        emc3 = (ImageView) findViewById(R.id.emc3);
        check1 = (ImageView) findViewById(R.id.check1);
        check2 = (ImageView) findViewById(R.id.check2);
        check3 = (ImageView) findViewById(R.id.check3);
        shop = (ImageView) findViewById(R.id.shop);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        String status = i.getExtras().getString("ID_KEY");

        int p_status = Integer.parseInt(status);

        if(p_status == 3){

            check1.setVisibility(View.VISIBLE);
            emc1.setVisibility(View.GONE);

            check2.setVisibility(View.VISIBLE);
            emc2.setVisibility(View.GONE);

        }else if(p_status == 4){
            check1.setVisibility(View.VISIBLE);
            shop.setVisibility(View.VISIBLE);
            emc1.setVisibility(View.GONE);

            check2.setVisibility(View.VISIBLE);
            emc2.setVisibility(View.GONE);

            check3.setVisibility(View.VISIBLE);
            emc3.setVisibility(View.GONE);


        }

    }
}