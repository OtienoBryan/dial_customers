package com.topline.hub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ActivationFragment extends Fragment {

    Activity act;

    private Button activate,register;
    private TextView textLogin, terms;
    private EditText editTextUsername,editTextDevice,editTextDeviceName;

    private ProgressDialog progressDialog;
    String token = "";
    private ApiInterface apiInterface;
    AlertDialog showLoginDialog=null;
    ArrayList<String> accountName = new ArrayList<>();
    ArrayList<String> accountId = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.new_activation, container, false);
        act=getActivity();
        //textLogin = (TextView)v.findViewById(R.id.textLogin);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        activate = v.findViewById(R.id.activateBtn);
        register = v.findViewById(R.id.register);
        terms = v.findViewById(R.id.terms);
        editTextUsername = v.findViewById(R.id.activatePhone);
        editTextDevice = v.findViewById(R.id.activateDevice);
        editTextDeviceName = v.findViewById(R.id.deviceName);

        progressDialog = new ProgressDialog(act);
        progressDialog.setMessage("Please wait...");

        /*textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new LoginFragment());
            }
        });*/

        ///GET THE DEVICE ID
        String android_id = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Android", "Android ID : " + android_id);
        editTextDevice.setText(android_id);

        //GET THE DEVICE NAME AND MODEL
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        //to add to textview

        editTextDeviceName.setText(deviceName);
        //btnIssue= (Button)v.findViewById(R.id.btnActivationIssue);



        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == activate){
                    userActivation();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, Register.class));
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, Terms.class));
            }
        });

//        btnIssue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //issueTracker();
//            }
//        });



        return v;
    }


    private void issueTracker(){
        final View loginView = getLayoutInflater().inflate(R.layout.issue_tracker_login_model, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setView(loginView);
        final EditText edtIssue = (EditText) loginView.findViewById(R.id.edtTrackerLoginIssue);
        final EditText edtName = (EditText) loginView.findViewById(R.id.edtTrackerIssueName);
        final EditText edtPhone = (EditText) loginView.findViewById(R.id.edtTrackerIssuePhone);
        final Spinner spnAccount = (Spinner) loginView.findViewById(R.id.spnTrackerLoginAccount);
        final Button btnSubmit = (Button) loginView.findViewById(R.id.btnIssueLoginSend);
        Button btnCancel = (Button) loginView.findViewById(R.id.btnIssueLoginCancel);
        final boolean[] result = {false};

        retrofit2.Call<List<account_model>> call = apiInterface.getAccounts();

        call.enqueue(new Callback<List<account_model>>() {
            @Override
            public void onResponse(retrofit2.Call<List<account_model>> call, retrofit2.Response<List<account_model>> response) {
                for (int i = 0; i < response.body().size(); i++){
                    accountName.add(response.body().get(i).getName());
                    accountId.add(""+response.body().get(i).getId());
                }
                if (response.body().size() > 0) {
                    ArrayAdapter<String> spnAdapter = new ArrayAdapter(act, R.layout.support_simple_spinner_dropdown_item, accountName);
                    spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnAccount.setAdapter(spnAdapter);
                } else {
                    Toast.makeText(act, "No accounts found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<account_model>> call, Throwable t) {
                Toast.makeText(act, "Error Loading accounts Try again.", Toast.LENGTH_SHORT).show();

            }
        });

//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!edtIssue.getText().equals("") && !edtName.getText().equals("") && !edtPhone.getText().equals("")) {
//                    btnSubmit.setText("Submitting ...");
//                    btnSubmit.setClickable(false);
//                    apiInterface.postIssue("Not logged in",
//                            edtName.getText().toString(),
//                            edtPhone.getText().toString(),
//                            accountId.get(spnAccount.getSelectedItemPosition()),
//                            edtIssue.getText().toString()).enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
//                            btnSubmit.setText("Submit");
//                            btnSubmit.setClickable(true);
//                            if (response.body().equals(null)) {
//                                Toast.makeText(act, "Error! Check your internet connection", Toast.LENGTH_SHORT).show();
//                            } else {
//                                if (response.body().get("status").getAsBoolean()) {
//                                    showLoginDialog.dismiss();
//                                    Toast.makeText(act, response.body().get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(act, "Error! " + response.body().get("message").getAsString(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                            t.printStackTrace();
//                            btnSubmit.setText("Submit");
//                            btnSubmit.setClickable(true);
//                            Toast.makeText(act, "Error! Try again", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(act, "The issue, name of merchandiser and phone number are required", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog.dismiss();
            }
        });

        try {

            showLoginDialog = builder.create();
            showLoginDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            final ViewGroup parentViewGroup = (ViewGroup) loginView.getParent();
            parentViewGroup.removeView(loginView);
            showLoginDialog = builder.create();
            showLoginDialog.show();
        }

    }


    private void userActivation(){
        final String telephone = editTextUsername.getText().toString().trim();
        final String deviceid = editTextDevice.getText().toString().trim();
        final String devicename = editTextDeviceName.getText().toString().trim();
        token = SharedPrefManager.getInstance(act).getDeviceToken();
        //final String token = "xbxnxnxnxnxnxnxjxjxx27282929292";

        progressDialog.show();

        if (token == null) {
            progressDialog.dismiss();
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String timestamp = String.valueOf(df.format("yyyy-MM-dd_hh:mm:ss a", new Date()));
            token = "Token_not_generated_"+timestamp;
//            Toast.makeText(act, "Token not generated", Toast.LENGTH_LONG).show();
//            return;
        }

        Log.e("Data",""+telephone+deviceid+devicename+token);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface.postActivation(deviceid,telephone,devicename,token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if(!response.body().get("error").getAsBoolean()){
                    Toast.makeText(
                            act,
                            response.body().get("message").getAsString(),
                            Toast.LENGTH_LONG
                    ).show();

                    showStartActivation();

                    setFragment(new LoginFragment());
                }else{
                    Toast.makeText(
                            act,
                            response.body().get("message").getAsString(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(act, "An error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                Constants.URL_ACTIVATION,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//
//                        try {
//                            JSONObject obj =new JSONObject(response);
//                            if (!obj.getBoolean("error")){
//                               /* SharedPrefManager.getInstance(act)
//                                        .userActivation(
//
//                                                obj.getString("deviceid")
//                                        );*/
//                                Toast.makeText(
//                                        act,
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();
//
//                                showStartActivation();
//
//                                setFragment(new LoginFragment());
//
//                            }else {
//                                Toast.makeText(
//                                        act,
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();
//                            }
//
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//
//                        Toast.makeText(
//                                act,
//                                "Error Occurred try again",
//                                Toast.LENGTH_LONG
//                        ).show();
//
//                    }
//                }
//
//        ){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("telephone", telephone);
//                params.put("device_id", deviceid);
//                params.put("device_name", devicename);
//                params.put("token", token);
//
//                return params;
//
//            }
//        };
//
//
//        RequestHandler.getInstance(act).addToRequestQueue(stringRequest);
    }


    public void setFragment(Fragment f){

        FragmentManager fManager = getFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.frame, f);
        ft.commit();

    }

    private void showStartActivation() {


        SharedPreferences prefs = act.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        setFragment(new ActivationFragment());
    }


}
