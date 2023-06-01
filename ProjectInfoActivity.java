package com.example.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class ProjectInfoActivity extends AppCompatActivity {
    private Button howToUseButton;
    private Button contactMeButton;
    private Customer currentCustomer;
    private Button aboutMeButton;
    private SportingActivityAdapter adapter;
    private ActionBarDrawerToggle toggle;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Context context;

    private boolean isAboutMeVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        init();
        func_contact_me_clicked();
        func_about_me_clicked();
        func_how_to_use_clicked();
    }

    public void func_how_to_use_clicked() {
        howToUseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHowToUse fragmentHowToUse = new FragmentHowToUse();
                loadFragment(fragmentHowToUse);
            }
        });
    }

    public void func_about_me_clicked() {
        aboutMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAboutMeVisible) {
                    hideAboutMe();
                } else {
                    showAboutMe();
                }
            }
        });
    }

    public void func_contact_me_clicked() {
        contactMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_contact_me = new Intent(ProjectInfoActivity.this, ContactMeActivity.class);
                startActivity(intent_contact_me);
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.myFragmentContainerView, fragment);
        transaction.commit();
    }

    public void init() {
        howToUseButton = findViewById(R.id.howToUseButton);
        contactMeButton = findViewById(R.id.contactMeButton);
        aboutMeButton = findViewById(R.id.aboutMeButton);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        context = getApplicationContext();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigationView);
        context = ProjectInfoActivity.this;
        Intent intent = getIntent();
        currentCustomer = (Customer) intent.getSerializableExtra("customer");
        // materialToolbar = findViewById(R.id.materialToolbar);
        SomeClass.SetNavigationForActivities(drawerLayout,
                navigationView, this, currentCustomer);
    }

    private void showAboutMe() {
        AboutMeFragment aboutMeFragment = new AboutMeFragment();
        loadFragment(aboutMeFragment);
        aboutMeButton.setText("Hide"); // Change the button text to "Hide"
        isAboutMeVisible = true;
    }

    private void hideAboutMe() {
        // Remove the AboutMeFragment
        getSupportFragmentManager().beginTransaction()
                .remove(getSupportFragmentManager().findFragmentById(R.id.myFragmentContainerView))
                .commit();
        aboutMeButton.setText("About Me"); // Change the button text back to "About Me"
        isAboutMeVisible = false;
    }
}