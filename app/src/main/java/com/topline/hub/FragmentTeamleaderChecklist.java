package com.topline.hub;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTeamleaderChecklist extends Fragment {

    private ConnectionDetector cd;
    public static String currentTime;
    private Context context;
    private AlertDialog addDialog = null;
    private AlertDialog addDeliveryDialog = null;
    private Button btnMpa,btnAvailability,btnVisibility,btnOrder,btnDelivery,btnCheckout;
    private ProgressDialog progressDialog, checkOutProgress;

    @SuppressLint("ValidFragment")
    public FragmentTeamleaderChecklist(Context context) {
        this.context = context;
    }

    public FragmentTeamleaderChecklist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_teamleader_checklist, container, false);

        btnMpa = (Button) view.findViewById(R.id.btnTeamleaderMpa);
        btnAvailability = (Button) view.findViewById(R.id.btnTeamleaderNPL);
        btnDelivery = (Button) view.findViewById(R.id.btnTeamleaderDelivery);
        btnOrder = (Button) view.findViewById(R.id.btnTeamleaderOrder);
        btnCheckout = (Button) view.findViewById(R.id.btnTeamleaderCheckOut);
        btnVisibility = (Button) view.findViewById(R.id.btnTeamleaderVisibility);

        btnMpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(context.getApplicationContext(), ProductCategoryActivity.class);
               startActivity(intent);
            }
        });


        btnAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), MySos.class);
                startActivity(intent);
            }
        });
        btnVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ExpiryProductViewActivity.class);
                startActivity(intent);
            }
        });


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), AssetTrackerActivity.class);
                startActivity(intent);
            }
        });
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deliveryDialog();
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave();
            }
        });


        checkOutProgress = new ProgressDialog(context);
        checkOutProgress.setMessage("Please Wait... You are being checked Out");
        cd = new ConnectionDetector(context);
        currentTime = DateFormat.getTimeInstance().format(new Date());


        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Submitting ....");
        return view;
    }


    private void loadSpinner(Spinner spinner, ArrayList<String> arrayList) {
        ArrayAdapter<String> spnAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayList);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spnAdapter);
    }

    private void orderDialog(){
        final View addView = getLayoutInflater().inflate(R.layout.view_teamleader_order, null);
        final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(addView);

        final EditText edtRemark = (EditText) addView.findViewById(R.id.edtOrderRemark);
        final Spinner spnAchieve = (Spinner) addView.findViewById(R.id.spnOrderMade);
        final Button btnAdd = (Button) addView.findViewById(R.id.btnOrderReport);
        final Button btnClose = (Button) addView.findViewById(R.id.btnOrderClose);

        edtRemark.setText("");

        ArrayList<String> options = new ArrayList<>();
        options.clear();
        options.add("Yes");
        options.add("No");
        loadSpinner(spnAchieve,options);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtRemark.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Your remark is required", Toast.LENGTH_SHORT).show();
                }else{
                    submitOrder(edtRemark.getText().toString(),spnAchieve.getSelectedItem().toString(), addDialog);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.dismiss();
            }
        });

        try {

            addDialog = builder.create();
            addDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            final ViewGroup parentViewGroup = (ViewGroup) addView.getParent();
            parentViewGroup.removeView(addView);
            addDialog = builder.create();
            addDialog.show();
        }

    }

    private void deliveryDialog(){
        final View addView = getLayoutInflater().inflate(R.layout.view_teamleader_delivery, null);
        final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(addView);

        final EditText edtRemark = (EditText) addView.findViewById(R.id.edtDeliveryRemark);
        final Spinner spnAchieve = (Spinner) addView.findViewById(R.id.spnDeliveryMade);
        final Button btnAdd = (Button) addView.findViewById(R.id.btnDeliveryReport);
        final Button btnClose = (Button) addView.findViewById(R.id.btnDeliveryClose);

        edtRemark.setText("");

        ArrayList<String> options = new ArrayList<>();
        options.clear();
        options.add("Yes");
        options.add("No");
        loadSpinner(spnAchieve,options);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtRemark.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(context, "Your remark is required", Toast.LENGTH_SHORT).show();
                }else{
                    submitDelivery(edtRemark.getText().toString(),spnAchieve.getSelectedItem().toString(),addDeliveryDialog);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeliveryDialog.dismiss();
            }
        });

        try {

            addDeliveryDialog = builder.create();
            addDeliveryDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            final ViewGroup parentViewGroup = (ViewGroup) addView.getParent();
            parentViewGroup.removeView(addView);
            addDeliveryDialog = builder.create();
            addDeliveryDialog.show();
        }

    }

    private String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void submitOrder(final String remark, final String report, final AlertDialog dialog){
        final String odate = getDate();
        final String userId = SharedPrefManager.getInstance(context).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(context).getUsername()+ " " + SharedPrefManager.getInstance(context).getUserLastname();
        final String outlet = SharedPrefManager.getInstance(context).getOutletName().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REPORT_ORDER,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occurred Try again", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("outlet_name", outlet);
                params.put("remark", remark);
                params.put("report", report);
                params.put("date", odate);

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void submitDelivery(final String remark, final String report, final AlertDialog dialog){
        final String odate = getDate();
        final String userId = SharedPrefManager.getInstance(context).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(context).getUsername()+ " " + SharedPrefManager.getInstance(context).getUserLastname();
        final String outlet = SharedPrefManager.getInstance(context).getOutletName().trim();


        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REPORT_DELIVERY,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Report submitted.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occurred Try again", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("outlet_name", outlet);
                params.put("remark", remark);
                params.put("report", report);
                params.put("date", odate);

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void leave(){
        if(SharedPrefManager.getInstance(context).isMpa()) {
            checkoutDialog();
        }else{
            Toast.makeText(context, "The MPA Availability report is required.", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkout(){

        SharedPrefManager.getInstance(context);
        final String user_id = SharedPrefManager.getUserId().toString();
        final String outlet_id = SharedPrefManager.getInstance(context).getOutletId().toString(); // Outlet Id is the appointment ID.
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

                                SharedPrefManager.getInstance(context).doneMPA();

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
                params.put("appoint_id", outlet_id);
                params.put("currentTime", current_time);
                Log.e("outlet","="+outlet_id);

                return params;

            }
        };


        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void checkoutDialog(){
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
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

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
