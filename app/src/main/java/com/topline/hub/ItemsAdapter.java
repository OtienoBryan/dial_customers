package com.topline.hub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<CartModel> cats;

    public ItemsAdapter(Context mCtx, List<CartModel> cats) {
        this.mCtx = mCtx;
        this.cats = cats;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_items_adapter, null);
        return new ProductViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final CartModel cat = cats.get(position);



        final Integer id = cat.getId();

        //final String cat_id = cat.getCat_id();
        holder.cat_name.setText(cat.getName());
        holder.quantity.setText("Qty: "+cat.getQuantity());
        holder.price.setText("Unit Price: Ksh. "+cat.getPrice());
        holder.subTotal.setText("Sub Total: Ksh. "+cat.getSub_total());
        Glide.with(mCtx).load(cat.getImage()).into(holder.product_image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete(id.toString(), cat.getCat_id(),cat.getName());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView cat_name, quantity, price, subTotal;
        // RelativeLayout relativeLayout;
        CardView cardView;
        ImageView delete;
        ImageView product_image;


        public ProductViewHolder(View itemView) {
            super(itemView);

            cat_name = itemView.findViewById(R.id.txtProductCategory);
            cardView = itemView.findViewById(R.id.cardView);
            product_image = itemView.findViewById(R.id.product_image);
            quantity = itemView.findViewById(R.id.txtQuantity);
            price = itemView.findViewById(R.id.unitPrice);
            delete = itemView.findViewById(R.id.imgDelete);
            subTotal = itemView.findViewById(R.id.subTotal);


        }
    }

    private void delete(String id, String cat_id, String name){
        final String e_product_id = cat_id;

        final String userId = SharedPrefManager.getInstance(mCtx).getUserId().toString().trim();
        final String userName = SharedPrefManager.getInstance(mCtx).getUsername().trim();
        final String adminId = SharedPrefManager.getInstance(mCtx).getUserUnit().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_POST_ORDER_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(mCtx, "Item Deleted", Toast.LENGTH_LONG).show();
                        mCtx.startActivity(new Intent (mCtx,Cart.class));

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

                params.put("product_id", e_product_id);


                return params;
            }
        };

        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);
    }

}
