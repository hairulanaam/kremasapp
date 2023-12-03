package com.project.angsurapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
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

public class VerificationCode extends AppCompatActivity {
    Button verifikasi_button;
    EditText verifikasi_et;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);

        verifikasi_button = findViewById(R.id.verifikasi_button);
        verifikasi_et = findViewById(R.id.verifikasi_et);

        Dialog progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        String no = getIntent().getStringExtra("no_handphone");
        verifikasi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = verifikasi_et.getText().toString().trim();
                showProgressDialog();
                verifyOtp(no, enteredOtp);
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

    private void verifyOtp(final String no, final String enteredOtp) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.verifikasipassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dismissProgressDialog();
                        if (response.equals("success")) {
                            showAlert(no);

                        } else {
                            showAlertsalah(no);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                        Toast.makeText(VerificationCode.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_handphone", no);
                params.put("kode", enteredOtp);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showAlert(String no) {
        View dialogView = LayoutInflater.from(VerificationCode.this).inflate(R.layout.alert_otp, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationCode.this);
        builder.setView(dialogView);


        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        Intent intent = new Intent(VerificationCode.this, AturPassword.class);
        intent.putExtra("no_handphone", no);
        alertDialog.cancel();
        startActivity(intent);
    }

    public void showAlertsalah(String no) {
        View dialogView = LayoutInflater.from(VerificationCode.this).inflate(R.layout.alert_otpsalah, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationCode.this);
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
    private void showOtpMismatchAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationCode.this);
        builder.setMessage("Kode OTP salah, masukkan kembali kode OTP yang benar")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        verifikasi_et.setText("");
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}