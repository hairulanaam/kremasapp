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

public class KategoriPerabotan extends RecyclerView.Adapter<KategoriPerabotan.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<KategoriModel> imageModelArrayList;

    public KategoriPerabotan(Context ctx, ArrayList<KategoriModel> imageModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public KategoriPerabotan.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.kategori_perabotan, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(KategoriPerabotan.MyViewHolder holder, int position) {
        holder.iv2.setImageResource(imageModelArrayList.get(position).getImage());
        holder.textt.setText(imageModelArrayList.get(position).getName());
        holder.cv_perabotan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InputDatDiri.class);
                intent.putExtra("key", "Perabotan");
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv2;
        TextView textt;
        CardView cv_perabotan;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv2 = (ImageView) itemView.findViewById(R.id.iv2);
            textt = (TextView) itemView.findViewById(R.id.textt);
            cv_perabotan = (CardView) itemView.findViewById(R.id.cv_perabotan);
        }

    }
}
