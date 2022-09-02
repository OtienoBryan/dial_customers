package com.topline.hub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewExpiryUpdate extends AppCompatActivity {

    private ConnectionDetector cd;
    TextView outletName;
    EditText productName,manu_date,batchNo,quantity,expiryDate,comments,edtQty;
    Button updateProduct;
    public static String my_id;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expiry_update);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Product...");


        Intent i = this.getIntent();
        final String id = i.getExtras().getString("ID_KEY");
        final String outlet_Name = i.getExtras().getString("OUTLET_KEY");
        final String product_Name = i.getExtras().getString("PRODUCT_NAME_KEY");
        final String product_Code = i.getExtras().getString("PRODUCT_CODE_KEY");
        final String batch_No = i.getExtras().getString("BATCH_NO_KEY");
        final String p_quantity = i.getExtras().getString("QUANTITY_KEY");
        final String p_expiryDate = i.getExtras().getString("EXPIRY_DATE_KEY");
        final String p_manuDate = i.getExtras().getString("MANU_DATE_KEY");
        final String p_comments = i.getExtras().getString("COMMENTS_KEY");

        my_id = id;


        updateProduct = (Button)findViewById(R.id.btnUpdateProduct);
        outletName = (TextView)findViewById(R.id.appmisc);
        productName = (EditText)findViewById(R.id.productName);
        manu_date = (EditText)findViewById(R.id.manu_date);
        batchNo = (EditText)findViewById(R.id.batchNo);
        quantity = (EditText)findViewById(R.id.quantity);
        edtQty = (EditText)findViewById(R.id.edtExpiryQuantity);
        expiryDate = (EditText)findViewById(R.id.expiryDate);
        comments = (EditText)findViewById(R.id.comments);

        ///// Set the values to the test

        outletName.setText(outlet_Name);
        productName.setText(product_Name);
       // productCode.setText(product_Code);
        batchNo.setText(batch_No);
        quantity.setText(p_quantity);
        expiryDate.setText(p_expiryDate);
        manu_date.setText(p_manuDate);
        comments.setText(p_comments);


        cd = new ConnectionDetector(NewExpiryUpdate.this);

        cd = new ConnectionDetector(getApplicationContext());



        updateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {

                    UpdateExpiryTracker();

                } else {
                    Toast.makeText(NewExpiryUpdate.this, "No Internet Connection !", Toast.LENGTH_LONG).show();
                }
                //UpdateExpiryTracker();
            }
        });




    }



    private void UpdateExpiryTracker(){

        final String this_id = my_id;
        final String e_quantity = edtQty.getText().toString().trim();
        final String e_comment = comments.getText().toString().trim();

        //progressDialog.setMessage("Submiting Report please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_NEW_EXPIRY_REPORT_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        //Toast.makeText(CompetitiveActivity.this, "ACTIVITY SUCCESS", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(CompetitiveActivity.this, QuestionsActivity.class));
//                        CompetitiveActivity.this.finish();

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                NewExpiryUpdate.this).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Success !");

                        // Setting Dialog Message
                        alertDialog.setMessage("Short Expiry Successfully Submitted");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.tick_green);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(NewExpiryUpdate.this, ExpiryProductViewActivity.class);
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
                        Toast.makeText(NewExpiryUpdate.this, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("p_id", this_id);
                params.put("quantity", e_quantity);
                params.put("comment", e_comment);


                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);



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
