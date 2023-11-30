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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.project.angsurapp.LogRes.Login;

import java.util.HashMap;
import java.util.Map;

public class AturPassword extends AppCompatActivity {
    EditText passwordbaru, konfirmasipassword;
    Button savebutton;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_password);

        passwordbaru = findViewById(R.id.passwordbaru);
        konfirmasipassword = findViewById(R.id.konfirmasipassword);
        savebutton = findViewById(R.id.savebutton);

        Dialog progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordBaru = passwordbaru.getText().toString().trim();
                String konfirmasiPassword = konfirmasipassword.getText().toString().trim();


                if (passwordBaru.equals(konfirmasiPassword)) {
                    showProgressDialog();
                    updatePassword(passwordBaru);
                } else {
                    logsalah();
                }
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
    private void updatePassword(final String newPassword) {
        String email = getIntent().getStringExtra("email");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.aturpassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissProgressDialog();
                        showAlert();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                        logsalah();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", newPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showAlert() {
        View dialogView = LayoutInflater.from(AturPassword.this).inflate(R.layout.alert_password, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AturPassword.this);
        builder.setView(dialogView);

        MaterialTextView BTN_OK = dialogView.findViewById(R.id.btntutup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    public void logsalah() {
        View dialogView = LayoutInflater.from(AturPassword.this).inflate(R.layout.alert_passwordsalah, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(AturPassword.this);
        builder.setView(dialogView);

        MaterialTextView BTN_OK = dialogView.findViewById(R.id.btntutup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }
}