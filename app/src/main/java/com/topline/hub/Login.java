package com.topline.hub;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ///CHECK IF THE USER IS LOGGED IN
        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        /// THIS TO APPLY THE ACTIVATION ONCE ON APP RUN BEFORE ACTIVATION...
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

//        if (firstStart) {
//
//            setFragment(new ActivationFragment());
//
//        }else{
//            setFragment(new LoginFragment());
//        }

        setFragment(new LoginFragment());

        /// THIS TO APPLY THE ACTIVATION ONCE ON APP RUN BEFORE ACTIVATION...END

    }

    public void exitApp(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You are about to exit the App!..");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        System.exit(0);

                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//    public void onBackPressed(){
//
//        exitApp();
//
//    }


    public void setFragment(Fragment f){

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.frame, f);
        ft.commit();

    }
}
