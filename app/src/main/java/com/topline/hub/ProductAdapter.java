package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<ProductModel> cats;



    public ProductAdapter(Context mCtx, List<ProductModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_product_adapter2, null);
        return new ProductViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final ProductModel cat = cats.get(position);



        final Integer id = cat.getId();
        final Integer available = Integer.valueOf(cat.getStatus());

        if(available.equals(1)){
            holder.add_cart.setVisibility(View.VISIBLE);
            holder.stock_out.setVisibility(View.GONE);

        }else if(available.equals(0)){
            holder.add_cart.setVisibility(View.GONE);
            holder.stock_out.setVisibility(View.VISIBLE);
        }

        //final String cat_id = cat.getCat_id();
        holder.cat_name.setText(cat.getName());
        holder.price.setText(cat.getPrice() +" /-");
        Glide.with(mCtx).load(cat.getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDetailActivity(id.toString(), cat.getCat_id(),cat.getName(), cat.getImage(),  cat.getPrice(), cat.getDescription(), cat.getUsage(), cat.getStatus(), cat.getAbv(), cat.getSub(), cat.getBrand(), cat.getCountry(), cat.getDetails() );
            }
        });

        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_to_cart(id.toString(), cat.getCat_id(),cat.getName(), cat.getImage(),  cat.getPrice(), cat.getDescription(), cat.getUsage(), cat.getStatus(), cat.getAbv(), cat.getSub(), cat.getBrand(), cat.getCountry(), cat.getDetails() );
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favorite(id.toString(), cat.getCat_id(),cat.getName(), cat.getImage(),  cat.getPrice(), cat.getDescription(), cat.getUsage(), cat.getStatus(), cat.getAbv(), cat.getSub(), cat.getBrand(), cat.getCountry(), cat.getDetails() );
            }
        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, price;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView image, fav, stock_out, add_cart;
//        Button add_cart;



        public ProductViewHolder(View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);
            image = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.price);
            add_cart = itemView.findViewById(R.id.add_cart);
            fav = itemView.findViewById(R.id.b_fav);
            stock_out = itemView.findViewById(R.id.stockout);

        }
    }

    private void openDetailActivity(String product_id,String product_code, String product_name, String product_image, String price, String description, String usage, String status, String abv, String sub, String brand, String country, String details){

        Intent i = new Intent(mCtx, ExpiryProductActivity.class);

        //PACK DATA
        i.putExtra("ID_KEY", product_id);
        i.putExtra("PRODUCT_NAME_KEY", product_name);
        i.putExtra("PRODUCT_CODE_KEY", product_code);
        i.putExtra("PRODUCT_IMAGE", product_image);
        i.putExtra("PRODUCT_PRICE", price);
        i.putExtra("PRODUCT_DESC", description);
        i.putExtra("PRODUCT_USAGE", usage);
        i.putExtra("PRODUCT_STATUS", status);
        i.putExtra("PRODUCT_ABV", abv);
        i.putExtra("PRODUCT_SUB", sub);
        i.putExtra("PRODUCT_BRAND", brand);
        i.putExtra("PRODUCT_COUNTRY", country);
        i.putExtra("PRODUCT_DETAILS", details);

        mCtx.startActivity(i);

    }

    private void add_to_cart(String product_id,String product_code, String product_name, String product_image, String price, String description, String usage, String status, String abv, String sub, String brand, String country, String details){

        final String e_quantity = "1";
        final String e_product_name = product_name;
        final String e_product_code = product_id;
        final String e_product_price = price;
        final String e_product_image = product_image;

        final String userId = SharedPrefManager.getInstance(mCtx).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(mCtx).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(mCtx).getUserUnit().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_POST_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(mCtx, "Added to Cart", Toast.LENGTH_LONG).show();
                        mCtx.startActivity(new Intent(mCtx, Cart.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mCtx, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);

                params.put("product_name", e_product_name);
                params.put("product_code", e_product_code);
                params.put("product_price", e_product_price);
                params.put("product_image", e_product_image);
                params.put("quantity", e_quantity);

                return params;
            }
        };

        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);



    }

    private void favorite(String product_id,String product_code, String product_name, String product_image, String price, String description, String usage, String status, String abv, String sub, String brand, String country, String details){

        final String e_quantity = "1";
        final String e_product_name = product_name;
        final String e_product_code = product_id;
        final String e_product_price = price;
        final String e_product_image = product_image;

        final String userId = SharedPrefManager.getInstance(mCtx).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(mCtx).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(mCtx).getUserUnit().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_POST_FAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(mCtx, "Added to Favourite List", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(Cart.this, Cart.class));
//                        Cart.this.finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mCtx, "Error Occurred Try again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_name", userName);
                params.put("user_id", userId);
                params.put("admin_id", adminId);

                params.put("product_name", e_product_name);
                params.put("product_code", e_product_code);
                params.put("product_price", e_product_price);
                params.put("product_image", e_product_image);
                params.put("quantity", e_quantity);

                return params;
            }
        };

        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);



    }

}
