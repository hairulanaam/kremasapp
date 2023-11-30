package com.project.angsurapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textview.MaterialTextView;
import com.project.angsurapp.LogRes.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomShet extends BottomSheetDialogFragment {
    TextView sisa, total, nama, nama_barang, detail_barang, tgl_tempo, perbulan, angsuran_ke, dendaa, dp;
    Button bayar_button;
    EditText bayar_et;
    ImageView riwayat_bayar;
    ListView listView;
    PaymentHistoryAdapter historyAdapter;
    private boolean isRiwayatBayarOpened ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_shet, container, false);

        sisa = view.findViewById(R.id.sisa);
        total = view.findViewById(R.id.total);
        nama = view.findViewById(R.id.nama);
        nama_barang = view.findViewById(R.id.nama_barang);
        detail_barang = view.findViewById(R.id.detail_barang);
        tgl_tempo = view.findViewById(R.id.tgl_tempo);
        perbulan = view.findViewById(R.id.perbulan);
        dp = view.findViewById(R.id.dp);
        angsuran_ke = view.findViewById(R.id.angsuran_ke);
        dendaa = view.findViewById(R.id.dendaa);
        bayar_button = view.findViewById(R.id.bayar_button);
        bayar_et = view.findViewById(R.id.bayar_et);
        riwayat_bayar = view.findViewById(R.id.riwayat_bayar);
        listView = view.findViewById(R.id.listView);
        historyAdapter = new PaymentHistoryAdapter(getContext(), new ArrayList<>());
        listView.setAdapter(historyAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("data");
            try {
                JSONObject jsonObject = new JSONObject(data);
                String sisaAngsuran = jsonObject.getString("sisa_angsuran");
                String totalAngsuran = jsonObject.getString("total_angsuran");
                String lamaAngsuran = jsonObject.getString("lama_angsuran");
                String perbulanAngsuran = jsonObject.getString("angsuran_perbulan");
                String dpAngsuran = jsonObject.getString("jumlah_dp");
                String angsuranke = jsonObject.getString("angsuran_ke");
                String jatuhtempo = jsonObject.getString("tgl_jatuhtempo");
                String noKtp = jsonObject.getString("no_ktp");
                String namapelanggan = jsonObject.getString("nama_pelanggan");
                String namabarang = jsonObject.getString("nama_barang");
                String detailbarang = jsonObject.getString("detail_barang");
                String denda = jsonObject.getString("denda");

                int angsuranPerBulan = 0;
                switch (lamaAngsuran) {
                    case "3 Bulan":
                        angsuranPerBulan =  3;
                        break;
                    case "6 Bulan":
                        angsuranPerBulan =  6;
                        break;
                    case "12 Bulan":
                        angsuranPerBulan =  12;
                        break;
                }

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                numberFormat.setMaximumFractionDigits(0);
                sisa.setText(numberFormat.format(Integer.parseInt(sisaAngsuran)));
                total.setText(numberFormat.format(Integer.parseInt(totalAngsuran)));
                dp.setText(numberFormat.format(Integer.parseInt(dpAngsuran)));
                perbulan.setText(numberFormat.format(Integer.parseInt(perbulanAngsuran)));
                dendaa.setText(numberFormat.format(Integer.parseInt(denda)));
                angsuran_ke.setText(angsuranke+" dari "+angsuranPerBulan);
                tgl_tempo.setText(jatuhtempo+" setiap bulan");
                nama.setText(namapelanggan);
                nama_barang.setText(namabarang);
                detail_barang.setText(detailbarang);

                riwayat_bayar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRiwayatBayarOpened) {
                            listView.setVisibility(View.GONE);
                            isRiwayatBayarOpened = false;
                        } else {
                            fetchPaymentHistory(noKtp);
                            listView.setVisibility(View.VISIBLE);
                            isRiwayatBayarOpened = true;
                        }
                    }
                });


                bayar_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if  (Integer.parseInt(sisaAngsuran) == 0 && Integer.parseInt(totalAngsuran) == 0 && Integer.parseInt(perbulanAngsuran) == 0) {
                            bayar();
                            bayar_button.setEnabled(false);
                        } else {
                            int paymentAmount = Integer.parseInt(bayar_et.getText().toString().trim());
                            int installmentPerMonth = Integer.parseInt(perbulanAngsuran);
                            int dendabayar = Integer.parseInt(denda);

                            if (paymentAmount == installmentPerMonth + dendabayar) {
                                getDataFromMySQL(noKtp);
                            } else {
                                nobayar();
                            }
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
        return view;
    }



private void getDataFromMySQL(String noKtp) {
    String urlGetData = "http://krediraka.tifa.myhost.id/krediraka/tes/api_getdata.php?no_ktp=" + noKtp;

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlGetData, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String noktp = response.getString("no_ktp");
                        String kodeAngsuran = response.getString("kode_angsuran");
                        String denda = response.getString("denda");

                        sendPaymentData(noktp, kodeAngsuran, denda);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error getting data from MySQL: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    Volley.newRequestQueue(getContext()).add(request);
}

    private void sendPaymentData(String noktp, String kodeAngsuran, String denda) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String kodeAkun = sharedPreferences.getString("kode_pegawai", "Akun tidak ditemukan");
        JSONObject paymentData = new JSONObject();
        try {
            paymentData.put("jumlah_bayar", bayar_et.getText().toString().trim());
            paymentData.put("no_ktp", noktp);
            paymentData.put("kode_angsuran", kodeAngsuran);
            paymentData.put("denda", denda);
            paymentData.put("kode_pegawai", kodeAkun);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Api.bayar, paymentData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showAlert();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(request);
    }

private void fetchPaymentHistory(String noKtp) {
    String url = "http://krediraka.tifa.myhost.id/krediraka/tes/api_riw.php?no_ktp=" + noKtp;

    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("hasil")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("hasil");
                            List<PaymentItem> paymentItems = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject paymentObject = jsonArray.getJSONObject(i);
                                String tglBayar = paymentObject.getString("tgl_bayar");
                                String angsuranKe = paymentObject.getString("angsuran_ke");
                                Integer dendaValue = paymentObject.getInt("denda");
                                Integer jumlahValue = paymentObject.getInt("jumlah_bayar");
                                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                numberFormat.setMaximumFractionDigits(0);
                                String denda = numberFormat.format(dendaValue);
                                String jumlahBayar = numberFormat.format(jumlahValue);
                                PaymentItem paymentItem = new PaymentItem(tglBayar, jumlahBayar, angsuranKe, denda);
                                paymentItems.add(paymentItem);
                            }
                            historyAdapter.clear();
                            historyAdapter.addAll(paymentItems);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("fetchPaymentHistory", "'hasil' key not found in JSON response");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("fetchPaymentHistory", "Error parsing JSON response");
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
    requestQueue.add(stringRequest);
}


    public void showAlert() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_pembayaran, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        MaterialTextView BTN_OK = dialogView.findViewById(R.id.btntutup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                dismiss();
            }
        });
    }

    public void bayar() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_bayar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        MaterialTextView BTN_OK = dialogView.findViewById(R.id.btntutup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void nobayar() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_nobayar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView);

        MaterialTextView BTN_OK = dialogView.findViewById(R.id.btntutup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}