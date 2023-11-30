package com.project.angsurapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<KategoriModel> imageModelArrayList;

    public KategoriAdapter(Context ctx, ArrayList<KategoriModel> imageModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public KategoriAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.kategori_elektronik, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(KategoriAdapter.MyViewHolder holder, int position) {
        holder.iv.setImageResource(imageModelArrayList.get(position).getImage());
        //holder.text.setText(imageModelArrayList.get(position).getName());
//        holder.cv_elektronik.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), InputDatDiri.class);
//                intent.putExtra("key", "Elektronik");
//                view.getContext().startActivity(intent);
//                ((Activity) view.getContext()).overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView text;
        CardView cv_elektronik;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            //text = (TextView) itemView.findViewById(R.id.text);
            cv_elektronik = (CardView) itemView.findViewById(R.id.cv_elektronik);
        }

    }
}
