package com.topline.hub;

import android.content.Context;
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

public class SosCategoryCompetitorAdapter extends RecyclerView.Adapter<SosCategoryCompetitorAdapter.MyViewHolder> {

    private List<SosCategoryCompetitor_model> categoryCompetitors;
    private Context context;

    public SosCategoryCompetitorAdapter(List<SosCategoryCompetitor_model> categoryCompetitors, Context context) {
        this.categoryCompetitors = categoryCompetitors;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sos_competitor_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Integer id = categoryCompetitors.get(position).getId();

        holder.category_competitor_name.setText(categoryCompetitors.get(position).getCategory_competitor_name());
        holder.editcategory_competitor_size.setHint(categoryCompetitors.get(position).getCategory_competitor_name());

        // Saving the price value
        holder.editcategory_competitor_size.setText(categoryCompetitors.get(position).getEditTextSize());
        Log.d("print","yes");


    }

    @Override
    public int getItemCount() {
        return categoryCompetitors.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView category_competitor_name;
        EditText editcategory_competitor_size;
        //LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            category_competitor_name = itemView.findViewById(R.id.categoryName);
            editcategory_competitor_size = itemView.findViewById(R.id.categorySize);
            //linearLayout = itemView.findViewById(R.id.linearLayout);



            editcategory_competitor_size.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    categoryCompetitors.get(getAdapterPosition()).setEditTextSize(editcategory_competitor_size.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });



        }
    }




}
