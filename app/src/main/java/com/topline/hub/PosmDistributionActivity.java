package com.topline.hub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PosmDistributionActivity extends AppCompatActivity implements View.OnClickListener {

    public static String user_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;
    public static String material;
    public static String admin_id;

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

    TextView outlet;
    EditText outletQuantity,note, edtQty;
    Button select_photo, uploadReport;
    ImageView selected_image_preview;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(material);
        setContentView(R.layout.activity_posm_distribution);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        String id = i.getExtras().getString("ID_KEY");
        material = i.getExtras().getString("MATERIAL_KEY");
        String quantity = i.getExtras().getString("QUANTITY_KEY");

        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();


        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        select_photo = (Button)findViewById(R.id.scanFile);
        uploadReport = (Button)findViewById(R.id.saveReportButton);

        outlet = (TextView)findViewById(R.id.clientname);
        outletQuantity = (EditText)findViewById(R.id.edtoutletquantity);
        edtQty = (EditText)findViewById(R.id.edtoutletquantityrequired);
        note = (EditText)findViewById(R.id.notes);
        selected_image_preview = (ImageView)findViewById(R.id.scanPreview);

        outletQuantity.setText(quantity);

        outlet.setText(outlet_name);

        uploadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //competitor_brand.clear(); // this list which you hava passed in Adapter for your listview
                //array.notifyDataSetChanged(); // notify to listview for refresh

                //submitCompetitive();

                if (cd.isConnectingToInternet()) {
                    if (!upflag) {
                        Toast.makeText(PosmDistributionActivity.this, "Image Not Captured..!", Toast.LENGTH_LONG).show();
                    } else {
                        submitPosmTracker();
                        saveFile(bitmapRotate, file);
                    }



                } else {
                    Toast.makeText(PosmDistributionActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
            }
        });




        cd = new ConnectionDetector(PosmDistributionActivity.this);

        cd = new ConnectionDetector(getApplicationContext());

        //btnSubmit.setOnClickListener(this);
        select_photo.setOnClickListener(this);



    }




    private void submitPosmTracker(){

        final String p_quantity = outletQuantity.getText().toString().trim();
        final String r_quantity = edtQty.getText().toString().trim();
        final String p_material = material;
        final String p_note = note.getText().toString().trim();
        final String image_name = fname;

        final String userId = SharedPrefManager.getInstance(this).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(this).getUsername().trim();
        final String outlet = SharedPrefManager.getInstance(this).getOutletName().trim();
        final String outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString().trim();
        final String adminId = SharedPrefManager.getInstance(this).getUserUnit().trim();


        progressDialog.setMessage("Submitting please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_POSM_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Toast.makeText(CompetitiveActivity.this, "ACTIVITY SUCCESS", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(CompetitiveActivity.this, QuestionsActivity.class));
//                        CompetitiveActivity.this.finish();

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                PosmDistributionActivity.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Success !");

                        // Setting Dialog Message
                        alertDialog.setMessage("POSM Successfully Sent");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.tick_green);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(PosmDistributionActivity.this, PosmAllocationActivity.class);
                                startActivity(intent);
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                        //Toast.makeText(CartActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(PosmDistributionActivity.this, "Error occured Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);
                params.put("outlet_name", outlet);
                params.put("outlet_id", outlet_id);
                params.put("quantity", p_quantity);
                params.put("quantity_required", r_quantity);
                params.put("material", p_material);
                params.put("note", p_note);
                params.put("image_name", image_name);

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
            case R.id.scanFile:

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
                            fname = "topline" + n + ".jpg";

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
                new PosmDistributionActivity.DoFileUpload().execute();
            } else {
                Toast.makeText(PosmDistributionActivity.this, "No Internet Connection..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(PosmDistributionActivity.this);
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
