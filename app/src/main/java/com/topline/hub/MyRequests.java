package com.topline.hub;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MyRequests extends AppCompatActivity {

    private final int MY_PERMISSIONS_CALL= 7;
    LinearLayout call;
    Button comment, pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);


        //RECEIVE INTENT
        Intent i = this.getIntent();
        final String id = i.getExtras().getString("ID_KEY");
        String outlet_name = i.getExtras().getString("NAME_KEY");
        String outlet_id = i.getExtras().getString("OUTLET_ID_KEY");
        String status = i.getExtras().getString("STATUS_KEY");

        setTitle("Request : #" + id);
        comment = (Button) findViewById(R.id.btnComment);
        pay = (Button) findViewById(R.id.pay);

        if(status.equals("Completed")){
            comment.setVisibility(View.GONE);
        }



//        comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(MyRequests.this, BidcoActivity.class));
//
//            }
//        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailActivity(id);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(id);
            }
        });




    }

    private void openDetailActivity(String id) {
        Intent i = new Intent(this, BidcoActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", id);

        this.startActivity(i);


    }

    private void pay(String id) {
        Intent i = new Intent(this, Pay.class);

        //PACK DATA
        i.putExtra("ID_KEY", id);

        this.startActivity(i);


    }
}