package com.project.angsurapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DaftarPelanggan extends AppCompatActivity {
    LinearLayout back2;
    EditText search2;
    ListView listPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pelanggan);

        listPelanggan = findViewById(R.id.listPelanggan);
        search2 = findViewById(R.id.search2);
        back2 = findViewById(R.id.back2);

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaftarPelanggan.this, Dashboard.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tampilkanSemuaPelanggan();
        search2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cari_data2(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void tampilkanSemuaPelanggan() {
        cari_data2("");
    }

    ArrayList<Object2> model;
    void cari_data2(String data_cari2) {
        String url = "http://krediraka.tifa.myhost.id/krediraka/tes/api_daftarplg.php?data="+data_cari2;
        StringRequest stringRequest2 = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasill");
                    model = new ArrayList<>();
                    for (int i=0; i<getHasil.length(); i++) {
                        JSONObject getData = getHasil.getJSONObject(i);
                        String nama_pelanggan = getData.getString("nama_pelanggan");
                        String no_hp = "No Hp : "+getData.getString("no_hp");
                        String alamat = "Alamat : "+getData.getString("alamat");

                        Integer sisaAngsuranValue = getData.getInt("sisa_angsuran");
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                        numberFormat.setMaximumFractionDigits(0);
                        String sisa_angsuran = numberFormat.format(sisaAngsuranValue);
                        model.add(new Object2(nama_pelanggan, no_hp, alamat, sisa_angsuran));
                    }
                    adapter2 adapter = new adapter2(DaftarPelanggan.this, model);
                    listPelanggan.setAdapter(adapter);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaftarPelanggan.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest2);
    }
}

class adapter2 extends BaseAdapter {
    ArrayList<Object2> model;
    LayoutInflater inflater;
    Context context;

    adapter2(Context context, ArrayList<Object2> model) {
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.model=model;
    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object2 getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView nama_pelanggan, no_hp, alamat, sisa_angsuran;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_pelanggan, parent, false);
        nama_pelanggan=view.findViewById(R.id.nama_pelanggan);
        no_hp=view.findViewById(R.id.no_hp);
        alamat=view.findViewById(R.id.alamat);
        sisa_angsuran=view.findViewById(R.id.sisa_angsuran);

        nama_pelanggan.setText(model.get(position).getNama_pelanggan());
        no_hp.setText(model.get(position).getNo_hp());
        alamat.setText(model.get(position).getAlamat());
        sisa_angsuran.setText(model.get(position).getSisa_angsuran());
        return view;
    }
}