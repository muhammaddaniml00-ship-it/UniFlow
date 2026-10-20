package com.student.uniflow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.student.uniflow.fragment.HomeFragment;
import com.student.uniflow.fragment.TasksFragment;
import com.student.uniflow.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_tasks) {
                fragment = new TasksFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                return false;
            }
            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fragment_fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void navigateToTasks() {
        bottomNav.setSelectedItemId(R.id.nav_tasks);
    }
}
