package com.topline.hub;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTeamleaderObjective extends Fragment {

    private Context context;
    private Spinner spn;
    private EditText edtObjective,edtChallenge,edtPlan, edtReco;
    private Button btn, btnCheckout;

    private ConnectionDetector cd;

    public static String currentTime;
    private ProgressDialog progressDialog, checkOutProgress;

    @SuppressLint("ValidFragment")
    public FragmentTeamleaderObjective(Context context) {
        this.context = context;
    }

    public FragmentTeamleaderObjective() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_fragment_teamleader_objective, container, false);
        spn = (Spinner) v.findViewById(R.id.spnObjective);
        edtObjective = (EditText) v.findViewById(R.id.edtObjective);
        edtChallenge = (EditText) v.findViewById(R.id.edtObjectiveChallenge);
        edtPlan = (EditText) v.findViewById(R.id.edtObjectivePlanning);
        edtReco = (EditText) v.findViewById(R.id.edtObjectiveReco);
        btn = (Button) v.findViewById(R.id.btnObjective);
        //btnCheckout = (Button) v.findViewById(R.id.btnTeamleaderCheckOut);

        checkOutProgress = new ProgressDialog(context);
        checkOutProgress.setMessage("Please Wait... You are being checked Out");

        cd = new ConnectionDetector(context);
        //cd = new ConnectionDetector(getApplicationContext());

        ArrayList<String> options = new ArrayList<>();
        options.clear();
        options.add("Yes");
        options.add("No");
        loadSpinner(spn,options);

//        btnCheckout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkoutDialog();
//
//            }
//        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtObjective.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Action is required", Toast.LENGTH_SHORT).show();
                }else if(spn.getSelectedItem().toString().equalsIgnoreCase("No")){
                    if(edtChallenge.getText().toString().equalsIgnoreCase("") || edtPlan.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(context, "Enter the plan", Toast.LENGTH_SHORT).show();
                    }else{
                        submitObjective();
                    }
                }else {
                    submitObjective();
                }
            }
        });

        return v;
    }

    private void submitObjective(){
        final String userId = SharedPrefManager.getInstance(context).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(context).getUsername();
        final String outlet = SharedPrefManager.getInstance(context).getOutletName().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_OBJECTIVE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Submitted successfully.", Toast.LENGTH_SHORT).show();
                        edtChallenge.setText("");
                        edtObjective.setText("");
                        edtPlan.setText("");
                        edtReco.setText("");
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occured Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("outlet_name", outlet);
                params.put("segment", "");
                params.put("objective", edtObjective.getText().toString());
                params.put("challenge", edtChallenge.getText().toString());
                params.put("planning", edtPlan.getText().toString());
                params.put("reco", edtReco.getText().toString());
                params.put("achieve", spn.getSelectedItem().toString());

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void loadSpinner(Spinner spinner, ArrayList<String> arrayList) {
        ArrayAdapter<String> spnAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayList);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spnAdapter);
    }



    private void checkout(){
        currentTime = DateFormat.getTimeInstance().format(new Date());

        SharedPrefManager.getInstance(context);
        final String user_id = SharedPrefManager.getUserId().toString();
        final String appoint_id = SharedPrefManager.getInstance(context).getAppointId().trim(); // the appointment ID.
        //progressDialog.show();
        final String current_time = currentTime;


        checkOutProgress.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkOutProgress.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Toast.makeText(
                                        context,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);


                            }else {
                                Toast.makeText(
                                        context,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkOutProgress.dismiss();

                        Toast.makeText(
                                context,
                                "Error Ocured try again",
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("appoint_id", appoint_id);
                params.put("currentTime", current_time);

                return params;

            }
        };


        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void checkoutDialog(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("You are about to checkout from" + "\n" + SharedPrefManager.getInstance(context).getOutletName());
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        startActivity(new Intent(QuestionsActivity.this, MainActivity.class));

                        checkout();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
