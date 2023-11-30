package com.project.angsurapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class RiwayatFragment extends Fragment {
    ListView list_riwayat;
    TextView selectedDate;
    Button datePicker;
    String startDateString = "";
    String endDateString = "";
    Dialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);
        list_riwayat = view.findViewById(R.id.list_riwayat);

        datePicker = view.findViewById(R.id.datePicker);
        selectedDate = view.findViewById(R.id.selectedDate);

        progressDialog = new Dialog(requireContext());
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        riwayat();
        return  view;
    }

    private void showDatePickerDialog() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");


        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        progressDialog.show();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Long startDate = selection.first;
                Long endDate = selection.second;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                startDateString = sdf.format(new Date(startDate));
                endDateString = sdf.format(new Date(endDate));

                String selectedDateRange = startDateString + " - " + endDateString;
                selectedDate.setText(selectedDateRange);

                riwayat();
            }
        });
        datePicker.addOnDismissListener(dialog -> progressDialog.dismiss());
        datePicker.show(getChildFragmentManager(), "DATE_PICKER");
    }
    ArrayList<ObjectRiwayat> model;


    void riwayat() {
        progressDialog.show();
        String apiUrl = Api.riwayat + "?start_date=" + startDateString + "&end_date=" + endDateString;
        if (startDateString.isEmpty() && endDateString.isEmpty()) {
            apiUrl = Api.riwayat;
        } else {
            apiUrl = Api.riwayat + "?start_date=" + startDateString + "&end_date=" + endDateString;
        }
        StringRequest stringRequest3 = new StringRequest(
                Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model = new ArrayList<>();
                    for (int i = 0; i < getHasil.length(); i++) {
                        JSONObject getData = getHasil.getJSONObject(i);
                        String nama = getData.getString("nama_pelanggan");
                        String tanggal = getData.getString("tgl_bayar");

                        Integer jumlahBayar = getData.getInt("jumlah_bayar");
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                        numberFormat.setMaximumFractionDigits(0);
                        String jumlah = numberFormat.format(jumlahBayar);
                        String angsurke = getData.getString("angsuran_ke");
                        String barang = "Nama Brang : "+getData.getString("nama_barang");
                        model.add(new ObjectRiwayat(nama, tanggal, jumlah, angsurke, barang));
                    }
                    adapter_riwayat adapter = new adapter_riwayat(getActivity(), model);
                    list_riwayat.setAdapter(adapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "gagal", Toast.LENGTH_SHORT).show();
            }
        }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest3);
    }
}

class adapter_riwayat extends BaseAdapter {
    ArrayList<ObjectRiwayat> model;
    LayoutInflater inflater;
    Context context;
    ListView list_pengajuan;

    adapter_riwayat(Context context, ArrayList<ObjectRiwayat> model) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public ObjectRiwayat getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView nama, tanggal, jumlah, angsurke, barang;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_riwayat, parent, false);

        nama = view.findViewById(R.id.nama);
        tanggal = view.findViewById(R.id.tanggal);
        jumlah = view.findViewById(R.id.jumlah);
        angsurke = view.findViewById(R.id.angsurke);
        barang = view.findViewById(R.id.barang);

        nama.setText(model.get(position).getNama());
        tanggal.setText(model.get(position).getTanggal());
        jumlah.setText(model.get(position).getJumlah());
        angsurke.setText(model.get(position).getAngsurke());
        barang.setText(model.get(position).getBarang());

        return view;
    }
}