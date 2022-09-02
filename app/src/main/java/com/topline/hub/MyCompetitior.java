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

public class MyCompetitior extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

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


    Spinner productCategory, competitorBrand,promotion,effectSales,spnBrand;
    EditText notes,quantity,edtRrp,edtPrice,activity,space,price, edtPriceFrom,edtPriceTo;
    Button btnSubmit;
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
    private ArrayList<String> promotion_type= new ArrayList<>();

    String mycategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_competitior);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ///// initialise the views................
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        productCategory = (Spinner)findViewById(R.id.spPromoType);
        competitorBrand = (Spinner)findViewById(R.id.spCompetitorBrand);
        //promotion = (Spinner)findViewById(R.id.spPromotion);
        //spnBrand = (Spinner)findViewById(R.id.spnBrands);

        notes = (EditText) findViewById(R.id.notes);
        //quantity = (EditText) findViewById(R.id.effect_on_sales_quantity);
        edtPriceFrom = (EditText) findViewById(R.id.edtPriceFrom);
        edtPriceTo = (EditText) findViewById(R.id.edtPriceTo);
        activity = (EditText) findViewById(R.id.activity);
        space = (EditText) findViewById(R.id.space);
        //price = (EditText) findViewById(R.id.price);

        start_date =(TextView)findViewById(R.id.start_date);
        end_date =(TextView)findViewById(R.id.end_date);

        select_start_date =(LinearLayout) findViewById(R.id.select_start_date);
        select_end_date =(LinearLayout) findViewById(R.id.select_end_date);
        select_photo =(LinearLayout) findViewById(R.id.select_photo);
        selected_image_preview =(ImageView) findViewById(R.id.selected_image_preview);

        btnSubmit =(Button) findViewById(R.id.submit_button);

        product_category = new ArrayList<>();
        product_category_ids = new ArrayList<>();
        competitor_brand = new ArrayList<>();
        brands = new ArrayList<>();
        brands_id = new ArrayList<>();
        promotion_list = new ArrayList<>();
        promotion_type = new ArrayList<>();


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



                new DatePickerDialog(MyCompetitior.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        select_end_date.setOnClickListener(new View.OnClickListener() {
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

                        end_date.setText(sdf.format(myCalendar.getTime()));

                    }

                };



                new DatePickerDialog(MyCompetitior.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //competitor_brand.clear(); // this list which you have passed in Adapter for your listview
                //array.notifyDataSetChanged(); // notify to listview for refresh

                //submitCompetitive();

                if (cd.isConnectingToInternet()) {
                    if(space.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(MyCompetitior.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    }else {
                        submitCompetitive();
                    }

                } else {
                    Toast.makeText(MyCompetitior.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });

        loadProductCategories();
        loadProducts();
        //loadPromotions(promotion,promotion_list);
        loadPromoType(productCategory,promotion_type);


// TO Set the onclick listener of spiner
        //productCategory.setOnItemSelectedListener(BidcoActivity.this);

        //spnBrand.setOnItemSelectedListener(BidcoActivity.this);


        cd = new ConnectionDetector(MyCompetitior.this);

        cd = new ConnectionDetector(getApplicationContext());

        //btnSubmit.setOnClickListener(this);
        select_photo.setOnClickListener(this);





    }


    //method to load all the Products Categories from database
    private void loadProductCategories() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Categories...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_FETCH_PRODUCT_CATEGORIES+admin_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("productCategories");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
                                    product_category.add(d.getString("name"));
                                    product_category_ids.add(d.getString("id"));
                                }

                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        MyCompetitior.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        product_category);

                                //productCategory.setAdapter(arrayAdapter);
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

    //method to load all the Products Categories from database
    private void loadProducts() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Fetching Categories...");
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_BRANDS+admin_id,
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
                                        MyCompetitior.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        brands);

                                competitorBrand.setAdapter(arrayAdapter);
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
//    private void loadPromotions(Spinner spinner, ArrayList<String> arrayList) {
//
//        promotion_list.clear();
//        promotion_list.add("250ml");
//        promotion_list.add("300ml");
//        promotion_list.add("350ml");
//        promotion_list.add("375ml");
//        promotion_list.add("500ml");
//        promotion_list.add("750ml");
//        promotion_list.add("1000ml");
//
//        if(arrayList.size() > 0) {
//            ArrayAdapter<String> spnAdapter = new ArrayAdapter(BidcoActivity.this, android.R.layout.simple_spinner_item, arrayList);
//            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(spnAdapter);
//        }else{
//            arrayList.add("Sync to get outlets");
//            ArrayAdapter<String> spnAdapter = new ArrayAdapter(BidcoActivity.this, android.R.layout.simple_spinner_item, arrayList);
//            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(spnAdapter);
//
//        }
//    }

    private void loadPromoType(Spinner spinner, ArrayList<String> arrayList) {

        promotion_type.clear();
        promotion_type.add("BUNDLING");
        promotion_type.add("PRICE CUT");
        promotion_type.add("BOGOF");

        if(arrayList.size() > 0) {
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(MyCompetitior.this, android.R.layout.simple_spinner_item, arrayList);
            spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spnAdapter);
        }else{
            arrayList.add("Sync to get outlets");
            ArrayAdapter<String> spnAdapter = new ArrayAdapter(MyCompetitior.this, android.R.layout.simple_spinner_item, arrayList);
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
                                    MyCompetitior.this,
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


        final String c_promoType = productCategory.getSelectedItem().toString();
        final String c_brand = competitorBrand.getSelectedItem().toString();
        final String c_start_date = start_date.getText().toString().trim();
        final String c_end_date = end_date.getText().toString().trim();
        final String c_space = space.getText().toString().trim();
        final String c_priceFrom = edtPriceFrom.getText().toString().trim();
        final String c_priceTo = edtPriceTo.getText().toString().trim();
        final String c_bundle = activity.getText().toString().trim();
        final String c_note = notes.getText().toString().trim();
        //final String image_name = fname;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String outlet = SharedPrefManager.getInstance(this).getOutletName().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Submitting please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_COMPE_ACTIVITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Toast.makeText(CompetitiveActivity.this, "ACTIVITY SUCCESS", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(CompetitiveActivity.this, QuestionsActivity.class));
//                        CompetitiveActivity.this.finish();
                        Toast.makeText(MyCompetitior.this, "Report Submitted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MyCompetitior.this, QuestionsActivity.class));
                        MyCompetitior.this.finish();






                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(MyCompetitior.this, "Error occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);
                params.put("outlet_name", outlet);
                params.put("brand", c_brand);
                params.put("promo_type", c_promoType);
                params.put("price_from", c_priceFrom);
                params.put("price_to", c_priceTo);
                params.put("bundle", c_bundle);
                params.put("space", c_space);
                params.put("start_date", c_start_date);
                params.put("end_date", c_end_date);
                params.put("note", c_note);

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

                            selected_image_preview.setVisibility(View.VISIBLE);
                            selected_image_preview.setImageBitmap(bitmapRotate);

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
                new MyCompetitior.DoFileUpload().execute();
            } else {
                Toast.makeText(MyCompetitior.this, "No Internet Connection..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(MyCompetitior.this);
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
