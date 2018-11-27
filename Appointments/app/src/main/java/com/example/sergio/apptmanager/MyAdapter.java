package com.example.sergio.apptmanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by dfreer on 10/4/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//use alt + enter


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return  new ViewHolder(v);
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textDoctor;
        public ImageView showMap;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewHead = (TextView) itemView.findViewById(R.id.printLocation);
            textViewDesc = (TextView) itemView.findViewById(R.id.printAddress);
            textDoctor = (TextView) itemView.findViewById(R.id.printDoctor);
            showMap   = (ImageView) itemView.findViewById(R.id.btnMap);
        }
    }

    private List<Appointment> listItems;
    private Context context;


    public MyAdapter(List<Appointment> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Appointment listItem = listItems.get(position);
        holder.textViewDesc.setText(listItem.getAddress());
        holder.textViewHead.setText(listItem.getLocationName());
        holder.textDoctor.setText(listItem.getDoctorName());
        final String add = listItem.getAddress();
        holder.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad = add;
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + ad);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                view.getContext().startActivity(intent);
            }
        });
    }

    public void Notify(List<Appointment> list) {
        if (listItems != null) {
            listItems.clear();
            listItems.addAll(list);

        } else {
            listItems = list;
        }
        notifyDataSetChanged();
    }
}
