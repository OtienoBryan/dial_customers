package com.topline.hub;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationReciever extends BroadcastReceiver {
    public static String user_id;
    Context ctx;


    @Override
    public void onReceive(Context context, Intent intent) {

        doTask();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context,AppointmentsActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //if we want ring on notifcation then uncomment below line//
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Check Your Recurring Appointments for tomorrow ")
                .setContentTitle("Recurring Appointments")
            .setSound(alarmSound)
    //setDefaults(Notification.DEFAULT_SOUND).
    .setAutoCancel(true);
        notificationManager.notify(100,builder.build());


    }

    public void doTask() {
        final String userId = SharedPrefManager.getInstance(ctx).getUserId().toString().trim();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_RECURRING_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressDialog.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

//                                Toast.makeText(
//                                        ImageActivity.this,
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();



//                                startActivity(new Intent(ImageActivity.this, QuestionsActivity.class));
//                                ImageActivity.this.finish();

                            }else {
//                                Toast.makeText(
//                                        ImageActivity.this,
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();

//                        Toast.makeText(
//                                ImageActivity.this,
//                                "Error Ocured try again",
//                                Toast.LENGTH_LONG
//                        ).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                //  params.put("outlet_id", outletId);

                return params;

            }
        };


        RequestHandler.getInstance(ctx).addToRequestQueue(stringRequest);


    }
}
