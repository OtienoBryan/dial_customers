package com.topline.hub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NewSos extends AppCompatActivity {

    private final int MY_PERMISSIONS_CALL= 7;
    LinearLayout call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sos);
        setTitle("Sos Emergency");

        call = (LinearLayout) findViewById(R.id.call);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "0726961710";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                if (ActivityCompat.checkSelfPermission(NewSos.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    //ActivityCompat.requestPermissions(context, new String[] { permission }, requestCode);

                    Toast.makeText(NewSos.this, "Enable call permission in APP Settings", Toast.LENGTH_SHORT).show();
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions

                    return;
                }
                startActivity(intent);

            }
        });
    }
}