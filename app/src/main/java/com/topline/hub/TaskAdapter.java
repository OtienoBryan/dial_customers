package com.topline.hub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ProductViewHolder>{
    private Context mCtx;
    private List<TaskModel> taskList;

    public TaskAdapter(Context mCtx, List<TaskModel> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.task_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final TaskModel task = taskList.get(position);

        //loading the image
      /*  Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.imageView);*/

        final Integer id = task.getId();

        holder.textViewId.setText(id.toString());
        holder.startDate.setText(task.getStart_date());
        holder.endDate.setText(task.getEnd_date());
        holder.description.setText(task.getDescription());


       //final String title = holder.textViewTitle.getText().toString();

       final String myid = id.toString();





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Toast.makeText(mCtx, "You clicked" + product.getTitle(),
                    Toast.LENGTH_LONG).show();*/

               // Intent i = new Intent(mCtx, SubCategoryActivity.class);
              //  mCtx.startActivity(i);

              //  openDetailActivity(id.toString(),productModel.getTitle());
            }
        });

        holder.btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog alertDialog = new AlertDialog.Builder(
                        mCtx).create();

                // Setting Dialog Title
                alertDialog.setTitle("End Task");

                // Setting Dialog Message
                alertDialog.setMessage("You are about to Terminate this Task ");

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.tick_green);

                // Setting OK Button
                alertDialog.setButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();



                        final String user_id = SharedPrefManager.getInstance(mCtx).getUserId().toString();
                        final String task_id = holder.textViewId.getText().toString().trim();


                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                Constants.URL_TERMINATE_TASK,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {


                                        try {
                                            JSONObject obj =new JSONObject(response);
                                            if (!obj.getBoolean("error")){
                                                Toast.makeText(
                                                        mCtx,
                                                        "Task successfully Finished",
                                                        Toast.LENGTH_LONG
                                                ).show();


                                                //setFragment(new LoginFragment());

                                            }else {
                                                Toast.makeText(
                                                        mCtx,
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

                                        Toast.makeText(
                                                mCtx,
                                                "Error connecting to server Check Internet connection",
                                                Toast.LENGTH_LONG
                                        ).show();

                                    }
                                }

                        ){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                //params.put("userid", user_id);
                                params.put("taskid", task_id);
                                return params;

                            }
                        };


                        RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);






                    }
                });

                // Showing Alert Message
                alertDialog.show();












            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView startDate,endDate,description,textViewId;
       // RelativeLayout relativeLayout;
        CardView cardView;
        Button btnEnd;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textViewId);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            description = itemView.findViewById(R.id.description);

            cardView = itemView.findViewById(R.id.cardView);

           // relativeLayout = itemView.findViewById(R.id.relativeLayout);

            //txtproduct = itemView.findViewById(R.id.txtProducts);
            btnEnd = itemView.findViewById(R.id.btnEndTask);
        }
    }







}
