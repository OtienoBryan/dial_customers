package com.topline.hub;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PromotionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //// for Images
    //Button btnCamera;
    private ImageView ivImage;
    private ConnectionDetector cd;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname;
    File file;
    //// for Images.........END

    public static String admin_id;

    private int hours,minute, mYear, mMonth, mDay;


    Spinner productCategory, competitorBrand,promotion,effectSales,spnBrand, promotionBrand,display;
    EditText notes,duration, patient, age, location, emergency, hospital, allergies;
    Button btnSubmit;
    Spinner service, gender, careAge, careGender;
    ImageView selected_image_preview;
    TextView start_date,end_date;
    LinearLayout select_start_date, select_end_date, select_photo;

    private ProgressDialog pd, progressDialog;

    private List<String> product_category;
    private List<String> product_category_ids;
    private List<String> competitor_brand;
    private List<String> brands;
    private List<String> brands_id;
    //private List<String> promotion_list;
    private ArrayList<String> promotion_list= new ArrayList<>();

    private ArrayList<String> age_list= new ArrayList<>();

    String mycategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///// initialise the views................
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        setTitle("CareGiving Request");

        productCategory = (Spinner)findViewById(R.id.spProductCategory);
        service = (Spinner)findViewById(R.id.spPromotionBrand);
        location = (EditText) findViewById(R.id.location);
        start_date =(TextView)findViewById(R.id.start_date);
        duration = (EditText) findViewById(R.id.activity);
        patient = (EditText) findViewById(R.id.p_name);
        age = (EditText) findViewById(R.id.age);
        gender = (Spinner)findViewById(R.id.spPromotion);
        careAge = (Spinner)findViewById(R.id.spcareAge);
        careGender = (Spinner)findViewById(R.id.spcareGender);
        notes = (EditText) findViewById(R.id.notes);
        emergency = (EditText) findViewById(R.id.emergency);
        hospital = (EditText) findViewById(R.id.hospitals);
        allergies = (EditText) findViewById(R.id.allergies);



        select_start_date =(LinearLayout) findViewById(R.id.select_start_date);
        select_end_date =(LinearLayout) findViewById(R.id.select_end_date);
       // select_photo =(LinearLayout) findViewById(R.id.select_photo);
        //selected_image_preview =(ImageView) findViewById(R.id.selected_image_preview);

        btnSubmit =(Button) findViewById(R.id.submit_button);

        product_category = new ArrayList<>();
        product_category_ids = new ArrayList<>();
        competitor_brand = new ArrayList<>();
        brands = new ArrayList<>();
        brands_id = new ArrayList<>();
        promotion_list = new ArrayList<>();


        select_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();

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

                        start_date.setText(sdf.format(myCalendar.getTime()));

                    }

                };



                new DatePickerDialog(PromotionActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


//        select_start_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(PromotionActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                start_date.setText( year + "-" +(monthOfYear + 1) +"-" +dayOfMonth );
//
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//
//            }
//        });

//        select_end_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(PromotionActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                end_date.setText(year + "-" +(monthOfYear + 1) +"-" +dayOfMonth);
//
//                            }
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//
//            }
//        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //competitor_brand.clear(); // this list which you hava passed in Adapter for your listview
                //array.notifyDataSetChanged(); // notify to listview for refresh

                //submitCompetitive();

                if (cd.isConnectingToInternet()) {
                    if(patient.getText().toString().trim().equalsIgnoreCase("") || location.getText().toString().trim().equalsIgnoreCase("") ||start_date.getText().toString().trim().equalsIgnoreCase("") || duration.getText().toString().trim().equalsIgnoreCase("")
                    || age.getText().toString().trim().equalsIgnoreCase("") || notes.getText().toString().trim().equalsIgnoreCase("") || emergency.getText().toString().trim().equalsIgnoreCase("") || hospital.getText().toString().trim().equalsIgnoreCase("")
                    || allergies.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(PromotionActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitCompetitive();
                    }

                } else {
                    Toast.makeText(PromotionActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });

       // loadProductCategories();
        loadProducts();
        loadPromotions(gender,promotion_list);
        loadAge(careAge,age_list);
        loadPromotions(careGender,promotion_list);
        //loadPromotions(display,promotion_list);


// TO Set the onclick listener of spiner
        productCategory.setOnItemSelectedListener(PromotionActivity.this);

        //spnBrand.setOnItemSelectedListener(PromotionActivity.this);


        cd = new ConnectionDetector(PromotionActivity.this);

        cd = new ConnectionDetector(getApplicationContext());

        //btnSubmit.setOnClickListener(this);
        //select_photo.setOnClickListener(this);





    }


    //method to load all the Products Categories from database
//    private void loadProductCategories() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Categories...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_FETCH_PRODUCT_CATEGORIES+admin_id,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        JSONObject obj = null;
//                        try {
//                            obj = new JSONObject(response);
//                            if (!obj.getBoolean("error")) {
//                                JSONArray jsonDevices = obj.getJSONArray("productCategories");
//
//                                for (int i = 0; i < jsonDevices.length(); i++) {
//                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    product_category.add(d.getString("name"));
//                                    product_category_ids.add(d.getString("id"));
//                                }
//
//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                        PromotionActivity.this,
//                                        android.R.layout.simple_spinner_dropdown_item,
//                                        product_category);
//
//                                productCategory.setAdapter(arrayAdapter);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//
//        };
//        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
//    }

    //method to load all the Products Categories from database
    private void loadProducts() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Categories...");
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_SERVICE+admin_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("my_brands");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
                                    brands.add(d.getString("name"));
                                    brands_id.add(d.getString("id"));
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        PromotionActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        brands);

                                service.setAdapter(arrayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    //method to load all the Promotions Categories from database
    private void loadPromotions(Spinner spinner, ArrayList<String> arrayList) {

        promotion_list.clear();
        promotion_list.add("MALE");
        promotion_list.add("FEMALE");

        if(arrayList.size() > 0) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(PromotionActivity.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);
        }else{
            arrayList.add("Sync to get records");
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(PromotionActivity.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);

        }
    }

    private void loadAge(Spinner spinner, ArrayList<String> arrayList) {

        age_list.clear();
        age_list.add("22 - 25");
        age_list.add("26 - 30");
        age_list.add("31 - 35");
        age_list.add("36 - 40");
        age_list.add("Above 40");

        if(arrayList.size() > 0) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(PromotionActivity.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);
        }else{
            arrayList.add("Sync to get age");
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(PromotionActivity.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String selectedString;
        String categoryId;

        //2: From the spinner
        //selectedString = spinner.getSelectedItem().toString();
        mycategory = productCategory.getSelectedItem().toString();
        categoryId = product_category_ids.get(position);

        //customerName.setText(chanelId);


//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Dealers...");
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_FETCH_PRODUCT_COMPETITOR+categoryId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        //JSONObject obj = null;
                        try {

                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            // obj = new JSONObject(response);

                            // JSONArray jsonDevices = obj.getJSONArray("channels");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject d = array.getJSONObject(i);
                                competitor_brand.add(d.getString("name"));
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    PromotionActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    competitor_brand);

                            //competitorBrand.setAdapter(arrayAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private void submitCompetitive(){

        //final String c_promotion= promotion.getSelectedItem().toString();
        final String c_service= service.getSelectedItem().toString();
        final String c_location = location.getText().toString().trim();
        final String c_start_date = start_date.getText().toString().trim();
        final String c_duration = duration.getText().toString().trim();
        final String c_patient= patient.getText().toString().trim();
        final String c_gender = gender.getSelectedItem().toString();
        final String c_age = age.getText().toString().trim();
        final String c_note = notes.getText().toString().trim();
        final String c_care_gender= careGender.getSelectedItem().toString();
        final String c_care_age= careAge.getSelectedItem().toString();
        final String c_emergency = emergency.getText().toString().trim();
        final String c_hospital = hospital.getText().toString().trim();
        final String c_allergy = allergies.getText().toString().trim();
        final String image_name = fname;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
//        final String outlet = SharedPrefManager.getInstance(this).getOutletName().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Submitting please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PROMOTION_ACTIVITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(PromotionActivity.this, "Submitted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(PromotionActivity.this, MainActivity.class));
                        PromotionActivity.this.finish();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(PromotionActivity.this, "Error occured Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);
               // params.put("outlet_name", outlet);
                params.put("service", c_service);
                params.put("location", c_location);
                params.put("start_date", c_start_date);
                params.put("duration", c_duration);
                params.put("patient", c_patient);
                params.put("gender", c_gender);
                params.put("age", c_age);
                params.put("note", c_note);
                params.put("care_gender", c_care_gender);
                params.put("care_age", c_care_age);
                params.put("emergency", c_emergency);
                params.put("allergy", c_allergy);
                params.put("hospital", c_hospital);

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



    }









    ///////// BLOCK CODE FOR UPLOADING IMAGES






    @Override
    public void onClick(View v) {

        // competitor_brand.clear(); // this list which you hava passed in Adapter for your listview

        switch (v.getId()) {
            case R.id.select_photo:

                Intent cameraintent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent, 101);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            switch (requestCode) {
                case 101:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            selectedImage = data.getData(); // the uri of the image taken
                            if (String.valueOf((Bitmap) data.getExtras().get("data")).equals("null")) {
                                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            } else {
                                bitmap = (Bitmap) data.getExtras().get("data");
                            }
                            if (Float.valueOf(getImageOrientation()) >= 0) {
                                bitmapRotate = rotateImage(bitmap, Float.valueOf(getImageOrientation()));
                            } else {
                                bitmapRotate = bitmap;
                                bitmap.recycle();
                            }

//                            selected_image_preview.setVisibility(View.VISIBLE);
//                            selected_image_preview.setImageBitmap(bitmapRotate);

//                            Saving image to mobile internal memory for sometime
                            String root = getApplicationContext().getFilesDir().toString();
                            File myDir = new File(root + "/androidlift");
                            myDir.mkdirs();

                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);

//                            Give the file name that u want
                            fname = "impulse" + n + ".jpg";

                            imagepath = root + "/androidlift/" + fname;
                            file = new File(myDir, fname);
                            upflag = true;
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    //    In some mobiles image will get rotate so to correting that this code will help us
    private int getImageOrientation() {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns, null, null, imageOrderBy);

        if (cursor.moveToFirst()) {
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            System.out.println("orientation===" + orientation);
            cursor.close();
            return orientation;
        } else {
            return 0;
        }
    }

    //    Saving file to the mobile internal memory
    private void saveFile(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();
        try {
            FileOutputStream out = new FileOutputStream(destination);
            sourceUri.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
            if (cd.isConnectingToInternet()) {
                new PromotionActivity.DoFileUpload().execute();
            } else {
                Toast.makeText(PromotionActivity.this, "No Internet Connection..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(PromotionActivity.this);
            pDialog.setMessage("wait uploading Image..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(imagepath);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload(Constants.URL_IMAGE_UPLOAD_REPORT, "ftitle", "fdescription", fname);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (upflag) {
//                Toast.makeText(getApplicationContext(), "Uploading Complete", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(CompetitiveActivity.this, QuestionsActivity.class));
//                CompetitiveActivity.this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "Unfortunately file is not Uploaded..", Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
