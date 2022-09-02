package com.topline.hub;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<Asset_model> assets;

    public AssetAdapter(Context mCtx, List<Asset_model> assets) {
        this.mCtx = mCtx;
        this.assets = assets;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.asset_list_items, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Asset_model asset = assets.get(position);

        final Integer id = asset.getId();
        final String asset_name = asset.getAsset_name();
        //final String availability = product.getAvailability();

        holder.asset_name.setText(asset.getAsset_name());
        //holder.asset_quantity.setText(asset.getQuantity());

        holder.asset_quantity.setText(assets.get(position).getEditTextValue());
        Log.d("print","yes");

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  openDetailActivity(id.toString(),productModel.getTitle());
            }
        });



    }

    @Override
    public int getItemCount() {
        return assets.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView asset_name;
        EditText asset_quantity;
        // RelativeLayout relativeLayout;
        CardView cardView;

        //private CheckBox mCheckBox, nCheckBox;


        public ProductViewHolder(View itemView) {
            super(itemView);

            asset_name = itemView.findViewById(R.id.assetName);
            asset_quantity = itemView.findViewById(R.id.assetQuantity);
            cardView = itemView.findViewById(R.id.cardView);


            asset_quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    assets.get(getAdapterPosition()).setEditTextValue(asset_quantity.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



        }
    }







}
