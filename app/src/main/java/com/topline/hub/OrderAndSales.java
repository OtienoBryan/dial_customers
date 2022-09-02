package com.topline.hub;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.topline.hub.Storage.DatabaseHelper;
import com.topline.hub.Storage.Product;
import com.topline.hub.Storage.Tables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAndSales extends AppCompatActivity {

    private int hours,minute, mYear, mMonth, mDay;
    RadioButton rbOrder,rbSale;
    Spinner spnCategory,spnProduct,spnPackaging;
    Button btnAdd,btnSubmit;
    RecyclerView recyclerView;
    TextView txtTotal,txtTQty;
    EditText edtDate,edtQty,edtPrice;
    ProgressDialog progressDialog = null;
    LinearLayout lnDelivery;
    private ApiInterface apiInterface;
    ArrayList<String> arrayProductId = new ArrayList<>();
    ArrayList<Order> orderList = new ArrayList<>();
    DatabaseHelper helper=new DatabaseHelper(OrderAndSales.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_and_sales);
        setTitle("Order and Sales");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(OrderAndSales.this);

        rbOrder = (RadioButton) findViewById(R.id.rbOrder);
        rbSale = (RadioButton) findViewById(R.id.rbSale);
        lnDelivery = (LinearLayout) findViewById(R.id.lnOrderDelivery);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerOrder);

        btnAdd = (Button) findViewById(R.id.btnOrderAddList);
        btnSubmit = (Button) findViewById(R.id.btnOrderSubmit);

        txtTQty = (TextView) findViewById(R.id.txtOrderTotalQty);
        txtTotal = (TextView) findViewById(R.id.txtOrderTotalCost);

        edtDate = (EditText) findViewById(R.id.edtOrderDelivery);
        edtQty = (EditText) findViewById(R.id.edtOrderQuantity);
        edtPrice = (EditText) findViewById(R.id.edtOrderSelling);

        spnPackaging = (Spinner) findViewById(R.id.spnOrderPackaging);
        spnCategory = (Spinner) findViewById(R.id.spOrderCategory);
        spnProduct = (Spinner) findViewById(R.id.spOrderProduct);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderAndSales.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        rbOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lnDelivery.setVisibility(View.VISIBLE);
                }
            }
        });
        rbSale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    lnDelivery.setVisibility(View.GONE);
                }
            }
        });

        loadProducts();


        ArrayList<String> arrayPackage = new ArrayList<>();
        arrayPackage.clear();
        arrayPackage.add("Select ...");
        arrayPackage.add("Cartons");
        arrayPackage.add("Parcels/Cases");
        final ArrayList<String> arrayEmpty = new ArrayList<>();
        arrayEmpty.clear();
        arrayEmpty.add("Select a category ...");
        ArrayAdapter<String> spnAdapter = new ArrayAdapter(OrderAndSales.this,
                R.layout.support_simple_spinner_dropdown_item, arrayPackage);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPackaging.setAdapter(spnAdapter);
        final ArrayList<String> loadingEmpty = new ArrayList<>();
        arrayEmpty.clear();
        arrayEmpty.add("Loading ...");
        ArrayAdapter<String> loadAdapter = new ArrayAdapter(OrderAndSales.this,
                R.layout.support_simple_spinner_dropdown_item, loadingEmpty);
        loadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(loadAdapter);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> spnAdapter = new ArrayAdapter(OrderAndSales.this,
                        R.layout.support_simple_spinner_dropdown_item, helper.getProducts(spnCategory.getSelectedItem().toString()));
                spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProduct.setAdapter(spnAdapter);
                arrayProductId = helper.getProductIds(spnCategory.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ArrayAdapter<String> spnAdapter = new ArrayAdapter(OrderAndSales.this,
                        R.layout.support_simple_spinner_dropdown_item, arrayEmpty);
                spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnProduct.setAdapter(spnAdapter);

            }
        });

        spnProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String price = helper.getProductPrice(spnProduct.getSelectedItem().toString());
                edtQty.setText("");
                if(!price.equalsIgnoreCase("")){
                    edtPrice.setText(price);
                }else{
                    edtPrice.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtQty.getText().toString().equalsIgnoreCase("") ){
                    Order order = new Order();
                    order.setId(arrayProductId.get(spnProduct.getSelectedItemPosition()));
                    order.setProduct(spnProduct.getSelectedItem().toString());
                    order.setQuantity(edtQty.getText().toString());
                    order.setCost(edtPrice.getText().toString());
                    if(spnPackaging.getSelectedItemPosition() == 2){
                        order.setPackaging("pls");
                    }else{
                        order.setPackaging("crt");
                    }
                    orderList.add(order);
                    loadOrderList();
                }else{
                    Toast.makeText(OrderAndSales.this, "Enter the quantity.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbOrder.isChecked()){
                    if(edtDate.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(OrderAndSales.this, "Enter the delivery date.", Toast.LENGTH_SHORT).show();
                    }else{
                        if(orderList.size() == 0){
                            Toast.makeText(OrderAndSales.this, "Add products to the order", Toast.LENGTH_SHORT).show();
                        }else {
                            postOrder();
                        }
                    }
                }else{
                    if(orderList.size() == 0){
                        Toast.makeText(OrderAndSales.this, "Add products to the order", Toast.LENGTH_SHORT).show();
                    }else {
                        postSales();
                    }
                }
            }
        });


    }

    private void postOrder(){
        progressDialog.setMessage("Submitting ....");
        progressDialog.show();

        final String delivery =  edtDate.getText().toString();
        final String userId =  SharedPrefManager.getInstance(OrderAndSales.this).getUserId().toString();
        final String outlet =  SharedPrefManager.getInstance(OrderAndSales.this).getOutletName();
        final String adminId = SharedPrefManager.getInstance(OrderAndSales.this).getUserUnit().trim();
        final String outletId = ""+SharedPrefManager.getInstance(OrderAndSales.this).getOutletId();
        final String userName = SharedPrefManager.getInstance(OrderAndSales.this).getUsername() + " " + SharedPrefManager.getInstance(OrderAndSales.this).getUserLastname();

        Gson gson=new Gson();
        final String newDataArray=gson.toJson(orderList);
        final String server_url="https://impulsepromotions.co.ke/impulse/mobile/v1/order_report.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        progressDialog.dismiss();

                        final String result=response.toString();
                        Log.d("response", "result : "+result); //when response come i will log it
                        Toast.makeText(OrderAndSales.this, "Order submitted successfully", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(OrderAndSales.this, QuestionsActivity.class));
                        OrderAndSales.this.finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        error.printStackTrace();
                        error.getMessage();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String, String>();
                param.put("array",newDataArray); // array is key which we will use on server side
                param.put("user_id",userId);
                param.put("outlet",outlet);
                param.put("admin_id",adminId);
                param.put("outlet_id",outletId);
                param.put("user_name",userName);
                param.put("delivery",delivery);
                return param;
            }
        };
        Volley.newRequestQueue(OrderAndSales.this).add(stringRequest);
    }

    private void postSales(){
        progressDialog.setMessage("Submitting ....");
        progressDialog.show();

        final String userId =  SharedPrefManager.getInstance(OrderAndSales.this).getUserId().toString();
        final String outlet =  SharedPrefManager.getInstance(OrderAndSales.this).getOutletName();
        final String adminId = SharedPrefManager.getInstance(OrderAndSales.this).getUserUnit().trim();
        final String outletId = ""+SharedPrefManager.getInstance(OrderAndSales.this).getOutletId();
        final String userName = SharedPrefManager.getInstance(OrderAndSales.this).getUsername() + " " + SharedPrefManager.getInstance(OrderAndSales.this).getUserLastname();

        Gson gson=new Gson();
        final String newDataArray=gson.toJson(orderList);
        final String server_url="https://impulsepromotions.co.ke/bidco/mobile/v1/sales_report.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, server_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        progressDialog.dismiss();

                        final String result=response.toString();
                        Log.e("response", "result : "+result); //when response come i will log it
                        Toast.makeText(OrderAndSales.this, "Sale submitted successfully", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(OrderAndSales.this, QuestionsActivity.class));
                        OrderAndSales.this.finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        error.printStackTrace();
                        error.getMessage();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<String, String>();
                param.put("array",newDataArray); // array is key which we will use on server side
                param.put("user_id",userId);
                param.put("outlet",outlet);
                param.put("admin_id",adminId);
                param.put("outlet_id",outletId);
                param.put("user_name",userName);
                return param;
            }
        };
        Volley.newRequestQueue(OrderAndSales.this).add(stringRequest);
    }

    private void loadOrderList(){
        loadTotal();
        OrderAdapter adapter = new OrderAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderAndSales.this));
        recyclerView.setAdapter(adapter);
    }

    private void loadTotal(){
        int amount = 0;
        int qty = 0;
        for (int i =0; i < orderList.size(); i++){
            amount = amount + Integer.parseInt(orderList.get(i).getCost());
            qty = qty + Integer.parseInt(orderList.get(i).getQuantity());
        }
        txtTotal.setText("KES "+amount);
        txtTQty.setText(""+qty);

    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder>{

        @NonNull
        @Override
        public OrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.order_model,viewGroup,false);
            OrderHolder holder = new OrderHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull OrderHolder holder, int i) {
            Order obj = orderList.get(i);
            holder.txtProduct.setText(obj.getProduct());
            holder.txtPrice.setText(obj.getCost());
            holder.txtQty.setText(obj.getQuantity()+" "+obj.getPackaging());
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public class OrderHolder extends RecyclerView.ViewHolder{
            TextView txtProduct,txtQty,txtPrice;
            public OrderHolder(@NonNull View itemView) {
                super(itemView);
                txtProduct = (TextView) itemView.findViewById(R.id.txtOrderModelProduct);
                txtQty = (TextView) itemView.findViewById(R.id.txtOrderModelQty);
                txtPrice = (TextView) itemView.findViewById(R.id.txtOrderModelCost);
            }
        }
    }

    private void loadCategory(){
        ArrayAdapter<String> spnAdapter = new ArrayAdapter(OrderAndSales.this,
                R.layout.support_simple_spinner_dropdown_item, helper.getProductCategory());
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(spnAdapter);
    }

    private void loadProducts(){
        progressDialog.setMessage("Setting up products ...");
        progressDialog.show();
        //apiInterface.getProducts("10").enqueue(new Callback<JsonArray>() {
        apiInterface.getProducts(SharedPrefManager.getInstance(OrderAndSales.this).getUserAccount()).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                helper.DeleteAllRecord(Tables.TableProduct.TABLENAME);
                for(int i=0;i<response.body().size();i++){
                    JsonObject jsonObject=response.body().get(i).getAsJsonObject();
                    Product obj=new Product();
                    obj.setId(jsonObject.get("id").getAsString());
                    if(!jsonObject.get("name").isJsonNull()) {
                        obj.setName(jsonObject.get("name").getAsString());
                    }
                    if(!jsonObject.get("price").isJsonNull()) {
                        obj.setPrice(jsonObject.get("price").getAsString());
                    }
                    if(!jsonObject.get("category").isJsonNull()) {
                        obj.setCategory(jsonObject.get("category").getAsString());
                    }
                    if(!jsonObject.get("sku").isJsonNull()) {
                        obj.setSku(jsonObject.get("sku").getAsString());
                    }
                    helper.AddProduct(obj);
                }
                loadCategory();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(OrderAndSales.this, "Error Loading products! Try again.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
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
