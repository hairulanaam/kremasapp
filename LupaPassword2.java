package com.project.angsurapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LupaPassword2 extends AppCompatActivity {
    Button lupa_button;
    EditText lupa_et;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password2);

        lupa_et = findViewById(R.id.lupa_et);
        lupa_button = findViewById(R.id.lupa_button);
        lupa_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = lupa_et.getText().toString().trim();
                if (!no.startsWith("+62")) {
                    no = "+62" + no;
                }
                showProgressDialog();
                send(no);
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private void send(final String no) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.lupapassword2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissProgressDialog();
                        if (response != null && response.contains("Nomor handphone tidak terdaftar.")) {
                            showNotRegisteredNotification();
                        } else {
                            showAlert(no);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_handphone", no);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showNotRegisteredNotification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LupaPassword2.this);
        builder.setMessage("Nomor handphone tidak terdaftar.")
                .setPositiveButton("OK", null)
                .create()
                .show();
    }
    public void showAlert(String no) {
        View dialogView = LayoutInflater.from(LupaPassword2.this).inflate(R.layout.alert_kode, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LupaPassword2.this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Intent intent = new Intent(LupaPassword2.this, VerificationCode.class);
        intent.putExtra("no_handphone", no);
        alertDialog.cancel();
        startActivity(intent);
    }
}