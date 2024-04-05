package com.topline.hub;

import static com.topline.hub.api.util.AppConstants.BUSINESS_SHORT_CODE;
import static com.topline.hub.api.util.AppConstants.CALLBACKURL;
import static com.topline.hub.api.util.AppConstants.PARTYB;
import static com.topline.hub.api.util.AppConstants.PASSKEY;
import static com.topline.hub.api.util.AppConstants.TRANSACTION_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.topline.hub.api.model.AccessToken;
import com.topline.hub.api.model.STKPush;
import com.topline.hub.api.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MyMpesa extends AppCompatActivity {

    private ApiClient mApiClient;
    private String mFireBaseRegId;

    EditText phone_number, amount;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mpesa);

        setTitle("Lipa na M-Pesa");
        //RECEIVE INTENT
        Intent i = this.getIntent();
        String total = i.getExtras().getString("TOTAL");
        String customer = i.getExtras().getString("CUSTOMER");

        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();

        phone_number = (EditText) findViewById(R.id.phone_number);
        amount = (EditText) findViewById(R.id.amount);
        pay = (Button) findViewById(R.id.pay);

        amount.setText(total);
        phone_number.setText(customer);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_numbers = phone_number.getText().toString();
                performSTKPush(phone_numbers);

                Toast.makeText(MyMpesa.this, "Request Sent", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(MyMpesa.this, ThankYou.class));
                MyMpesa.this.finish();
            }
        });
    }

    public void performSTKPush(String phone_number) {
        String amounts = amount.getText().toString();

        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                amounts,
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL + mFireBaseRegId,
                "House of Wines", //The account reference
                "Customer Pay"  //The transaction description
        );

        mApiClient.setGetAccessToken(false);

        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                //mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().string());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                //mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }


    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    public void onBackPressed(){
        //startActivity(new Intent(MyMpesa.this, RequestCash.class));
        MyMpesa.this.finish();

    }
}