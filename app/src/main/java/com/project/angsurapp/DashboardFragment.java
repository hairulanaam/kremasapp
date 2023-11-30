package com.project.angsurapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    RecyclerView kategori, kategori2;
    ArrayList<KategoriModel> imageModelArrayList;
    ArrayList<KategoriModel> imageModelArrayList2;
    KategoriAdapter adapter;
    KategoriPerabotan adapter2;
    TextView nama_akun, total_orang, total_angsuran, total_pengajuan;
    CardView daftar_barang, daftar_pelanggan, card_elektronik, card_perabotan, card_gadget;
    ListView list_datatelat, list_riwayatbarang;
    TelatAdapter telatAdapter;
    RiwayatBarangAdapter riwayatAdapter;
    private int[] myImageList = new int[]{R.drawable.cardbg, R.drawable.cardbg,R.drawable.cardbg};
//    private String[] myImageNameList = new String[]{"Televisi","Kulkas" ,"Mesin Cuci","Handphone","Laptop","Kipas Angin"};
//    private int[] myImageList2 = new int[]{R.drawable.sofa, R.drawable.springbed,R.drawable.kitchenset, R.drawable.lemari};
//    private String[] myImageNameList2 = new String[]{"Sofa","Springbed","Kitchen Set","Lemari"};

    private int currentPage = 1;    private boolean hasMore = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        list_datatelat = view.findViewById(R.id.list_datatelat);
        telatAdapter = new TelatAdapter(getContext(), new ArrayList<>());
        list_datatelat.setAdapter(telatAdapter);

        list_riwayatbarang = view.findViewById(R.id.list_riwayatbarang);
        riwayatAdapter = new RiwayatBarangAdapter(getContext(), new ArrayList<>());
        list_riwayatbarang.setAdapter(riwayatAdapter);

        total_orang = view.findViewById(R.id.total_orang);
        total_angsuran = view.findViewById(R.id.total_angsuran);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.jml_angsuran, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int totalRows = response.getInt("total_rows");
                            int totalValue = response.getInt("total_nilai");

                            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                            numberFormat.setMaximumFractionDigits(0);
                            String formattedTotal = numberFormat.format(totalValue);
                            total_orang.setText(String.valueOf(totalRows));
                            total_angsuran.setText(formattedTotal);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

        total_pengajuan= view.findViewById(R.id.total_pengajuan);

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, Api.jml_pengajuan, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int totalRows = response.getInt("total_rows");
                            total_pengajuan.setText(String.valueOf(totalRows));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity());
        requestQueue2.add(request2);

        card_elektronik = view.findViewById(R.id.card_elektronik);
        card_elektronik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InputDatDiri.class);
                intent.putExtra("key", "Elektronik");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
            }
        });

        card_perabotan = view.findViewById(R.id.card_perabotan);
        card_perabotan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InputDatDiri.class);
                intent.putExtra("key", "Perabotan");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
            }
        });

        card_gadget = view.findViewById(R.id.card_gadget);
        card_gadget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InputDatDiri.class);
                intent.putExtra("key", "Gadget");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
            }
        });

        daftar_pelanggan = view.findViewById(R.id.daftar_pelanggan);
        daftar_pelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DaftarPelanggan.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String namaAkun = sharedPreferences.getString("nama_akun", "Akun tidak ditemukan");
        String gambarURL = sharedPreferences.getString("foto_pegawai", null);

        TextView namaAkunTextView = view.findViewById(R.id.nama_akun);
        namaAkunTextView.setText(namaAkun);

        if (gambarURL != null) {
            String fotoPegawaiUrl = Api.foto + gambarURL;
            ImageView fotoPegawaiImageView = view.findViewById(R.id.gambar_et);
            Glide.with(this).load(fotoPegawaiUrl).into(fotoPegawaiImageView);
        }

        TextView textDataSekarang = view.findViewById(R.id.textDataSekarang);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);

        textDataSekarang.setText(formattedDate);
        telat();
        riwayat_barang();
        return view;
    }

    private void telat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.telat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("hasil")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("hasil");
                                List<ObjectTelat> objectTelats = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject telatObject = jsonArray.getJSONObject(i);
                                    String nama_pelanggan = telatObject.getString("nama_pelanggan");
                                    String tgl_jatuhtempo = telatObject.getString("tgl_jatuhtempo");

                                    Integer dendaValue = telatObject.getInt("denda");
                                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                    numberFormat.setMaximumFractionDigits(0);
                                    String denda = numberFormat.format(dendaValue);
                                    ObjectTelat telatItem = new ObjectTelat(nama_pelanggan,tgl_jatuhtempo,denda);
                                    objectTelats.add(telatItem);
                                }

                                telatAdapter.setData(objectTelats);
                                telatAdapter.notifyDataSetChanged();

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

    private void riwayat_barang() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.riwayatbarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("hasil")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("hasil");
                                List<ObjectRiwayatBarang> objectRiwayats = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject riwayatObject = jsonArray.getJSONObject(i);
                                    if (!riwayatObject.getString("nama_barang").equals("Belum Ada")) {
                                        String nama_barang = riwayatObject.getString("nama_barang");
                                        String jenis_barang = riwayatObject.getString("jenis_barang");

                                        Integer hargaValue = riwayatObject.getInt("harga_jual");
                                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                                        numberFormat.setMaximumFractionDigits(0);
                                        String harga_jual = numberFormat.format(hargaValue);
                                        ObjectRiwayatBarang riwayatItem = new ObjectRiwayatBarang(nama_barang, jenis_barang, harga_jual);
                                        objectRiwayats.add(riwayatItem);
                                    }
                                }

                                riwayatAdapter.setData(objectRiwayats);
                                riwayatAdapter.notifyDataSetChanged();

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

}