package com.topline.hub;

import android.app.Activity;
import android.app.Dialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
import retrofit2.Response;

public class SosActivity extends AppCompatActivity implements View.OnClickListener {

    public static String user_id;
    public static String user_name;
    public static String outlet_id;
    public static String outlet_name;
    public static String admin_id;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<SosCategoryCompetitor_model> categoryCompetitor;
    private SosCategoryCompetitorAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;

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

    private EditText ourProduct,notes;
    public static String userId, catId, cat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        //RECEIVE INTENT
        Intent i = this.getIntent();
        catId = i.getExtras().getString("ID_KEY");
        cat_name = i.getExtras().getString("CATEGORY_NAME_KEY");


        user_id = SharedPrefManager.getInstance(this).getUserId().toString();
        outlet_name = SharedPrefManager.getInstance(this).getOutletName();
        outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString();
        user_name = SharedPrefManager.getInstance(this).getUsername();
        admin_id = SharedPrefManager.getInstance(this).getUserUnit();

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryTitle = (TextView)findViewById(R.id.categoryTitle);
        totalShelf = (TextView)findViewById(R.id.totalShelf);
        notes = (EditText)findViewById(R.id.notes);
        ourProduct = (EditText) findViewById(R.id.ourProduct);
        ourProduct.setHint(cat_name);

        shelfSize = (Button)findViewById(R.id.btnShelfSize);
        submitShelf_report = (Button)findViewById(R.id.saveReportButton);
        select_photo = (Button)findViewById(R.id.scanFile);
        selected_image_preview = (ImageView)findViewById(R.id.scanPreview);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        categoryTitle.setText("Submitting for Share of Shelf "+cat_name+" Category");


        categoryCompetitor = new ArrayList<>();

        fetchCompetitorCategory(catId);


        shelfSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

                //sendShelf();

            }
        });

        submitShelf_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( TextUtils.isEmpty(totalShelf.getText())){

                    totalShelf.setError( "This field is required!" );

                }else if(TextUtils.isEmpty(ourProduct.getText())){

                    ourProduct.setError( "This field is required!" );

                }else if(TextUtils.isEmpty(notes.getText())){

                    notes.setError( "This field is required!" );

                }else{



                    if (cd.isConnectingToInternet()) {
                        if (!upflag) {
                            Toast.makeText(SosActivity.this, "Image Not Captured..!", Toast.LENGTH_LONG).show();
                        } else {

                            saveFile(bitmapRotate, file);
                            sendShareOfShelf();

                        }


                    } else {
                        Toast.makeText(SosActivity.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });



        cd = new ConnectionDetector(SosActivity.this);

        cd = new ConnectionDetector(getApplicationContext());

        //btnSubmit.setOnClickListener(this);
        select_photo.setOnClickListener(this);





    }



    public void showDialog(){
        final Dialog dialog = new Dialog(SosActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_shelf_size_layout);

        final EditText editSize = (EditText) dialog.findViewById(R.id.shelfsize);

        Button updateSize = (Button) dialog.findViewById(R.id.updateShelfSize);
        Button cancelUpdateSize = (Button) dialog.findViewById(R.id.cancelShelfSizeUpdate);
        updateSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalShelf.setText(editSize.getText().toString());


                dialog.dismiss();

            }
        });


        cancelUpdateSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }








    public void fetchCompetitorCategory(String catId){

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        retrofit2.Call<List<SosCategoryCompetitor_model>> call = apiInterface.getCompetitorProductCategories(catId);

        call.enqueue(new Callback<List<SosCategoryCompetitor_model>>() {
            @Override
            public void onResponse(retrofit2.Call<List<SosCategoryCompetitor_model>> call, Response<List<SosCategoryCompetitor_model>> response) {

                progressBar.setVisibility(View.GONE);

                categoryCompetitor = response.body();
                adapter = new
                        SosCategoryCompetitorAdapter(categoryCompetitor, SosActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<List<SosCategoryCompetitor_model>> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(SosActivity.this, "Error Loading Data Try again", Toast.LENGTH_SHORT).show();

            }
        });





    }




    public void sendShareOfShelf(){

        progressBar.setVisibility(View.VISIBLE);

        final String userId = user_id;
        final String adminId = admin_id;
        final String userName = user_name ;
        final String outlet = outlet_name ;
        final String outletId = outlet_id ;
        final String categoryId = catId ;
        final String category_name = cat_name;
        final String s_totalShelf = totalShelf.getText().toString().trim();
        final String note = notes.getText().toString().trim();
        final String our_Product = ourProduct.getText().toString().trim();
        final String image_name = fname;
        // now here we convert this list array into json string

        Gson gson=new Gson();

        final String newDataArray=gson.toJson(categoryCompetitor); // dataarray is list aaray

        // now we have json string lets send it to server using volly

        final String server_url="https://impulsepromotions.co.ke/bidco/mobile/v1/shareofshelf_report.php"; // url of server check this 100 times it must be working

        // volley

        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

                        progressBar.setVisibility(View.GONE);

                        final String result=response.toString();
                        Log.d("response", "result : "+result); //when response come i will log it
                        Toast.makeText(SosActivity.this, " Share of Shelf Saved Successfully", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(SosActivity.this, SosCategoryActivity.class));
                        SosActivity.this.finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressBar.setVisibility(View.GONE);
                        error.printStackTrace();
                        error.getMessage(); // when error come i will log it
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String, String>();
                param.put("array",newDataArray); // array is key which we will use on server side
                param.put("user_id",userId);
                param.put("outlet_name",outlet);
                param.put("user_name",userName);
                param.put("outlet_id",outletId);
                param.put("admin_id",adminId);

                param.put("category_name",category_name);
                param.put("category_id",categoryId);
                param.put("ourProduct",our_Product);
                param.put("total_shelf",s_totalShelf);
                param.put("image_name", image_name);
                param.put("notes",note);


                return param;
            }
        };
        //Vconnection.getnInstance(this).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
        Volley.newRequestQueue(SosActivity.this).add(stringRequest);



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
                new SosActivity.DoFileUpload().execute();
            } else {
                Toast.makeText(SosActivity.this, "No Internet Connection..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(SosActivity.this);
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
