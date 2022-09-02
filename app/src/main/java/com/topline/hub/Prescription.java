package com.topline.hub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Callback;

public class Prescription extends AppCompatActivity implements View.OnClickListener {

    public static String user_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;
    public static String admin_id;

    private final int MY_PERMISSIONS_REQUEST_CAMERA = 7;

    private List<SosCategoryCompetitor_model> categoryCompetitor;
    private SosCategoryCompetitorAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

    private List<String> disty;
    private List<String> dist_id;
    private List<String> dist_cat;
    private List<String> brands;
    private List<String> brands_id;

    private List<String> gauges;
    private List<String> gauge_id;

    //// for Images
    private ConnectionDetector cd;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname;
    File file;
    //// for Images.........END

    Button select_photo;
    ImageView selected_image_preview;
    TextView categoryTitle,totalShelf;
    Button submitShelf_report, shelfSize;
    Spinner l1, dist, gauge;
    Button confirm, confirming;

    private EditText ourProduct,notes, quantity;
    public static String userId, catId, cat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        //RECEIVE INTENT
        Intent i = this.getIntent();
//        catId = i.getExtras().getString("ID_KEY");
//        cat_name = i.getExtras().getString("CATEGORY_NAME_KEY");
        disty = new ArrayList<>();
        dist_id = new ArrayList<>();
        dist_cat = new ArrayList<>();

        gauges = new ArrayList<>();
        gauge_id = new ArrayList<>();

        brands = new ArrayList<>();
        brands_id = new ArrayList<>();
        setTitle("Upload Prescription");


        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        ///outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        submitShelf_report = (Button)findViewById(R.id.saveReportButton);
        select_photo = (Button)findViewById(R.id.scanFile);
        selected_image_preview = (ImageView)findViewById(R.id.scanPreview);

        submitShelf_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if (!upflag) {
                        Toast.makeText(Prescription.this, "Image Not Captured..!", Toast.LENGTH_LONG).show();
                    } else {

                        saveFile(bitmapRotate, file);
                        sendShareOfShelf();

                    }


                } else {
                    Toast.makeText(Prescription.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }


            }
        });



        cd = new ConnectionDetector(Prescription.this);

        cd = new ConnectionDetector(getApplicationContext());

        //btnSubmit.setOnClickListener(this);
        select_photo.setOnClickListener(this);





    }


    public void sendShareOfShelf(){

        //progressBar.setVisibility(View.VISIBLE);

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();

        final String image_name = "https://geojakltd.co.ke/pharmacy/admin/prescriptions/"+fname;
        // now here we convert this list array into json string

        Gson gson=new Gson();

        //final String newDataArray=gson.toJson(categoryCompetitor); // dataarray is list aaray

        // now we have json string lets send it to server using volly

        final String server_url="https://geojakltd.co.ke/pharmacy/mobile/v1/presReport.php"; // url of server check this 100 times it must be working

        // volley

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        //progressBar.setVisibility(View.GONE);

                        final String result=response.toString();
                        Log.d("response", "result : "+result); //when response come i will log it
                        Toast.makeText(Prescription.this, " Prescription sent Successfully", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(Prescription.this, MainActivity.class));
                        Prescription.this.finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                        error.getMessage(); // when error come i will log it
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String, String>();
                //param.put("array",newDataArray); // array is key which we will use on server side

                param.put("user_name", userName);
                param.put("user_id", userId);
                param.put("image_name", image_name);


                return param;
            }
        };
        //Vconnection.getnInstance(this).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
        Volley.newRequestQueue(Prescription.this).add(stringRequest);



    }




    ///////// BLOCK CODE FOR UPLOADING IMAGES


    @Override
    public void onClick(View v) {

        // competitor_brand.clear(); // this list which you hava passed in Adapter for your listview

        switch (v.getId()) {
            case R.id.scanFile:
                Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
                            fname = "mrm" + n + ".png";

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
            @SuppressLint("Range") int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
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
            sourceUri.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            if (cd.isConnectingToInternet()) {
                new Prescription.DoFileUpload().execute();
            } else {
                Toast.makeText(Prescription.this, "No Internet Connection..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(Prescription.this);
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
