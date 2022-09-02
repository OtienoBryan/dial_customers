package com.topline.hub;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    EditText et,et2,et3,et4;
    Button bt,btSend;
    ListView lv;

//    ArrayList<String> arrayList;
//    ArrayAdapter<String> adapter;

    List<Data> dataArray;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        et = (EditText)findViewById(R.id.editText);
        et2 = (EditText)findViewById(R.id.editText2);
        et3 = (EditText)findViewById(R.id.editText3);
        et4 = (EditText)findViewById(R.id.editText4);

        bt = (Button)findViewById(R.id.button_addData);
        btSend = (Button)findViewById(R.id.button_send);
        lv = (ListView)findViewById(R.id.listView_lv);





        arrayList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(TestActivity.this, android.R.layout.simple_list_item_1,arrayList);
       lv.setAdapter(adapter);


//        arrayList = new ArrayList<>();
//
//        adapter = new ArrayAdapter<String>(TestActivity.this, android.R.layout.simple_list_item_1,arrayList);
//        lv.setAdapter(adapter);

        onBtnClick();


    }

    public void onBtnClick(){

        dataArray=new ArrayList<Data>();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String result = et.getText().toString();
//                String result1 = et2.getText().toString();
//                arrayList.add(result);
//                arrayList.add(result1);
//
//                adapter.notifyDataSetChanged();


                String name = et.getText().toString();
                String number = et2.getText().toString();
                String addres = et3.getText().toString();
                Integer id = Integer.parseInt(et4.getText().toString());





                Data dt=new Data(name,number,addres,id);

                // add into list array
                dataArray.add(dt);


                Gson gson=new Gson();

                final String newDataArray=gson.toJson(dataArray); // dataarray is list aaray

                arrayList.add(newDataArray);
                //arrayList.add(String.valueOf(dataArray));

                adapter.notifyDataSetChanged();





            }
        });



        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


// now here we convert this list array into json string

//                Gson gson=new Gson();
//
//                final String newDataArr=gson.toJson(arrayList); // dataarray is list aaray
                // now we have json string lets send it to server using volly

                // now here we convert this list array into json string

                Gson gson=new Gson();

                final String newDataArray=gson.toJson(dataArray); // dataarray is list aaray

                final String server_url="https://impulsepromotions.co.ke/impulse/mobile/v1/volly_array.php"; // url of server check this 100 times it must be working

                // volley

                StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response)
                            {

                                final String result=response.toString();
                                Log.d("response", "result : "+result); //when response come i will log it
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
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

                        return param;
                    }
                };
                //Vconnection.getnInstance(this).addRequestQue(stringRequest); // vConnection i claas which used to connect volley
                Volley.newRequestQueue(TestActivity.this).add(stringRequest);





            }
        });










    }
}
