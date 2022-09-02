package com.topline.hub;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coaching extends AppCompatActivity {

    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ProgressDialog checkOutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaching);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTeamleader);
//        toolbar.setTitle("Collage");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.containerTeamleader);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutTeamleader);
        tabLayout.setupWithViewPager(mViewPager);

        SharedPrefManager.getInstance(Coaching.this).removeMpa();

        checkOutProgress = new ProgressDialog(this);
        checkOutProgress.setMessage("Please Wait... You are being checked Out");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                leave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() { leave();
    }

    public void leave(){
        checkoutDialog();
    }
    private void checkout(){

        SharedPrefManager.getInstance(this);
        final String user_id = SharedPrefManager.getUserId().toString();
        final String outlet_id = SharedPrefManager.getInstance(this).getOutletId().toString(); // Outlet Id is the appointment ID.
        //progressDialog.show();
        final String current_time = DateFormat.getTimeInstance().format(new Date());

        checkOutProgress.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        checkOutProgress.dismiss();

                        try {
                            JSONObject obj =new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                Toast.makeText(
                                        Coaching.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();

                                SharedPrefManager.getInstance(Coaching.this).doneMPA();

                                Intent intent = new Intent(Coaching.this, MainActivity.class);
                                startActivity(intent);



                            }else {
                                Toast.makeText(
                                        Coaching.this,
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }


                        }catch (JSONException e){
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkOutProgress.dismiss();

                        Toast.makeText(
                                Coaching.this,
                                "Error Ocured try again",
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }

        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("appoint_id", outlet_id);
                params.put("currentTime", current_time);
                //Log.e("outlet","="+outlet_id);

                return params;

            }
        };


        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void checkoutDialog(){
        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You are about to checkout from" + "\n" + SharedPrefManager.getInstance(this).getOutletName());
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        startActivity(new Intent(QuestionsActivity.this, MainActivity.class));

                        checkout();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                dialog.dismiss();
            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Coaching(Coaching.this), "Activity");
        adapter.addFragment(new FragmentTeamleaderChecklist(Coaching.this), "Collage");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
