package com.project.angsurapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.project.angsurapp.LogRes.Login;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {
    TextView nama_et, email_et, no_et, log_out;
    DrawerLayout drawerLayout;
    NavigationView navView;
    FrameLayout frame;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String namaAkun = sharedPreferences.getString("nama_akun", "Akun tidak ditemukan");
        String emailAkun = sharedPreferences.getString("email_akun", "Akun tidak ditemukan");
        String noAkun = sharedPreferences.getString("no_akun", "Akun tidak ditemukan");
        String gambarURL = sharedPreferences.getString("foto_pegawai", null);

        if (gambarURL != null) {
            String fotoPegawaiUrl = Api.foto + gambarURL;
            ImageView fotoPegawaiImageView = view.findViewById(R.id.gambar_et);
            Glide.with(this).load(fotoPegawaiUrl).into(fotoPegawaiImageView);
        }
        TextView namaTextView = view.findViewById(R.id.nama_tv);
        TextView emailTextView = view.findViewById(R.id.email_tv);
        TextView noTextView = view.findViewById(R.id.no_tv);
        namaTextView.setText(namaAkun);
        emailTextView.setText(emailAkun);
        noTextView.setText(noAkun);


        log_out = view.findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Login.class);
                startActivity(intent);
            }
        });
        return view;
    }
}


