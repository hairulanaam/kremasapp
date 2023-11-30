package com.project.angsurapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity {
    BottomNavigationView bottom_navigation;
    DrawerLayout drawerLayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        loadFragment(new DashboardFragment());

        bottom_navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    if (item.getItemId() == R.id.navigation_dashboard) {
                        fragment = new DashboardFragment();
                    } else if (item.getItemId() == R.id.navigation_transaksi) {
                        fragment = new TransaksiFragment();
                    } else if (item.getItemId() == R.id.navigation_riwayat) {
                        fragment = new RiwayatFragment();
                    } else if (item.getItemId() == R.id.navigation_profile) {
                        fragment = new ProfileFragment();
                    }
                    return loadFragment(fragment);

            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }

    private void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
