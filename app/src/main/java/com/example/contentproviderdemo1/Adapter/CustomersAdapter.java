package com.example.contentproviderdemo1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.contentproviderdemo1.R;
import com.example.contentproviderdemo1.listener.OnRecyclerItemClickListener;
import com.example.contentproviderdemo1.model.Customer;

import java.util.ArrayList;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<Customer> objects;
    OnRecyclerItemClickListener recyclerItemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener recyclerItemClickListener){
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public CustomersAdapter(Context context, int resource, ArrayList<Customer> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    public CustomersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(resource,parent,false);
        final CustomersAdapter.ViewHolder holder = new CustomersAdapter.ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomersAdapter.ViewHolder holder, int position) {
        Customer customer  = objects.get(position);

        // Extracting Data from News Object and Setting the data on list_item
        holder.txtTitle.setText(customer.name);
        holder.txtUrl.setText(customer.phone);

    }

    @Override
    public int getItemCount() {
        return objects.size(); // how many list items we wish to have in our recycler view

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.textViewTitle);
            txtUrl = itemView.findViewById(R.id.textViewURL);

        }
    }
    }

