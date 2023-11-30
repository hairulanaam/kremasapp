package com.project.angsurapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;

public class TransaksiFragment extends Fragment {
    TextView pengajuan_text, aktif_text;
    Button pengajuan, aktif, detail_angsuran, bayar_button;
    EditText search_pengajuan, bayar_et;
    ListView list_pengajuan;
    Dialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);
        list_pengajuan = view.findViewById(R.id.list_pengajuan);
        pengajuan_text = view.findViewById(R.id.pengajuan_text);
        aktif_text = view.findViewById(R.id.aktif_text);

        progressDialog = new Dialog(getActivity());
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        pengajuan_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengajuan_text.setBackgroundResource(R.drawable.active);
                pengajuan_text.setTextColor(Color.WHITE);
                aktif_text.setBackgroundResource(R.drawable.nonactive);
                aktif_text.setTextColor(Color.BLACK);
                tampilkanSemua("pengajuan");
            }
        });

        aktif_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pengajuan_text.setBackgroundResource(R.drawable.nonactive);
                pengajuan_text.setTextColor(Color.BLACK);
                aktif_text.setBackgroundResource(R.drawable.active);
                aktif_text.setTextColor(Color.WHITE);
                tampilkanSemua("aktif");
            }
        });
        pengajuan_text.setBackgroundResource(R.drawable.active);
        pengajuan_text.setTextColor(Color.WHITE);
        aktif_text.setBackgroundResource(R.drawable.nonactive);
        aktif_text.setTextColor(Color.BLACK);
        tampilkanSemua("pengajuan");

        return view;

    }

    void tampilkanSemua(String status) {
        showProgressDialog();
        cari_pengajuan(status);
    }


    ArrayList<ObjectTrs> model;
    void cari_pengajuan(String status) {
        String url = "http://krediraka.tifa.myhost.id/krediraka/tes/api_daftarpengajuan.php?data="+ "&status=" + status;
        StringRequest stringRequest2 = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray getHasil = jsonObject.getJSONArray("hasil");
                    model = new ArrayList<>();
                    for (int i=0; i<getHasil.length(); i++) {
                        JSONObject getData = getHasil.getJSONObject(i);
                        String nama_pelanggan = getData.getString("nama_pelanggan");
                        String no_ktp = getData.getString("no_ktp");
                        String jenis_barang = "Jenis Barang : " + getData.getString("jenis_barang");
                        model.add(new ObjectTrs(nama_pelanggan,no_ktp, jenis_barang));
                    }
                    adapter_trs adapter = new adapter_trs(getActivity(), model);
                    list_pengajuan.setAdapter(adapter);
                    hideProgressDialog();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "gagal", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest2);
    }

    private void showProgressDialog() {
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }
}

class adapter_trs extends BaseAdapter {
    ArrayList<ObjectTrs> model;
    LayoutInflater inflater;
    Context context;
    Button detail_angsuran;
    ListView list_pengajuan;

    adapter_trs(Context context, ArrayList<ObjectTrs> model) {
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.model=model;
    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public ObjectTrs getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView nama, no_ktp, jenis_barang, sisa, total;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_pengajuan, parent, false);

        nama=view.findViewById(R.id.nama);
        no_ktp=view.findViewById(R.id.no_ktp);
        jenis_barang=view.findViewById(R.id.jenis_barang);

        nama.setText(model.get(position).getNama());
        no_ktp.setText(model.get(position).getNo_ktp());
        jenis_barang.setText(model.get(position).getJenis_barang());

        detail_angsuran = view.findViewById(R.id.detail_angsuran);
        detail_angsuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataAndShowBottomSheet(model.get(position));
            }
        });
        return view;
    }

    private void fetchDataAndShowBottomSheet(ObjectTrs data) {
        String url = "http://krediraka.tifa.myhost.id/krediraka/tes/api_angsuran.php?no_ktp=" + data.getNo_ktp();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showBottomSheetWithData(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showBottomSheetWithData(String data) {
        BottomShet bottomSheet = new BottomShet();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bottomSheet.setArguments(bundle);
        bottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), bottomSheet.getTag());
    }


}