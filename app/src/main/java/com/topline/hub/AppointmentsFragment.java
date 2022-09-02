package com.topline.hub;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment {
    Activity act;

    //CardView newAppointments,tasks, follow;
    CardView lnOffer, lnArrival,lnGift,lnLife, lnSoS, lnActivity, lnCoach;
    Button btnNew;
    LinearLayout category, home, notification, cart, call, press;
    TextView user_name, user_phone, user_role, user_team,activity,txtDate;

    RecyclerView recyclerView;
    List<ProductModel> cats;
    private final int MY_PERMISSIONS_CALL= 7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_appointments, container, false);
        View v = inflater.inflate(R.layout.myhome, container, false);
        act=getActivity();

        category = (LinearLayout) v.findViewById(R.id.category);
        home = (LinearLayout) v.findViewById(R.id.home);
        notification = (LinearLayout) v.findViewById(R.id.notification);
        cart = (LinearLayout) v.findViewById(R.id.cart);
        call = (LinearLayout) v.findViewById(R.id.call);
        press = (LinearLayout) v.findViewById(R.id.press);
        //notice = (LinearLayout) v.findViewById(R.id.notice);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(act, 2));

        cats = new ArrayList<>();
        loadCategories();


        //newAppointments = (CardView)v.findViewById(R.id.cvNew);
        lnOffer = (CardView)v.findViewById(R.id.lnOffer);
        lnArrival = (CardView)v.findViewById(R.id.lnArrival);
        lnGift = (CardView)v.findViewById(R.id.lnGift);
        //lnLife = (CardView)v.findViewById(R.id.lnLife);

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) act,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) act,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



//        notice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, Notice.class));
//
//            }
//        });


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, ProductCat.class));

            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, MainActivity.class));

            }
        });

        press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, Prescription.class));

            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, Notice.class));

            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, Cart.class));

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "0712345678";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));

                if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    //ActivityCompat.requestPermissions(context, new String[] { permission }, requestCode);

                    Toast.makeText(act, "Enable call permission in APP Settings", Toast.LENGTH_SHORT).show();
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions

                    return;
                }
                startActivity(intent);

            }
        });

//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, PromotionActivity.class));
//
//            }
//        });

//        support.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, About.class));
//
//            }
//        });

//        sos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, NewSos.class));
//
//            }
//        });

//        history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, AppointmentsActivity.class));
//
//            }
//        });

//        lnTasks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(SharedPrefManager.getInstance(act).getUserLastname().equalsIgnoreCase("56")){
//                    startActivity(new Intent(act, TeamLeaderCollage.class));
//                }else {
//                    startActivity(new Intent(act, ActivityTrackerView.class));
//                }
//
//
//
//
//            }
//        });

        lnOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, OfferFragment.class));
            }
        });

        lnArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, NewArrivals.class));
            }
        });

        lnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, GiftPack.class));
            }
        });

//        lnVisit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, OutletsVisited.class));
//            }
//        });

//        lnExpiry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, MyExpiryProduct.class));
//            }
//        });
//
//        lnSoS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, OutletsVisited.class));
//            }
//        });

//        lnActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, ActivityTrackerView.class));
//            }
//        });

//        lnCoach.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startActivity(new Intent(act, TeamLeaderCollage.class));
//            }
//        });


        //btnNew = (Button) v.findViewById(R.id.btnNew);
        //tasks = (CardView)v.findViewById(R.id.cvTasks);


       /* btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, AppointmentsActivity.class));

            }
        });*/




        return v;
    }

    private void loadCategories() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_HOME_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject cat = array.getJSONObject(i);

                                //adding the Task to Task list
                                cats.add(new ProductModel(
                                        cat.getInt("id"),
                                        cat.getString("cat_id"),
                                        cat.getString("cat_name"),
                                        cat.getString("image"),
                                        cat.getString("price"),
                                        cat.getString("description"),
                                        cat.getString("usage"),
                                        cat.getString("status")
                                        //cat.getString("catcolor_id")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            ProductAdapter adapter = new ProductAdapter(act, cats);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(act, "Error Loading Product Category Try again", Toast.LENGTH_SHORT).show();

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(act).add(stringRequest);
    }


    public void setFragment(Fragment f){

        FragmentManager fManager = getFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(R.id.frame, f);
        ft.commit();

    }


}
