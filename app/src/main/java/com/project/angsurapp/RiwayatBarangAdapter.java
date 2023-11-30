package com.project.angsurapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RiwayatBarangAdapter extends BaseAdapter {
    private Context context;
    private List<ObjectRiwayatBarang> objectRiwayatList;

    public RiwayatBarangAdapter(Context context, List<ObjectRiwayatBarang> objectRiwayatList) {
        this.context = context;
        this.objectRiwayatList = objectRiwayatList;
    }

    @Override
    public int getCount() {
        return objectRiwayatList.size();
    }

    @Override
    public ObjectRiwayatBarang getItem(int position) {
        return objectRiwayatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.riwayat_barang, parent, false);
        }

        ObjectRiwayatBarang objectRiwayatBarang = objectRiwayatList.get(position);

        TextView nama = convertView.findViewById(R.id.nama_barang);
        TextView jenis = convertView.findViewById(R.id.jenis_barang);
        TextView total = convertView.findViewById(R.id.harga);

        nama.setText(objectRiwayatBarang.getNama_barang());
        jenis.setText(objectRiwayatBarang.getJenis_barang());
        total.setText(objectRiwayatBarang.getHarga_jual());

        return convertView;
    }

    public void setData(List<ObjectRiwayatBarang> newData) {
        objectRiwayatList.clear();
        objectRiwayatList.addAll(newData);
        notifyDataSetChanged();
    }
}
