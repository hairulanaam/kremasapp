package com.project.angsurapp.LogRes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.project.angsurapp.Dashboard;
import com.project.angsurapp.Api;
import com.project.angsurapp.LupaPassword;
import com.project.angsurapp.LupaPassword2;
import com.project.angsurapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText username, password;

    private Button login, BTN_OK;
    Dialog progressDialog;
    TextView lupapw;
    TextView daftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        daftar = findViewById(R.id.daftar);
        lupapw = findViewById(R.id.lupapw);

        Dialog progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernamee = username.getText().toString();
                String passwordd = password.getText().toString();

                if (! (usernamee.isEmpty()) || (passwordd.isEmpty())) {
                    progressDialog.show();
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.urlLogin + "?username=" + usernamee + "&password=" + passwordd, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equals("0")) {
                                logsalah();
                            } else {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String namaAkun = jsonResponse.getString("nama");
                                    String emailAkun = jsonResponse.getString("email");
                                    String noAkun = jsonResponse.getString("no_telfon");
                                    String kodeAkun = jsonResponse.getString("kode_pegawai");
                                    String gambarURL = jsonResponse.getString("foto_pegawai");

                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("nama_akun", namaAkun);
                                    editor.putString("email_akun", emailAkun);
                                    editor.putString("no_akun", noAkun);
                                    editor.putString("kode_pegawai", kodeAkun);
                                    editor.putString("foto_pegawai", gambarURL);
                                    editor.apply();

                                    showAlert();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(stringRequest);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lupapw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, LupaPassword2.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }
    public void showAlert() {
        View dialogView = LayoutInflater.from(Login.this).inflate(R.layout.alert_login, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setView(dialogView);

        MaterialTextView BTN_OK = dialogView.findViewById(R.id.btntutup);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        BTN_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
    }

    public void logsalah() {
        View dialogView = LayoutInflater.from(Login.this).inflate(R.layout.alert_logsalah, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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