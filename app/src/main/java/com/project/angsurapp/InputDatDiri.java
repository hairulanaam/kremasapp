package com.project.angsurapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.project.angsurapp.LogRes.Login;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InputDatDiri extends AppCompatActivity {
    Button nextButton;
    LinearLayout back;
    EditText nama_et, no_ktp_et, no_hp_et, alamat_et, jenisbrg_et, detail_et, tanggal_et, jumlahdp_et;
    ImageView gambar_et;
    Bitmap bitmap;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_diri);

        nama_et = findViewById(R.id.nama_et);
        no_ktp_et = findViewById(R.id.no_ktp_et);
        no_hp_et = findViewById(R.id.no_hp_et);
        alamat_et = findViewById(R.id.alamat_et);
        gambar_et = findViewById(R.id.gambar_et);
        jenisbrg_et = findViewById(R.id.jenisbrg_et);
        jenisbrg_et.setEnabled(false);
        detail_et = findViewById(R.id.detail_et);
        jumlahdp_et = findViewById(R.id.jumlahdp_et);
        nextButton = findViewById(R.id.nextButton);
        tanggal_et = findViewById(R.id.tanggal_et);

        no_ktp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateNoKTP(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        jumlahdp_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateJumlahDP(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Dialog progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();

        if (intent.hasExtra("key")) {
            String value = intent.getStringExtra("key");
            jenisbrg_et.setText(value);
        }

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

        tanggal_et = findViewById(R.id.tanggal_et);
        tanggal_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream = new ByteArrayOutputStream();
                if(bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

                    String no_ktp=no_ktp_et.getText().toString();
                    String nama=nama_et.getText().toString();
                    String no_hp=no_hp_et.getText().toString();
                    String alamat=alamat_et.getText().toString();
                    String jenis=jenisbrg_et.getText().toString();
                    String detail=detail_et.getText().toString();
                    String jumlahDp=jumlahdp_et.getText().toString();
                    String tanggal = tanggal_et.getText().toString();

                    if (!jumlahDp.isEmpty()) {
                        int nominalDP = Integer.parseInt(jumlahDp);
                        if (nominalDP >= 200000) {

                        } else {
                            terdaftar();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Jumlah DP tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    }
                    RadioGroup jenisAngsuran = findViewById(R.id.jenis_angsuran);
                    int selectedRadioButtonId = jenisAngsuran.getCheckedRadioButtonId();
                    String jenisAngsuranValue = "";

                    if (selectedRadioButtonId != -1) {
                        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                        jenisAngsuranValue = selectedRadioButton.getText().toString();
                    } else {
                        Toast.makeText(getApplicationContext(), "Pilih jenis angsuran", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String kodeAkun = sharedPreferences.getString("kode_pegawai", "Akun tidak ditemukan");


                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String finalJenisAngsuranValue = jenisAngsuranValue;

                    if (!jumlahDp.isEmpty()) {
                        int nominalDP = Integer.parseInt(jumlahDp);
                        if (nominalDP >= 200000) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.input_data,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            progressDialog.dismiss();
                                            switch (response) {
                                                case "Ada":
                                                    terdaftar();
                                                    break;
                                                default:
                                                    showAlert();
                                                    break;
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                protected Map<String, String> getParams() {
                                    Map<String, String> paramV = new HashMap<>();
                                    paramV.put("no_ktp", no_ktp);
                                    paramV.put("nama_pelanggan", nama);
                                    paramV.put("no_hp", no_hp);
                                    paramV.put("alamat", alamat);
                                    paramV.put("jenis_barang", jenis);
                                    paramV.put("detail_barang", detail);
                                    paramV.put("jumlah_dp", jumlahDp);
                                    paramV.put("jenis_angsuran", finalJenisAngsuranValue);
                                    paramV.put("image", base64Image);
                                    paramV.put("kode_pegawai", kodeAkun);
                                    paramV.put("tgl_jatuhtempo", tanggal);
                                    return paramV;
                                }
                            };
                            queue.add(stringRequest);
                        } else {
                            progressDialog.dismiss();
                            dp();
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Lengkapi Seluruh Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputDatDiri.this, Dashboard.class));
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            String selectedDay = String.valueOf(dayOfMonth);
            tanggal_et.setText(selectedDay);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void validateNoKTP(String noKTP) {
        if (noKTP.isEmpty()) {
            no_ktp_et.setError("Nomor KTP tidak boleh kosong");
        } else {
            new CheckKTPTask().execute(noKTP);
        }
    }

    private class CheckKTPTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String noKTP = params[0];
            return checkKTPInDatabase(noKTP);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("exists")) {
                no_ktp_et.setError("Nomor KTP sudah terdaftar");
            } else {
                no_ktp_et.setError(null);
            }
        }
    }

    private String checkKTPInDatabase(String noKTP) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api.cekktp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("exists")) {
                            no_ktp_et.setError("Nomor KTP sudah terdaftar");
                        } else {
                            no_ktp_et.setError(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no_ktp", noKTP);
                return params;
            }
        };

        queue.add(stringRequest);

        return "request_sent";
    }

    private void validateJumlahDP(String jumlahDP) {
        if (!jumlahDP.isEmpty()) {
            int nominalDP = Integer.parseInt(jumlahDP);
            if (nominalDP < 200000) {
                jumlahdp_et.setError("Minimal Jumlah Dp 200,000");
            } else {
                jumlahdp_et.setError(null);
            }
        } else {
            jumlahdp_et.setError("Jumlah DP tidak boleh kosong");
        }
    }
    public void showAlert() {
        View dialogView = LayoutInflater.from(InputDatDiri.this).inflate(R.layout.alert_input, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(InputDatDiri.this);
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
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
    }

    public void terdaftar() {
        View dialogView = LayoutInflater.from(InputDatDiri.this).inflate(R.layout.alert_terdaftar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(InputDatDiri.this);
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

    public void dp() {
        View dialogView = LayoutInflater.from(InputDatDiri.this).inflate(R.layout.alert_dp, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(InputDatDiri.this);
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