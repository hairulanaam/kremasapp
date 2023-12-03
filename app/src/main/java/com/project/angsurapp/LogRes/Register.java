package com.project.angsurapp.LogRes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.project.angsurapp.Api;
import com.project.angsurapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText username, password, nama, email, no_telfon;
    private Button register;
    private TextView login;
    ImageView gambar_et;
    LinearLayout back;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        no_telfon = findViewById(R.id.no_telfon);
        gambar_et = findViewById(R.id.gambar_et);
        register = findViewById(R.id.register);

        Dialog progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        ActivityResultLauncher<Intent> activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                gambar_et.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

        gambar_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent) ;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                    String usernamee = username.getText().toString();
                    String passwordd = password.getText().toString();
                    String namaa = nama.getText().toString();
                    String emaill = email.getText().toString();
                    String nomerr = no_telfon.getText().toString();
                    if (!nomerr.startsWith("+62")) {
                        nomerr = "+62" + nomerr;
                    }
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    String finalNomerr = nomerr;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.urlRegister,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    if (response.equals("terdaftar")) {
                                        Toast.makeText(getApplicationContext(), "Nama sudah terdaftar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        showAlert();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), error.toString() , Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String, String> getParams(){
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("username", usernamee);
                            paramV.put("password", passwordd);
                            paramV.put("nama", namaa);
                            paramV.put("email", emaill);
                            paramV.put("no_handphone", finalNomerr);
                            paramV.put("image", base64Image);
                            return paramV;

                        }
                    };
                    queue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Lengkapi seluruh data anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }
    public void showAlert() {
        View dialogView = LayoutInflater.from(Register.this).inflate(R.layout.alert_register, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
}