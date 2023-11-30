package com.project.angsurapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TelatAdapter extends BaseAdapter {
    private Context context;
    private List<ObjectTelat> objectTelatList;

    public TelatAdapter(Context context, List<ObjectTelat> objectTelatList) {
        this.context = context;
        this.objectTelatList = objectTelatList;
    }

    @Override
    public int getCount() {
        return objectTelatList.size();
    }

    @Override
    public ObjectTelat getItem(int position) {
        return objectTelatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_telat, parent, false);
        }

        ObjectTelat objectTelat = objectTelatList.get(position);

        TextView nama = convertView.findViewById(R.id.nama_et);
        TextView tgl = convertView.findViewById(R.id.tgl_et);
        TextView denda = convertView.findViewById(R.id.denda_et);

        nama.setText(objectTelat.getNama_pelanggan());
        tgl.setText(objectTelat.getTgl_jatuhtempo());
        denda.setText(objectTelat.getDenda());

        return convertView;
    }

    public void setData(List<ObjectTelat> newData) {
        objectTelatList.clear();
        objectTelatList.addAll(newData);
        notifyDataSetChanged();
    }
}
