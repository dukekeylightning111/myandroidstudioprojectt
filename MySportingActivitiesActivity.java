package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MySportingActivitiesActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Context context;
    private Customer currentCustomer;
    //    private MaterialToolbar materialToolbar;
    private AppDatabase app_db;
    private CustomerDao customerDao;
    private FloatingActionButton myFAB;
    private Customer customer;
    private SportingActivityAdapter adapter;
    private ActionBarDrawerToggle toggle;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView customer_name;
    private SportingActivityClassDao sportingActivityClassDao;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sporting_activities);
        Intent intent = getIntent();
        init();
        customer = (Customer) intent.getSerializableExtra("customer");
        customer_name.setText("הפעולות של " + customer.getName());
        recyclerView = findViewById(R.id.sporting_activity_recycler_view);
        func_view_sport_activities();
        func_create_sport_activity();
    }

    public void func_create_sport_activity() {
        myFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createSportIntent = new Intent(context, CreateSportActivity.class);
                createSportIntent.putExtra("customer", customer);
                startActivity(createSportIntent);
            }
        });
    }

    public void init() {
        drawerLayout = findViewById(R.id.my_drawer_layout);
        app_db = AppDatabase.getDatabase(this);
        customerDao = app_db.customerDAO();
        customer_name = findViewById(R.id.customer_name);
        sportingActivityClassDao = app_db.SportingActivityClassDao();
        myFAB = findViewById(R.id.myFAB);
        context = getApplicationContext();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigationView);
        context = MySportingActivitiesActivity.this;
        Intent intent = getIntent();
        currentCustomer = (Customer) intent.getSerializableExtra("customer");
        // materialToolbar = findViewById(R.id.materialToolbar);
        SomeClass.SetNavigationForActivities(drawerLayout,
                navigationView, this, currentCustomer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }

    private List<SportingActivityClass> customerSportingActivitiesList;

    public void func_view_sport_activities() {
        new AsyncTask<Void, Void, List<SportingActivityClass>>() {
            @Override
            protected List<SportingActivityClass> doInBackground(Void... voids) {
                return customer.getSportingActivityClasses();
            }

            @Override
            protected void onPostExecute(List<SportingActivityClass> sportingActivities) {
                customerSportingActivitiesList = customer.getSportingActivityClasses();

                adapter = new SportingActivityAdapter(customer, sportingActivityClassDao, MySportingActivitiesActivity.this, MySportingActivitiesActivity.this);
                adapter.setSportingActivities(sportingActivities);

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MySportingActivitiesActivity.this));

                SwipeToDelete swipeToDelete = new SwipeToDelete(adapter, app_db, MySportingActivitiesActivity.this);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDelete);
                itemTouchHelper.attachToRecyclerView(recyclerView);

                recyclerView.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
}