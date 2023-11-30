package com.project.angsurapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PaymentHistoryAdapter extends ArrayAdapter<PaymentItem> {
    public PaymentHistoryAdapter(Context context, List<PaymentItem> paymentItems) {
        super(context, 0, paymentItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PaymentItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_payment_history, parent, false);
        }

        TextView tglBayar = convertView.findViewById(R.id.tanggal_item);
        TextView jumlahBayar = convertView.findViewById(R.id.jumlah_item);
        TextView angsuranKe = convertView.findViewById(R.id.angsuran_item);
        TextView dendaaa = convertView.findViewById(R.id.denda_item);

        tglBayar.setText(item.getTglBayar());
        jumlahBayar.setText(item.getJumlahBayar());
        angsuranKe.setText("Angsuran Ke : " + item.getAngsuranKe());
        dendaaa.setText("Denda : Rp. " + item.getDenda_angsur());

        return convertView;
    }
}

