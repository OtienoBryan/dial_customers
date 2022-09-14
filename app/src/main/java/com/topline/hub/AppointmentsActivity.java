package com.topline.hub;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentsActivity extends AppCompatActivity {

    public static String user_id;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Appointments> appoint;
    private AppointmentsAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    AlertDialog addDialog = null;


    private ArrayList<String> daysList = new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>();

    private ArrayList<String> outletList = new ArrayList<>();
    private ArrayList<String> outletIdList = new ArrayList<>();
    private ArrayList<String> outletTime = new ArrayList<>();
    private List<OutletModel> outlets;




    private EditText editSearch;
    String userId = "";
    String adminId = "";
    String dayOfTheWeek = "";

    private int mYear, mMonth, mDay;
    Button btnApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("My Orders");

        progressBar = (ProgressBar)findViewById(R.id.progress);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        btnApprove = (Button) findViewById(R.id.btnApprove);

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();

        editSearch = (EditText)findViewById(R.id.textSearch);

      userId = SharedPrefManager.getInstance(this).getUserId().toString();
      adminId = SharedPrefManager.getInstance(this).getUserUnit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait! Posting ...");

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppointmentsActivity.this,ApprovalAppointment.class));
            }
        });


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String currentDate = sdf.format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);



        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  final Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        String myFormat = "yyyy-MM-dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                        editSearch.setText(sdf.format(myCalendar.getTime()));


                        //updateLabel();
                    }

                };

                new DatePickerDialog(AppointmentsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();   */

            }
        });




        editSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable newText) {
                //after the change calling the method and passing the search input
                //filter(editable.toString());
                fetchAppoints( newText.toString(), userId, adminId);
            }
        });

       // currentDate   to be used on as a key based on

        fetchAppoints(dayOfTheWeek,userId,adminId);
       // fetchAppoints("",userId);
        fetchOutlet(userId);
        // fetchRegion();
        //fetchTeam();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appointment_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.menu_appoint_sync:
//                fetchAppoints(dayOfTheWeek,userId,adminId);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public void fetchAppoints(String key,String userId,String adminId){

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<Appointments>> call = apiInterface.getAppointments(key,userId,adminId);

        call.enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Appointments>> call, Response<List<Appointments>> response) {

                progressBar.setVisibility(View.GONE);

                appoint = response.body();
                adapter = new AppointmentsAdapter(appoint, AppointmentsActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (appoint.size() > 0) {
                    Toast.makeText(AppointmentsActivity.this, "Records updated.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppointmentsActivity.this, "No updates found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Appointments>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(AppointmentsActivity.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

            }
        });


    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchAppoints(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchAppoints( newText);
                return false;
            }
        });
        return true;
    }*/

    public void fetchOutlet(String userId){
        daysList.clear();
        daysList.add("Sunday");
        daysList.add("Monday");
        daysList.add("Tuesday");
        daysList.add("Wednesday");
        daysList.add("Thursday");
        daysList.add("Friday");
        daysList.add("Saturday");



        regionList.clear();
        regionList.add("Nairobi");
        regionList.add("Mombasa");
        regionList.add("Kisumu");
        regionList.add("Nakuru");





        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<OutletModel>> callb = apiInterface.getOutlets(userId);

        callb.enqueue(new Callback<List<OutletModel>>() {
            @Override
            public void onResponse(retrofit2.Call<List<OutletModel>> callb, Response<List<OutletModel>> response) {

                progressBar.setVisibility(View.GONE);

                outlets = response.body();
                for (int i =0; i < outlets.size(); i++){
                    outletList.add(outlets.get(i).getOutlet_name());
                    outletIdList.add(outlets.get(i).getOutlet_id());
                    outletTime.add(outlets.get(i).getTime());


                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<OutletModel>> callb, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AppointmentsActivity.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();
            }
        });


   }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
//        else if(id == R.id.menu_appointment_add) {
//
//            addAppointment();
//
////            if(dayOfTheWeek.equals("Friday") || dayOfTheWeek.equals("Saturday") || dayOfTheWeek.equals("Sunday")){
////                addAppointment();
////
////            }else
////                Toast.makeText(AppointmentsActivity.this, "Can not add Journey Plan, Contact Admin", Toast.LENGTH_SHORT).show();
////
//
//
//            }
        else if (id == R.id.menu_appoint_sync){
            fetchAppoints(dayOfTheWeek, userId, adminId);
        }
        return super.onOptionsItemSelected(item);
    }



    private void addAppointment() {


        final View addView = getLayoutInflater().inflate(R.layout.view_add_appointment, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentsActivity.this);
        builder.setView(addView);

        final TextView edtDate = (TextView) addView.findViewById(R.id.edtAddAppointmentDate);
        final TextView edtStartTime = (TextView) addView.findViewById(R.id.edtStartTime);
        final TextView edtEndTime = (TextView) addView.findViewById(R.id.edtEndTime);
        //final TextView txtDate = (TextView) findViewById(R.id.txtTodayDate);
        final EditText edtAgenda = (EditText) addView.findViewById(R.id.edtAddAppointmentAgenda);
        final Spinner spnDay = (Spinner) addView.findViewById(R.id.spnAddAppointmentDay);
        final Spinner spnOutlet = (Spinner) addView.findViewById(R.id.spnAddAppointmentOutlet);
        //final Spinner spnSegment = (Spinner) addView.findViewById(R.id.spnAddAppointmentSegment);
        final Button btnAdd = (Button) addView.findViewById(R.id.btnAddAppointment);
        final Button btnClose = (Button) addView.findViewById(R.id.btnAddAppointmentClose);


        loadSpinner(spnDay,daysList);
       //loadSpinner(spnOutlet,regionList);

        //outlets
        if(outlets != null){
            if(outletList.size() > 0){
                loadSpinner(spnOutlet,outletList);
            }else{
                outletList.add("No outlets at the moment");
                loadSpinner(spnOutlet,outletList);
            }
        }else{
            fetchOutlet(userId);
        }

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        String myFormat = "yyyy/MM/dd"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                        edtDate.setText(sdf.format(myCalendar.getTime()));


                   /*     String currentDate = DateFormat.getDateInstance().format(myCalendar.getTime());
                        EditText textViewDate = findViewById(R.id.text_view_date);
                        textViewDate.setText(currentDate);*/


                        //updateLabel();
                    }

                };



                new DatePickerDialog(AppointmentsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtStartTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();

                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        myCalendar.set(Calendar.MINUTE,minute);



                        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                        edtStartTime.setText(myFormat.format(myCalendar.getTime()));



                    }
                };

                new TimePickerDialog(AppointmentsActivity.this, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();

            }
        });

        edtEndTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();

                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        myCalendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                        edtEndTime.setText(myFormat.format(myCalendar.getTime()));



                    }
                };

                new TimePickerDialog(AppointmentsActivity.this, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),false).show();

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtAgenda.getText().toString().equalsIgnoreCase("") || edtDate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(AppointmentsActivity.this, "Set a date and an agenda.", Toast.LENGTH_SHORT).show();
                } else {
                    postAppointment(edtDate.getText().toString(), spnDay.getSelectedItem().toString(), edtAgenda.getText().toString(),
                            spnOutlet.getSelectedItem().toString(), outletIdList.get(spnOutlet.getSelectedItemPosition()), outletTime.get(spnOutlet.getSelectedItemPosition()), edtStartTime.getText().toString(), edtEndTime.getText().toString());

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

    private void postAppointment(final String jdate, final String day, final String agenda, final String outlet,final String outlet_id,final String outlet_time,final String starttime, final String endtime){
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_ADD_APPOINTMENT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            Log.e("Testing Post","=>"+ response.toString());
                            JSONObject obj =new JSONObject(response);

                            if (!obj.getBoolean("error")){
                                Toast.makeText(
                                        AppointmentsActivity.this,
                                        "Journey plan posted",
                                        Toast.LENGTH_LONG
                                ).show();
                                finish();

                            }else {
                                Toast.makeText(
                                        AppointmentsActivity.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(
                                AppointmentsActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }

        )
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String status = "0";
                if(SharedPrefManager.getInstance(AppointmentsActivity.this).getUserLastname().equalsIgnoreCase("56")){
                    status = "1";
                }else {
                    status = "0";
                }
                Map<String, String> params = new HashMap<>();
                params.put("adminid", SharedPrefManager.getInstance(AppointmentsActivity.this).getUserUnit());
                params.put("userid", SharedPrefManager.getInstance(AppointmentsActivity.this).getUserIdString());
                params.put("name", SharedPrefManager.getInstance(AppointmentsActivity.this).getUsername());
                        //+ " " + SharedPrefManager.getInstance(AppointmentsActivity.this).getUserLastname());
                params.put("outlet", outlet);
                params.put("outlet_id", outlet_id);
                params.put("outlet_time", outlet_time);
                params.put("date", jdate);
                params.put("day", day);
                params.put("agenda", agenda);
                params.put("starttime", starttime);
                params.put("endtime", endtime);
                params.put("role", SharedPrefManager.getInstance(AppointmentsActivity.this).getUserLastname());
                params.put("team", SharedPrefManager.getInstance(AppointmentsActivity.this).getUserTeam());
                params.put("status", status);


                return params;

            }
        };


        RequestHandler.getInstance(AppointmentsActivity.this).addToRequestQueue(stringRequest);
    }


    private void loadSpinner(Spinner spinner, ArrayList<String> arrayList) {
        if(arrayList.size() > 0) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(AppointmentsActivity.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);
        }else{
            arrayList.add("Sync to get outlets");
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(AppointmentsActivity.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);

        }
        //spinner.setOnItemSelectedListener(new changeBackground());

    }
    }
